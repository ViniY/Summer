package ec.app.tutorial5;
import ec.app.tutorial4.Task;
import ec.app.tutorial4.VirtualMachine;

import java.util.*;

public class PSO {
    public Queue<Task>  queue = new LinkedList<>();
//    private double[][] ETC;
    private double[][] Pbest;
    private double[] Gbest;// set with the index of the best particle
//    private double[] VEC;
    private int generation = 0;
    private ArrayList<Task> task_list = new ArrayList<>();
    private  ArrayList<VirtualMachine> ls_vms = new ArrayList<>();
    private int Swarm_Size = 3; //without specification the swarm size is 20
    private double c1 = 2.05;
    private double c2 = 2.05;
    private double w = 0.5314;
    private int MAX_ITER  = 3;
//    private int numberOfVM;
    private ArrayList<Particle> list_particle = new ArrayList<>();
    private int[][] MAP;//hold the mapping schedule (task - VM )
    public boolean best_solution_boolean = false;
    public ArrayList<Object> updateVals = new ArrayList<>(); // returned arraylist which is used to calculate the fitness
    public int[] final_best_solution;//store the best solution in each generation
    public double Min_fiteness = Double.MAX_VALUE; // initial with the maximum value
    private double gbest_fitness = -Double.MAX_VALUE;
    private int[] bestSolution;
    private double Gbest_coeff= 1;
//    public Particle bestParticle = new Particle(this.task_list,this.ls_vms,-1);


    //Constructor for PSO which holds particles -(represents solution)
    public PSO(ArrayList<Task> taskList, ArrayList<VirtualMachine> ls_vms, int seed){//j is useless
        java.util.Random seedGenerator = new java.util.Random();
        seedGenerator.setSeed((long) seed);
        this.ls_vms = ls_vms;
        this.task_list = taskList;
        int generation = 0 ;
        this.generation = generation;
//        this.VEC = new double[this.task_list.size()];
//        this.numberOfVM = this.ls_vms.size();
        this.MAP =new int[this.task_list.size()][this.task_list.size()];
        this.Pbest = new double[this.Swarm_Size][this.task_list.size()];
        this.final_best_solution = new int[this.task_list.size()];
        this.Gbest = new double[this.task_list.size()];
        this.bestSolution = new int[this.task_list.size()];
        Initialization();
    }

    public PSO() {// constructor built here to let the particle class extend
    }

    @SuppressWarnings("Duplicates")
    //  intialise POP and VEC this two arrays with this function with random value varies from 0 to 1 based on uniform distribution
    public void Initialization() {
        System.out.println("Printing hash code for old list and the first task inside of it:" + " 1  :" + this.task_list.hashCode() + " 2 :" + this.task_list.get(0).hashCode()  );
        for (int i=0; i < this.Swarm_Size; i++){
//            ArrayList<Task> clone_task = (ArrayList<Task>) Utility.DeepClone_Seializable(this.task_list);
            ArrayList<Task> clone_task = new ArrayList<>();
            clone_task = Utility.TaskListCloning(task_list);
            System.out.println("Printing the new Hash Codes :" + " 1 : " + clone_task.hashCode() + " 2 : " + clone_task.get(0).hashCode());
            Particle p = new Particle(this.task_list,this.ls_vms,-1);
            this.list_particle.add(p);
        }
        for (int i = 0; i < this.Pbest.length; i++){
            for(int j=0; j < this.Pbest[0].length; j++){
                Pbest[i][j] = Double.MAX_VALUE;
            }
        }
        generateETCMatrix(this.task_list,this.ls_vms);
        System.out.println("PSO initialised");
        return;
    }
//    calculate the ETC matrix
    private double[][] generateETCMatrix(ArrayList<Task> taskList, ArrayList<VirtualMachine> ls_vms) {//if nothing inside of it then just throw a exception(better change here to an specific exception like define an exception)
        double[][] ETC = new double[taskList.size()][ls_vms.size()];
        if (!(taskList.isEmpty() || ls_vms.isEmpty())) {
            for (int i = 0; i < taskList.size(); i++) {
                for (int j = 0; j < ls_vms.size(); j++) {
                    ETC[i][j] = taskList.get(i).task_size / ls_vms.get(j).velocity;
                }
            }
//            this.ETC = ETC;
        }
        return ETC;
    }

    public Map<String, ArrayList> taskMapping(int j){
        Map<String,ArrayList> returning = new HashMap<>();
//            ArrayList<Object> updateVal = new ArrayList<>();//the final Mapping solution
            returning = Main_Procedure();
        return returning;
    }





