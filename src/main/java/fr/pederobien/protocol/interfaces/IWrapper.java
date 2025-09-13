package fr.pederobien.protocol.interfaces;

public interface IWrapper {

    /**
     * From the input object returns an array of bytes.
     *
     * @param value The object from which an array of bytes is created.
     * @return An array of bytes.
     */
    byte[] getBytes(Object value);

    /**
     * Creates an object from the given array of bytes.
     *
     * @param bytes The array of bytes used to create an object.
     * @return The created object.
     */
    Object parse(byte[] bytes);
}
