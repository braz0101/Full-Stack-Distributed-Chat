import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ChatService {
    private static java.util.Vector<String> messages = new java.util.Vector<>();
    // Format : 14:05:22
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public String envoyerMessage(String pseudo, String texte) {
        String heure = LocalTime.now().format(formatter);
        // On ajoute l'heure au début du message
        String msg = "[" + heure + "] " + pseudo + " > " + texte;
        messages.add(msg);
        return "OK";
    }

    public String[] recupererMessages() {
        return messages.toArray(new String[0]);
    }

    public String effacerMessages() {
        messages.clear();
        return "Chat effacé.";
    }
}