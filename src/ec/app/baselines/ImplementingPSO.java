package ec.app.baselines;

//import com.sun.java.swing.plaf.windows.TMSchema;
import ec.pso.Particle;
import org.jfree.util.ArrayUtilities;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ImplementingPSO {
    int numOfParticle = 10000;
    public ArrayList<Particle>  pBest;
    public Particle p_current;
    public Particle g_best; //best particle in this generation
    int numOfBestParticle = 50; // here just initialise as a constant which shouldnt be
    public ArrayList<Particle> Particle_list;

    public ImplementingPSO() {//constructor
        this.Particle_list = new ArrayList<Particle>();
        this.pBest =new ArrayList<Particle>();
    }

    public void main() {
        for (int i = 0; i < numOfParticle; i++) { // first for loop
            p_current = Particle_list.get(i);
            pBest.add(Generate_initial_position(p_current));

        }
        for (int index = 0; index < numOfBestParticle; index++) {
            g_best = Max(pBest);


        }


    }
    // this function will select the solution which has the minimum makespan and the minimum  time cost solution
    private Particle Max(ArrayList<Particle> pBest) {



        return null;
    }
    private Particle minMakespan(ArrayList<Particle>pBest){


        return null;
    }
    private Particle minTimeConsuming(ArrayList<Particle>pBest){
        return null;
    }

    private Particle Generate_initial_position(Particle p_current) {

        return null;
    }


    // inner class just to remove the exception
    public class Task {
        public Task() {
        }

    }
    public class VM{
        double price;
        int number;
        String type;
        public VM(int num,String type, double price ){
            this.price = price;
            this.type = type;
            this.number = num;
        }

    }

    public class Particle {
        int num_makespan;
        double execution_time ;
        public Particle() {
            this.num_makespan=0;
            this.execution_time = 0;

        }
    }
}
