package com.ute.shopping.util;

import org.junit.jupiter.api.Test;

import com.ute.common.util.MailUtil;

public class UtilTests {

	@Test
	public void testSendMail() {
		String reciever = "19110009@student.hcmute.edu.vn";
		String subject = "Hello";
		String text = "What's up";
		MailUtil.sendMail(reciever, subject, text);
		System.out.println("Send mail successfully");
	}

}
