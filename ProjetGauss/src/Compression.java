import java.io.IOException;

public class Compression {
    public static void main(String[] args) throws IOException {
        int[] nb_centres = {5, 10, 15, 20};
        for(int i : nb_centres) {
            Results.compressImage("vaiana.png", i, "Results/Compressions/vaiana_com_" + i + ".png");
            System.out.println("Compression avec " + i + " centres terminée");
        }
    }
}