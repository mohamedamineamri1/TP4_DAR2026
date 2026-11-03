package etape2;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
public class ClientChatUDP {
    private static final String HOTE_SERVEUR = "localhost";
    private static final int PORT_SERVEUR = 1234;
    private static final int TAILLE_BUFFER = 1024;

    public static void main(String[] args) {
        String hote = (args.length > 0) ? args[0] : HOTE_SERVEUR;   // IP du serveur en argument ? (optionnel)
        try (DatagramSocket socket = new DatagramSocket();
             Scanner clavier = new Scanner(System.in)) {
            InetAddress adresseServeur = InetAddress.getByName(hote);
            System.out.print("Entrez votre nom d'utilisateur : ");
            String nom = clavier.nextLine().trim();
            Thread reception = new Thread(() -> {
                byte[] buffer = new byte[TAILLE_BUFFER];
                while (!socket.isClosed()) {
                    try {
                        DatagramPacket recu = new DatagramPacket(buffer,buffer.length);
                        socket.receive(recu);   // bloquant, mais n'empeche pas la saisie
                        String msg = new String(recu.getData(),0,recu.getLength(),
                                StandardCharsets.UTF_8);
                        System.out.println("\n" + msg);
                    } catch (SocketException e) {
                        break;   // socket fermee => fin normale du thread
                    } catch (Exception e) {
                        System.err.println("[Client] Erreur reception : " + e.getMessage());
                    }
                }
            });
            reception.setDaemon(true);   
            reception.start();
            envoyer(socket, adresseServeur, "[" + nom + "] : a rejoint le chat");
            System.out.println("Connecte. Tapez vos messages (\"/quit\" pour quitter).");
            while (true) {
                String texte = clavier.nextLine();
                if (texte.equalsIgnoreCase("/quit")) {
                    envoyer(socket, adresseServeur, "[" + nom + "] : /quit");
                    break;
                }
                envoyer(socket, adresseServeur, "[" + nom + "] : " + texte);
            }
            System.out.println("Deconnecte.");
        } catch (Exception e) {
            System.err.println("[Client] Erreur : " + e.getMessage());
        }
    }
    private static void envoyer(DatagramSocket socket,InetAddress dest,String message) throws Exception {
        byte[] donnees = message.getBytes(StandardCharsets.UTF_8);
        socket.send(new DatagramPacket(donnees,donnees.length,dest,PORT_SERVEUR));
    }
}

