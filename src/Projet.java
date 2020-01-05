import java.io.IOException;
import java.util.Random;

public class Projet{


    public static void main(String[] args) throws IOException {
        Random rand = new Random();


        String path = "./";
        String imageMMS = path + "mms.png";

        //Nombre de Gaussiennes
        int K = 20  ;
        //Dimension
        int D = 3;

        // On charge nos données en HSB
        double[][] data = Results.loadDatafromImage(imageMMS);

        double[][] centres = new double[K][D];
        double[][] ecarts = new double[K][D];
        double[] ro = new double[K];

        //On initialise nos tableau de sigma et de ro
        for (int i = 0; i< K; i++){
            ro[i] = 1.0/K;
            for (int j= 0; j < D; j++){
                ecarts[i][j] = 1;
            }
        }

        // Partie pour assigner les centres de facons aleatoire
        for (int i = 0; i < K; i++){
            for (int j = 0; j < D; j++){
                centres[i][j] = rand.nextDouble();
            }
        }


        double eps=0.01;
        double maj = 10;

        double [][] a = MixGauss.Assigner(data, centres, ro, ecarts);
        while(maj>eps){
            maj = MixGauss.Deplct(data, centres, ro, ecarts, a);
            a = MixGauss.Assigner(data, centres, ro, ecarts);
        }


        double[] score = new double[data.length];
        for (int i = 0; i < data.length; i++){
            score[i] = MixGauss.score(centres, ecarts, ro, data[i]);
        }
        System.out.println("Le score moyen est de : " + MixGauss.scoreMoy(score));

        // On enregsitre nos differentes images dans un fichier
        Results.separateImage(imageMMS, centres, ro, ecarts);
    }
}