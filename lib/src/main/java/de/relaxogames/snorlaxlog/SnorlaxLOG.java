package de.relaxogames.snorlaxlog;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

public class SnorlaxLOG {
    private static String username;
    private static String password;
    private static String apiURlString;
    private static UserRole userRole;
    private static String jwtToken;

    public SnorlaxLOG(String username, String password) {
        SnorlaxLOG.username = username;
        SnorlaxLOG.password = password;
        SnorlaxLOG.apiURlString = "localhost:8000";
        SnorlaxLOG.userRole = UserRole.USER;
    }

    public SnorlaxLOG(String username, String password, String apiURlString) {
        SnorlaxLOG.username = username;
        SnorlaxLOG.password = password;
        SnorlaxLOG.apiURlString = apiURlString;
        SnorlaxLOG.userRole = UserRole.USER;
    }

    public SnorlaxLOG(String username, String password, String apiURlString, UserRole userRole) {
        SnorlaxLOG.username = username;
        SnorlaxLOG.password = password;
        SnorlaxLOG.apiURlString = apiURlString;
        SnorlaxLOG.userRole = userRole;
    }
}
