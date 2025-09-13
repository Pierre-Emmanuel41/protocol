package fr.pederobien.protocol.interfaces;

public interface IRequest {

    /**
     * @return The version of the communication protocol
     */
    float getVersion();

    /**
     * @return The request identifier.
     */
    IIdentifier getIdentifier();

    /**
     * @return The request error code value.
     */
    IError getError();

    /**
     * @return The payload object of this request.
     */
    Object getPayload();

    /**
     * Generates a bytes array with the following format:<br>
     * <br>
     * Byte 0 -> 3: Protocol version<br>
     * Byte 4 -> 7: Request identifier<br>
     * Byte 8 -> 11: Error code<br>
     * Byte 12 -> 15: Payload length<br>
     * Byte 15 -> end: Payload
     *
     * @return The bytes array to send to the remote.
     */
    byte[] getBytes();
}
