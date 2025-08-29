# 1) Presentation

This project propose an easy way to define how data can be exchanged between a client and a server and preserve backward compatibility using protocol versions. The goal of this project is to be used with the [communication](https://github.com/Pierre-Emmanuel41/communication) project.


# 2) Download and compilation

First you need to download this project on your computer. To do so, you can use the following command line:

```git
git clone https://github.com/Pierre-Emmanuel41/protocol.git
```

Executing the batch file <code>deploy.bat</code> will download each dependency and build everything. Finally, you can add the project as maven dependency to your maven project :

```xml
<dependency>
	<groupId>fr.pederobien</groupId>
	<artifactId>protocol</artifactId>
	<version>1.0-SNAPSHOT</version>
</dependency>
```

# 3) Tutorial

A picture is worth a thousand words, but in our case a complete example is always better than a big description to understand how easy it is to create different communication protocol and to generate an array of bytes, or parsing an array of bytes, depending on the protocol version and on the payload to send:

```java
IProtocolManager manager = new ProtocolManager();
manager.getErrorCodeFactory().register(0, "No Error");

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
```

The Output:

```
Request with protocol 1.0: {identifier=1,errorCode=[value=0,message=No Error],payload={type=Player,name=Jack,age=30,city=Not defined}}
Bytes with protocol 1.0: [63,-128,0,0,0,0,0,1,0,0,0,0,0,0,0,22,0,0,0,6,80,108,97,121,101,114,0,0,0,4,74,97,99,107,0,0,0,30], size in bytes: 38
Received request match the sent request for protocol 1.0
Request with protocol 2.0: {identifier=2,errorCode=[value=0,message=No Error],payload={type=Player,name=Jack,age=30,city=Sea}}
Bytes with protocol 2.0: [64,0,0,0,0,0,0,2,0,0,0,0,0,0,0,29,0,0,0,6,80,108,97,121,101,114,0,0,0,4,74,97,99,107,0,0,0,30,0,0,0,3,83,101,97], size in bytes: 45
Received request match the sent request for protocol 2.0
```

The classes referenced in this example can be found in the testing folder.