package fr.pederobien.protocol.interfaces;

public interface IError {

    /**
     * @return The code of the error
     */
    int getCode();

    /**
     * @return Th message error.
     */
    String getMessage();
}
