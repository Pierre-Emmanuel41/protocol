package fr.pederobien.protocol.testing;

import fr.pederobien.protocol.impl.ProtocolManager;
import fr.pederobien.protocol.interfaces.IError;
import fr.pederobien.protocol.interfaces.IProtocol;
import fr.pederobien.protocol.interfaces.IProtocolManager;
import fr.pederobien.protocol.interfaces.IRequest;
import fr.pederobien.utils.ByteWrapper;

public class ProtocolTest {

    public static void main(String[] args) {
        IProtocolManager manager = new ProtocolManager();
        manager.getErrorManager().register(Errors.NO_ERROR);

        // The request identifier
        int identifier = 1;

        // Registering protocol 1.0
        IProtocol protocol10 = manager.getOrCreate(1.0f);

        // The protocol 1.0 supports the request identifier 1
        // When an array of bytes needs to be parsed, the protocol version is extracted
        // and if the protocol is 1.0 and the request identifier is 1 the EntityWrapperV10
        // will be used to parse the bytes array.
        // When data has to be sent to the remote and the latest protocol that supports
        // the identifier 1 is the protocol 1.0 then the EntityWrapperV10 will be used
        // to generate the array of bytes.
        protocol10.register(identifier, new EntityWrapperV10());

        // Getting the request associated to the latest protocol: 1.0
        IRequest request = manager.get(identifier);

        Entity payload = new Entity("Player", "Jack", 30);
        request.setPayload(payload);

        String formatter = "Request with protocol 1.0: %s";
        System.out.println(String.format(formatter, request));

        // Simulating a request being sent to the remote
        byte[] data = request.getBytes();

        // Request structure:
        // Byte 0 -> 3: Protocol version number
        // Byte 4 -> 7: Request identifier
        // Byte 8 -> 11: Error code
        // Byte 12 -> 15: Payload length
        // Byte 16 -> 16 + length: Payload
        formatter = "Bytes with protocol 1.0: %s, size in bytes: %s";
        System.out.println(String.format(formatter, ByteWrapper.wrap(data), data.length));

        // Simulating a request being received from the remote
        IRequest received = manager.parse(data);
        if (received.getIdentifier() == identifier && received.getPayload().equals(payload)) {
            System.out.println("Received request match the sent request for protocol 1.0");
        } else
            System.out.println("An issue occurred");

        // Simulating an evolution of the Entity properties (field city added)
        IProtocol protocol20 = manager.getOrCreate(2.0f);

        identifier = 2;

        // The protocol 2.0 supports the request identifier 1
        // When an array of bytes needs to be parsed, the protocol version is extracted
        // and if the protocol is 2.0 and the request identifier is 1 the EntityWrapperV20
        // will be used to parse the bytes array. If the protocol version is 1.0 then
        // the EntityWrapperV10 will be used.
        // When data has to be sent to the remote and the latest protocol that supports
        // the identifier 1 is the protocol 2.0 then the EntityWrapperV20 will be used
        // to generate the array of bytes.
        protocol20.register(identifier, new EntityWrapperV20());

        // Getting the request associated to the latest protocol: 2.0
        request = manager.get(identifier);

        payload = new Entity("Player", "Jack", 30, "Sea");
        request.setPayload(payload);

        // Request structure:
        // Byte 0 -> 3: Protocol version number
        // Byte 4 -> 7: Request identifier
        // Byte 8 -> 11: Error code
        // Byte 12 -> 15: Payload length
        // Byte 16 -> 16 + length: Payload
        formatter = "Request with protocol 2.0: %s";
        System.out.println(String.format(formatter, request));

        // Simulating a request being sent to the remote
        data = request.getBytes();

        formatter = "Bytes with protocol 2.0: %s, size in bytes: %s";
        System.out.println(String.format(formatter, ByteWrapper.wrap(data), data.length));

        // Simulating a request being received from the remote
        received = manager.parse(data);
        if (received.getIdentifier() == identifier && received.getPayload().equals(payload)) {
            System.out.println("Received request match the sent request for protocol 2.0");
        } else
            System.out.println("An issue occurred");
    }

    private enum Errors implements IError {
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
}