    public Map<String, ArrayList> Main_Procedure(){
        double makeSpan = 0.0;
        double fitness = Double.MAX_VALUE;
        int generation_best_index = 0;
        ArrayList<Task> updated_task = new ArrayList<>();
        ArrayList<Object> update  = new ArrayList<>();
        for(int i =0 ; i < list_particle.size(); i++){
            list_particle.get(i).setFitness(CalFitness(list_particle.get(i)));
            if(list_particle.get(i).getFitness() < fitness){
                fitness = list_particle.get(i).fitness;
            }
        }
        this.gbest_fitness = fitness;
        int generation_best[] = new int[task_list.size()];
        int best_index =-1;//represent the index of the current best solution
        for(int index = 0; index < Swarm_Size; index++) {
//            if(Min_fiteness > F(Pbest[index])){
//                Min_fiteness = F(Pbest[index]);
//                best_index = index;
//                Gbest = Pbest[index];
//            }
            CalFitness(list_particle.get(index));
        }
        int index_of_best_particle = -1;
        for(int i=0; i < Swarm_Size; i++){
            if(list_particle.get(i).fitness <  Min_fiteness){
                Min_fiteness = list_particle.get(i).fitness;
                index_of_best_particle= i ;
            }
        }
        if(index_of_best_particle == -1 ) index_of_best_particle =0;
        Gbest = list_particle.get(index_of_best_particle).POP;
        int Ietr = 0 ;
//        bestParticle.fitness = Double.MAX_VALUE;//initial the most valued particle here used to store the best particle which make it way easier to return the global best solution
        while(Ietr < MAX_ITER){
            System.out.println("********************************************");
            for(int k=0; k < Swarm_Size; k++){
                Particle p= list_particle.get(k);
                p.updateVelocity(Ietr,this.Gbest);
                p.MapTaskToVM(Ietr);
                CalFitness(p);
                if(p.fitness < p.pbest_fitness){
                    p.setPbest_fitness(p.fitness,p.getSolution());
                    p.setLocal_best_index(Ietr);
                    System.out.println("local best updating");
                }
                if(p.pbest_fitness < gbest_fitness){
                    gbest_fitness = p.current_fitness;
                    this.Gbest  = p.POP;//g_best = Pbest k
                    best_index = k;
                    updated_task = p.task_list;
                    System.out.println("global best updating ");
//                    this.bestParticle = p;
                }
            }
            Ietr++;
//            makeSpan = bestParticle.fitness;
            if(best_index!=-1){
            makeSpan = list_particle.get(best_index).pbest_fitness;
            }
//            if(Ietr == MAX_ITER) System.out.println("this is the best solution" + makeSpan);
//            System.out.println("Now we are in the "+ Ietr +" Iteration and our makespan of the current best is: " + makeSpan );
            System.out.println("Gbest Makespan : "+ gbest_fitness);
        }

        if(Ietr == MAX_ITER){
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
            for(int i=0; i < task_list.size(); i++){
                update.add(task_list.get(i));
                update.add(ls_vms.get( bestSolution[i]));
            }
        }
        Map<String,ArrayList> returning = new HashMap<>();
        returning.put("solution_updated",update);
        returning.put("updated_tasks",updated_task);
        return returning;
    }

    private double CalculateMakeSpan() {
        return 0;
    }

    private double F(double[] doubles) {
        return 0;
    }

