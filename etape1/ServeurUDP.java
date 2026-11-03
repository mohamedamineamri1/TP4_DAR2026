package etape1;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
public class ServeurUDP {
    private static final int PORT = 1234;
    private static final int TAILLE_BUFFER = 1024;
    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket(null)) {
            socket.bind(new InetSocketAddress(PORT));
            System.out.println("[Serveur] En ecoute sur le port " + PORT + "...");
            byte[] buffer = new byte[TAILLE_BUFFER];
            while (true) {
                // 2) Preparer un paquet vide qui recevra le datagramme
                DatagramPacket paquet = new DatagramPacket(buffer,buffer.length);
                // 3) Appel BLOQUANT : attend l'arrivee d'un datagramme
                socket.receive(paquet);
                // 4) Convertir UNIQUEMENT les octets recus (getLength) en String
                String message = new String(paquet.getData(),0,
                        paquet.getLength(),StandardCharsets.UTF_8);
                // 5) Afficher l'expediteur (IP + port) et le contenu
                System.out.println("[Serveur] Recu de " + paquet.getAddress().getHostAddress()
                        + ":" + paquet.getPort() + " -> " + message);
            }
        } catch (Exception e) {
            System.err.println("[Serveur] Erreur : " + e.getMessage());
        }
    }
}
