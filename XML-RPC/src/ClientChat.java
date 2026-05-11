import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Vector;
import org.apache.xmlrpc.XmlRpcClient;

public class ClientChat {
    
    // UI
    private String title = "Chat XML-RPC";
    private String pseudo = null;
    private JFrame window = new JFrame(this.title);
    private JTextArea txtOutput = new JTextArea();
    private JTextField txtMessage = new JTextField();
    private JButton btnSend = new JButton("Envoyer");
    
    // XML-RPC
    private XmlRpcClient client;
    private String serverUrl = "http://localhost:8080/";
    
    // Compteur pour savoir où on en est dans la conversation
    private int monIndexMessage = 0;

    public ClientChat() {
        this.createIHM();
        this.requestPseudo();
        this.connect();
        this.lancerPolling(); // Démarrage de la mise à jour automatique
    }

    public void createIHM() {
        JPanel panel = (JPanel)this.window.getContentPane();
        JScrollPane sclPane = new JScrollPane(txtOutput);
        panel.add(sclPane, BorderLayout.CENTER);
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(this.txtMessage, BorderLayout.CENTER);
        southPanel.add(this.btnSend, BorderLayout.EAST);
        panel.add(southPanel, BorderLayout.SOUTH);

        window.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { System.exit(0); }
        });
        
        // Action bouton Envoyer
        ActionListener sendAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) { envoyerMessage(); }
        };
        btnSend.addActionListener(sendAction);
        txtMessage.addActionListener(sendAction); // Permet d'envoyer avec "Entrée"

        this.txtOutput.setBackground(new Color(230,240,250));
        this.txtOutput.setEditable(false);
        this.window.setSize(500,400);
        this.window.setVisible(true);
        this.txtMessage.requestFocus();
    }

    public void requestPseudo() {
        this.pseudo = JOptionPane.showInputDialog(window, "Pseudo :", title, JOptionPane.QUESTION_MESSAGE);
        if (this.pseudo == null || this.pseudo.trim().isEmpty()) System.exit(0);
        window.setTitle("Chat XML-RPC - " + pseudo);
    }

    public void connect() {
        try {
            client = new XmlRpcClient(serverUrl);
            txtOutput.append("--- Connecté au serveur ---\n");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(window, "Erreur connexion: " + e);
            System.exit(0);
        }
    }

    // --- C'est ici que la magie opère ---
    
    public void envoyerMessage() {
        try {
            String text = txtMessage.getText();
            if (text.isEmpty()) return;

            Vector params = new Vector();
            params.add(pseudo);
            params.add(text);
            
            // Appel au serveur : chat.posterMessage(pseudo, text)
            client.execute("chat.posterMessage", params);
            
            txtMessage.setText("");
        } catch (Exception e) {
            e.printStackTrace();
            txtOutput.append("[Erreur envoi]\n");
        }
    }

    public void lancerPolling() {
        // Un Timer qui appelle 'verifierNouveauxMessages' toutes les 1000ms (1 seconde)
        Timer timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                verifierNouveauxMessages();
            }
        });
        timer.start();
    }

    public void verifierNouveauxMessages() {
        try {
            Vector params = new Vector();
            params.add(new Integer(monIndexMessage)); // "J'en suis au message numéro X"

            // Appel au serveur : chat.lireMessages(index)
            Vector resultats = (Vector) client.execute("chat.lireMessages", params);
            
            // Si on a reçu des messages
            if (resultats.size() > 0) {
                for (int i = 0; i < resultats.size(); i++) {
                    String msg = (String) resultats.get(i);
                    txtOutput.append(msg + "\n");
                }
                // On met à jour notre index pour ne pas retélécharger les mêmes messages
                monIndexMessage += resultats.size();
                
                // Scroll en bas
                txtOutput.setCaretPosition(txtOutput.getDocument().getLength());
            }
        } catch (Exception e) {
            // On ne fait rien si ça échoue (pour ne pas spammer d'erreurs)
            System.err.println("Erreur polling: " + e);
        }
    }

    public static void main(String[] args) {
        new ClientChat();
    }
}