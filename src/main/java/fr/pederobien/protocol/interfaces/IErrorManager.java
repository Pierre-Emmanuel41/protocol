package fr.pederobien.protocol.interfaces;

import java.util.List;

public interface IErrorManager {

    /**
     * Register an error. It is composed of a code and a message explaining the rror.
     *
     * @param error The error to register.
     */
    void register(IError error);

    /**
     * Register each error from the given list if it is not yet registered.
     *
     * @param errors The list of errors to register.
     */
    void register(List<IError> errors);

    /**
     * Get the message associated to the given value.
     *
     * @param value The error code value
     * @return The message associated to the value, or "VALUE_NOT_SUPPORTED" if the
     * error code value if not supported.
     */
    String getMessage(int value);
}
