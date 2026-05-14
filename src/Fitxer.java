import java.io.*;
import java.nio.file.*;

public class Fitxer {
    private String nom;
    private byte[] contingut;

    public Fitxer(String nom) {
        this.nom = nom;
    }

    public byte[] getContingut() throws IOException {
        File fitxer = new File(nom);
        if (!fitxer.exists()) {
            throw new FileNotFoundException("El fitxer no existeix: " + nom);
        }
        contingut = Files.readAllBytes(fitxer.toPath());
        return contingut;
    }
}