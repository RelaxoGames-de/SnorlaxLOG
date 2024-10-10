package de.relaxogames.SnorlaxLOG.shared.model;

import java.util.Date;
import java.util.List;

/**
 * This class is used to define a User in the system.
 * A user is the central object in the db and will be 
 * used to (de)serialize the data from the database and 
 * into the GameLogic.\
 * 
 * The User is composed of the following attributes:
 * - ID: The id of the user in the database
 * - UUID: The minecraft account uuid of the user
 * - NAME: The minecraft account name of the user
 * - FIRST_JOIN: The timestamp of the first join of the user
 * - LAST_JOIN: The timestamp of the last join of the user
 * - DISCORD_ID: The discord id of the user
 * - ONLINE_TIME: The online time of the user in seconds
 * - CACHED_IP: The ip of the user
 * - ROLES: The roles of the user
 * 
 * @version 1.0
 * @since 2.0
 * @see Role
 */
public class User {
    
    /**
     * The id of the user in the database.
     * 
     * @since 2.0
     */
    private static int ID;
    
    /**
     * The minecraft account uuid of the user.
     * This is used to identify the user definitly
     * both in the database and in the game.
     * 
     * @since 2.0
     */
    private static String UUID;
    
    /**
     * The minecraft account name of the user.
     * This is used to identify the user in the database.
     * 
     * @since 2.0
     */
    private static String NAME;
    
    /**
     * The timestamp of the first join of the user.
     * 
     * @since 2.0
     */
    private static Date FIRST_JOIN;
    
    /**
     * The timestamp of the last join of the user.
     * 
     * @since 2.0
     */
    private static Date LAST_JOIN;
    
    /**
     * The discord id of the user.
     * This is used to identify the user definitly
     * on discord, so this does not have to be set.
     * 
     * @since 2.0
     */
    private static String DISCORD_ID;
    
    /**
     * The online time of the user in seconds.
     * 
     * @since 2.0
     */
    private static int ONLINE_TIME;
    
    /**
     * The ip of the user.
     * 
     * @since 2.0
     */
    private static String CACHED_IP;
    
    /**
     * The roles of the user.
     * 
     * @since 2.0
     * @see Role
     */
    private static List<Role> ROLES;

    /**
     * Creates a new User with the given parameters.
     * 
     * @param ID The id of the user in the database.
     * @param UUID The minecraft account uuid of the user.
     * @param NAME The minecraft account name of the user.
     * @param FIRST_JOIN The timestamp of the first join of the user.
     * @param LAST_JOIN The timestamp of the last join of the user.
     * @param DISCORD_ID The discord id of the user.
     * @param ONLINE_TIME The online time of the user in seconds.
     * @param CACHED_IP The ip of the user.
     * @param ROLES The roles of the user.
     * @since 2.0
     */
    public User(int ID, String UUID, String NAME, Date FIRST_JOIN, Date LAST_JOIN, String DISCORD_ID, int ONLINE_TIME, String CACHED_IP, List<Role> ROLES) {
        User.ID = ID;
        User.UUID = UUID;
        User.NAME = NAME;
        User.FIRST_JOIN = FIRST_JOIN;
        User.LAST_JOIN = LAST_JOIN;
        User.DISCORD_ID = DISCORD_ID;
        User.ONLINE_TIME = ONLINE_TIME;
        User.CACHED_IP = CACHED_IP;
        User.ROLES = ROLES;
    }

    /**
     * Returns the id of the user.
     * 
     * @return The id of the user.
     * @since 2.0
     */
    public int getID() {
        return User.ID;
    }

    /**
     * Returns the minecraft account uuid of the user.
     * 
     * @return The minecraft account uuid of the user.
     * @since 2.0
     */
    public String getUUID() {
        return User.UUID;
    }

    /**
     * Returns the minecraft account name of the user.
     * 
     * @return The minecraft account name of the user.
     * @since 2.0
     */
    public String getNAME() {
        return User.NAME;
    }

    /**
     * Returns the timestamp of the first join of the user.
     * 
     * @return The timestamp of the first join of the user.
     * @since 2.0
     */
    public Date getFIRST_JOIN() {
        return User.FIRST_JOIN;
    }

