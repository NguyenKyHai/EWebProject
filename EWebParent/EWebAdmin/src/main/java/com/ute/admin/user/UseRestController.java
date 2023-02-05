package com.ute.admin.user;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
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
import com.ute.admin.request.UserRequest;
import com.ute.admin.response.ResponseMessage;
import com.ute.admin.response.UserResponse;
import com.ute.admin.role.RoleService;
import com.ute.admin.user.export.UserExcelExporter;
import com.ute.admin.user.export.UserPdfExporter;
import com.ute.admin.utils.FileUploadUtil;
import com.ute.common.constants.Constants;
import com.ute.common.entity.Role;
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
	@Autowired
	private RoleService roleService;

	@GetMapping("/users")
	@RolesAllowed("ROLE_ADMIN")
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
			return new ResponseEntity<>(new ResponseMessage("The email is existed"), HttpStatus.NOT_ACCEPTABLE);
		}

		User user = new User(userRequest.getEmail(), userRequest.getPassword(), userRequest.getFirstName(),
				userRequest.getLastName());
		Set<String> strRole = userRequest.getRoles();
		Set<Role> roles = new HashSet<>();

		strRole.forEach(role -> {
			switch (role) {
			case Constants.ROLE_ADMIN:
				Role adminRole = roleService.findByName(Constants.ROLE_ADMIN)
						.orElseThrow(() -> new RuntimeException("Role not found"));
				roles.add(adminRole);
				break;
			case Constants.ROLE_SALESPERSON:
				Role salesRole = roleService.findByName(Constants.ROLE_SALESPERSON)
						.orElseThrow(() -> new RuntimeException("Role not found"));
				roles.add(salesRole);
				break;
			case Constants.ROLE_ASSISTANT:
				Role assistantRole = roleService.findByName(Constants.ROLE_ASSISTANT)
						.orElseThrow(() -> new RuntimeException("Role not found"));
				roles.add(assistantRole);
				break;
			case Constants.ROLE_SHIPPER:
				Role shipperRole = roleService.findByName(Constants.ROLE_SHIPPER)
						.orElseThrow(() -> new RuntimeException("Role not found"));
				roles.add(shipperRole);
				break;
			case Constants.ROLE_EDITOR:
				Role editorRole = roleService.findByName(Constants.ROLE_EDITOR)
						.orElseThrow(() -> new RuntimeException("Role not found"));
				roles.add(editorRole);
				break;
			}
		});

		user.setRoles(roles);
		userService.save(user);
		return new ResponseEntity<>(new ResponseMessage("Create a new user success!"), HttpStatus.CREATED);
	}

	@PostMapping("/user/photo/save")
	public ResponseEntity<?> createUser(@RequestParam Map<String, String> params,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {
		String email = params.get("email");
		User user = new User(email, params.get("password"), params.get("firstName"), params.get("lastName"));

		if (userService.existsByEmail(email)) {
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

	@GetMapping("/users/filter")
	public Page<User> filterAdnSortedUser(@RequestParam(defaultValue = "") String firstNameFilter,
										 @RequestParam(defaultValue = "") String lastNameFilter, 
										 @RequestParam(defaultValue = "1") int page,
										 @RequestParam(defaultValue = "30") int size, 
										 @RequestParam(defaultValue = "") List<String> sortBy,
										 @RequestParam(defaultValue = "ASC") Sort.Direction sortOrder) {
		
		return userService.listByPage(firstNameFilter, lastNameFilter, page, size,
				sortBy, sortOrder.toString());
	}
}
