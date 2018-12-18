package ec.app.tutorial5;
import com.sun.javafx.collections.MappingChange;
import ec.app.tutorial4.Task;
import ec.app.tutorial4.VirtualMachine;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


//public class PSO_based_approach  implements IAlgorithm
public class PSO_based_approach {
    public double[][] ETC ;
    private ArrayList<Task> taskList = new ArrayList<>();//tasks
    private int j;
    private ArrayList<VirtualMachine> ls_vms;//VMs
    int numberOfVM = 0;
    public int[][] MAP;//hold the mapping schedule (task - VM )
    public double[][] POP;
    private double[] VEC;
    //  public double Min_Fit;// changed it to a local variable
    public double besti;
    public int number_particle = 20;
    private double[][] Pbest = new double[number_particle][taskList.size()];
    private double[] Gbest = new double[taskList.size()];
    private int Ietr;//index of current iteration
    private int Max_Ietr=500;
    public boolean best_solution = false;// indicate rather find the best solution or not
    private int pop_size = 20;
    private static double c1 = 2.05;
    private static double c2 = 2.05;
    private static double w = 0.5314;
    private double current_fitness =0;
    private double[] V = new double[number_particle];// represents velocity of each particle
    private ArrayList<double[][]> Solution = new ArrayList<>();
    private  double[] S = new double[number_particle]; // represents each particle's position (here the position represent an possible solution )

    
    public PSO_based_approach(Object task, int number_particle, double[] VEC) {
        this.number_particle = number_particle;
        this.VEC = VEC;
    }
    public PSO_based_approach(ArrayList<Task> taskList,  ArrayList<VirtualMachine> ls_vms, int j) {
        this.ls_vms = ls_vms;
        this.j = j;
        this.taskList = taskList;
        double[][] POP =new double [this.number_particle][this.taskList.size()];
        double[] VEC = new double[this.taskList.size()];
        int[][] MAP = new int[number_particle][taskList.size()];
        this.POP = POP;
        this.MAP = MAP;
        this.VEC = VEC;
        this.Pbest= new double[POP.length][taskList.size()];
        Initialization(POP,VEC);//initialise
    }
    public ArrayList<Object> taskMapping( int j) {
        ArrayList<Object> updatedVals = new ArrayList<Object>();//it is used as the returning value which holds the matching between task and VMs

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
        for(int i =0 ; i < MAP.length; i++){
            for(int j=0; j < MAP[0].length; j++){
                MAP[i][j] = 0;
            }
        }
        MapTaskToVM(this.POP,this.MAP,this.VEC);
        return;
    }
    //    the initial mapping generated --it will create the initial mapping
    public void MapTaskToVM(double[][] POP, int[][] MAP, double[] VEC) {
        for (int i = 0; i < POP.length; i++) {
            for (int j = 0; j < POP[0].length; j++) {
                MAP[i][j] = (int) Math.ceil(POP[i][j] * VEC[j]);
            }
        }
        this.ETC = generateETCMatrix(this.taskList,this.ls_vms);
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

    // calculate CM matrix// no use
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
                updateVelocity(Ietr);
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

    private void updateVelocity(int iter) {
        if(S[iter]<0) S[iter] = 0.001;
        else S[iter] = 0.999;
        double r1 =0;
        double r2 =0;
        while(r1==0 ||r2==0){
            r1 = Math.random();
            r2 = Math.random();
        }
        V[iter] = w * V[iter-1] + c1 * r1 *(Pbest[iter] - S[iter -1]) + c2 * r2 * (Gbest[iter] - S )
    }

    private double CalFitness(double[][] ETC, int[] Map_Col){
        double makespan = 0;
        for(int index =0; index < Map_Col.length; index++){
            makespan+= ETC[index][Map_Col[index]];
        }
        return makespan;
    }

    private double F(double[] solution) {// find the best value and return
     double makespan =0;
        for(int index =0; index < solution.length; index++) {
            makespan += taskList.get(index).task_size / ls_vms.get((int)solution[index]).velocity;
        }
        return makespan;
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

    public int[][] getMAP() {
        return MAP;
    }

    public void setMAP(int[][] MAP) {
        this.MAP = MAP;
    }

    public int getNumberOfParticle(){
        return this.number_particle;
    }
}
