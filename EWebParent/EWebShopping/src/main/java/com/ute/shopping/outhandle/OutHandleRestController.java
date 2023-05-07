package com.ute.shopping.outhandle;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OutHandleRestController {
    @GetMapping("/")
    public ResponseEntity<?> listProducts() {

        return new ResponseEntity<>("Welcome to our API HDK Web Shopping!", HttpStatus.OK);
    }

}
