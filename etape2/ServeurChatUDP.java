package etape2;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.Set;
public class ServeurChatUDP {

    private static final int PORT = 1234;
    private static final int TAILLE_BUFFER = 1024;
    private static final Set<SocketAddress> clients = new LinkedHashSet<>();
    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket(null)) {
            socket.bind(new InetSocketAddress(PORT));
            System.out.println("[Serveur] Chat UDP demarre sur le port " + PORT);

            byte[] buffer = new byte[TAILLE_BUFFER];

            while (true) {
                DatagramPacket recu = new DatagramPacket(buffer,buffer.length);
                socket.receive(recu);

                String message = new String(recu.getData(),0,recu.getLength(),
                        StandardCharsets.UTF_8);
                SocketAddress expediteur = recu.getSocketAddress();
                if (message.endsWith(": /quit")) {
                    clients.remove(expediteur);
                    String info = "[Serveur] Un client a quitte le chat (" + expediteur + ")";
                    System.out.println(info);
                    diffuser(socket, info, null);
                    continue;
                }

                if (clients.add(expediteur)) {   
                    System.out.println("[Serveur] Nouveau client : " + expediteur
                            + " (total : " + clients.size() + ")");
                }

                System.out.println("[Serveur] " + expediteur + " -> " + message);
                diffuser(socket,message,expediteur);
            }
        } catch (Exception e) {
            System.err.println("[Serveur] Erreur : " + e.getMessage());
        }
    }
   private static void diffuser(DatagramSocket socket, String message, SocketAddress exclu) {
        byte[] donnees = message.getBytes(StandardCharsets.UTF_8);
        for (SocketAddress client : clients) {
            if (client.equals(exclu)) continue;
            try {
               
                socket.send(new DatagramPacket(donnees,donnees.length,client));
            } catch (Exception e) {
                System.err.println("[Serveur] Echec d'envoi vers " + client + " : " + e.getMessage());
            }
        }
    }
}
