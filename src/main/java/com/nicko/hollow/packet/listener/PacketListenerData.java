package com.nicko.hollow.packet.listener;

import java.lang.reflect.Method;

import com.nicko.hollow.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Uma 'wrapper' que contem as informações para
 * para indentificar e executar uma 'message function'
 *
 */
@AllArgsConstructor
@Getter
public class PacketListenerData {

	private Object instance;
	private Method method;
	private Class packetClass;

	public boolean matches(Packet packet) {
		return this.packetClass == packet.getClass();
	}

}
