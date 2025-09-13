package fr.pederobien.protocol.impl;

import fr.pederobien.protocol.interfaces.*;
import fr.pederobien.utils.ReadableByteWrapper;

import java.util.*;

public class ProtocolManager implements IProtocolManager {
    private final NavigableMap<Float, Protocol> protocols;
    private final ErrorManager manager;

    /**
     * Creates a protocol manager to insure the backward compatibility.
     */
    public ProtocolManager() {
        protocols = new TreeMap<Float, Protocol>();
        manager = new ErrorManager();
    }

    @Override
    public IProtocol getOrCreate(float version) {
        Protocol protocol = protocols.get(version);
        if (protocol == null) {
            protocol = new Protocol(version, manager);
            protocols.put(version, protocol);
        }

        return protocol;
    }

    @Override
    public IRequest get(int identifier) {
        Iterator<Map.Entry<Float, Protocol>> iterator = protocols.descendingMap().entrySet().iterator();
        IRequest request = null;

        while (iterator.hasNext() && request == null) {
            request = iterator.next().getValue().get(identifier);
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
    public IErrorManager getErrorManager() {
        return manager;
    }

    private static class ErrorManager implements IErrorManager {
        private static final String NOT_SUPPORTED = "CODE_NOT_SUPPORTED";
        private final Map<Integer, String> errors;

        public ErrorManager() {
            errors = new HashMap<Integer, String>();
        }

        @Override
        public void register(IError error) {
            String message = errors.get(error.getCode());

            // Error not yet registered
            if (message == null)
                errors.put(error.getCode(), error.getMessage());
        }

        @Override
        public void register(List<IError> errors) {
            for (IError error : errors)
                register(error);
        }

        @Override
        public String getMessage(int value) {
            String message = errors.get(value);
            return message == null ? NOT_SUPPORTED : message;
        }
    }
}
