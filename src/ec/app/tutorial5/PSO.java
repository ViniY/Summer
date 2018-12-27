package ec.app.tutorial5;
import ec.app.tutorial4.Task;
import ec.app.tutorial4.VirtualMachine;
import java.io.IOException;
import java.util.ArrayList;

public class PSO {
    private double[][] ETC ;
    private double[][] POP;
    private double[][] Pbest;
    private double[] Gbest;
    private double[] VEC;
    private ArrayList<Task> task_list = new ArrayList<>();
    private  ArrayList<VirtualMachine> ls_vms = new ArrayList<>();
    private int generation;
    private int Swarm_Size = 20; //without specification the swarm size is 20
    private double c1 = 2.05;
    private double c2 = 2.05;
    private double w = 0.5314;
    private int MAX_ITER  = 500;
    private int numberOfVM;
    private ArrayList<Particle> list_particle = new ArrayList<>();
    private int[][] MAP;//hold the mapping schedule (task - VM )
    public PSO(ArrayList<Task> taskList, ArrayList<VirtualMachine> ls_vms, int j){//j is useless
        this.ls_vms = ls_vms;
        this.task_list = taskList;
        int generation = 0 ;
        this.generation = generation;
        this.POP = new double[this.Swarm_Size][this.task_list.size()];
        this.VEC = new double[this.task_list.size()];
        this.numberOfVM = this.ls_vms.size();
        this.MAP =new int[this.task_list.size()][this.task_list.size()];
        Initialization(POP,VEC);
    }
    @SuppressWarnings("Duplicates")
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
        this.ETC = generateETCMatrix(this.task_list,this.ls_vms);
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









    // getter and setters
    public void setTask_list(ArrayList<Task> task_list) {
        this.task_list = task_list;
    }

    public void setLs_vms(ArrayList<VirtualMachine> ls_vms) {
        this.ls_vms = ls_vms;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public void setC1(double c1) {
        this.c1 = c1;
    }

    public void setC2(double c2) {
        this.c2 = c2;
    }

    public void setW(double w) {
        this.w = w;
    }
    public void set_Swarm_Size(int num){
        this.Swarm_Size = num;
    }
    public ArrayList<Task> getTask_list() {
        return task_list;
    }
    public ArrayList<VirtualMachine> getLs_vms() {
        return ls_vms;
    }
    public int getGeneration() {
        return generation;
    }
    public int getSwarm_Size() {
        return Swarm_Size;
    }
    public double getC1() {
        return c1;
    }
    public double getC2() {
        return c2;
    }
    public double getW() {
        return w;
    }




    class Particle{
        private  ArrayList<VirtualMachine> ls_vms = new ArrayList<>();
        private ArrayList<Task> task_list = new ArrayList<>();
        private double[] Solution;
        private double fitness = 0;

        public Particle(ArrayList<Task> taskList, ArrayList<VirtualMachine> ls_vms, int j){
            this.Solution = new double[taskList.size()];
            this.task_list = taskList;
            this.ls_vms = ls_vms;
        }




    }
}
