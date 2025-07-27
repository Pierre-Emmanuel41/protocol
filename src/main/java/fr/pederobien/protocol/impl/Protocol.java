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
	private final Map<Integer, RequestConfig> configs;
	private final IErrorCodeFactory factory;

	/**
	 * Creates a protocol associated to a specific version.
	 * 
	 * @param version The protocol version
	 */
	public Protocol(float version, IErrorCodeFactory factory) {
		this.version = version;
		this.factory = factory;

		configs = new HashMap<Integer, RequestConfig>();
	}

	@Override
	public float getVersion() {
		return version;
	}

	@Override
	public void register(int identifier, IWrapper wrapper) {
		configs.put(identifier, new RequestConfig(identifier, wrapper));
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
		RequestConfig config = configs.get(identifier);

		// Check if identifier is supported
		if (config == null)
			return null;

		return new Request(version, factory, config.identifier(), 0, config.wrapper());
	}

	private record RequestConfig(int identifier, IWrapper wrapper) {
		/**
		 * Creates a request configuration.
		 *
		 * @param identifier The request identifier.
		 * @param wrapper    The wrapper that parse/generates a bytes array from an
		 *                   object payload.
		 */
		private RequestConfig {
		}

			/**
			 * @return The request identifier.
			 */
			@Override
			public int identifier() {
				return identifier;
			}

			/**
			 * @return The payload wrapper.
			 */
			@Override
			public IWrapper wrapper() {
				return wrapper;
			}
		}
}
