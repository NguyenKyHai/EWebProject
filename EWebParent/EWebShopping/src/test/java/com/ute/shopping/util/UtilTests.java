package com.ute.shopping.util;

import org.junit.jupiter.api.Test;

import com.ute.common.entity.util.MailUtil;

public class UtilTests {

	@Test
	public void testSendMail() {
		String reciever = "19110227@student.hcmute.edu.vn";
		String subject = "Hello";
		String text = "Hehe Khanh DC";
		MailUtil.sendMail(reciever, subject, text);
		System.out.println("Send mail successfully");
	}

}
