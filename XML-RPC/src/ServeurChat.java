import org.apache.xmlrpc.WebServer;
import java.util.Vector;

public class ServeurChat {

    // Notre historique de messages
    // On utilise Vector car c'est ce que XML-RPC gère le mieux par défaut
    private Vector historique = new Vector();

    // Méthode 1 : Recevoir un message
    public boolean posterMessage(String pseudo, String message) {
        String format = pseudo + " > " + message;
        System.out.println("Nouveau message : " + format);
        historique.add(format);
        return true;
    }

    // Méthode 2 : Donner les messages manquants à un client
    // Le client dit "J'ai déjà 5 messages, donne-moi la suite"
    public Vector lireMessages(int indexDernierMessage) {
        Vector nouveauxMessages = new Vector();
        
        // Si le serveur a plus de messages que le client n'en connait
        if (historique.size() > indexDernierMessage) {
            // On copie les messages manquants
            for (int i = indexDernierMessage; i < historique.size(); i++) {
                nouveauxMessages.add(historique.get(i));
            }
        }
        return nouveauxMessages;
    }

    public static void main(String[] args) {
        try {
            System.out.println("Lancement du Serveur Chat XML-RPC...");
            WebServer server = new WebServer(8080);
            server.addHandler("chat", new ServeurChat());
            server.start();
            System.out.println("--- Serveur Prêt (Port 8080) ---");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}