package fr.pederobien.protocol.testing;

import java.util.StringJoiner;

public class Entity {
    private final String type;
    private final String name;
    private final int age;
    private final String city;

    /**
     * Creates an entity.
     *
     * @param type The entity type
     * @param name The entity name
     * @param age  The entity age.
     * @param city The entity city.
     */
    public Entity(String type, String name, int age, String city) {
        this.type = type;
        this.name = name;
        this.age = age;
        this.city = city;
    }

    /**
     * Creates an entity.
     *
     * @param type The entity type
     * @param name The entity name
     * @param age  The entity age.
     */
    public Entity(String type, String name, int age) {
        this(type, name, age, "Not defined");
    }

    /**
     * @return The entity type, available from protocol 1.0
     */
    public String getType() {
        return type;
    }

    /**
     * @return The entity name, available from protocol 1.0
     */
    public String getName() {
        return name;
    }

    /**
     * @return The entity age, available from protocol 1.0
     */
    public int getAge() {
        return age;
    }

    /**
     * @return The entity city, available from protocol 2.0
     */
    public String getCity() {
        return city;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (!(obj instanceof Entity other))
            return false;

        return type.equals(other.type) && name.equals(other.name) && age == other.age && city.equals(other.city);
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(",", "{", "}");
        joiner.add("type=" + getType());
        joiner.add("name=" + getName());
        joiner.add("age=" + getAge());
        joiner.add("city=" + getCity());

        return joiner.toString();
    }
}