    public double CalFitness(Particle p) {
        return p.CalculateFitness(p.Solution);
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



    class Particle extends PSO implements Cloneable{
        private ArrayList<VirtualMachine> ls_vms = new ArrayList<>();
        private ArrayList<Task> task_list = new ArrayList<>();
        private int[] Solution;
        private double[] POP;
        private double[] VEC;
        private double[] Pbest_k;
        private double fitness = Double.MAX_VALUE;
        private double pbest_fitness;//store the local best fitness
        private int num_VMS = 0;
        private int num_iter = 0;
        private double current_fitness;
        private double Velocity[];
        private double S[];
        private int[] bestSolutionSoFar;
        private int local_best_index;
        private double[]local_best_pop;

        public Particle(ArrayList<Task> taskList, ArrayList<VirtualMachine> ls_vms, int j){
            super();
            this.Solution = new int[taskList.size()];
            this.task_list = taskList;
            this.ls_vms = ls_vms;
            this.POP = new double[this.task_list.size()];
            this.VEC = new double[this.task_list.size()];
            this.Pbest_k = new double[this.task_list.size()];
            this.num_VMS = ls_vms.size();
            this.Velocity = new double[this.task_list.size()];
//            this.S = new double[MAX_ITER];
            this.pbest_fitness = Double.MAX_VALUE;

            this.bestSolutionSoFar = new int[this.task_list.size()];
            Initialise_Particle();
            this.local_best_pop = POP;
            MapTaskToVM(0);
            System.out.println("new Particle initialised");
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
        public void MapTaskToVM(int iter ) {
            int[] solution = new int[this.task_list.size()];
//            if(iter ==0) {

                for (int i = 0; i < POP.length; i++) {
                    solution[i] = (int) Math.floor(this.POP[i] * this.VEC[i]);// should be ceil in the paper but bound problem occured not sure what should do here
//                solution[i] =(int) Math.ceil(this.POP[i]*this.VEC[i]);
                }
//            }
//            else{
//                for(int i=0; i < POP.length; i++){
//                    solution[i] = (int) Math.floor(this.POP[i] * this.Velocity[i]);
//                }
//            }
            for(int index= 0; index < POP.length; index++){
                    if(solution[index]<0 ){solution[index] = 0;}
                    if(solution[index]>(ls_vms.size()-1)){solution[index]=(ls_vms.size()-1);}
            }

            this.Solution = solution;
        return;
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

      @SuppressWarnings("Duplicates")

        public void updateVelocity(int iter,double[] Gbest) {
//            if(S[iter]<0) S[iter] = 0.001;
//            else if (S[iter]>1) S[iter] = 0.999;
            double r1 =0;
            double r2 =0;
            while(r1==0 ||r2==0){
                r1 = Math.random();
                r2 = Math.random();
            }
//          for(int i =0 ; i < task_list.size(); i++) {//velocity check
//              System.out.println("Velocity t: " + Velocity[i]);
//          }
            double ww =0;
            double c1r1 =0;
            double c2r2=0;
            for(int i =0 ; i < task_list.size(); i++){
                Velocity[i] = w * Velocity[i] + c1 * r1 *(this.local_best_pop[i] - this.POP[i]) + c2 * r2 * (Gbest[i] - this.POP[i]);
                 ww = w * Velocity[i];
                 c1r1 = c1 * r1 *(this.local_best_pop[i] - this.POP[i]);
                 c2r2 = c2 * r2 * (Gbest[i] - this.POP[i]);
                POP[i] = POP[i] + Velocity[i];
                    if(POP[i] < 0 ) POP[i] = 0.001;
                    if(POP[i] > 1 ) POP[i] = 0.999;

                }

        }

        public double getPbest_fitness() {
            return pbest_fitness;
        }
        public void setPbest_fitness(double pbest_fitness,int[] BestSolutionSoFar) {
            this.pbest_fitness = pbest_fitness;
            this.bestSolutionSoFar = BestSolutionSoFar;
        }
        public double CalculateFitness(int[] Solution) {
            resetTaskStartFinishTime(Solution);
            setTaskFinishTime(Solution);
            double total_time = 0;
            total_time += Utility.getTasksMaxSpan(this.task_list);
            if(total_time<this.pbest_fitness) this.pbest_fitness = total_time;
            this.fitness = total_time;
            this.fitness = total_time;
            this.local_best_pop = POP;
            return total_time;
        }

        private void setTaskFinishTime(int[] solution) {
            for(int index =0; index < task_list.size(); index++){
                Task t = task_list.get(index);
                ArrayList<Task> parentTasks = new ArrayList<>();
                parentTasks  = Utility.getParentTasksById(task_list,t.getId());
                t.setAllocation_time(Utility.getMaxFinishTime(parentTasks));
                t.setExe_time((double) t.getTask_size()/ls_vms.get(Solution[index]).getVelocity());

                double preFinishTime = Utility.getMaxFinishTime(ls_vms.get(Solution[index]).getPriority_queue());
                t.setStart_time(Utility.getMaxStartTime(preFinishTime,t.getAllocation_time()));
                t.setRelative_finish_time();
                t.setFinish_time();
                ls_vms.get(Solution[index]).setPriority_queue(t);
            }
//            System.out.println("reset the task execution time");
        }
        private void resetTaskStartFinishTime(int[] solution) {
            for(Task t : task_list){
                t.waiting_time = 0;
                t.setStart_time(0);
                t.finish_time=0;
                t.setFinish_time();
            }
        }
        public Particle clone(){
            try{
                Particle pObj =  (Particle)(super.clone());
                return pObj;
            }
            catch (CloneNotSupportedException e){
                e.printStackTrace();
            }
            return null;
        }

        public int getLocal_best_index() {
            return local_best_index;
        }

        public void setLocal_best_index(int local_best_index) {
            this.local_best_index = local_best_index;

        }
    }


}