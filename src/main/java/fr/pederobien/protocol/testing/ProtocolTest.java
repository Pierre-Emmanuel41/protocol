package fr.pederobien.protocol.testing;

import fr.pederobien.protocol.impl.ProtocolManager;
import fr.pederobien.protocol.interfaces.IProtocol;
import fr.pederobien.protocol.interfaces.IProtocolManager;
import fr.pederobien.protocol.interfaces.IRequest;
import fr.pederobien.utils.ByteWrapper;

public class ProtocolTest {

    public static void main(String[] args) {
        IProtocolManager manager = new ProtocolManager();
        manager.registerErrors(Errors.NO_ERROR);

        // Registering protocol 1.0
        IProtocol protocol10 = manager.getOrCreate(1.0f);

        // The protocol 1.0 supports the request identifier 1
        // When an array of bytes needs to be parsed, the protocol version is extracted
        // and if the protocol is 1.0 and the request identifier is 1 the EntityWrapperV10
        // will be used to parse the bytes array.
        // When data has to be sent to the remote and the latest protocol that supports
        // the identifier 1 is the protocol 1.0 then the EntityWrapperV10 will be used
        // to generate the array of bytes.
        protocol10.register(Identifiers.ID_1, new EntityWrapperV10());

        // Getting the request associated to the latest protocol: 1.0
        Object payload = new Entity("Player", "Jack", 30);
        IRequest request = manager.get(Identifiers.ID_1, Errors.NO_ERROR, payload);

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
        if (received.getIdentifier() == Identifiers.ID_1 && received.getPayload().equals(payload)) {
            System.out.println("Received request match the sent request for protocol 1.0");
        } else
            System.out.println("An issue occurred");

        // Simulating an evolution of the Entity properties (field city added)
        IProtocol protocol20 = manager.getOrCreate(2.0f);

        // The protocol 2.0 supports the request identifier 1
        // When an array of bytes needs to be parsed, the protocol version is extracted
        // and if the protocol is 2.0 and the request identifier is 1 the EntityWrapperV20
        // will be used to parse the bytes array. If the protocol version is 1.0 then
        // the EntityWrapperV10 will be used.
        // When data has to be sent to the remote and the latest protocol that supports
        // the identifier 1 is the protocol 2.0 then the EntityWrapperV20 will be used
        // to generate the array of bytes.
        protocol20.register(Identifiers.ID_2, new EntityWrapperV20());

        // Getting the request associated to the latest protocol: 2.0
        payload = new Entity("Player", "Jack", 30, "Sea");
        request = manager.get(Identifiers.ID_2, Errors.NO_ERROR, payload);

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
        if (received.getIdentifier() == Identifiers.ID_2 && received.getPayload().equals(payload)) {
            System.out.println("Received request match the sent request for protocol 2.0");
        } else
            System.out.println("An issue occurred");
    }
}
