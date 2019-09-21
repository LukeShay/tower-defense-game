package com.pvptowerdefense.test;

import com.pvptowerdefense.test.websocketclient.SS5WebSocketClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;

public class SocketTests {
	private static final String URL = "coms-309-ss-5.misc.iastate" +
			".edu:8080/socket/";

	@Test
	public void connectToSocketTest() throws URISyntaxException, InterruptedException {
		SS5WebSocketClient webSocket1 = new SS5WebSocketClient("1");
		SS5WebSocketClient webSocket2 = new SS5WebSocketClient("2");
		SS5WebSocketClient webSocket3 = new SS5WebSocketClient("3");
		SS5WebSocketClient webSocket4 = new SS5WebSocketClient("4");

		Thread.sleep(1000);
		webSocket1.send("####### HELLO1 #######");
		Thread.sleep(1000);
		webSocket2.send("####### HELLO2 #######");
		Thread.sleep(1000);
		webSocket3.send("####### HELLO3 #######");
		Thread.sleep(1000);
		webSocket4.send("####### HELLO4 #######");

		Assertions.assertAll(
				() -> Assertions.assertTrue(webSocket1.isOpen()),
				() -> Assertions.assertTrue(webSocket2.isOpen()),
				() -> Assertions.assertTrue(webSocket3.isOpen()),
				() -> Assertions.assertTrue(webSocket4.isOpen())
		);

		System.out.println(webSocket1.getMessages().toString());
		System.out.println(webSocket2.getMessages().toString());
		System.out.println(webSocket3.getMessages().toString());
		System.out.println(webSocket4.getMessages().toString());

		webSocket1.close();
		webSocket2.close();
		webSocket3.close();
		webSocket4.close();

		Thread.sleep(5000);

		Assertions.assertAll(
				() -> Assertions.assertTrue(webSocket1.isClosed()),
				() -> Assertions.assertTrue(webSocket2.isClosed()),
				() -> Assertions.assertTrue(webSocket3.isClosed()),
				() -> Assertions.assertTrue(webSocket4.isClosed())
		);
	}
}