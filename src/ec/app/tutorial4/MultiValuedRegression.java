/*
  Copyright 2006 by Sean Luke
  Licensed under the Academic Free License version 3.0
  See the file "LICENSE" for more information
*/

package ec.app.tutorial4;

import ec.util.*;
import ec.*;
import ec.gp.*;
import ec.simple.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import ec.app.tutorial5.HEFT;
import ec.app.tutorial5.Utility;

public class MultiValuedRegression extends GPProblem implements SimpleProblemForm {
	private static final long serialVersionUID = 1;

	public double taskSize;
	public double velocity;
	public double unitCost;

	public double startTime; // start time
	public double allocationTime;
	public double waitingTime; //
	public double executionTime; // execution time
	public double finishTime; // finish time
	public double relativeFinishTime; // RFT
	//public double constVal; // constant no use

	public ArrayList<Task> ls_tasks;
	public ArrayList<VirtualMachine> ls_vms;

	private static final int TRAINING_SET = 0;
	private static final int TESTING_SET = 1;

	public void setup(final EvolutionState state, final Parameter base) {
		super.setup(state, base);

		// verify our input is the right class (or subclasses from it)
		if (!(input instanceof DoubleData))
			state.output.fatal("GPData class must subclass from " + DoubleData.class, base.push(P_DATA), null);
	}
    @SuppressWarnings("Duplicates")
	public void evaluate(final EvolutionState state, final Individual ind, final int subpopulation,
			final int threadnum) {
		if (!ind.evaluated) // don't bother reevaluating
		{
			DoubleData input = (DoubleData) (this.input);

			try {
				double totalCost = 0;
				double totalTime = 0;
				int v = 0;
				File[] taskFiles = Utility.getTaskFileList(TRAINING_SET); // training set
//				File[] taskFiles = Utility.getTaskFileList(TESTING_SET); // testing set
				File[] vmFiles = Utility.getVMFileList();
//
//				double maxcost = NormalizationHelper.getMaxCost(TRAINING_SET);
//				double mincost = NormalizationHelper.getMinCost(TRAINING_SET);
//				double maxmakespan = NormalizationHelper.getMaxMakeSpan(TRAINING_SET);
//				double minmakespan = NormalizationHelper.getMinMakeSpan(TRAINING_SET);

//				System.out.println("max cost: " + maxcost);
//				System.out.println("min cost: " + mincost);
//				System.out.println("max time: " + maxmakespan);
//				System.out.println("min time: " + minmakespan);

				for (File tf : taskFiles) {// test cases
					ArrayList<Task> ls_tasks = Utility.getTaskList(tf.getPath());
					ArrayList<VirtualMachine> ls_vms = Utility.getVMList(vmFiles[v].getPath());
					v++;
					if (v == vmFiles.length)
						v = 0;

					Queue<Task> queue = new LinkedList<Task>();
					queue = Utility.setTaskPriorityQueue(ls_tasks);
					// System.out.println("Task | VM | Start Time | Execution Time | Finish Time");

					for (int i = 0; i < ls_tasks.size(); i++) {
						if (!queue.isEmpty()) {
							Task t = queue.poll();
							//this.constVal = state.random[threadnum].nextDouble();

							ArrayList<Task> parentTasks = Utility.getParentTasksById(ls_tasks, t.getId());
							ArrayList<Object> udpatedVal = new ArrayList<Object>();
							udpatedVal = taskMapping(state, threadnum, input, ind, parentTasks, ls_vms, t); //GP mapping

//							HEFT heft = new HEFT();
//							udpatedVal = heft.taskMapping(parentTasks, ls_vms, t, 0); // 0: no use

							for (Object o : udpatedVal) {
								if (o instanceof Task) {
									t = (Task) o;
									for (Task p : ls_tasks) {
										if (p.getId().equals(t.getId()))
											p = t;
									}
								} else if (o instanceof VirtualMachine) {
									VirtualMachine m = (VirtualMachine) o;
									for (VirtualMachine u : ls_vms) {
										if (u.getId().equals(m.getId()))
											u = m;
									}
								}
							}
						}
					}

					for (VirtualMachine vm : ls_vms) {
						double totalRFT = 0;
						if (!vm.getPriority_queue().isEmpty()) {
							totalRFT = Utility.getTasksMaxSpan(vm.getPriority_queue());
						}
						totalCost += (double) totalRFT * vm.getUnit_cost_vm();
					}

					totalTime += Utility.getTasksMaxSpan(ls_tasks);
				}

				GPIndividual gpInd = (GPIndividual) ind;


				//gpInd.setFileSize(taskFiles.length);
				double average_total = (double) totalCost / taskFiles.length;
				double average_makespan = (double) totalTime / taskFiles.length;
//
				gpInd.setTotalCost(average_total);
				gpInd.setTotalTime(average_makespan);

//				double nor_cost = (average_total - mincost) / (maxcost - mincost);
//				double nor_makespan = (average_makespan - minmakespan) / (maxmakespan - minmakespan);
//
//				double fitness = 0.4 * nor_cost + 0.6 * nor_makespan;

//				SimpleFitness f = ((SimpleFitness) ind.fitness);
//
//				f.setFitness(state, fitness, false);
				// System.out.println("fitness value: " + f.fitness());

		//		ind.evaluated = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private ArrayList<Object> taskMapping(EvolutionState state, int threadnum, DoubleData input, Individual ind,
			ArrayList<Task> parentTasks, ArrayList<VirtualMachine> ls_vms, Task t) {
		ArrayList<Object> updatedVals = new ArrayList<Object>();
		this.taskSize = t.getTask_size();

		for (VirtualMachine vm : ls_vms) {
			this.unitCost = vm.getUnit_cost_vm();
			this.velocity = vm.getVelocity();
			this.executionTime = this.taskSize / (double) this.velocity;
			this.allocationTime = Utility.getMaxFinishTime(parentTasks);

			double preFinishTime = Utility.getMaxFinishTime(vm.getPriority_queue());

			this.startTime = Utility.getMaxStartTime(preFinishTime, this.allocationTime);
			this.waitingTime = this.startTime - this.allocationTime;
			this.relativeFinishTime = this.waitingTime + this.executionTime;

			((GPIndividual) ind).trees[0].child.eval(state, threadnum, input, stack, ((GPIndividual) ind), this);

			vm.setFit_val(input.x);
		}

		VirtualMachine vmSel = getVMWithMinFitness(ls_vms);

		t.setExe_time(t.getTask_size() / (double) vmSel.getVelocity());
		t.setAllocation_time(this.allocationTime);

		// calculate the max finish time in the selected vm
		double preFinishTime = Utility.getMaxFinishTime(vmSel.getPriority_queue());

		t.setStart_time(Utility.getMaxStartTime(preFinishTime, t.getAllocation_time()));
		t.setWaiting_time();
		t.setRelative_finish_time();
		t.setFinish_time();
		vmSel.setPriority_queue(t);

		updatedVals.add(t);
		updatedVals.add(vmSel);

		return updatedVals;

	}

	private VirtualMachine getVMWithMinFitness(ArrayList<VirtualMachine> vms) {
		Collections.sort(vms, new Comparator<VirtualMachine>() {
			@Override
			public int compare(VirtualMachine v1, VirtualMachine v2) {
				return Double.compare(v1.getFit_val(), v2.getFit_val());
			}
		});

		return vms.get(0);
	}
    @SuppressWarnings("Duplicates")
	public void describe(EvolutionState state, Individual ind, int subpopulation, int threadnum, int log) {
		DoubleData input = (DoubleData) (this.input);
		// print out rules
		GPIndividual gpInd = (GPIndividual) ind;
		GPTree gpt = ((GPIndividual) ind).trees[0];
		GPNode child = gpt.child;

		String rule = child.makeCTree(true, true, true);

		state.output.println("The best rule is: " + rule, log);

		try {
			double totalCost = 0;
			double totalTime = 0;
			int v = 0;
			File[] taskFiles = Utility.getTaskFileList(TESTING_SET); // testing set
			File[] vmFiles = Utility.getVMFileList();

			for (File tf : taskFiles) {// test cases
				ArrayList<Task> ls_tasks = Utility.getTaskList(tf.getPath());
				ArrayList<VirtualMachine> ls_vms = Utility.getVMList(vmFiles[v].getPath());
				v++;
				if (v == vmFiles.length)
					v = 0;

				Queue<Task> queue = new LinkedList<Task>();
				queue = Utility.setTaskPriorityQueue(ls_tasks);
				// System.out.println("Task | VM | Start Time | Execution Time | Finish Time");

				for (int i = 0; i < ls_tasks.size(); i++) {
					if (!queue.isEmpty()) {
						Task t = queue.poll();
						ArrayList<Task> parentTasks = Utility.getParentTasksById(ls_tasks, t.getId());
						ArrayList<Object> udpatedVal = taskMapping(state, threadnum, input, ind, parentTasks, ls_vms,
								t);

						for (Object o : udpatedVal) {
							if (o instanceof Task) {
								t = (Task) o;
								for (Task p : ls_tasks) {
									if (p.getId().equals(t.getId()))
										p = t;
								}
							} else if (o instanceof VirtualMachine) {
								VirtualMachine m = (VirtualMachine) o;
								for (VirtualMachine u : ls_vms) {
									if (u.getId().equals(m.getId()))
										u = m;
								}
							}
						}
					}
				}

				for (VirtualMachine vm : ls_vms) {
					double totalRFT = 0;
					if (!vm.getPriority_queue().isEmpty()) {
						totalRFT = Utility.getTasksMaxSpan(vm.getPriority_queue());
					}
					totalCost += (double) totalRFT * vm.getUnit_cost_vm();
				}

				totalTime += Utility.getTasksMaxSpan(ls_tasks);
			}

			double average_total = (double) totalCost / taskFiles.length;
			double average_makespan = (double) totalTime / taskFiles.length;

			state.output.println("GP - average total cost of testing set is: " + average_total, log);
			System.out.println("GP - average total cost of testing set is: " + average_total);

			state.output.println("GP - average makespan of testing set is: " + average_makespan, log);
			System.out.println("GP - average makespan of testing set is: " + average_makespan);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
