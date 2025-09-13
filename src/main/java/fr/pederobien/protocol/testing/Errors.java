package fr.pederobien.protocol.testing;

import fr.pederobien.protocol.interfaces.IError;

public enum Errors implements IError {
    NO_ERROR(0, "No Error");

    private int code;
    private String message;

    /**
     * Creates an error composed of a code and an explanation.
     *
     * @param code    The error code.
     * @param message The error explanation.
     */
    private Errors(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
