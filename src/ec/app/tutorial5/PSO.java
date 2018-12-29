package ec.app.tutorial5;
import ec.app.tutorial4.Task;
import ec.app.tutorial4.VirtualMachine;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class PSO {
    private double[][] ETC;
    private double[][] Pbest;
    private int[][] Gbest;// set with the index of the best particle
    private double[] VEC;
    private int generation = 0;
    private ArrayList<Task> task_list = new ArrayList<>();
    private  ArrayList<VirtualMachine> ls_vms = new ArrayList<>();
    private int Swarm_Size = 20; //without specification the swarm size is 20
    private double c1 = 2.05;
    private double c2 = 2.05;
    private double w = 0.5314;
    private int MAX_ITER  = 500;
    private int numberOfVM;
    private ArrayList<Particle> list_particle = new ArrayList<>();
    private int[][] MAP;//hold the mapping schedule (task - VM )
    public boolean best_solution = false;
    public ArrayList<Object> updateVals = new ArrayList<>(); // returned arraylist which is used to calculate the fitness


    //Constructor for PSO which holds particles -(represents solution)
    public PSO(ArrayList<Task> taskList, ArrayList<VirtualMachine> ls_vms, int j){//j is useless
        this.ls_vms = ls_vms;
        this.task_list = taskList;
        int generation = 0 ;
        this.generation = generation;
        this.VEC = new double[this.task_list.size()];
        this.numberOfVM = this.ls_vms.size();
        this.MAP =new int[this.task_list.size()][this.task_list.size()];
        this.Pbest = new double[this.generation][this.Swarm_Size];
        Initialization();
    }
    @SuppressWarnings("Duplicates")
    //  intialise POP and VEC this two arrays with this function with random value varies from 0 to 1 based on uniform distribution
    public void Initialization() {
        for (int i=0; i < this.Swarm_Size; i++){
            Particle p = new Particle(this.task_list,this.ls_vms,-1);
            this.list_particle.add(p);
        }
        System.out.println("Particles initialised");
        for (int i = 0; i < this.Pbest.length; i++){
            for(int j=0; j < this.Pbest[0].length; j++){
                Pbest[i][j] = Double.MAX_VALUE;
            }
        }
        System.out.println("Initialised Pbest Matrix with max values");
        return;
    }
//    //    the initial mapping generated --it will create the initial mapping
//    public void MapTaskToVM(double[][] POP, int[][] MAP, double[] VEC) {
//        for (int i = 0; i < POP.length; i++) {
//            for (int j = 0; j < POP[0].length; j++) {
//                MAP[i][j] = (int) Math.ceil(POP[i][j] * VEC[j]);
//            }
//        }
//        this.ETC = generateETCMatrix(this.task_list,this.ls_vms);
//    }
//    calculate the ETC matrix
    private double[][] generateETCMatrix(ArrayList<Task> taskList, ArrayList<VirtualMachine> ls_vms) {//if nothing inside of it then just throw a exception(better change here to an specific exception like define an exception)
        double[][] ETC = new double[taskList.size()][ls_vms.size()];
        if (!(taskList.isEmpty() || ls_vms.isEmpty())) {
            for (int i = 0; i < taskList.size(); i++) {
                for (int j = 0; j < ls_vms.size(); j++) {
                    ETC[i][j] = taskList.get(i).task_size / ls_vms.get(j).velocity;
                }
            }
            this.ETC = ETC;
        }
        return ETC;
    }

    public void Main_Procedure(){
        double fitness = Double.MAX_VALUE;
        int generation_best_index = 0;
        for(int i =0 ; i < list_particle.size(); i++){
            list_particle.get(i).setFitness(CalFitness(list_particle.get(i)));
            if(list_particle.get(i).getFitness() < fitness){
                fitness = list_particle.get(i).fitness;
            }

        }
        int generation_best[] = new int[task_list.size()];





    }
    public double CalFitness(Particle p){
        return 0.0;
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












    class Particle {
        private  ArrayList<VirtualMachine> ls_vms = new ArrayList<>();
        private ArrayList<Task> task_list = new ArrayList<>();
        private int[] Solution;
        private double[] POP;
        private double[] VEC;
        private double fitness = 0;
        private int num_VMS = 0;

        public Particle(ArrayList<Task> taskList, ArrayList<VirtualMachine> ls_vms, int j){
            this.Solution = new int[taskList.size()];
            this.task_list = taskList;
            this.ls_vms = ls_vms;
            this.POP = new double[this.task_list.size()];
            this.VEC = new double[this.task_list.size()];
            this.num_VMS = ls_vms.size();
            Initialise_Particle();
            MapTaskToVM(this.Solution, this.POP, this.VEC);
        }

//        1 Initialise
        public void Initialise_Particle() {
            for (int i = 0; i < this.task_list.size(); i++) {
                POP[i] = Math.random();
            }
            for (int j = 0; j < POP.length; j++) {
                int tmp = j * num_VMS;
                if (tmp > num_VMS) {
                    while (tmp > num_VMS) {
                        tmp = (int) Math.floor(tmp / num_VMS);
                    }
                }
                VEC[j] = tmp;
            }
            this.POP = POP;//update two array(POP and VEC ) hold in field
            this.VEC = VEC;
            for (int index = 0; index < Solution.length; index++) {
                Solution[index] = 0;//initialise the solution matrix
            }

        }

//      2. Mapping

        public void MapTaskToVM(int[] solution, double[] VEC, double[] POP) {
            for(int i = 0; i < POP.length; i++){
                solution[i] =(int) Math.ceil(this.POP[i]*this.VEC[i]);
            }
            this.Solution = solution;
        return;
        }


        public void update_solution(double[] updating){



        }

        public ArrayList<VirtualMachine> getLs_vms() {
            return ls_vms;
        }
        public void setLs_vms(ArrayList<VirtualMachine> ls_vms) {
            this.ls_vms = ls_vms;
        }
        public ArrayList<Task> getTask_list() {
            return task_list;
        }
        public void setTask_list(ArrayList<Task> task_list) {
            this.task_list = task_list;
        }
        public int[] getSolution() {
            return Solution;
        }
        public void setSolution(int[] solution) {
            Solution = solution;
        }
        public double getFitness() {
            return fitness;
        }
        public void setFitness(double fitness) {
            this.fitness = fitness;
        }
    }
}