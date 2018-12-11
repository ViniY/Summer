package ec.app.tutorial5;

import ec.app.tutorial4.Task;
import ec.app.tutorial4.VirtualMachine;

import java.util.ArrayList;

public class PSO_based_approach {
    int numberOfVM = 0;//
    public double[][] MAP;//
    public double[][] POP;//
    public double[] VEC;
    public double Min_Fit;
    public double besti;
    private double[][] Pbest;
    private double[] Gbest;
    public double Ietr;
    private double Max_Ietr;

    public PSO_based_approach(Object task, int number_particle, double[] VEC) {

    }

    public int number_particle;

    public PSO_based_approach(ArrayList<Task> parentTasks, ArrayList<VirtualMachine> ls_vms, Task t, int j) {

    }

    //    public
    public void Initialization(double[][] POP, double[] VEC) {
        for (int i = 0; i < POP.length; i++) {
            for (int j = 0; j < POP[0].length; j++) {
                POP[i][j] = Math.random();//here initial the matrix which number of rows represent the population size and the column size equal to the number of tasks
            }
        }
        for (int j = 0; j < POP[0].length; j++) {
            double tmp = j * numberOfVM;
            if (tmp > numberOfVM) {
                while (tmp > numberOfVM) {
                    tmp = Math.floor(tmp / numberOfVM);
                }
            }
            VEC[j] = tmp;
        }
        return;
    }

    public void MapTaskToVM(double[][] POP, double[][] MAP, double[] VEC) {
        for (int i = 0; i < POP.length; i++) {
            for (int j = 0; j < POP[0].length; j++) {
                MAP[i][j] = Math.ceil(POP[i][j] * VEC[j]);
            }
        }
        return;
    }

    public void cost_matrix() {//this was represented as CM(Communication cost matrix)


    }

    public void CalFitness(double[][] ETC, double[] Row_of_Map, double[][] CM) {


    }


    //ETC is the Expected time to compute
    //CM is the Communication cost matrix
    public void Main_Procedure(double[][] POP, double[][] ETC, double[][] CM) {
        for (int i = 0; i < POP.length; i++) {
            CalFitness(ETC, MAP[i], CM);
            Pbest[i] = POP[i];
        }
        for (int i = 0; i < POP.length; i++) {
            double temp = F(Pbest[i]);
            if (Min_Fit > temp) {
                Min_Fit = temp;
                Gbest = Pbest[i];
            }
        }
        Ietr = 0; //??????? no idea what is Ietr here
        while (Ietr < Max_Ietr) {
            for (int k = 0; k < POP.length; k++) {
//                update velocity and position of POPk Using eq8 and 9
                MapTaskToVM(POP, MAP, VEC);
                CalFitness(ETC, MAP[k], CM);
                if (F(POP[k]) < F(Pbest[k])) {
                    Pbest[k] = POP[k];
                }
                if (F(Pbest[k]) < F(Gbest)) {
                    Gbest = Pbest[k];
                }
            }
            Ietr++;
            //calculate makespan and resource utilization

        }


    }

    private double F(double[] best) {// find the best value and return

        return Double.parseDouble(null);

    }

    public ArrayList<Object> taskMapping() {

    }
}

// here we are using our own fitness calculation function
//    public void fitness_function(double[][] POP,double[] VEC,){
//            for (int i=0; i<POP.length; i++){
//                CalFitness(ETC,)
//            }
//        }
//
//
//    }







