package com.ute.admin.user;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;
import com.lowagie.text.DocumentException;
import com.ute.admin.jwt.JwtTokenUtil;
import com.ute.admin.response.ResponseMessage;
import com.ute.admin.response.UserResponse;
import com.ute.admin.user.export.UserExcelExporter;
import com.ute.admin.user.export.UserPdfExporter;
import com.ute.admin.utils.FileUploadUtil;
import com.ute.common.entity.User;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UseRestController {

	@Autowired
	private IUserService userService;
	@Autowired
	AuthenticationManager authManager;
	@Autowired
	JwtTokenUtil jwtUtil;

	@GetMapping("/users")
	@RolesAllowed("ROLE_ADMIN")
	public ResponseEntity<?> getListUsers() {
		List<User> listUsers = userService.getAllUsers();
		if (listUsers.isEmpty()) {
			return new ResponseEntity<>(new ResponseMessage("List of users is empty!"), HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(listUsers, HttpStatus.OK);
	}

	@PostMapping("/user/photo/save")
	public ResponseEntity<?> createUser(@RequestBody User user, @RequestParam("image") MultipartFile multipartFile)
			throws IOException {
		if (userService.existsByEmail(user.getEmail())) {
			return new ResponseEntity<>(new ResponseMessage("The email is existed"), HttpStatus.BAD_REQUEST);
		}
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			User savedUser = userService.save(user);

			String uploadDir = "user-photos/" + savedUser.getId();

			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			return new ResponseEntity<>(new UserResponse("Create User Successfully!", fileName), HttpStatus.OK);

		} else {
			if (user.getPhotos().isEmpty())
				user.setPhotos(null);
			userService.save(user);
		}

		return new ResponseEntity<>(new ResponseMessage("Create User Successfully!"), HttpStatus.OK);
	}

	@PutMapping("/user/{id}")
	public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody User u, MultipartFile multipartFile)
			throws IOException {
		try {
			User user = userService.findUserById(id);
			user.setFirstName(u.getFirstName());
			user.setPassword(u.getPassword());
			user.setLastName(u.getLastName());

			userService.save(user);
			return new ResponseEntity<User>(user, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(new ResponseMessage("User not found"), HttpStatus.OK);
		}
	}

	@PutMapping("/user/photo/{id}")
	public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestParam("image") MultipartFile multipartFile)
			throws IOException {
		try {
			User user = userService.findUserById(id);
			if (!multipartFile.isEmpty()) {
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				user.setPhotos(fileName);
				User savedUser = userService.save(user);

				String uploadDir = "user-photos/" + savedUser.getId();

				FileUploadUtil.cleanDir(uploadDir);
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			} else {
				if (user.getPhotos().isEmpty())
					user.setPhotos(null);
				userService.save(user);
			}
			return new ResponseEntity<User>(user, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(new ResponseMessage("User not found"), HttpStatus.OK);
		}
	}

	@GetMapping("/user/{id}")
	public ResponseEntity<?> getUser(@PathVariable Integer id) {

		try {
			User user = userService.findUserById(id);
			return new ResponseEntity<User>(user, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(new ResponseMessage("User not found"), HttpStatus.OK);
		}

	}

	@DeleteMapping("/user/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Integer id) {

		try {
			userService.deleteUserById(id);
			return new ResponseEntity<>(new ResponseMessage("Delete user successfully"), HttpStatus.OK);
		} catch (EmptyResultDataAccessException e) {
			return new ResponseEntity<>(new ResponseMessage("User not found"), HttpStatus.OK);
		}
	}

	@GetMapping("/users/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);

		List<User> listUsers = userService.getAllUsers();

		UserExcelExporter excelExporter = new UserExcelExporter();

		excelExporter.export(listUsers, response);
	}

	@GetMapping("/users/export/pdf")
	public void exportToPDF(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
		response.setHeader(headerKey, headerValue);

		List<User> listUsers = userService.getAllUsers();

		UserPdfExporter exporter = new UserPdfExporter();
		exporter.export(listUsers, response);

	}

	@GetMapping("/users/page/{pageNum}")
	public ResponseEntity<?> listByPage(@PathVariable(name = "pageNum") int pageNum) {
		if (pageNum < 1)
			return new ResponseEntity<>(new ResponseMessage("Page index must not be less than zero!"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		Page<User> page = userService.listByPage(pageNum);
		List<User> listUsers = page.getContent();
		long startCount = (pageNum - 1) * UserService.USERS_PER_PAGE + 1;
		long endCount = startCount + UserService.USERS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		return new ResponseEntity<>(listUsers, HttpStatus.OK);

	}
}
