/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pso;

import java.util.List;
//import java.util.*;
import java.lang.Math;

/**
 *
 * @author xuebing
 */
public class AckleyProblem extends Problem {

    public AckleyProblem() {
        setMinimization(true);
        setMinDomain(-30);
        setMaxDomain(30);
        setMaxVelocity(10);
    }

    public double fitness(List<Double> position) {
        double sum1 = 0;
        double sum2 = 0;
        double fitness = 0;
        for (int i = 0; i < position.size(); ++i) {
            sum1 += position.get(i) * position.get(i);
            sum2 += Math.cos(2 * Math.PI * position.get(i));
        }
        //m_dFitness 计算出的当前值
        fitness = -20 * Math.exp(-0.2 * Math.sqrt((1.0 / position.size()) * sum1)) - Math.exp((1.0 / position.size()) * sum2) + 20 + Math.E;

        return fitness;
    }
}
