import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class MixGauss {

    public static final int D=3; // deux dimensions
    public static final int k=6; // deux gausssiennes

    private static final int NB_POINTS = 1000;

    public static Random rand = new Random();


    public static int min(double[] tab){
        double min = tab[0];
        int indice = 0;
        for (int i =1; i < tab.length; i++){
            if (tab[i] < min){
                min = tab[i];
                indice = i;
            }
        }
        return indice;
    }

    public static double dist(double[] a, double[] b){
        double res = 0d;
        for (int i = 0; i < a.length; i++){
            res += Math.pow(a[i]-b[i],2);
        }
        return Math.sqrt(res);
    }




    public static double[] gaussian(double ecart, double esp, int min, int max){
        double[] res = new double[NB_POINTS];
        double x = min;
        double padding = (double) (max - min) / (double) (NB_POINTS - 1);
        for(int i = 0; i<NB_POINTS; i++){
            res[i] = (1.0 / (Math.sqrt(2 * Math.PI * ecart))) * Math.exp(- Math.pow(x - esp, 2) / (2 * ecart));
            x += padding;
        }
        return res;
    }

    public static void save_gausian1D(double[][] gaussians, int min, int max, String path) throws IOException {
        double[] ys = new double[gaussians[0].length];
        for(int i = 0; i<gaussians.length; i++){
            for(int j = 0; j<gaussians[i].length; j++){
                ys[j] += gaussians[i][j];
            }
        }
        File ff = new File(path);
        ff.createNewFile();
        FileWriter file = new FileWriter(ff);
        double x = min;
        double padding = (double) (max - min) / (double) (NB_POINTS - 1);
        for (double y : ys) {
            file.write(x + " " + y);
            file.write('\n');
            x += padding;
        }

        file.close();

    }

    public static void save_gaussian1D(double ecart, double esp, int min, int max, String path) throws IOException {
        File ff = new File(path);
        ff.createNewFile();
        FileWriter file = new FileWriter(ff);
        for(double i = min; i<=max; i+=0.01){
            double y = (1.0 / (Math.sqrt(2 * Math.PI * ecart))) * Math.exp(- Math.pow(i - esp, 2) / (2 * ecart));
            file.write(i + " " + y);
            file.write('\n');
        }
        file.close();
    }

    public static double likelihood_num(double[] X, double[][] centres, double[] ro, double[][] ecarts, int k){
        double res = ro[k];
        for(int i = 0; i<X.length; i++){
            res *= (1.0 / (Math.sqrt(2 * Math.PI * ecarts[k][i]))) * Math.exp(-Math.pow(X[i] - centres[k][i], 2) / (2*ecarts[k][i]));
        }
        return res;
    }

    public static double likelihood(double[] X, double[][] centres, double[] ro, double[][] ecarts, int k){
        double num = likelihood_num(X, centres, ro, ecarts, k);
        double denom = 0;
        for(int i = 0; i<centres.length; i++){
            denom += likelihood_num(X, centres, ro, ecarts, i);
        }
        return num/denom;
    }

    public static double[][] Assigner(double[][] X, double[][] centres, double[] ro, double[][] ecarts){
        double[][] res = new double[X.length][centres.length];
        for(int i = 0; i<X.length; i++){
            for(int j = 0; j<centres.length; j++){
                res[i][j] = likelihood(X[i], centres, ro, ecarts, j);
            }
        }
        return res;
    }

    public static double Rk(double[][] assignement, int k){
        double res = 0;
        for(int i = 0; i<assignement.length; i++){
            res += assignement[i][k];
        }
        return res;
    }

    public static double ecartk(double[][] X, double[][] centres, double[][] assignement, int k, int i){
        double res = 0;
        for(int d = 0; d<X.length; d++){
            res += assignement[d][k] * Math.pow(X[d][i] - centres[k][i], 2);
        }
        return res/Rk(assignement, k);
    }

    public static double mk(double[][] X, double[][] assignement, int k, int i){
        double res = 0;
        for(int d = 0; d<X.length; d++){
            res += assignement[d][k] * X[d][i];
        }
        return res/Rk(assignement, k);
    }


    public static double Deplct(double[][] X, double[][] centres, double[] ro, double[][] ecarts, double[][] assignement){
        double ret = 0;
        for(int k = 0; k<centres.length; k++){
            ro[k] = Rk(assignement, k) / X.length;

            double[] old_centre = centres[k].clone();
            //Ne pas faire les assignements de centres et d'écarts dans la même boucle for
            for(int d = 0; d<X[0].length; d++){
                centres[k][d] = mk(X, assignement, k, d);
            }

            double[][] test = centres.clone();
            ret += dist(old_centre, centres[k]);
            for(int d = 0; d<X[0].length; d++){
                ecarts[k][d] = ecartk(X, centres, assignement, k, d);
            }
        }
        return ret;
    }


    public static void ecritDoubleGaussienne2D(double[][] centre, double[][] ecarts, double[] ro){
        String tmp = "splot " +
                "  (("+ro[0] +"* " + ro[0]+")/(2*3.14*" + ecarts[0][0] + "*" + ecarts[0][1]+"))*exp((-((x-"+ centre[0][0] + ")**2)/(2*("+ ecarts[0][0] +")**2))-(((y-"+centre[0][1]+")**2)/(2*("+ecarts[0][1]+")**2)))" +
                "+ (("+ro[1] +"* " + ro[1]+")/(2*3.14*" + ecarts[1][0] + "*" + ecarts[1][1]+"))*exp((-((x-"+ centre[1][0] + ")**2)/(2*("+ ecarts[1][0] +")**2))-(((y-"+centre[1][1]+")**2)/(2*("+ecarts[1][1]+")**2)))" ;
        TasGaussien.ecrire("please.gnu", tmp);
    }

    public static int belongTo(double[] X, double[][] centres, double[] ro, double[][] ecarts){
        double[][] data = new double[1][X.length];
        data[0] = X;
        double [][] prob = Assigner(data, centres, ro, ecarts);
        double max = 0;
        int indice = -1;
        for(int i = 0; i<prob[0].length; i++){
            if(prob[0][i] > max){
                max = prob[0][i];
                indice = i;
            }
        }
        return indice;
    }

}
