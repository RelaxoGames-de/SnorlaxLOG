package de.relaxogames.SnorlaxLOG.shared.model;

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
}
