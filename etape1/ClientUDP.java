package etape1;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClientUDP {
    private static final String HOTE_SERVEUR = "localhost";
    private static final int PORT_SERVEUR = 1234;
    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket();   // port local choisi par l'OS
             Scanner clavier = new Scanner(System.in)) {
            InetAddress adresseServeur = InetAddress.getByName(HOTE_SERVEUR);
            System.out.print("Entrez votre nom d'utilisateur : ");
            String nom = clavier.nextLine().trim();
            System.out.println("Tapez vos messages (\"quit\" pour quitter).");
            while (true) {
                String texte = clavier.nextLine();
                if (texte.equalsIgnoreCase("quit")) break;
                // Prefixe demande par l'enonce : [Nom] :message
                String message = "[" + nom + "] : " + texte;
                byte[] donnees = message.getBytes(StandardCharsets.UTF_8);
                // Un datagramme = donnees + adresse + port de destination
                DatagramPacket paquet = new DatagramPacket(
                        donnees, donnees.length, adresseServeur, PORT_SERVEUR);
                socket.send(paquet);   // envoi sans connexion ni accuse de reception
            }
            System.out.println("Client ferme.");
        } catch (Exception e) {
            System.err.println("[Client] Erreur : " + e.getMessage());
        }
    }
}

