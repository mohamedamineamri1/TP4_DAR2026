# TP 4 — Chat Client/Serveur avec sockets UDP (mode non-connecté)
Matière : Développement d'applications réparties Auditoire : LSI 3 — Faculté des Sciences de Sfax — 2026/2027 

Enseignante des TPs : Mme Zeineb DAOUD 

Étudiant : Mohamed Amine AMRI

# Sommaire 
etape2/	Chat unidirectionnel : un client envoie, le serveur affiche 

etape3/	Chat bidirectionnel : le serveur diffuse chaque message aux autres clients  

# Fonctionnement détaillé 
Principe d'UDP (mode non-connecté)

Avec UDP, il n'y a ni connexion préalable ni flux (pas de accept() comme en TCP).  
Chaque message est un datagramme indépendant qui contient les données, l'adresse et le port de destination.  
L'envoi se fait avec send(), la réception avec receive(), sans garantie de livraison ni d'ordre  

# Étape 1 Chat unidirectionnel 

# Serveur (ServeurUDP)

new DatagramSocket(null) crée une socket non liée, puis bind(new InetSocketAddress(1234)) la lie au port 1234. 

Dans une boucle infinie, receive(paquet) bloque jusqu'à l'arrivée d'un datagramme. 

Seuls les getLength() octets reçus sont convertis en String . 

Le serveur affiche l'IP et le port de l'expéditeur (getAddress(), getPort()) et le contenu.

# Client (ClientUDP)

new DatagramSocket() : le système choisit un port local libre. 

Le message est construit au format [NomUtilisateur] : texte, converti en octets, placé dans un DatagramPacket avec l'adresse et le port du serveur, 

puis envoyé par send(). 

<img width="1382" height="362" alt="Capture d&#39;écran 2026-11-03 231658" src="https://github.com/user-attachments/assets/b2e46de1-e6ea-423a-9637-3120768166a3" /> 

<img width="877" height="240" alt="Capture d&#39;écran 2026-11-03 231712" src="https://github.com/user-attachments/assets/c9089c60-506f-4062-be8a-f656fc0475ae" />

# Étape 2 – Chat bidirectionnel avec diffusion 

# Serveur (ServeurChatUDP)

Il garde un Set<SocketAddress> des clients connus. Un ensemble évite les doublons.
À chaque message reçu : 

    l'adresse IP:port de l'expéditeur est ajoutée si elle est nouvelle;
    le message est renvoyé à tous les clients enregistrés sauf l'expéditeur .
    Le message /quit retire le client de la liste et prévient les autres.

# Client (ClientChatUDP)

receive() est bloquant : il faut donc deux threads pour envoyer et recevoir en même temps. 
Thread de réception : boucle sur receive() et affiche les messages entrants.
Thread principal : lit le clavier et envoie les messages.  

Au démarrage, le client envoie « a rejoint le chat ». Ce premier datagramme permet au serveur d'enregistrer son adresse,  
sans quoi il ne recevrait aucun message tant qu'il n'a rien écrit 

<img width="627" height="193" alt="Capture d&#39;écran 2026-11-03 232050" src="https://github.com/user-attachments/assets/451341e7-f3b9-4ccf-8ff5-ce4f4c995398" /> 
<img width="666" height="240" alt="Capture d&#39;écran 2026-11-03 232126" src="https://github.com/user-attachments/assets/497a571f-9464-49b9-b9d2-04fc330cb30e" /> 

# Synthèse 

UDP est non connecté : le serveur identifie un client uniquement grâce à l'adresse IP:port source du datagramme reçu. 
receive() est bloquant : côté client,un thread dédié à la réception est indispensable pour un chat en temps réel. 
Taille du buffer et getLength() : on ne lit que les octets réellement reçus, 
sinon on récupère des données résiduelles. 

L'encodage UTF-8 est imposé des deux côtés.
Limites d'UDP : pas de garantie de livraison, d'ordre ni de détection de perte.  
Un client qui plante sans /quit reste dans la liste du serveur.
Améliorations possibles : pseudo unique, messages privés,détection des clients inactifs , interface graphique.


