package ec.app.tutorial5;

public class PSO_based_approach {
    int numberOfVM =0;//
    public double[][] MAP;//
    public double[][] POP;//
    public double[] VEC;
    public PSO_based_approach(Object task, int number_particle, double[] VEC){

    }
    public int number_particle;
//    public
    public void Initialization(double[][] POP, double[]VEC){
        for(int i=0; i<POP.length; i++){
            for(int j =0 ; j< POP[0].length; j++){
                POP[i][j] = Math.random();//here initial the matrix which number of rows represent the population size and the column size equal to the number of tasks
            }
        }
        for(int j =0; j < POP[0].length; j++){
            double tmp = j*numberOfVM;
            if(tmp >numberOfVM){
                while(tmp >numberOfVM){
                    tmp = Math.floor(tmp/numberOfVM);
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
    public void cost_matrix(){//this was represented as CM(Communication cost matrix)



    }
    public void CalFitness(){

    }

    public void Main_Procedure(double[][] POP){
        for(int i=0; i < POP.length; i++){
            CalFitness();
            Pbest[i] = POP[i];


        }



    }




    public void fitness_function(double[][] POP,double[] VEC,){
            for (int i=0; i<POP.length; i++){
                CalFitness(ETC,)
            }
        }


    }







