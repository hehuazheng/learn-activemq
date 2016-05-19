package com.hzz.activemq.t1;

import java.util.Date;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.secoo.jms.utility.IConsumer;

public class Q1ConsumerWithRetry implements IConsumer {
	private static volatile boolean stop = false;

	public static void main(String[] args) throws Exception {
		AbstractApplicationContext context = new ClassPathXmlApplicationContext(
				"t1/consumerWithRetry.xml");
		context.start();
		while (!stop) {
			Thread.sleep(1000L);
		}
		context.stop();
		System.out.println("complete");
		context.close();
	}

	@Override
	public int consume(Object arg0) {
		String msg = (String) arg0;
		if ("illegal".equals(msg)) {
			System.out.println(new Date() + "illegal invoked");
			throw new RuntimeException("try it again");
		} else if ("1".equals(msg)) {
			return 1;
		} else if ("end".equals(msg)) {
			stop = true;
		} else {
			System.out.println("message Received " + msg);
		}
		return 0;
	}

}
