package fr.pederobien.protocol.interfaces;

public interface IProtocolManager {

    /**
     * Creates a protocol if no protocol is existing for the given version.
     *
     * @param version The protocol version to get or to create.
     * @return The protocol associated to the given version.
     */
    IProtocol getOrCreate(float version);

    /**
     * Find the latest protocol version that support the given identifier. If a
     * protocol supports the identifier then a request will be created.
     *
     * @param identifier The identifier of the request to generate.
     * @param error      The error code of the request.
     * @param payload    The payload of the request.
     * @return The request if the identifier is supported, null otherwise.
     */
    IRequest get(IIdentifier identifier, IError error, Object payload);

    /**
     * Parse the given bytes array. The input array shall have the following
     * format:<br>
     * <p>
     * Byte 0 -> 3: Protocol version<br>
     * Byte 4 -> 7: Request identifier<br>
     * Byte 8 -> 11: Error code<br>
     * Byte 12 -> 15: Payload length<br>
     * Byte 15 -> end: Payload
     *
     * @param data The bytes array that contains message information.
     * @return The message corresponding to the content of the bytes array, null if
     * the protocol version is not supported or if the message identifier is
     * not supported for the protocol version.
     */
    IRequest parse(byte[] data);

    /**
     * Register each error from the given list if it is not yet registered.
     *
     * @param errors The list of errors to register.
     */
    void registerErrors(IError... errors);

    /**
     * Get the message associated to the given code.
     *
     * @param code The error code value
     * @return The error object associated to the given code.
     */
    IError getError(int code);
}
