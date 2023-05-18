package com.ute.shopping.outhandle;


import com.ute.common.entity.Product;
import com.ute.common.entity.ProductImage;
import com.ute.common.response.ResponseMessage;
import com.ute.common.util.HelperUtil;
import com.ute.common.util.MailUtil;
import com.ute.shopping.util.MailTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
public class OutHandleRestController {

    @GetMapping("/")
    public ResponseEntity<?> welcome() {

        return new ResponseEntity<>("Welcome to our API HDK Web Shopping 1.3.3!", HttpStatus.OK);
    }

    @GetMapping("/mail")
    public ResponseEntity<?> mailTemplate() throws MessagingException {


        String verifyCode = MailTemplate.verifyCode(HelperUtil.randomString());

        boolean check = MailUtil.sendMail("19110197@student.hcmute.edu.vn", "Verification code",
                verifyCode);
        if (check) {
            return new ResponseEntity<>("Send mail successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error in processing", HttpStatus.OK);
        }
    }
}
