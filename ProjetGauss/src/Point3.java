import java.io.IOException;
import java.util.Arrays;

public class Point3 {
    public static void main(String[] args) throws IOException {
        String path = "./";
        String imageMMS = path + "vaiana.png";

        //Nombre de Gaussiennes
        int K = 20;
        //Dimension
        int D = 3;
        double[][] data = Results.loadDatafromImage(imageMMS);
        double[][] centres = MixGauss.createCentre(K, D, 0, 1);
        double[][] ecarts = new double[K][D];
        double[] ro = new double[K];

        for (int i = 0; i< ro.length; i++){
            ro[i] = 1.0/ro.length;
            Arrays.fill(ecarts[i], Math.pow(0.3,2));
        }

        double eps=0.01;
        double maj = 10;

        double [][] a = MixGauss.Assigner(data, centres, ro, ecarts);
        while(maj>eps){
            maj = MixGauss.Deplct(data, centres, ro, ecarts, a);
            a = MixGauss.Assigner(data, centres, ro, ecarts);
        }

        Results.separateImage(imageMMS, centres, ro, ecarts, "Vaiana");
    }
}