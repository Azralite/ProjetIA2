import java.io.IOException;

public class Projet_Point1{


    public static void main(String[] args) throws IOException {
        String path = "./";
        String imageMMS = path + "mms.png";

        //Nombre de Gaussiennes
        int K = 10  ;
        //Dimension
        int D = 3;

        // On charge nos données en HSB
        double[][] data = Results.loadDatafromImage(imageMMS);

        // Partie pour assigner les centres de facons aleatoire
        double[][] centres = MixGauss.createCentre(K, 3, 0, 1);
        double[][] ecarts = new double[K][D];
        double[] ro = new double[K];

        //On initialise nos tableaux de sigma et de ro
        for (int i = 0; i< K; i++){
            ro[i] = 1.0/K;
            for (int j= 0; j < D; j++){
                ecarts[i][j] = 1;
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

        // On enregistre nos différentes images dans un fichier
        Results.separateImage(imageMMS, centres, ro, ecarts, "");
    }
}