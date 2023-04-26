package com.ute.shopping.customer;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ute.common.entity.ShippingAddress;
import com.ute.common.request.AddressRequest;
import com.ute.common.util.MailUtil;
import com.ute.shopping.address.IAddressService;
import com.ute.shopping.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.ute.common.constants.AuthProvider;
import com.ute.common.constants.Constants;
import com.ute.common.entity.Customer;
import com.ute.common.request.ChangePasswordRequest;
import com.ute.common.request.LoginRequest;
import com.ute.common.request.SignupRequest;
import com.ute.common.response.LoginResponse;
import com.ute.common.response.ResponseMessage;
import com.ute.common.util.HelperUtil;
import com.ute.shopping.jwt.JwtTokenFilter;
import com.ute.shopping.jwt.JwtTokenUtil;
import com.ute.shopping.security.UserPrincipal;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class CustomerRestController {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenUtil jwtUtil;
    @Autowired
    ICustomerService customerService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtTokenFilter jwtTokenFilter;
    @Autowired
    Cloudinary cloudinary;
    @Autowired
    CustomUserDetailsService customUserDetailsService;
    @Autowired
    IAddressService addressService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            String token = jwtUtil.generateAccessToken(authentication);
            customerService.updateStatus(userPrincipal.getId(), Constants.STATUS_ACTIVE);
            customerService.updateSessionString(userPrincipal.getId(), HelperUtil.randomString());
            LoginResponse response = new LoginResponse(userPrincipal.getEmail(), token);

            return ResponseEntity.ok().body(response);

        } catch (BadCredentialsException ex) {
            return new ResponseEntity<>(new ResponseMessage("Please check your email or password!"),
                    HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> createCustomer(@RequestBody @Valid SignupRequest signupRequest) {

        if (customerService.existsByEmail(signupRequest.getEmail())) {
            return new ResponseEntity<>(new ResponseMessage("The email is existed"), HttpStatus.BAD_REQUEST);
        }
        String password = passwordEncoder.encode(signupRequest.getPassword());
        Customer customer = new Customer();
        customer.setEmail(signupRequest.getEmail());
        customer.setPassword(password);
        customer.setFullName(signupRequest.getFullName());
        customer.setPhotos(Constants.PHOTO_IMAGE_DEFAULT);
        customer.setPublicId(Constants.PRODUCT_PUBLIC_ID_DEFAULT);
        customer.setStatus(Constants.STATUS_VERIFY);
        customer.setCreatedTime(new Date());
        String randomString = HelperUtil.randomString();
        customer.setVerificationCode(randomString);
        customer.setProvider(AuthProvider.local);
//		MailUtil.sendMail(signupRequest.getEmail(), "Ma code xac nhan",
//				"Cam on ban da dang ky.\n Ma code xac nhan cua ban la: " + randomString);
        customerService.save(customer);
        return new ResponseEntity<>(new ResponseMessage("Create a new customer successfully!"), HttpStatus.CREATED);
    }

    @PostMapping("/customer/verify")
    public ResponseEntity<?> verifyAccount(@RequestBody Map<String, String> param) {

        String code = param.get("code");

        Customer customer = customerService.findByVerificationCode(code);
        if (customer != null) {
            customerService.updateVerificationCode(customer.getId(), null);
            customerService.updateStatus(customer.getId(), Constants.STATUS_ACTIVE);
            return new ResponseEntity<>(new ResponseMessage("Verify successfully"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseMessage("Invalid code"), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/customer/change-password")
    public ResponseEntity<?> changePassword(HttpServletRequest request, @RequestBody @Valid ChangePasswordRequest authRequest) {
        String jwt = jwtTokenFilter.getAccessToken(request);
        if (jwt == null)
            return new ResponseEntity<>(new ResponseMessage("Token not found"), HttpStatus.BAD_REQUEST);
        String email = jwtUtil.getUerNameFromToken(jwt);
        Optional<Customer> customer = customerService.findCustomerByEmail(email);
        if (!customer.isPresent())
            return new ResponseEntity<>(new ResponseMessage("Customer not found"), HttpStatus.NOT_FOUND);
        boolean matches = passwordEncoder.matches(authRequest.getOldPassword(), customer.get().getPassword());
        if (matches) {
            String newPassword = passwordEncoder.encode(authRequest.getChangePassword());
            customer.get().setPassword(newPassword);
            customerService.save(customer.get());
        } else {
            return new ResponseEntity<>(new ResponseMessage("Password does not match!"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseMessage("Update password successfully"), HttpStatus.OK);
    }

    @PutMapping("customer/update-photo")
    public ResponseEntity<?> updatePhoto(@RequestParam("image") MultipartFile multipartFile)
            throws IOException {
        Customer customer = customUserDetailsService.getCurrentCustomer();
        if (multipartFile.getOriginalFilename() == null) {
            return new ResponseEntity<>(new ResponseMessage("Image not found"), HttpStatus.NOT_FOUND);
        }
        Map uploadResult;
        if (!multipartFile.isEmpty()) {
            if (customer.getPublicId() != null) {
                cloudinary.uploader().destroy(customer.getPublicId(),
                        ObjectUtils.asMap("public_id",
                                "customers/" + customer.getId() + "/" + customer.getPublicId()));
            }
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            uploadResult = cloudinary.uploader().upload(multipartFile.getBytes(),
                    ObjectUtils.asMap("public_id", "customers/" + customer.getId() + "/"
                            + HelperUtil.deleteExtensionFileImage(fileName)));

            String photo = uploadResult.get("secure_url").toString();
            String publicId = uploadResult.get("public_id").toString();

            customer.setPhotos(photo);
            customer.setPublicId(publicId);

        } else {
            if (customer.getPhotos().isEmpty())
                customer.setPhotos(Constants.PHOTO_IMAGE_DEFAULT);
        }
        customerService.save(customer);

        return new ResponseEntity<>(new ResponseMessage("Updated photo successfully"), HttpStatus.OK);
    }

    @PostMapping("/customer/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> param) {
        String email = param.get("email");
        Optional<Customer> customer = customerService.findCustomerByEmail(email);
        if (!customer.isPresent()) {
            return new ResponseEntity<>(new ResponseMessage("Email does not exist!"), HttpStatus.BAD_REQUEST);
        }
        String randomString = HelperUtil.randomString();
        customerService.updateVerificationCode(customer.get().getId(), randomString);
        MailUtil.sendMail(email, "Ma code xac nhan", "Ma code xac nhan cua ban la: " + randomString);

        return new ResponseEntity<>(new ResponseMessage("Please check your code that sent via your email"),
                HttpStatus.OK);
    }

    @PostMapping("customer/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody Map<String, String> param) {
        String email = param.get("email");
        String password = param.get("password");
        Optional<Customer> customer = customerService.findCustomerByEmail(email);
        if (!customer.isPresent()) {
            return new ResponseEntity<>(new ResponseMessage("Email does not exist!"), HttpStatus.BAD_REQUEST);
        }
        String newPassword = passwordEncoder.encode(password);
        customer.get().setPassword(newPassword);
        customerService.save(customer.get());
        return new ResponseEntity<>(new ResponseMessage("Change password successfully"), HttpStatus.OK);

    }

    @GetMapping("/customer/profile")
    public ResponseEntity<?> getCurrentCustomer() {
        Customer customer = customUserDetailsService.getCurrentCustomer();
        if (customer == null) {
            return new ResponseEntity<>(new ResponseMessage("Customer not found"), HttpStatus.NOT_FOUND);
        }
        if (customer.getAddress() != null) {
            Set<ShippingAddress> addressList = customer.getAddress();
            customer.setAddress((addressList.stream().filter(t -> !t.isDeleteFlag())
                    .collect(Collectors.toSet())));
        }
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @PutMapping("/customer/profile")
    public ResponseEntity<?> changeProfile(@RequestBody Map<String, String> param) {
        Customer customer = customUserDetailsService.getCurrentCustomer();
        String fullName = param.get("fullName");
        customer.setFullName(fullName);
        customerService.save(customer);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @PostMapping("/shipping-address/create")
    public ResponseEntity<?> createAddress(@RequestBody AddressRequest request) {
        Customer customer = customUserDetailsService.getCurrentCustomer();
        if (customer == null) {
            return new ResponseEntity<>(new ResponseMessage("Customer not found"), HttpStatus.NOT_FOUND);
        }
        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setName(request.getName());
        shippingAddress.setPhoneNumber(request.getPhoneNumber());
        shippingAddress.setStreet(request.getStreet());
        shippingAddress.setDistrict(request.getDistrict());
        shippingAddress.setDefaultAddress(request.isDefaultAddress());
        shippingAddress.setDistrictId(request.getDistrictId());
        shippingAddress.setWard(request.getWard());
        shippingAddress.setWardCode(request.getWardCode());
        shippingAddress.setDeleteFlag(false);
        addressService.save(shippingAddress);
        Set<ShippingAddress> shippingAddresses = customer.getAddress();
        shippingAddresses.add(shippingAddress);
        customer.setAddress(shippingAddresses);
        customerService.save(customer);

        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @PutMapping("/shipping-address/update/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable Integer id, @RequestBody AddressRequest request) {
        Customer customer = customUserDetailsService.getCurrentCustomer();
        if (customer == null) {
            return new ResponseEntity<>(new ResponseMessage("Customer not found"), HttpStatus.NOT_FOUND);
        }
        Optional<ShippingAddress> shippingAddress = addressService.findById(id);
        if (!shippingAddress.isPresent()) {
            return new ResponseEntity<>(new ResponseMessage("Address not found"), HttpStatus.NOT_FOUND);
        }
        shippingAddress.get().setName(request.getName());
        shippingAddress.get().setPhoneNumber(request.getPhoneNumber());
        shippingAddress.get().setStreet(request.getStreet());
        shippingAddress.get().setDistrict(request.getDistrict());
        shippingAddress.get().setDistrictId(request.getDistrictId());
        shippingAddress.get().setWard(request.getWard());
        shippingAddress.get().setWardCode(request.getWardCode());

        boolean defaultAddress = request.isDefaultAddress();
        if (defaultAddress) {
            addressService.updateDefaultAddress(id, true);
        }
        addressService.save(shippingAddress.get());

        return new ResponseEntity<>(new ResponseMessage("Update shipping address successfully"), HttpStatus.OK);
    }

    @PutMapping("/shipping-address/delete/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable Integer id) {
        Customer customer = customUserDetailsService.getCurrentCustomer();
        if (customer == null) {
            return new ResponseEntity<>(new ResponseMessage("Customer not found"), HttpStatus.NOT_FOUND);
        }
        Optional<ShippingAddress> shippingAddress = addressService.findById(id);
        if (!shippingAddress.isPresent()) {
            return new ResponseEntity<>(new ResponseMessage("Address not found"), HttpStatus.NOT_FOUND);
        }
        if (shippingAddress.get().isDefaultAddress()) {
            return new ResponseEntity<>(new ResponseMessage("You do not permission to delete default address"),
                    HttpStatus.NOT_ACCEPTABLE);
        }
        addressService.deleteAddress(id, true);
        return new ResponseEntity<>(new ResponseMessage("Delete shipping address successfully"), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String jwt = jwtTokenFilter.getAccessToken(request);
        if (jwt == null)
            return new ResponseEntity<>(new ResponseMessage("Token not found"), HttpStatus.NOT_FOUND);
        String email = jwtUtil.getUerNameFromToken(jwt);
        Optional<Customer> customer = customerService.findCustomerByEmail(email);
        if (!customer.isPresent())
            return new ResponseEntity<>(new ResponseMessage("Customer not found"), HttpStatus.NOT_FOUND);

        customerService.updateStatus(customer.get().getId(), Constants.STATUS_LOGOUT);
        customerService.updateSessionString(customer.get().getId(), null);
        return new ResponseEntity<>(new ResponseMessage("You have been logout!"), HttpStatus.OK);
    }

    @PostMapping("/shutdown")
    public ResponseEntity<?> shutdown(HttpServletRequest request) {
        String jwt = jwtTokenFilter.getAccessToken(request);
        if (jwt == null)
            return new ResponseEntity<>(new ResponseMessage("Token not found"), HttpStatus.NOT_FOUND);
        String email = jwtUtil.getUerNameFromToken(jwt);
        Optional<Customer> customer = customerService.findCustomerByEmail(email);
        if (!customer.isPresent())
            return new ResponseEntity<>(new ResponseMessage("Customer not found"), HttpStatus.NOT_FOUND);

        customerService.updateStatus(customer.get().getId(), Constants.STATUS_LOGOUT);
        return new ResponseEntity<>(new ResponseMessage("You have been logout!"), HttpStatus.OK);
    }

}
