package fr.pederobien.protocol.interfaces;

public interface IRequest {

	/**
	 * @return The version of the communication protocol
	 */
	float getVersion();

	/**
	 * @return The request identifier.
	 */
	int getIdentifier();

	/**
	 * @return The request error code value.
	 */
	int getErrorCode();

	/**
	 * Set the error code of the request.
	 * 
	 * @param errorCode The value of the error code.
	 */
	void setErrorCode(int errorCode);

	/**
	 * Set the payload object to sent/received from the remote.
	 * 
	 * @param payload The payload of the request.
	 */
	void setPayload(Object payload);

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
