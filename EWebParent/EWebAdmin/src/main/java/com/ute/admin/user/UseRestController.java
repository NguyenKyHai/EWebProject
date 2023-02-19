package com.ute.admin.user;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.lowagie.text.DocumentException;
import com.ute.admin.jwt.JwtTokenUtil;
import com.ute.admin.request.AuthRequest;
import com.ute.admin.request.UserRequest;
import com.ute.admin.response.ResponseMessage;
import com.ute.admin.user.export.UserExcelExporter;
import com.ute.admin.user.export.UserPdfExporter;
import com.ute.common.constants.Constants;
import com.ute.common.entity.Role;
import com.ute.common.entity.User;

@RestController
@RequestMapping("/api")
public class UseRestController {

	@Autowired
	private IUserService userService;
	@Autowired
	AuthenticationManager authManager;
	@Autowired
	JwtTokenUtil jwtUtil;
	@Autowired
	private Cloudinary cloudinary;
	
	
//	@RolesAllowed("ROLE_ADMIN")
	@GetMapping("/users")
	public ResponseEntity<?> getListUsers() {
		List<User> listUsers = userService.getAllUsers();
		if (listUsers.isEmpty()) {
			return new ResponseEntity<>(new ResponseMessage("List of users is empty!"), HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(listUsers, HttpStatus.OK);
	}

	@PostMapping("/user/create")
	public ResponseEntity<?> createUser(@RequestBody @Valid UserRequest userRequest) {
		if (userService.existsByEmail(userRequest.getEmail())) {
			return new ResponseEntity<>(new ResponseMessage("The email is existed"), HttpStatus.BAD_REQUEST);
		}

		User user = new User(userRequest.getEmail(), userRequest.getPassword(), 
				userRequest.getFullName(), userRequest.getPhoneNumber(), userRequest.getAddress());
		Set<String> strRole = userRequest.getRoles();
		Set<Role> roles = userService.addRoles(strRole);
		user.setPhotos("default.png");
		user.setRoles(roles);
		userService.save(user);
		return new ResponseEntity<>(new ResponseMessage("Create a new user successfully!"), HttpStatus.CREATED);
	}

	@PutMapping("/user/photo/update/{id}")
	public ResponseEntity<?> updatePhoto(@PathVariable Integer id, @RequestParam("image") MultipartFile multipartFile)
			throws IOException {
		Optional<User> user = userService.findUserById(id);


		if (!user.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename().replace(".png", ""));
			Map uploadResult = cloudinary.uploader().upload(multipartFile.getBytes(), 
											ObjectUtils.asMap("public_id","users/"+id+"/"+fileName));
			String photo = uploadResult.get("secure_url").toString();
			user.get().setPhotos(photo);
			userService.save(user.get());

		} else {
			if (user.get().getPhotos().isEmpty())
				user.get().setPhotos(null);
			userService.save(user.get());
		}

		return new ResponseEntity<>(new ResponseMessage("Updated photo successfully"), HttpStatus.OK);
	}

	@GetMapping("/user/{id}")
	public ResponseEntity<?> getUser(@PathVariable Integer id) {

		Optional<User> user = userService.findUserById(id);
		if (!user.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<User>(user.get(), HttpStatus.OK);

	}

	@PutMapping("/user/{id}")
	public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody User request,
			MultipartFile multipartFile) throws IOException {

		Optional<User> user = userService.findUserById(id);

		if (!user.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		user.get().setFullName(request.getFullName());
		user.get().setPassword(request.getPassword());
		user.get().setPhoneNumber(request.getPhoneNumber());
		user.get().setAddress(request.getAddress());

		userService.save(user.get());
		return new ResponseEntity<User>(user.get(), HttpStatus.OK);
	}
	
	@PutMapping("/user/roles/{id}")
	public ResponseEntity<?> updateUserRole(@PathVariable Integer id, @RequestBody Map<String, Set<String>> param,
			MultipartFile multipartFile) throws IOException {

		Optional<User> user = userService.findUserById(id);
		if (!user.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Set<String> strRole = param.get("roles");
		Set<Role> roles = userService.addRoles(strRole);
		user.get().setRoles(roles);
		userService.save(user.get());
		return new ResponseEntity<>(new ResponseMessage("Updates roles successfully!"), HttpStatus.OK);
	}

	@PutMapping("user/block/{id}")
	public ResponseEntity<?> blockUser(@PathVariable Integer id) {
		Optional<User> user = userService.findUserById(id);

		if (!user.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		user.get().isEnabled();
		userService.updateStatus(id, Constants.STATUS_BLOCKED);
		return new ResponseEntity<>(new ResponseMessage("Block user successfully"), HttpStatus.OK);
	}

	@PutMapping("user/unblock/{id}")
	public ResponseEntity<?> unBlockUser(@PathVariable Integer id) {
		Optional<User> user = userService.findUserById(id);

		if (!user.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		userService.updateStatus(id, Constants.STATUS_ACTIVE);
		return new ResponseEntity<>(new ResponseMessage("The user have been un blocked successfully"), HttpStatus.OK);
	}

	@DeleteMapping("/user/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Integer id) {

		try {
			userService.deleteUserById(id);
			return new ResponseEntity<>(new ResponseMessage("Deleted user successfully"), HttpStatus.OK);
		} catch (EmptyResultDataAccessException e) {
			return new ResponseEntity<>(new ResponseMessage("User not found"), HttpStatus.NOT_FOUND);
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

	@GetMapping("/users/filter")
	public Page<User> filterAdnSortedUser(@RequestParam(defaultValue = "") String fullNameFilter,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int size, @RequestParam(defaultValue = "") List<String> sortBy,
			@RequestParam(defaultValue = "ASC") Sort.Direction sortOrder) {

		return userService.listByPage(fullNameFilter, page, size, sortBy, sortOrder.toString());
	}

}
