package fr.pederobien.protocol.impl;

import java.util.StringJoiner;

import fr.pederobien.protocol.interfaces.IErrorCodeFactory;
import fr.pederobien.protocol.interfaces.IRequest;
import fr.pederobien.protocol.interfaces.IWrapper;
import fr.pederobien.utils.ByteWrapper;
import fr.pederobien.utils.ReadableByteWrapper;

public class Request implements IRequest {
	private final float version;
	private final IErrorCodeFactory factory;
	private final int identifier;
	private int errorCode;
	private Object payload;
	private final IWrapper wrapper;

	/**
	 * Creates a message to send to the remote.
	 * 
	 * @param version   The protocol version.
	 * @param factory   The factory to get the message associated to the error code.
	 * @param identifier The request identifier.
	 * @param errorCode The request error code.
	 * @param wrapper    The request wrapper that contains the bytes generator and parser.
	 */
	public Request(float version, IErrorCodeFactory factory, int identifier, int errorCode, IWrapper wrapper) {
		this.version = version;
		this.factory = factory;
		this.errorCode = errorCode;
		this.identifier = identifier;
		this.wrapper = wrapper;
	}

	@Override
	public float getVersion() {
		return version;
	}

	@Override
	public int getIdentifier() {
		return identifier;
	}

	@Override
	public int getErrorCode() {
		return errorCode;
	}

	@Override
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	@Override
	public void setPayload(Object payload) {
		this.payload = payload;
	}

	@Override
	public Object getPayload() {
		return payload;
	}

	@Override
	public byte[] getBytes() {
		ByteWrapper byteWrapper = ByteWrapper.create();

		// Byte 0 -> 3: Protocol version
		byteWrapper.putFloat(version);

		// Byte 4 -> 7: Request identifier
		byteWrapper.putInt(identifier);

		// Byte 8 -> 11: Error code
		byteWrapper.putInt(errorCode);

		// Getting the bytes array equivalent to the payload object
		byte[] data = wrapper.getBytes(payload);

		// Byte 12 -> 15: Payload length
		byteWrapper.putInt(data.length);

		// Byte 12 -> end: Request payload
		byteWrapper.put(data);

		return byteWrapper.get();
	}

	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(",", "{", "}");
		joiner.add("identifier=" + getIdentifier());

		String formatter = "errorCode=[value=%s,message=%s]";
		joiner.add(String.format(formatter, getErrorCode(), factory.getMessage(getErrorCode())));

		joiner.add("payload=" + payload);
		return joiner.toString();
	}

	/**
	 * Parse the content of the input wrapper. The input array shall have the
	 * following format:<br>
	 *
	 * Byte 0 -> 3: Error code
	 * Byte 4 -> 7: Payload length
	 * Byte 8 -> 8 + length: payload
	 * 
	 * @param byteWrapper The wrapper that contains request information.
	 */
	public IRequest parse(ReadableByteWrapper byteWrapper) {
		// Byte 0 -> 3: Error code
		errorCode = byteWrapper.nextInt();

		// Byte 4 -> 7: Payload length
		int length = byteWrapper.nextInt();

		// Byte 8 -> 8 + length: payload
		payload = wrapper.parse(byteWrapper.next(length));

		return this;
	}
}
