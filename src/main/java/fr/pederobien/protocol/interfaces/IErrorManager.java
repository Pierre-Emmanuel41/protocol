package fr.pederobien.protocol.interfaces;

public interface IErrorManager {

    /**
     * Register an error. It is composed of a code and a message explaining the rror.
     *
     * @param error The error to register.
     */
    void register(IError error);

    /**
     * Get the message associated to the given value.
     *
     * @param value The error code value
     * @return The message associated to the value, or "VALUE_NOT_SUPPORTED" if the
     * error code value if not supported.
     */
    String getMessage(int value);
}
