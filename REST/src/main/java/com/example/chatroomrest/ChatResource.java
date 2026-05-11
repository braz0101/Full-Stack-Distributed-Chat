import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.Vector;
import java.time.LocalTime;
import java.util.List;

@Path("/messages") // URI de la ressource (Page 80 du PDF REST)
public class ChatResource {

    // On utilise static pour que les messages soient partagés entre tous les clients
    private static Vector<String> messages = new Vector<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON) // Le serveur répond en JSON
    public List<String> getMessages() {
        return messages;
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) // Reçoit des données de formulaire
    public void addMessage(@FormParam("pseudo") String pseudo, @FormParam("texte") String texte) {
        String heure = LocalTime.now().toString().substring(0, 8);
        messages.add("[" + heure + "] " + pseudo + " : " + texte);
    }

    @DELETE
    public void clearChat() {
        messages.clear();
    }
}