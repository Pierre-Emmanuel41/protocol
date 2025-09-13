package fr.pederobien.protocol.impl;

import fr.pederobien.protocol.interfaces.IError;
import fr.pederobien.protocol.interfaces.IIdentifier;
import fr.pederobien.protocol.interfaces.IRequest;
import fr.pederobien.protocol.interfaces.IWrapper;
import fr.pederobien.utils.ByteWrapper;

import java.util.StringJoiner;

public class Request implements IRequest {
    private final float version;
    private final IIdentifier identifier;
    private final IError error;
    private final Object payload;
    private final IWrapper wrapper;

    /**
     * Creates a message to send to the remote.
     *
     * @param version    The protocol version.
     * @param identifier The request identifier.
     * @param error      The request error code.
     * @param wrapper    The request wrapper that contains the bytes generator and parser.
     */
    public Request(float version, IIdentifier identifier, IError error, Object payload, IWrapper wrapper) {
        this.version = version;
        this.identifier = identifier;
        this.error = error;
        this.payload = payload;
        this.wrapper = wrapper;
    }

    @Override
    public float getVersion() {
        return version;
    }

    @Override
    public IIdentifier getIdentifier() {
        return identifier;
    }

    @Override
    public IError getError() {
        return error;
    }

    @Override
    public Object getPayload() {
        return payload;
    }

    @Override
    public byte[] getBytes() {
        ByteWrapper byteWrapper = ByteWrapper.create();

        // Byte 0 -> 3: Protocol version
        byteWrapper.putFloat(version);

        // Byte 4 -> 7: Request identifier
        byteWrapper.putInt(identifier.getCode());

        // Byte 8 -> 11: Error code
        byteWrapper.putInt(error.getCode());

        if (payload == null)
            // Byte 12 -> 15: Payload length
            byteWrapper.putInt(0);
        else {
            // Getting the bytes array equivalent to the payload object
            byte[] data = wrapper.getBytes(payload);

            // Payload does not have the correct datatype
            if (data == null)
                // Byte 12 -> 15: Payload length
                byteWrapper.putInt(0);
            else {

                // Byte 12 -> 15: Payload length
                byteWrapper.putInt(data.length);

                // Byte 12 -> end: Request payload
                byteWrapper.put(data);
            }
        }

        return byteWrapper.get();
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(",", "{", "}");
        joiner.add("identifier=" + String.format("{code=%s, message=%s}", identifier.getCode(), identifier.getMessage()));
        joiner.add("error=" + String.format("{code=%s, message=%s}", error.getCode(), error.getMessage()));
        joiner.add("payload=" + payload);
        return joiner.toString();
    }
}
