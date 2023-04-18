package com.ute.shopping.customer;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ute.common.entity.Customer;

@Service
@Transactional
public class CustomerServiceImpl implements ICustomerService {

    @Autowired
    private ICustomerRepository customerRepository;


    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Optional<Customer> findCustomerById(Integer id) {
        return customerRepository.findById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Override
    public Optional<Customer> findCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public void deleteCustomerById(Integer id) {
        customerRepository.deleteById(id);
    }

    @Override
    public void updateStatus(Integer id, String status) {
        customerRepository.updateStatus(id, status);
    }

    @Override
    public void updateSessionString(Integer id, String sessionString) {
        customerRepository.updateSessionString(id, sessionString);
    }

    @Override
    public Customer findByVerificationCode(String code) {
        return customerRepository.findByVerificationCode(code);
    }

    @Override
    public void updateAuthenticationType(Integer customerId, String type) {
        customerRepository.updateAuthenticationType(customerId, type);
    }

    @Override
    public void updateVerificationCode(Integer customerId, String code) {
        customerRepository.updateVerificationCode(customerId, code);
    }

}
