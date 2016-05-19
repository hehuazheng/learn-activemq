package com.hzz.activemq.t1;

import java.util.Scanner;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.secoo.jms.utility.JMSMessageProducer;

public class Q1Producer {

	public static void main(String[] args) {
		AbstractApplicationContext context = new ClassPathXmlApplicationContext(
				"t1/producer.xml");
		JMSMessageProducer producer = context.getBean("q1Producer",
				JMSMessageProducer.class);
		Scanner scan = new Scanner(System.in);
		boolean flag = true;
		while (flag) {
			System.out.println("enter message: ");
			String s = scan.next();
			producer.send(s);
			if ("end".equals(s)) {
				flag = false;
			}
		}
		System.out.println("complete");
		context.close();
	}

}
