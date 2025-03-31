package fr.pederobien.protocol.testing;

import fr.pederobien.protocol.interfaces.IWrapper;
import fr.pederobien.utils.ByteWrapper;
import fr.pederobien.utils.ReadableByteWrapper;

public class EntityWrapperV20 implements IWrapper {

	@Override
	public byte[] getBytes(Object value) {
		if (value == null || !(value instanceof Entity))
			return new byte[0];

		ByteWrapper wrapper = ByteWrapper.create();

		Entity entity = (Entity) value;
		wrapper.putString(entity.getType(), true);
		wrapper.putString(entity.getName(), true);
		wrapper.putInt(entity.getAge());
		wrapper.putString(entity.getCity(), true);

		return wrapper.get();
	}

	@Override
	public Object parse(byte[] bytes) {
		ReadableByteWrapper wrapper = ReadableByteWrapper.wrap(bytes);

		String type = wrapper.nextString(wrapper.nextInt());
		String name = wrapper.nextString(wrapper.nextInt());
		int age = wrapper.nextInt();
		String city = wrapper.nextString(wrapper.nextInt());

		return new Entity(type, name, age, city);
	}
}
