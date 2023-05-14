package com.ute.shopping.outhandle;


import com.ute.common.entity.Product;
import com.ute.common.entity.ProductImage;
import com.ute.common.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
public class OutHandleRestController {

    @GetMapping("/")
    public ResponseEntity<?> welcome() {

        return new ResponseEntity<>("Welcome to our API HDK Web Shopping 1.3.2!", HttpStatus.OK);
    }
}
