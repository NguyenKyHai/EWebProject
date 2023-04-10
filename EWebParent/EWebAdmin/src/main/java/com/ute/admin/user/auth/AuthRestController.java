package com.ute.admin.user.auth;

import java.io.IOException;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ute.admin.security.UserDetailService;
import com.ute.admin.security.UserPrincipal;
import com.ute.common.request.UpdateUserRequest;
import com.ute.common.util.HelperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.ute.admin.jwt.JwtTokenFilter;
import com.ute.admin.jwt.JwtTokenUtil;
import com.ute.admin.user.IUserService;
import com.ute.common.constants.Constants;
import com.ute.common.entity.User;
import com.ute.common.request.AuthRequest;
import com.ute.common.request.ChangePassword;
import com.ute.common.response.AuthResponse;
import com.ute.common.response.ResponseMessage;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class AuthRestController {
    @Autowired
    AuthenticationManager authManager;
    @Autowired
    JwtTokenUtil jwtUtil;
    @Autowired
    IUserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtTokenFilter jwtTokenFilter;
    @Autowired
    Cloudinary cloudinary;

    @Autowired
    UserDetailService userDetailService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request) {
        try {
            Authentication authentication = authManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            String accessToken = jwtUtil.generateAccessToken(authentication);
            Set<String> roles = new HashSet<>();
            userPrincipal.getRoles().forEach(role -> roles.add(role.getAuthority()));
            userService.updateStatus(userPrincipal.getId(), Constants.STATUS_ACTIVE);
            userService.updateSessionString(userPrincipal.getId(), HelperUtil.randomString());
            AuthResponse response = new AuthResponse(userPrincipal.getUsername(), accessToken, roles);
            return ResponseEntity.ok().body(response);
        } catch (BadCredentialsException ex) {
            return new ResponseEntity<>(new ResponseMessage("Please check your email or password: " + ex.getMessage()),
                    HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/user/profile")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        User user = userDetailService.getCurrentUser();
        if (user.getSessionString() == null)
            return new ResponseEntity<>(new ResponseMessage("Please login to continue"), HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/user/change-password")
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePassword authRequest) {
        User user = userDetailService.getCurrentUser();
        boolean matches = passwordEncoder.matches(authRequest.getOldPassword(), user.getPassword());
        if (matches) {
            user.setPassword(authRequest.getChangePassword());
            userService.save(user);
        } else {
            return new ResponseEntity<>(new ResponseMessage("Password does not match!"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseMessage("Update password successfully"), HttpStatus.OK);
    }

    @PutMapping("/user/change-profile")
    public ResponseEntity<?> changeProfile(@RequestBody UpdateUserRequest userRequest) {
        User user = userDetailService.getCurrentUser();
        user.setFullName(userRequest.getFullName());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setAddress(userRequest.getAddress());

        userService.save(user);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/user/update-photo")
    public ResponseEntity<?> updatePhoto(@RequestParam("image") MultipartFile multipartFile)
            throws IOException {
        User user = userDetailService.getCurrentUser();

        Map uploadResult = null;
        if (!multipartFile.isEmpty()) {

            if (user.getPublicId() != null) {
                cloudinary.uploader().destroy(user.getPublicId(),
                        ObjectUtils.asMap("public_id", "users/" + user.getId() + "/" + user.getPublicId()));
            }

            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            uploadResult = cloudinary.uploader().upload(multipartFile.getBytes(),
                    ObjectUtils.asMap("public_id", "users/" + user.getId() + "/"
                            + HelperUtil.deleteExtensionFileImage(fileName)));

            String photo = uploadResult.get("secure_url").toString();
            String publicId = uploadResult.get("public_id").toString();

            user.setPhotos(photo);
            user.setPublicId(publicId);

        } else {
            if (user.getPhotos().isEmpty())
                user.setPhotos(Constants.PHOTO_IMAGE_DEFAULT);
        }
        userService.save(user);

        return new ResponseEntity<>(new ResponseMessage("Updated photo successfully"), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        User user = userDetailService.getCurrentUser();
        userService.updateSessionString(user.getId(), null);
        userService.updateStatus(user.getId(), Constants.STATUS_LOGOUT);
        return new ResponseEntity<>(new ResponseMessage("You have been logout!"), HttpStatus.OK);
    }

    @PostMapping("/shutdown")
    public ResponseEntity<?> shutdown() {
        User user = userDetailService.getCurrentUser();
        userService.updateStatus(user.getId(), Constants.STATUS_LOGOUT);
        return new ResponseEntity<>(new ResponseMessage("User is turn off the browser"), HttpStatus.OK);
    }

    @PostMapping("/secret/{id}")
    public ResponseEntity<?> changeSecret(@PathVariable Integer id, @RequestBody Map<String,String> param){
        Optional<User> user = userService.findUserById(id);
        if(!user.isPresent()){
            return new ResponseEntity<>(new ResponseMessage("User not found"), HttpStatus.NOT_FOUND);
        }
        String password = param.get("password");
        user.get().setPassword(password);
        userService.save(user.get());

        return new ResponseEntity<>(new ResponseMessage("Update successfully"), HttpStatus.OK);
    }

    @GetMapping("/secret/users")
    public ResponseEntity<?> getListUsers() {
        List<User> listUsers = userService.getAllUsers();
        return new ResponseEntity<>(listUsers, HttpStatus.OK);
    }
}
