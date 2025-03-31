package fr.pederobien.protocol.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import fr.pederobien.protocol.interfaces.IErrorCodeFactory;
import fr.pederobien.protocol.interfaces.IProtocol;
import fr.pederobien.protocol.interfaces.IProtocolManager;
import fr.pederobien.protocol.interfaces.IRequest;
import fr.pederobien.utils.ReadableByteWrapper;

public class ProtocolManager implements IProtocolManager {
	private NavigableMap<Float, Protocol> protocols;
	private IErrorCodeFactory factory;

	/**
	 * Creates a protocol manager to insure the backward compatibility.
	 */
	public ProtocolManager() {
		protocols = new TreeMap<Float, Protocol>();
		factory = new ErrorCodeFactory();
	}

	@Override
	public IProtocol getOrCreate(float version) {
		Protocol protocol = protocols.get(version);
		if (protocol == null) {
			protocol = new Protocol(version, factory);
			protocols.put(version, protocol);
		}

		return protocol;
	}

	@Override
	public IRequest get(int identifier) {
		Iterator<Map.Entry<Float, Protocol>> iterator = protocols.descendingMap().entrySet().iterator();
		IRequest request = null;

		while (iterator.hasNext() && request == null) {
			request = iterator.next().getValue().get(identifier);
		}

		return request;
	}

	@Override
	public IRequest parse(byte[] data) {
		ReadableByteWrapper wrapper = ReadableByteWrapper.wrap(data);

		// Byte 0 -> 3: Protocol version
		float version = wrapper.nextFloat();

		Protocol protocol = protocols.get(version);
		if (protocol == null) {
			return null;
		}

		return protocol.parse(wrapper);
	}

	@Override
	public IErrorCodeFactory getErrorCodeFactory() {
		return factory;
	}

	private class ErrorCodeFactory implements IErrorCodeFactory {
		private static final String NOT_SUPPORTED = "CODE_NOT_SUPPORTED";
		private Map<Integer, String> errorCodes;

		public ErrorCodeFactory() {
			errorCodes = new HashMap<Integer, String>();
		}

		@Override
		public void register(int value, String message) {
			errorCodes.put(value, message);
		}

		@Override
		public String getMessage(int value) {
			String message = errorCodes.get(value);
			return message == null ? NOT_SUPPORTED : message;
		}
	}
}
