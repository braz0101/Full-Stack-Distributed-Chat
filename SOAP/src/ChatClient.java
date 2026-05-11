import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import javax.xml.namespace.QName;
import java.util.Scanner;

public class ChatClient {
    private static String[] derniersMessages = new String[0];

    public static void main(String[] args) {
        try {
            RPCServiceClient serviceClient = new RPCServiceClient();
            Options options = serviceClient.getOptions();
            options.setTo(new EndpointReference("http://localhost:8080/axis2/services/ChatService"));

            Scanner scanner = new Scanner(System.in);
            System.out.print("Pseudo : ");
            String pseudo = scanner.nextLine();

            // --- THREAD DE REFRESH AUTOMATIQUE ---
            Thread refreshThread = new Thread(() -> {
                try {
                    QName opRecup = new QName("http://ws.apache.org/axis2", "recupererMessages");
                    while (true) {
                        Object[] response = serviceClient.invokeBlocking(opRecup, new Object[]{}, new Class[]{String[].class});
                        String[] messages = (String[]) response[0];

                        // Si le nombre de messages a changé, on affiche les nouveaux
                        if (messages != null && messages.length > derniersMessages.length) {
                            for (int i = derniersMessages.length; i < messages.length; i++) {
                                System.out.println("\n[Nouveau] " + messages[i]);
                                System.out.print("> "); // Réaffiche le curseur pour l'utilisateur
                            }
                            derniersMessages = messages;
                        } else if (messages != null && messages.length < derniersMessages.length) {
                            // Cas où le chat a été effacé (clear)
                            System.out.println("\n--- Le chat a été réinitialisé ---");
                            derniersMessages = messages;
                        }
                        Thread.sleep(2000); // Attend 2 secondes
                    }
                } catch (Exception e) { /* Erreur silencieuse en background */ }
            });
            refreshThread.setDaemon(true); // S'arrête quand on ferme le programme
            refreshThread.start();

            // --- BOUCLE PRINCIPALE (ENVOI) ---
            System.out.println("Chat prêt ! Tapez 'clear' pour vider ou 'exit' pour quitter.");
            while (true) {
                System.out.print("> ");
                String texte = scanner.nextLine();

                if (texte.equalsIgnoreCase("exit")) break;

                if (texte.equalsIgnoreCase("clear")) {
                    QName opClear = new QName("http://ws.apache.org/axis2", "effacerMessages");
                    serviceClient.invokeBlocking(opClear, new Object[]{}, new Class[]{String.class});
                } else {
                    QName opEnvoi = new QName("http://ws.apache.org/axis2", "envoyerMessage");
                    serviceClient.invokeRobust(opEnvoi, new Object[]{pseudo, texte});
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}