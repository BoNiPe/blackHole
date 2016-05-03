package utilities;

/**
 * Protocol Strings are used for verifying the communication between the Client
 * and the chat Server. Using those protocol Strings the process becomes thread
 * save and easy to handle. If the Client uses a GUI the protocol String will be
 * automatically generated. If not (telnet for ex.) the Client will be forced to
 * write them manually.
 */
public class Protocols {

    public static final String CONNECT = "CONNECT#";

    public static final String SEND = "SEND#";

    public static final String CLOSE = "CLOSE#";

    public static final String STATUS = "CONNECT#";

    public static final String MESSAGE = "MESSAGE#";

    public static final String NICKNAME = "NICKNAME#"; //Realtime nick changing.

    public static final String ONLINE = "ONLINE#";

    public static final String HashTag = "#"; //Neeed for private messages.

    public static final String ALL = "*#"; //Needed for public messages.
    
    public static final String rcon = "RCON_PASSWORD#"; //Admin menu.
    
}
