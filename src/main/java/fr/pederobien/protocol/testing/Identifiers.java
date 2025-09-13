package fr.pederobien.protocol.testing;

import fr.pederobien.protocol.interfaces.IIdentifier;

public enum Identifiers implements IIdentifier {
    ID_1(1, "Dummy ID 1"),
    ID_2(2, "Dummy_ID_2");

    private int code;
    private String message;

    /**
     * Creates an identifier
     *
     * @param code    The code associated to this identifier.
     * @param message The explanation associated to this identifier.
     */
    Identifiers(int code, String message) {
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
