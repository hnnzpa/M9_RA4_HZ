import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.Scanner;

public class Client {
    private static final String DIR_ARRIBADA = System.getProperty("os.name")
            .toLowerCase().startsWith("win") ? "C:\\tmp\\" : "/tmp/";

    private ObjectOutputStream sortida;
    private ObjectInputStream entrada;
    private Socket socket;

    public void connectar() throws IOException {
        System.out.println("Connectant a -> localhost:9999");
        socket = new Socket("localhost", 9999);
        System.out.println("Connexio acceptada: " + socket.getLocalAddress());

        // Important: ObjectOutputStream primer, després ObjectInputStream
        sortida = new ObjectOutputStream(socket.getOutputStream());
        entrada = new ObjectInputStream(socket.getInputStream());
    }

    public void rebreFitxers() throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Nom del fitxer a rebre ('sortir' per sortir): ");
        String nomFitxer = scanner.nextLine().trim();

        if (nomFitxer.equals("sortir")) {
            System.out.println("Sortint...");
            sortida.writeObject(null);
            return;
        }

        // Envia el nom del fitxer al servidor
        try {
            sortida.writeObject(nomFitxer);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Rep el byte[] del servidor
        try {
            byte[] contingut = (byte[]) entrada.readObject();

            if (contingut == null) {
                System.out.println("El servidor no ha pogut enviar el fitxer.");
                return;
            }

            // Guarda el fitxer a DIR_ARRIBADA
            String nomSense = new File(nomFitxer).getName();
            String ruta = DIR_ARRIBADA + nomSense;
            System.out.println("Nom del fitxer a guardar: " + ruta);

            Files.write(Paths.get(ruta), contingut);
            System.out.println("Fitxer rebut i guardat com: " + ruta);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.print("sortir\nSortint...");
    }

    public void tancarConnexio() throws IOException {
        socket.close();
        System.out.println("\nConnexió tancada.");
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.connectar();
        client.rebreFitxers();
        client.tancarConnexio();
    }
}