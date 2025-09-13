package fr.pederobien.protocol.impl;

import fr.pederobien.protocol.interfaces.*;
import fr.pederobien.utils.ReadableByteWrapper;

import java.util.HashMap;
import java.util.Map;

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

    /**
     * Creates a new request to send to the remote if the given identifier is
     * supported by the protocol.
     *
     * @param identifier The identifier of the request to create.
     * @param error      The error code of the request.
     * @param payload    The payload of the request
     * @return The created request if the identifier is supported, null otherwise.
     */
    protected IRequest get(IIdentifier identifier, IError error, Object payload) {
        IWrapper wrapper = wrappers.get(identifier);
        if (wrapper == null)
            return null;

        return new Request(version, identifier, error, payload, wrapper);
    }

    /**
     * Parse the content of the input wrapper. The input array shall have the
     * following format:<br>
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

        Object payload = null;

        if (length > 0)
            // Byte 12 -> 12 + length: payload
            payload = entry.getValue().parse(wrapper.next(length));

        return new Request(version, entry.getKey(), error, payload, entry.getValue());
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
