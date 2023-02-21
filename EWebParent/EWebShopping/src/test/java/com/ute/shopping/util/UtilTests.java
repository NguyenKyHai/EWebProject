package com.ute.shopping.util;

import org.junit.jupiter.api.Test;

import com.ute.common.util.MailUtil;

public class UtilTests {

	@Test
	public void testSendMail() {
		String reciever = "leafnote2022@gmail.com";
		String subject = "Hello";
		String text = "What's up";
		MailUtil.sendMail(reciever, subject, text);
		System.out.println("Send mail successfully");
	}

}
