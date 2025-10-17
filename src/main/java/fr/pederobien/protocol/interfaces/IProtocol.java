package fr.pederobien.protocol.interfaces;

public interface IProtocol {

	/**
	 * @return The version of this protocol.
	 */
	float getVersion();

	/**
	 * Register a request for the given identifier and payload
	 *
	 * @param identifier The request identifier.
	 * @param wrapper    The request wrapper to generate/parse bytes.
	 */
	void register(IIdentifier identifier, IWrapper wrapper);

	/**
	 * Creates a new request to send to the remote if the given identifier is supported by the protocol.
	 *
	 * @param identifier The identifier of the request to create.
	 * @param error      The error code of the request.
	 * @param payload    The payload of the request
	 * @return The created request if the identifier is supported, null otherwise.
	 */
	IRequest get(IIdentifier identifier, IError error, Object payload);
}
