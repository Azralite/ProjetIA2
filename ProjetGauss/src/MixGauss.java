import java.util.Random;

public class MixGauss {

    private static Random rand = new Random();

    //Créé un tableau de dimension de nb éléments de dimension dim. Chaque composante de dim sera comprise entre min et max.
    // Sert à créer un tableau de centres de manière aléatoire
    public static double[][] createCentre(int nb, int dim, float min, float max){
        double[][] res = new double[nb][dim];
        for(int i = 0; i<nb; i++){
            for(int j = 0; j<dim; j++){
                res[i][j] = (rand.nextDouble() + min) * max;
            }
        }
        return res;
    }

    // Fonction qui calcule la distance entre deux points
    public static double dist(double[] a, double[] b){
        double res = 0d;
        for (int i = 0; i < a.length; i++){
            res += Math.pow(a[i]-b[i],2);
        }
        return Math.sqrt(res);
    }


    // Fonction qui calcule un numérateur de rk
    public static double likelihood_num(double[] X, double[][] centres, double[] ro, double[][] ecarts, int k){
        double res = ro[k];
        for(int i = 0; i<X.length; i++){
            res *= (1.0 / (Math.sqrt(2 * Math.PI * ecarts[k][i]))) * Math.exp(-Math.pow(X[i] - centres[k][i], 2) / (2*ecarts[k][i]));
        }
        return res;
    }

    // Fonction qui calcule rk pour l'assignement
    public static double likelihood(double[] X, double[][] centres, double[] ro, double[][] ecarts, int k){
        double num = likelihood_num(X, centres, ro, ecarts, k);
        double denom = num;
        for(int i = 0; i<centres.length; i++){
            if(i != k)
                denom += likelihood_num(X, centres, ro, ecarts, i);
        }
        return num/denom;
    }


    // Fonction qui calcule Rk
    public static double Rk(double[][] assignement, int k){
        double res = 0;
        for(int i = 0; i<assignement.length; i++){
            res += assignement[i][k];
        }
        return res;
    }

    // Fonction pour calculer le deplacement des sigmas
    public static double ecartk(double[][] X, double[][] centres, double[][] assignement, int k, int i){
        double res = 0;
        for(int d = 0; d<X.length; d++){
            res += assignement[d][k] * Math.pow(X[d][i] - centres[k][i], 2);
        }
        return res/Rk(assignement, k);
    }

    // Fonction pour calculer le deplacement des centres
    public static double mk(double[][] X, double[][] assignement, int k, int i){
        double res = 0;
        for(int d = 0; d<X.length; d++){
            res += assignement[d][k] * X[d][i];
        }
        return res/Rk(assignement, k);
    }


    // Fonction qui assigne chaque point a une gaussienne
    public static double[][] Assigner(double[][] X, double[][] centres, double[] ro, double[][] ecarts){
        double[][] res = new double[X.length][centres.length];
        for(int i = 0; i<X.length; i++){
            for(int j = 0; j<centres.length; j++){
                res[i][j] = likelihood(X[i], centres, ro, ecarts, j);
            }
        }
        return res;
    }


    //Fonction qui deplace les centres, les sigma et les ro
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


    // Fonction pour tracer plusieurs gaussiennes en 3D avec Gnuplot
    public static void ecritDoubleGaussienne2D(double[][] centre, double[][] ecarts, double[] ro){
        int k = centre.length;
        String tmp = "splot ";
        for (int i = 0; i < k; i++){
            tmp += "  (("+ro[k] +"* " + ro[k]+")/(2*3.14*" + ecarts[k][0] + "*" + ecarts[k][1]+"))*exp((-((x-"+ centre[k][0] + ")**2)/(2*("+ ecarts[k][0] +")**2))-(((y-"+centre[k][1]+")**2)/(2*("+ecarts[k][1]+")**2)))";
        }

        TasGaussien.ecrire("please.gnu", tmp);
    }


    // Fonction qui renvoie le centre auquel le point est assigné
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


    // Fonction permettant de calculer le score pour une donnée
    public static double score(double[][] centres,double[][] ecarts, double[] ro, double[] data ){

        double res = 0;
        double tmp = 1d;
        for (int i = 0; i < centres.length; i++){
            for (int j = 0; j < data.length; j++){
                tmp = tmp * (1./Math.sqrt(2*Math.PI*ecarts[i][j]))*Math.exp(-1*(Math.pow(data[j]-centres[i][j],2)/(2*ecarts[i][j])));
            }
            res = res + (ro[i] * tmp);
            tmp = 1;
        }
        return Math.log(res);
    }

    // Fonction qui prend un tableau de score et renvoie la moyenne
    public static double scoreMoy(double[] score){
        double res = 0;
        for (int i = 0; i < score.length; i++){
            res += score[i];
        }
        return res/score.length;
    }


}