package com.hzz.activemq;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.google.common.collect.Lists;
import com.secoo.jms.utility.JMSMessageProducer;

/**
 * Hello world!
 *
 */
public class App {
	private static final String sendMsg = "605646890526,605647181809,605647189471,605646943034,605647201403,605647000761,921743037666,605647208159,605646917053,605646929964,605646934423,605646931924,605646935677,605647165248,605647058414,605646495795,605646440570,605647158294,605646438625,605647139124,605646431720,605647130663,605647134661,605647145508,605647153440,605647044336,605646493405,605646475668,605647196203,605646472451,605646956436,605646488044,605646465568,605646462051,605647216878,605646444514,605646483776,605646456409,605647030034,605646459582,605646441918,605646434796,605647246044,605646446295,605646479975,605646450563,605647260606,605647235780,605646898789,605647285807,605646908294,605647310756,605646122953,605646127134,605647349709,605647018733,605646951813,605646394288,605646043689,605646700843,605646642066,605646622565,605646142279,605646152544,605646160908,605646167151,605646326069,605646136786,605646324176,605646134778,605646158939,605646149095,605646139957,605646138625,605646144762,605646156303,605646334627,605646075551,605646093583,605646878430,605646365416,605646863375,605646318815,605646873599,605646260556,605646266147,605646378458,605646253212,605646297352,605646380833,605646284522,605647266533,605646304287,605646305925,605646294349,605646291973,605646376547,605646422422,605646359934,605646881865,605646307157,605646373710,605646287969,605646249871,605646281953,605646298895,605646273571,605647121348,605646415867,605646271218,605646844444,605646237879,605646308812,605646278060,605646301017,605647220643";

	public static void main(String[] args) throws Exception {
//		System.out.println(sendMsg.getBytes().length);
//		sendMessage("d:/test5m512-async7.csv", 10000);
//		 sendMessage(null, 10000);
		analysis(Lists.newArrayList("d:/test5m512-async7.csv", "d:/test5m512-async5.csv","d:/test5m512-async4.csv","d:/test5m512-async3.csv", "d:/test5m512-async2.csv", "d:/test5m512-async.csv"));
//		 analysis(Lists.newArrayList("d:/test5m512-async2.csv", "d:/test5m512-async.csv","d:/tt1.csv","d:/tt2.csv","d:/test5m512-3.csv","d:/test5m768.csv","d:/test3m512.csv",
//		 "d:/test10m1.csv",
//		 "d:/test10m2.csv","d:/test5m2.csv","d:/test5m512-2.csv","d:/test5m1g.csv","d:/test5m512.csv",
//		 "d:/test1m2.csv"));
	}

	static void analysis(List<String> fileList) throws Exception {
		for (String file : fileList) {
			processFile(file);
		}
	}

	static void processFile(String file) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(file)));
		String line = null;
		int max = -1;
		int min = Integer.MAX_VALUE;
		int sum = 0;
		while ((line = br.readLine()) != null) {
			String[] arr = line.split(",");
			int time = Integer.parseInt(arr[1]);
			if (min > time) {
				min = time;
			}
			if (max < time) {
				max = time;
			}
			sum += time;
		}
		System.out
				.println(file + " sum:" + sum + ",max:" + max + ",min:" + min);
		br.close();
	}

	static void calc() {
		System.out.println(sendMsg.getBytes().length);
	}

	static void sendMessage(String file, int times)
			throws FileNotFoundException, InterruptedException {
		AbstractApplicationContext context = new ClassPathXmlApplicationContext(
				"application-config.xml");
		final JMSMessageProducer producer = context.getBean(
				"helloTestProducer", JMSMessageProducer.class);
		System.out.println(producer.getTemplate().getConnectionFactory()
				.getClass().getCanonicalName());
		PrintWriter pw = null;

		if (file != null) {
			pw = new PrintWriter(new File(file));
		}
		
		SendThread st1 = new SendThread(producer, times, 0, pw);
//		SendThread st2 = new SendThread(producer, times/2, times/2, pw);
		
		st1.start();
//		st2.start();

		latch.await();
		
		if (pw != null) {
			pw.flush();
			pw.close();
		}
		context.close();
		System.out.println("complete");
	}

	private static final CountDownLatch latch = new CountDownLatch(1);
	
	static class SendThread extends Thread {
		private JMSMessageProducer producer;
		private int n;
		private int offset;
		private PrintWriter pw;

		public SendThread(JMSMessageProducer producer, int n, int offset,
				PrintWriter pw) {
			this.producer = producer;
			this.n = n;
			this.offset = offset;
			this.pw = pw;
		}

		@Override
		public void run() {
			for (int i = 0; i < n; i++) {
				long start = System.currentTimeMillis();
				producer.send(sendMsg);
				long end = System.currentTimeMillis();
				if (pw != null) {
					pw.println((offset + i + 1) + "," + (end - start));
				} else {
					System.out.println((offset + i + 1) + "," + (end - start));
				}
			}
			latch.countDown();
		}
	}
}
