import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.util.*;

public class ChatRoomImpl extends UnicastRemoteObject implements ChatRoom {
    
    // Liste pour garder en mémoire les utilisateurs connectés
    private ArrayList<ChatUser> users = new ArrayList<ChatUser>();

    public ChatRoomImpl() throws RemoteException {
        super();
    }

    public void subscribe(ChatUser user, String pseudo) throws RemoteException {
        if (!users.contains(user)) {
            users.add(user);
            postMessage("SERVEUR", pseudo + " a rejoint la discussion.");
            System.out.println(pseudo + " s'est connecté.");
        }
    }

    public void unsubscribe(String pseudo) throws RemoteException {
        postMessage("SERVEUR", pseudo + " a quitté la discussion.");
    }

    public void postMessage(String pseudo, String message) throws RemoteException {
        String fullMessage = pseudo + " > " + message;
        System.out.println(fullMessage); 
        
        for (ChatUser u : users) {
            try {
                u.displayMessage(fullMessage);
            } catch (RemoteException e) {
                // Ignore errors
            }
        }
    }

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            ChatRoomImpl server = new ChatRoomImpl();
            Naming.rebind("rmi://localhost/ChatRoom", server);
            System.out.println("--- Serveur ChatRoom est lancé ! ---");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}