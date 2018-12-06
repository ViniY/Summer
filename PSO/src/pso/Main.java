
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pso;

import java.io.*;

/**
 *
 * @author xuebing
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        int number_of_particles = 30;
        int number_of_iterations = 100;
        int number_of_runs = 10;
        double c1 = 1.49618, c2 = 1.49618;

        Swarm s = new Swarm();

        s.setProblem(new AckleyProblem());
         s.setTopology(new StarTopology());
       //  s.setTopology(new RingTopology(4));

        s.setVelocityClamp(new BasicVelocityClamp());

        for (int i = 0; i < number_of_particles; ++i) {
            Particle p = new Particle();
            p.setSize(20);
            p.setC1(c1);
            p.setC2(c2);

            p.setInertia(0.7298);
            s.addParticle(p);
        }

        /**
         *  // start operate PSO
         */
        /** Sava the results
         */
        double Fitness_Runs_Iterations[][] = new double[number_of_runs][number_of_iterations]; // get bestfitnes in each iterate in each run
        double AverageFitnessRuns[] = new double[number_of_iterations]; // average best fitness in each iterate in all runs
        double BestFitnessRuns[] = new double[number_of_runs];    // final get bestfitnes in each run
        /** start PSO
         */
        for (int r = 0; r < number_of_runs; ++r) {

            s.initialize();
            for (int i = 0; i < number_of_iterations; ++i) {
                s.iterate();
                // System.out.println("BEST=" + s.getParticle(11).getPersonalFitness());
                // System.out.println("Average=" + NewMath.Averageitera(s));

                /**
                 *  // get bestfitnes in every iterate for different topology except star
                 */
                Particle best_particle = null;
                double best_fitness = s.getProblem().getWorstFitness();
                for (int j = 0; j < s.numberOfParticles(); ++j) {
                    if (s.getProblem().isBetter(s.getParticle(j).getPersonalFitness(), best_fitness)) {
                        best_particle = s.getParticle(j);
                        best_fitness = best_particle.getPersonalFitness();
                    }
                }
                Fitness_Runs_Iterations[r][i] = best_fitness;
            // BestFitnessIterations[i] = best_fitness;
            /**
             *  // get bestfitnes in each iterate
             */
            }//end all iterations

            BestFitnessRuns[r] = Fitness_Runs_Iterations[r][number_of_iterations - 1];
        /**
         *  // get bestfitnes in each run
         */
        }//end all runs


        // get bestfitnes from final bestfitnes in all runs
        double Bestbest_fitness = s.getProblem().getWorstFitness();
        int bestrun = 0;
        for (int r = 0; r < number_of_runs; ++r) {
            if (s.getProblem().isBetter(BestFitnessRuns[r], Bestbest_fitness)) {
                bestrun = r;
                Bestbest_fitness = BestFitnessRuns[bestrun];
            // best_particle = s.getParticle(j);
            }
        }
        System.out.println(bestrun);
        System.out.println("Best result of " + number_of_runs + " runs is:" + Bestbest_fitness);
        System.out.println("Average of best results of " + number_of_runs + " runs is:" + NewMath.Best_Mean_STD(BestFitnessRuns)[0]);
        System.out.println("Standard Deviation of best results of " + number_of_runs + " runs is:" + NewMath.Best_Mean_STD(BestFitnessRuns)[1]);

        AverageFitnessRuns = NewMath.AverageRunIterations(Fitness_Runs_Iterations);  // average best fitness in each iterate in all runs

        /** Output the results to tex
         */
        // Print all the best fitness in all the runs to BestfitnessRuns.txt
        // Print all the best fitness in all the runs to Plot.txt, which are used to plot the averagefitness--iter figure
        try {
            // File path=new File("F:/test");
            File myFilePath = new File("BestfitnessRuns.txt");
            File myFilePath2 = new File("Plot.txt");

            if (!myFilePath.exists()) {
                myFilePath.createNewFile();
            }
            if (!myFilePath2.exists()) {
                myFilePath2.createNewFile();
            }

            FileWriter pfile = new FileWriter(myFilePath);
            PrintWriter myFile = new PrintWriter(pfile);

            FileWriter pfile2 = new FileWriter(myFilePath2);
            PrintWriter myFile2 = new PrintWriter(pfile2);

            for (int r = 0; r < number_of_runs; ++r) {
                myFile.println(BestFitnessRuns[r]);
            }
            pfile.close();
            for (int i = 0; i < number_of_iterations; ++i) {
                myFile2.println(AverageFitnessRuns[i]);
            // System.out.println(AverageFitnessRuns[i]);
            }
            pfile2.close();

        } catch (Exception e) {
            System.out.println("eorr");
        }
    }
}
