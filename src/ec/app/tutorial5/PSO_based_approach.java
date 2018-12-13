package ec.app.tutorial5;

import ec.app.tutorial4.Task;
import ec.app.tutorial4.VirtualMachine;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class PSO_based_approach  implements IAlgorithm{
    private ArrayList<Task> taskList;//tasks
    private int j;
    private ArrayList<VirtualMachine> ls_vms;//VMs
    int numberOfVM = 0;
    public double[][] MAP;
    public double[][] POP;
    private double[] VEC;
    public double Min_Fit;
    public double besti;
    private double[][] Pbest;
    private double[] Gbest;
    private double Ietr;
    private double Max_Ietr;
    public int number_particle = 100;
    private int pop_size;


    public PSO_based_approach(Object task, int number_particle, double[] VEC) {
        this.number_particle = number_particle;
        this.VEC = VEC;
    }
    public PSO_based_approach(ArrayList<Task> taskList,  ArrayList<VirtualMachine> ls_vms, int j) {
        this.ls_vms = ls_vms;
        this.j = j;
        this.taskList = taskList;
    }
    public ArrayList<Object> taskMapping( int j) {
        ArrayList<Object> updatedVals = new ArrayList<Object>();//it is used as the returning value which holds the matching between task and VMs
        double[][] POP =new double [this.number_particle][this.taskList.size()];
        double[] VEC = new double[this.taskList.size()];
        Initialization(POP,VEC);

        return updatedVals;
    }

//  intialise POP and VEC this two arrays with this function with random value varies from 0 to 1 based on uniform distribution
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
                    tmp = Math.floor(tmp / numberOfVM);//this math function will return a closest int value which is smaller than the value
                }
            }
            VEC[j] = tmp;
        }
        return;
    }
//    the initial mapping generated
    public void MapTaskToVM(double[][] POP, double[][] MAP, double[] VEC) {
        for (int i = 0; i < POP.length; i++) {
            for (int j = 0; j < POP[0].length; j++) {
                MAP[i][j] = Math.ceil(POP[i][j] * VEC[j]);
            }
        }
        return;
    }

    public void cost_matrix() {//this was represented as CM(Communication cost matrix) in the paper


    }
//    this function is used to calculate fitness of the solution but we are using yalian's fitness function which is also the same fitness function we are using in GP
//    So here we are not following this paper's fitness function
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


//  setters and getters starts from here
    public void setPop_size(int size){
        this.pop_size = size;
    }

    public int getNumberOfVM() {
        return numberOfVM;
    }

    public void setNumberOfVM(int numberOfVM) {
        this.numberOfVM = numberOfVM;
    }

    public double[][] getMAP() {
        return MAP;
    }

    public void setMAP(double[][] MAP) {
        this.MAP = MAP;
    }
    public int getNumberOfParticle(){
        return this.number_particle;
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







