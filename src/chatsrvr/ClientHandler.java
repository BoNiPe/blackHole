package chatsrvr;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import utilities.Protocols;
import utilities.SaveClients;

/**
 * This class provides the Server with the ability of handling X amount of
 * clients. It's both Thread and Entity class. It handles the actual
 * communication with the client via Scanner, which waits for the Client's input
 * (message) and a PrintWriter, which responds back to the client different type
 * of information.
 */
public class ClientHandler extends Thread
{

    private Scanner input;
    private PrintWriter writer;
    private Socket socket;
    private Server myServer;
    //Receives information directly from the server.
    private String listOfHandlerNames;
    //Handles the "DO-WHILE" Thread logic.
    private boolean isStopped = false;
    private boolean isIdentified = false;
    //Handles the entity aspect of the whole class.
    private String nickname;
    private Integer incorrectInputs;
    //Current Client Socket Information
    private String clientIP;
    private int clientPort;

    /**
     * Constructor, which initializes the connection between a particular client
     * and the Server.
     */
    public ClientHandler(Socket socket, Server myServer)
    {
        try
        {
            input = new Scanner(socket.getInputStream());
            writer = new PrintWriter(socket.getOutputStream(), true);
            this.socket = socket;
            this.myServer = myServer;
            clientIP = socket.getInetAddress().getHostAddress();
            clientPort = socket.getPort();
        }
        catch (IOException ex)
        {
            Logger.getLogger(Server.class.getName()).log(Level.WARNING, "***Exeption "
                    + "in the Client Handler Constructor : ", ex + "***");
        }
        start();
    }

    /**
     * The basic Thread logic. This method handles the inputs from the Client
     * and calls variety of method for further evaluation.
     */
    @Override
    public void run()
    {
        String message = "";
        //check with .split("\\#");
        do
        {
            //Waiting for the Client's input.
            message = input.nextLine();
            System.out.println(message);
            //Checks If Connected
            if (!isIdentified)
            {
                if (message.contains(Protocols.CONNECT))
                {
                    extractNickname(message);
                }
                else
                {
                    this.writer.println("Your input towards the server does not "
                            + "meet the required string : 'CONNECT#[your nickname]'"
                            + " .");
                    Logger.getLogger(Server.class.getName()).log(Level.WARNING,
                            "Input of Client with  IP:" + clientIP + ":"
                            + clientPort + "does not meet the required String "
                            + "'CONNECT#[your nickname]' .");
                }
            }
            else
            {
                if (message.contains(Protocols.ALL))
                {
                    System.out.println("Inside IF Public");
                    sendToPublic(message);
                }
                else if (message.contains(Protocols.NICKNAME))
                {
                    System.out.println("Inside IF Nickname");
                    extractNickname(message);
                }
                else if (message.equals(Protocols.CLOSE))
                {
                    System.out.println("Inside IF STOP");
                    isStopped = true; //Ending the DO-WHILE Cycle.
                }
                //Whispering since it also includes Protocol.SEND I was not able
                //to think of a proper condition for else if statement.
                else
                {
                    System.out.println("INSIDE IF PRIVATE");
                    sendToPrivate(message);
                }
            }
        } while (!isStopped);
        writer.println(Protocols.CLOSE);//Echo the stop message back to the client for a nice closedown 

        try
        {
            socket.close();
            myServer.removeHandler(this);
            sendOnlineUsers(); //Refreshing the list of active clients for the others, who are still connected.
        }
        catch (IOException ex)
        {
            Logger.getLogger(Server.class.getName()).log(Level.INFO, ">Exeption in ClientHandler", ex);
        }
        Logger.getLogger(Server.class.getName()).log(Level.INFO, ">Connection with "
                + this.getNickname() + " closed.");
    }

