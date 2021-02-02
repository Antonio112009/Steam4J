add Java 15v
add MIT Lisence
add ..


# Steam4J
Steam4J is an open-source project that strives provides full wrapping of game server queries.

## Navigation

1. [Dependencies](##Dependencies)
1. [Dependencies](##Features)
1. [Dependencies](##Installation)


##Dependencies
This project requires JDK 11+  
Maven dependencies in the project:
* [Lombok](https://github.com/rzwitserloot/lombok): 1.18.16

##Features
The list of the features currently implemented in the project:
* [Rcon](https://developer.valvesoftware.com/wiki/Source_RCON_Protocol):
    * Authentication to server with IP and Port
    * Executing commands to the server and receiving answer
    * Receiving commands from the server **without sending commands**. Listing to game console
    * Support of multi-package answers
* [Server Queries](https://developer.valvesoftware.com/wiki/Server_queries)
    * A2S_INFO
    * A2S_PLAYER
    * A2S_RULES
    * A2S_PING and A2S_SERVERQUERY_GETCHALLENGE are depricated

##Installation
Download project from release page of the project  

or   

Add dependency to your project manager  
**Maven**:
```xml
<dependency>
    <artefactId>com.antonio112009</artefactId>
    <groupId>steam4j</groupId>
    <version>1.0.0</version>
</dependency>
```

## Code examples

### Rcon examples: 
Execute single command:
```java
public class SendingSingleCommand {
 
  public static void main(String[] args) {
    Rcon rcon = new Rcon("0.0.0.0", 21114);
    rcon.openStream();
    boolean isAuthenticated = rcon.authenticate(password);
    if(isAuthenticated) {
        String answerFromServer = rcon.sendCommand("ListPlayers");
        System.out.println("Answer: " + answerFromServer);
    }
    rcon.closeStream(); //optional if it's in the end of the program
  }
  
}
```

Execute multiple command:
```java
public class SendingMultipleCommand {
 
  public static void main(String[] args) {
    Rcon rcon = new Rcon("0.0.0.0", 21114);
    rcon.openStream();
    boolean isAuthenticated = rcon.authenticate(password);
    if(isAuthenticated) {
      System.out.println("Answer1: " + rcon.sendCommand("ListPlayers"));
      System.out.println("Answer2: " + rcon.sendCommand("GetCurrentScore"));
      System.out.println("Answer3: " + rcon.sendCommand("ChatToAll Hello world!"));
        System.out.println("Answer: " + answerFromServer);
    }
    rcon.closeStream(); //optional if it's in the end of the program
  }
  
}
```

Receiving non-stop messages from a server (no execution of commands). Usually console logs:
```java
public class ReceiveServerLogs {
 
  public static void main(String[] args) {
    Rcon rcon = new Rcon("0.0.0.0", 21114);
    rcon.openStream();
    boolean isAuthenticated = rcon.authenticate(password);
    while (isAuthenticated) {
        try {
            System.out.println("Answer: " + rcon.receivingServerMessages());
        } catch (IOException e) {
            e.printStackTrace();
            rcon.closeStream(); //optional if it's in the end of the program
            break;
        }
    }
  }
  
}
```

### Server Queries (A2S):
All objects represent response format of data that are shown in details on the [official Valve developer page](https://developer.valvesoftware.com/wiki/Server_queries). 

### Server information
Getting information of the server. Read more [here](https://developer.valvesoftware.com/wiki/Server_queries#A2S_INFO)
```java
public class GetServerInformation {
    
  public static void main(String[] args) {
    A2s a2s = new A2S("0.0.0.0", 27166);
    ServerInfo serverInfo = a2S.getServerInfo();
    System.out.println("Result:\n" + serverInfo.toString());
  }
  
}
```

### Server players
Getting information of the server. Read more [here](https://developer.valvesoftware.com/wiki/Server_queries#A2S_PLAYER)
```java
public class GetServerInformation {
    
  public static void main(String[] args) {
    A2s a2s = new A2S("0.0.0.0", 27166);
    Players players = a2S.getPlayers();
    System.out.println("Result:\n" + players.toString());
  }
  
}
```

### Server rules
Getting information of the server. Read more [here](https://developer.valvesoftware.com/wiki/Server_queries#A2S_RULES)
```java
public class GetServerInformation {
    
  public static void main(String[] args) {
    A2s a2s = new A2S("0.0.0.0", 27166);
    Rules rules = a2S.getRules();
    System.out.println("Result:\n" + rules.toString());
  }
  
}
```

## Contribution
The contribution template and format is currently under development. Anyway, you are welcome to contribute this project.
