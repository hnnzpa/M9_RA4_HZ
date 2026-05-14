import java.io.*;
import java.net.*;

public class Servidor {
    private static final int PORT = 9999;
    private static final String HOST = "localhost";

    private ServerSocket serverSocket;

    public Socket connectar() throws IOException {
        serverSocket = new ServerSocket(PORT);
        System.out.println("Acceptant connexions en -> " + HOST + ":" + PORT);
        System.out.println("Esperant connexio...");
        Socket socket = serverSocket.accept();
        System.out.println("Connexio acceptada: " + socket.getInetAddress());
        return socket;
    }

    public void tancarConnexio(Socket socket) throws IOException {
        System.out.println("Tancant connexió amb el client: " + socket.getInetAddress());
        socket.close();
        serverSocket.close();
    }

    public void enviarFitxers(Socket socket) throws IOException {
        ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream sortida = new ObjectOutputStream(socket.getOutputStream());

        // Rep el nom del fitxer que vol el client
        String nomFitxer = null;
        try {
            nomFitxer = (String) entrada.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("Nomfitxer rebut: " + nomFitxer);

        if (nomFitxer == null) {
            System.out.println("Nom del fitxer buit o nul. Sortint...");
            sortida.writeObject(null);
            return;
        }

        // Llegeix el contingut del fitxer i l'envia
        try {
            Fitxer fitxer = new Fitxer(nomFitxer);
            byte[] contingut = fitxer.getContingut();
            System.out.println("Contingut del fitxer a enviar: " + contingut.length + " bytes");
            System.out.println("Fitxer enviat al client: " + nomFitxer);
            sortida.writeObject(contingut);
        } catch (IOException e) {
            System.out.println("Error llegint el fitxer del client: " + e.getMessage());
            sortida.writeObject(null);
        }
    }

    public static void main(String[] args) throws IOException {
        Servidor servidor = new Servidor();
        Socket socket = servidor.connectar();
        servidor.enviarFitxers(socket);
        servidor.tancarConnexio(socket);
    }
}