    /**
     * Method which generates a string which should be send to all active
     * Clients. It should be used only when the client input contains
     * Protocols.ALL. The Output string will contain Protocol.MESSAGE, name of
     * Sender, HashTag and the actual message. This message should be send to
     * all active clients.
     */
    private void sendToPublic(String message)
    {
        System.out.println("Hello from Public.");
        String[] result = message.split("#");
        //Checks if there is HashTag in actual message and If Client is sending 
        //a blank string : ""
        if (result.length == 3)
        {
            for (ClientHandler handler : myServer.getListofClients())
            {
                handler.writer.println(Protocols.MESSAGE + this.getNickname()
                        + Protocols.HashTag + result[2]);
            }
            //What is %1$S ?
            Logger.getLogger(Server.class.getName()).log(Level.INFO, ">Send from '"
                    + this.getNickname() + "' (" + clientIP + ":" + clientPort + ") "
                    + "to ALL : " + message);

        }
        else
        {
            Logger.getLogger(Server.class.getName()).log(Level.WARNING, "***"
                    + "Client '" + this.getNickname() + "' delivered unappropriate "
                    + "message containing either 'no value after hashTag' or "
                    + "'HashTag in message' (Wrong public message input)***");
        }

    }

    /**
     * Method which generates a string which should be send to specific Clients.
     * It should be used only when the client input contains Names and
     * Protocols.HashTag. The Output string will contain Protocol.MESSAGE, name
     * of Sender, HashTag and the actual message. Or said with other
     * words:"Whispering til en person eller mange mennesker"
     */
    private void sendToPrivate(String message)
    {
        boolean clientFound = false;
        String[] splitMessageString = message.split("#");
        if (splitMessageString.length == 3)
        {
            int size = myServer.getListofClients().size();
            if (splitMessageString[1].contains(","))
            { //, means that there are more than 1 names
                String[] listOfClientsToReceiveMessage = splitMessageString[1].
                        split(",");
                for (int i = 0; i < listOfClientsToReceiveMessage.length; i++)
                {
                    for (int j = 0; j < size; j++)
                    {
                        if (myServer.getListofClients().get(j).getNickname()
                                .equals(listOfClientsToReceiveMessage[i]))
                        {
                            myServer.getListofClients().get(j).writer.println(
                                    Protocols.MESSAGE + this.getNickname()
                                    + Protocols.HashTag + splitMessageString[2]);
                            Logger.getLogger(Server.class.getName()).log(
                                    Level.INFO, ">Send from :" + this.getNickname()
                                    + " to " + myServer.getListofClients().get(j)
                                    .getNickname() + " : " + splitMessageString[2]);
                            clientFound = true;
                        }
                    }
                }
                if (!clientFound)
                {
                    this.writer.print("Client(s) '" + splitMessageString[1] + ""
                            + "was not found.");
                    Logger.getLogger(Server.class.getName()).log(
                            Level.INFO, ">Client :" + this.getNickname()
                            + " tried to communicate with '" + splitMessageString[1]
                            + "', but the client(s) was not found.");
                }
            }
            else
            { //If there is only one name
                for (int j = 0; j < size; j++)
                {
                    if (myServer.getListofClients().get(j).getNickname()
                            .equals(splitMessageString[1]))
                    {
                        myServer.getListofClients().get(j).writer.println(
                                Protocols.MESSAGE + this.getNickname()
                                + Protocols.HashTag + splitMessageString[2]);
                        Logger.getLogger(Server.class.getName()).log(
                                Level.INFO, ">Send from :" + this.getNickname()
                                + " to " + myServer.getListofClients().get(j)
                                .getNickname() + " : " + splitMessageString[2]);
                        clientFound = true;
                    }
                }
                if (!clientFound)
                {
                    this.writer.print("Client(s) '" + splitMessageString[1] + ""
                            + "was not found.");
                    Logger.getLogger(Server.class.getName()).log(
                            Level.INFO, ">Client :" + this.getNickname()
                            + " tried to communicate with '" + splitMessageString[1]
                            + "', but the client(s) was not found.");
                }
            }
        }
        else
        {
            Logger.getLogger(Server.class.getName()).log(Level.WARNING, "***"
                    + "Client '" + this.getNickname() + "' included HashTag "
                    + "in the actual message, which is a forbidden symbol."
                    + "(Wrong private message input)***");
        }
    }

