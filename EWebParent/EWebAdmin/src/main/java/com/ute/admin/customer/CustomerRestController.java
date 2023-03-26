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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ute.common.constants.Constants;
import com.ute.common.entity.Customer;
import com.ute.common.response.ResponseMessage;

@RestController
@RequestMapping("/api")
@RolesAllowed({"ROLE_ADMIN", "ROLE_SALESPERSON"})
public class CustomerRestController {

    @Autowired
    ICustomerService customerService;

    @RolesAllowed({"ROLE_ADMIN", "ROLE_SALESPERSON"})
    @GetMapping("/customers")
    public ResponseEntity<?> getListCustomer() {
        List<Customer> listCustomers = customerService.getAllCustomers();
        if (listCustomers.isEmpty()) {
            return new ResponseEntity<>(new ResponseMessage("List of Customers is empty!"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(listCustomers, HttpStatus.OK);
    }

    @PutMapping("customer/block/{id}")
    public ResponseEntity<?> blockCustomer(@PathVariable Integer id, @RequestParam Map<String,String> param) {
        Optional<Customer> customer = customerService.findCustomerById(id);

        if (!customer.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String status = param.get("status");
        customerService.updateStatus(id, status);
        customerService.updateSessionString(id, null);
        return new ResponseEntity<>(new ResponseMessage("Blocked/un blocked user successfully"), HttpStatus.OK);
    }

    @GetMapping("/customers/filter")
    public Page<Customer> filterAdnSortedCustomer(
            @RequestParam(defaultValue = "") String fullName,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "") List<String> sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction order) {

        return customerService.filterCustomers(fullName, page, size, sortBy, order.toString());
    }
}
