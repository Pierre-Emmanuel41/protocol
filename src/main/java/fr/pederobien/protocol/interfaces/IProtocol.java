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
}
