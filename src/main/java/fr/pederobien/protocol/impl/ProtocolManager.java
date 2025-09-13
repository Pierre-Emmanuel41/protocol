package fr.pederobien.protocol.impl;

import fr.pederobien.protocol.interfaces.*;
import fr.pederobien.utils.ReadableByteWrapper;

import java.util.*;

public class ProtocolManager implements IProtocolManager {
    private final NavigableMap<Float, Protocol> protocols;
    private final Map<Integer, IError> errors;

    /**
     * Creates a protocol manager to insure the backward compatibility.
     */
    public ProtocolManager() {
        protocols = new TreeMap<Float, Protocol>();
        errors = new HashMap<Integer, IError>();
    }

    @Override
    public IProtocol getOrCreate(float version) {
        Protocol protocol = protocols.get(version);
        if (protocol == null) {
            protocol = new Protocol(version, this);
            protocols.put(version, protocol);
        }

        return protocol;
    }

    @Override
    public IRequest get(IIdentifier identifier, IError error, Object payload) {
        Iterator<Map.Entry<Float, Protocol>> iterator = protocols.descendingMap().entrySet().iterator();
        IRequest request = null;

        while (iterator.hasNext() && request == null) {
            request = iterator.next().getValue().get(identifier, error, payload);
        }

        return request;
    }

    @Override
    public IRequest parse(byte[] data) {
        ReadableByteWrapper wrapper = ReadableByteWrapper.wrap(data);

        // Byte 0 -> 3: Protocol version
        float version = wrapper.nextFloat();

        Protocol protocol = protocols.get(version);
        if (protocol == null) {
            return null;
        }

        return protocol.parse(wrapper);
    }

    @Override
    public void registerErrors(IError... errors) {
        for (IError error : errors) {
            IError e = this.errors.get(error.getCode());

            // Error not yet registered
            if (e == null)
                this.errors.put(error.getCode(), error);
        }
    }

    @Override
    public IError getError(int code) {
        return errors.get(code);
    }
}
