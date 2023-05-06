package com.ute.admin.customer;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.security.RolesAllowed;

import org.apache.commons.collections4.queue.PredicatedQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ute.common.constants.Constants;
import com.ute.common.entity.Customer;
import com.ute.common.response.ResponseMessage;

@RestController
@RequestMapping("/api")
@RolesAllowed({"ROLE_ADMIN", "ROLE_SALESPERSON"})
public class CustomerRestController {

    @Autowired
    ICustomerService customerService;

    @GetMapping("/customers")
    public ResponseEntity<?> getListCustomer() {
        List<Customer> listCustomers = customerService.getAllCustomers();
        return new ResponseEntity<>(listCustomers, HttpStatus.OK);
    }

    @PutMapping("customer/block/{id}")
    public ResponseEntity<?> blockCustomer(@PathVariable Integer id, @RequestBody Map<String, String> param) {
        Optional<Customer> customer = customerService.findCustomerById(id);

        if (!customer.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String status = param.get("status");
        if (Constants.STATUS_BLOCKED.equals(status)) {
            customerService.updateStatus(id, Constants.STATUS_BLOCKED);
        }
        if (Constants.STATUS_ACTIVE.equals(status)) {
            customerService.updateStatus(id, Constants.STATUS_ACTIVE);
        }
        customerService.updateSessionString(id, null);
        return new ResponseEntity<>(new ResponseMessage("Blocked/un blocked user successfully"), HttpStatus.OK);
    }

    @GetMapping("/customers/filter")
    public Page<Customer> filterAdnSortedCustomer(
            @RequestParam(defaultValue = "") String fullName,
            @RequestParam(defaultValue = "") String email,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "") List<String> sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction order) {

        return customerService.filterCustomers(fullName, email, page, size, sortBy, order.toString());
    }
}
