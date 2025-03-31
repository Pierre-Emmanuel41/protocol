package fr.pederobien.protocol.interfaces;

public interface IErrorCodeFactory {

	/**
	 * Creates an error code based on the given value and message.
	 * 
	 * @param value   The error code value.
	 * @param message The message, ie explanation, of the error code.
	 */
	void register(int value, String message);

	/**
	 * Get the message associated to the given value.
	 * 
	 * @param value The error code value
	 * 
	 * @return The message associated to the value, or "VALUE_NOT_SUPPORTED" if the
	 *         error code value if not supported.
	 */
	String getMessage(int value);
}
