import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.rmi.*;
import java.rmi.server.*;

public class ChatUserImpl extends UnicastRemoteObject implements ChatUser {
    
    private String title = "Logiciel de discussion en ligne";
    private String pseudo = null;
    private ChatRoom chatRoom = null;

    private JFrame window = new JFrame(this.title);
    private JTextArea txtOutput = new JTextArea();
    private JTextField txtMessage = new JTextField();
    private JButton btnSend = new JButton("Envoyer");

    public ChatUserImpl() throws RemoteException {
        this.createIHM();
        this.requestPseudo();
        this.connect();
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
            public void windowClosing(WindowEvent e) {
                window_windowClosing(e);
            }
        });
        btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnSend_actionPerformed(e);
            }
        });
        txtMessage.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent event) {
                if (event.getKeyChar() == '\n')
                    btnSend_actionPerformed(null);
            }
        });

        this.txtOutput.setBackground(new Color(220,220,220));
        this.txtOutput.setEditable(false);
        this.window.setSize(500,400);
        this.window.setVisible(true);
        this.txtMessage.requestFocus();
    }

    public void requestPseudo() {
        this.pseudo = JOptionPane.showInputDialog(
                this.window, "Entrez votre pseudo : ",
                this.title,  JOptionPane.OK_OPTION
        );
        if (this.pseudo == null) System.exit(0);
    }

    public void connect() {
        try {
            chatRoom = (ChatRoom) Naming.lookup("rmi://localhost/ChatRoom");
            chatRoom.subscribe(this, this.pseudo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(window, "Impossible de se connecter au serveur");
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void displayMessage(String message) throws RemoteException {
        this.txtOutput.append(message + "\n");
        this.txtOutput.setCaretPosition(this.txtOutput.getDocument().getLength());
    }

    public void window_windowClosing(WindowEvent e) {
        try {
            if (chatRoom != null) chatRoom.unsubscribe(pseudo);
        } catch (Exception ex) {}
        System.exit(0);
    }

    public void btnSend_actionPerformed(ActionEvent e) {
        try {
            if (chatRoom != null) {
                chatRoom.postMessage(pseudo, this.txtMessage.getText());
            }
            this.txtMessage.setText("");
            this.txtMessage.requestFocus();
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            new ChatUserImpl();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}