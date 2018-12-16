package ec.app.tutorial5;
import ec.app.tutorial4.Task;
import ec.app.tutorial4.VirtualMachine;

import java.io.IOException;
import java.util.ArrayList;


//public class PSO_based_approach  implements IAlgorithm
public class PSO_based_approach {
    public double[][] ETC ;
    private ArrayList<Task> taskList;//tasks
    private int j;
    private ArrayList<VirtualMachine> ls_vms;//VMs
    int numberOfVM = 0;
    public double[][] MAP;//hold the mapping schedule (task - VM )
    public double[][] POP;
    private double[] VEC;
    //  public double Min_Fit;// changed it to a local variable
    public double besti;
    private double[][] Pbest;
    private double[] Gbest;
    private double Ietr;//index of current iteration
    private double Max_Ietr=500;
    public int number_particle = 20;
    public boolean best_solution = false;// indicate rather find the best solution or not
    private int pop_size = 20;
    private static double c1 = 2.05;
    private static double c2 = 2.05;
    private static double w = 0.5314;
    private double current_fitness =0;

    public PSO_based_approach(Object task, int number_particle, double[] VEC) {
        this.number_particle = number_particle;
        this.VEC = VEC;
    }
    public PSO_based_approach(ArrayList<Task> taskList,  ArrayList<VirtualMachine> ls_vms, int j) {
        this.ls_vms = ls_vms;
        this.j = j;
        this.taskList = taskList;
        Initialization(POP,VEC);//initialise
    }
    public ArrayList<Object> taskMapping( int j) {
        ArrayList<Object> updatedVals = new ArrayList<Object>();//it is used as the returning value which holds the matching between task and VMs
        double[][] POP =new double [this.number_particle][this.taskList.size()];
        double[] VEC = new double[this.taskList.size()];
        Main_Procedure(this.POP,this.ETC);
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
        this.POP = POP;//update two array(POP and VEC ) hold in field
        this.VEC = VEC;
        MapTaskToVM(this.POP,this.MAP,this.VEC);
        return;
    }
    //    the initial mapping generated --it will create the initial mapping
    public void MapTaskToVM(double[][] POP, double[][] MAP, double[] VEC) {
        for (int i = 0; i < POP.length; i++) {
            for (int j = 0; j < POP[0].length; j++) {
                MAP[i][j] = Math.ceil(POP[i][j] * VEC[j]);
            }
        }
        ETC = generateETCMatrix(this.taskList,this.ls_vms);
    }
    //    calculate the ETC matrix
    private double[][] generateETCMatrix(ArrayList<Task> taskList, ArrayList<VirtualMachine> ls_vms)  {//if nothing inside of it then just throw a exception(better change here to an specific exception like define an exception)
        double[][] ETC = new double[taskList.size()][ls_vms.size()];
        if(!(taskList.isEmpty()||ls_vms.isEmpty())) {
            for (int i = 0; i < taskList.size(); i++) {
                for (int j = 0; j < ls_vms.size(); j++) {
                    ETC[i][j] = taskList.get(i).task_size / ls_vms.get(j).velocity;
                }
            }
            this.ETC = ETC;
        }
        return ETC;
    }

    // calculate CM matrix
    public void Communication_Matrix() {//this was represented as CM(Communication cost matrix) in the paper

    }

//    ETC is the Expected time to compute
//    CM is the Communication cost matrix
//    without any specification here we set it to 20 iteration
    public void Main_Procedure(double[][] POP, double[][] ETC) {
        double Min_Fit=0;
        for (int i = 0; i < POP.length; i++) {
//            CalFitness(ETC, MAP[i], CM);// it should have CM but currently have not figured out why I need this matrix
            CalFitness(ETC, MAP[i]);
            Pbest[i] = POP[i];
        }
        for (int i = 0; i < POP.length; i++) {
            double temp = F(Pbest[i]);
            if (Min_Fit > temp) {
                Min_Fit = temp;
                Gbest = Pbest[i];
            }
        }
        Ietr = 0; //number of iteration
        while (Ietr < Max_Ietr) {
            for (int k = 0; k < POP.length; k++) {
//                update velocity and position of POPk Using eq8 and 9

                MapTaskToVM(POP, MAP, VEC);
//                CalFitness(ETC, MAP[k], CM);
                if (F(POP[k]) < F(Pbest[k])) {
                    Pbest[k] = POP[k];
                }
                if (F(Pbest[k]) < F(Gbest)) {
                    Gbest = Pbest[k];
                }
            }
            Ietr++;
            if(Ietr == Max_Ietr) this.best_solution =true;
            //calculate makespan and resource utilization

        }


    }
    private double CalFitness(double[][] ETC, double[] Map_Col){
        return 0;
    }

    private double F(double[] best) {// find the best value and return
        return Double.parseDouble(null);
    }

    public double getC1() {
        return c1;
    }

    public void setC1(double c1) {
        this.c1 = c1;
    }

    public double getC2() {
        return c2;
    }

    public void setC2(double c2) {
        this.c2 = c2;
    }

    //  setters and getters starts from here
    public double getCurrent_fitness() {
        return current_fitness;
    }

    public void setCurrent_fitness(double current_fitness) {
        this.current_fitness = current_fitness;
    }
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
//        class Mapping_Option{
//            double[][] mapping;
//            double fitness;
//            public Mapping_Option(double[][] Map){
//                this.mapping = Map;
//
//
//        }
//
//
//    }

}


