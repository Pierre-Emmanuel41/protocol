package fr.pederobien.protocol.impl;

import java.util.HashMap;
import java.util.Map;

import fr.pederobien.protocol.interfaces.IError;
import fr.pederobien.protocol.interfaces.IIdentifier;
import fr.pederobien.protocol.interfaces.IProtocol;
import fr.pederobien.protocol.interfaces.IRequest;
import fr.pederobien.protocol.interfaces.IWrapper;
import fr.pederobien.utils.ReadableByteWrapper;
import fr.pederobien.utils.event.Logger;

public class Protocol implements IProtocol {
	private final float version;
	private final Map<IIdentifier, IWrapper> wrappers;
	private final ProtocolManager manager;

	/**
	 * Creates a protocol associated to a specific version.
	 *
	 * @param version The protocol version
	 */
	public Protocol(float version, ProtocolManager manager) {
		this.version = version;
		this.manager = manager;

		wrappers = new HashMap<IIdentifier, IWrapper>();
	}

	@Override
	public float getVersion() {
		return version;
	}

	@Override
	public void register(IIdentifier identifier, IWrapper wrapper) {
		IWrapper registered = wrappers.get(identifier);

		// Check if there is a wrapper registered for the given identifier
		if (registered != null)
			throw new IllegalArgumentException("Cannot register a wrapper for the given identifier, a wrapper already exist");

		wrappers.put(identifier, wrapper);
	}

	@Override
	public IRequest get(IIdentifier identifier, IError error, Object payload) {
		IWrapper wrapper = wrappers.get(identifier);
		if (wrapper == null)
			return null;

		IRequest request = new Request(version, identifier, error, payload, wrapper);
		Logger.debug("Created request: %s", request);
		return request;
	}

	/**
	 * Parse the content of the input wrapper. The input array shall have the following format:<br>
	 * <p>
	 * Byte 0 -> 3: Request identifier<br>
	 * Byte 4 -> 7: Error code<br>
	 * Byte 8 -> 11: Payload length<br>
	 * Byte 12 -> end: Payload
	 *
	 * @param wrapper The wrapper that contains request information.
	 */
	protected IRequest parse(ReadableByteWrapper wrapper) {
		// Byte 0 -> 3: Request identifier
		int id = wrapper.nextInt();

		Map.Entry<IIdentifier, IWrapper> entry = getIdentifier(id);

		// The identifier is not supported by this protocol
		if (entry == null)
			return null;

		// Byte 4 -> 7: Error code
		IError error = manager.getError(wrapper.nextInt());

		// Byte 8 -> 11: Payload length
		int length = wrapper.nextInt();

		// Byte 12 -> 12 + length: payload
		Object payload = entry.getValue().parse(wrapper.next(length));

		IRequest request = new Request(version, entry.getKey(), error, payload, entry.getValue());
		Logger.debug("Parsed request: %s", request);
		return request;
	}

	/**
	 * Get the entry that gather the identifier and the wrapper.
	 *
	 * @param code The code of the identifier.
	 * @return The associated entry if it exists, null otherwise.
	 */
	private Map.Entry<IIdentifier, IWrapper> getIdentifier(int code) {
		for (Map.Entry<IIdentifier, IWrapper> entry : wrappers.entrySet()) {
			if (entry.getKey().getCode() == code)
				return entry;
		}

		return null;
	}
}
