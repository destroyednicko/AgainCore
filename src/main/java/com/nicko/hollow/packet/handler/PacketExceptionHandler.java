package com.nicko.hollow.packet.handler;

public class PacketExceptionHandler {

	public void onException(Exception e) {
		System.out.println("Falha ao enviar pacote");
		e.printStackTrace();
	}

}
