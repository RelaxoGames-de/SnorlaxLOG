package de.relaxogames.SnorlaxLOG.shared.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to define the roles of the users.
 * A role is later on used to define the permissions of the user
 * and is composed of the string name of the role. 
 * 
 * The format of the String will be "snorlaxlog.*", "relaxogames.*"
 * and so on
 * 
 * @version 1.0
 * @since 2.0
 * @see User
 */
public class Role {

    /**
     * The name of the role.
     * 
     * @since 2.0
     */
    private static String NAME;

    /**
     * Creates a new Role with the given name.
     * 
     * @param NAME The name of the role.
     * @since 2.0
     */
    public Role(String NAME) {
        Role.NAME = NAME;
    }

    /**
     * Returns the name of the role.
     * 
     * @return The name of the role.
     * @since 2.0
     */
    public String getName() {
        return Role.NAME;
    }

    /**
     * A simple function to deserialize a List of Roles
     * in the db into a List of Role Classes
     * 
     * @param roleString the Roles as a String
     * @return The deserialized Roles
     * 
     * @see Role
     * @since 2.0
     * @version 1.0
     */
    public static List<Role> fromString(String roleString) {
        String[] rStringArr = roleString.split(";");
        List<Role> roles = new ArrayList<>();

        for (String rString : rStringArr) roles.add(new Role(rString));

        return roles;
    }

    /**
     * A simple function to serialize a List of Roles
     * in the db into a String
     * 
     * @param roles the Roles as a List
     * @return The serialized Roles
     * 
     * @see Role
     * @since 2.0
     * @version 1.0
     */
    public static String toString(List<Role> roles) {
        StringBuilder sb = new StringBuilder();

        for (Role role : roles) sb.append(role.getName()).append(";");

        return sb.toString();
    }
}