    /**
     * Returns the timestamp of the last join of the user.
     * 
     * @return The timestamp of the last join of the user.
     * @since 2.0
     */
    public Date getLAST_JOIN() {
        return User.LAST_JOIN;
    }

    /**
     * Returns the discord id of the user.
     * 
     * @return The discord id of the user.
     * @since 2.0
     */
    public String getDISCORD_ID() {
        return User.DISCORD_ID;
    }

    /**
     * Returns the online time of the user in seconds.
     * 
     * @return The online time of the user in seconds.
     * @since 2.0
     */
    public int getONLINE_TIME() {
        return User.ONLINE_TIME;
    }

    /**
     * Returns the ip of the user.
     * 
     * @return The ip of the user.
     * @since 2.0
     */
    public String getCACHED_IP() {
        return User.CACHED_IP;
    }

    /**
     * Returns the roles of the user.
     * 
     * @return The roles of the user.
     * @since 2.0
     */
    public List<Role> getROLES() {
        return User.ROLES;
    }

    /**
     * Sets the minecraft account uuid of the user.
     * 
     * @param UUID The minecraft account uuid of the user.
     * @since 2.0
     */
    public void setUUID(String UUID) {
        User.UUID = UUID;
    }

    /**
     * Sets the minecraft account name of the user.
     * 
     * @param NAME The minecraft account name of the user.
     * @since 2.0
     */
    public void setNAME(String NAME) {
        User.NAME = NAME;
    }

    /**
     * Sets the timestamp of the first join of the user.
     * 
     * @param FIRST_JOIN The timestamp of the first join of the user.
     * @since 2.0
     */
    public void setFIRST_JOIN(Date FIRST_JOIN) {
        User.FIRST_JOIN = FIRST_JOIN;
    }

    /**
     * Sets the timestamp of the last join of the user.
     * 
     * @param LAST_JOIN The timestamp of the last join of the user.
     * @since 2.0
     */
    public void setLAST_JOIN(Date LAST_JOIN) {
        User.LAST_JOIN = LAST_JOIN;
    }

    /**
     * Sets the discord id of the user.
     * 
     * @param DISCORD_ID The discord id of the user.
     * @since 2.0
     */
    public void setDISCORD_ID(String DISCORD_ID) {
        User.DISCORD_ID = DISCORD_ID;
    }

    /**
     * Sets the online time of the user in seconds.
     * 
     * @param ONLINE_TIME The online time of the user in seconds.
     * @since 2.0
     */
    public void setONLINE_TIME(int ONLINE_TIME) {
        User.ONLINE_TIME = ONLINE_TIME;
    }

    /**
     * Sets the ip of the user.
     * 
     * @param CACHED_IP The ip of the user.
     * @since 2.0
     */
    public void setCACHED_IP(String CACHED_IP) {
        User.CACHED_IP = CACHED_IP;
    }

    /**
     * Sets the roles of the user.
     * 
     * @param ROLES The roles of the user.
     * @since 2.0
     */
    public void setROLES(List<Role> ROLES) {
        User.ROLES = ROLES;
    }

    /**
     * Adds a role to the user.
     * 
     * @param role The role to add.
     * @since 2.0
     */
    public void addRole(Role role) {
        User.ROLES.add(role);
    }

    /**
     * Removes a role from the user.
     * 
     * @param role The role to remove.
     * @since 2.0
     */
    public void removeRole(Role role) {
        User.ROLES.remove(role);
    }

    /**
     * Checks if the user has a role.
     * 
     * @param role The role to check.
     * @return True if the user has the role, false otherwise.
     * @since 2.0
     * @see Role
     */
    public boolean hasRole(Role role) {
        return User.ROLES.contains(role);
    }

    /**
     * Checks if the user has a role.
     * 
     * @param role The role to check.
     * @return True if the user has the role, false otherwise.
     * @since 2.0
     */
    public boolean hasRole(String role) {
        for (Role r : User.ROLES) {
            if (r.getName().equals(role)) {
                return true;
            }
        }
        return false;
    }
}
