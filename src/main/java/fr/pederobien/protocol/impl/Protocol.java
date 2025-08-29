package fr.pederobien.protocol.impl;

import java.util.HashMap;
import java.util.Map;

import fr.pederobien.protocol.interfaces.IErrorCodeFactory;
import fr.pederobien.protocol.interfaces.IProtocol;
import fr.pederobien.protocol.interfaces.IRequest;
import fr.pederobien.protocol.interfaces.IWrapper;
import fr.pederobien.utils.ReadableByteWrapper;

public class Protocol implements IProtocol {
	private final float version;
	private final Map<Integer, IWrapper> wrappers;
	private final IErrorCodeFactory factory;

	/**
	 * Creates a protocol associated to a specific version.
	 * 
	 * @param version The protocol version
	 */
	public Protocol(float version, IErrorCodeFactory factory) {
		this.version = version;
		this.factory = factory;

		wrappers = new HashMap<Integer, IWrapper>();
	}

	@Override
	public float getVersion() {
		return version;
	}

	@Override
	public void register(int identifier, IWrapper wrapper) {
		IWrapper registered = wrappers.get(identifier);

		// Check if there is a wrapper registered for the given identifier
		if (registered != null)
			throw new IllegalArgumentException("Cannot register a wrapper for the given identifier, a wrapper already exist");

		wrappers.put(identifier, wrapper);
	}

	/**
	 * Creates a new request to send to the remote if the given identifier is
	 * supported by the protocol.
	 * 
	 * @param identifier The identifier of the request to create.
	 * 
	 * @return The created request if the identifier is supported, null otherwise.
	 */
	public IRequest get(int identifier) {
		return generateRequest(identifier);
	}

	/**
	 * Parse the content of the input wrapper. The input array shall have the
	 * following format:<br>
	 * <br>
	 * Byte 0 -> 3: Message identifier<br>
	 * Byte 4 -> 7: Error code<br>
	 * Byte 8 -> end: Payload<br>
	 * 
	 * @param wrapper The wrapper that contains request information.
	 */
	public IRequest parse(ReadableByteWrapper wrapper) {
		// Byte 0 -> 3: Request identifier
		Request request = generateRequest(wrapper.nextInt());
		if (request == null) {
			return null;
		}

		return request.parse(wrapper);
	}

	/**
	 * Check if the identifier is supported by this protocol. If so, returns a new
	 * Request associated to the given identifier.
	 * 
	 * @param identifier The request identifier.
	 * 
	 * @return The created request.
	 */
	private Request generateRequest(int identifier) {
		IWrapper wrapper = wrappers.get(identifier);

		// Check if identifier is supported
		if (wrapper == null)
			return null;

		return new Request(version, factory, identifier, 0, wrapper);
	}
}