    /**
     * Method which saves the nickname of a particular Client, who connects or
     * attempts to change his/her nickname in real time by extracting the actual
     * Nickname from Protocols.CONNECT or Protocols.NICKNAME
     */
    private void extractNickname(String message)
    {
        String[] selectedNickname = message.split("#");
        //Checks if there is something inserted after Protocols.CONNECT or 
        //Protocols.NICKNAME and if the message contains an unnecessary HashTag
        if (selectedNickname.length == 2)
        {
            if (selectedNickname[1].length() <= 8)
            {
                //Checks If the client is connecting or renaming himself/herself
                if (message.contains(Protocols.CONNECT))
                {
                    Logger.getLogger(Server.class.getName()).log(Level.INFO, ">>>"
                            + "Client Connected with Nickname '" + selectedNickname[1]
                            + "'.");
                }
                else
                {
                    Logger.getLogger(Server.class.getName()).log(Level.INFO, ">>>"
                            + "Client '" + this.getNickname() + "' changed his/her"
                            + " nickname to '" + selectedNickname[1] + "'.");
                }
                setNickname(selectedNickname[1]); //Extract Client's nickname
                sendOnlineUsers(); //Update list of active users.
                isIdentified = true;
            }
            else
            {
                Logger.getLogger(Server.class.getName()).log(Level.WARNING, "***"
                        + "Nickname of Client with  IP:" + clientIP + ":"
                        + clientPort + " is too big. ...And NO, that's not what "
                        + "she said!***");
            }
        }
        else
        {
            //Checks If the client is connecting or renaming himself/herself
            if (selectedNickname[0].equals(Protocols.CONNECT))
            {
                Logger.getLogger(Server.class.getName()).log(Level.WARNING, "***"
                        + "No value after hashTag of Client with  IP:" + clientIP
                        + ":" + clientPort + " or HashTag in [nickname], which is "
                        + "a forbidden symbol. (Wrong connect input)***");
            }
            else
            {
                Logger.getLogger(Server.class.getName()).log(Level.WARNING, "***"
                        + "No value after hashTag of Client '" + this.getNickname()
                        + "' or HashTag in [nickname], which is a forbidden symbol."
                        + "(Wrong nickname input)***");
            }
        }
    }

    /**
     * Method which generates a string containing list of Online Clients which
     * should be send as a message to all active clients. It will be executed
     * after a client connects, disconnects or changes his/her nickname, as a
     * real time response.
     */
    private void sendOnlineUsers()
    {
        SaveClients sc = new SaveClients();
        listOfHandlerNames = "";

        Integer ammountOfClients = myServer.getListofClients().size();
        for (int i = 0; i < ammountOfClients; i++)
        {
            if (i < ammountOfClients - 1)
            {
                listOfHandlerNames += myServer.getListofClients().get(i)
                        .getNickname() + ",";
            }
            else
            {
                listOfHandlerNames += myServer.getListofClients().get(i)
                        .getNickname();
            }
        }
        for (int i = 0; i < ammountOfClients; i++)
        {

            myServer.getListofClients().get(i).writer.println(Protocols.ONLINE
                    + listOfHandlerNames);

        }
        sc.savingClients(listOfHandlerNames);
        //chatLogger.log(Level.INFO, "Online people: " + listOfHandlerNames);
        Logger.getLogger(Server.class.getName()).log(Level.INFO, String.format(
                "Online users: : %1$S ", listOfHandlerNames.toUpperCase()));

    }

    private void sendHelp()
    {
        //TBA
    }

    private String getNickname()
    {
        return nickname;
    }

    private void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    private Integer getIncorrectInputs()
    {

        //TBA
        return incorrectInputs;
    }

    private void setIncorrectInputs(Integer incorrectInputs)
    {
        //TBA
        this.incorrectInputs = incorrectInputs;
    }

}
