package ec.app.tutorial5;

import java.io.File;
import java.util.*;

import ec.app.tutorial4.Task;
import ec.app.tutorial4.VirtualMachine;
@SuppressWarnings("Duplicates")
public class Comparison {
	private static final int ALGORITHM_HEFT = 0;
	private static final int ALGORITHM_GREEDY = 1;
	private static final int ALGORITHM_RR = 2;
	private static final int ALGORITHM_WRR = 3;
	private static final int ALGORITHM_RANDOM = 4;
	private static final int ALGORITHM_PSO = 5;
	private static PSO_based_approach pso;

	public double fitness_PSO(){



		return .0;
	}

	@SuppressWarnings("Duplicates")
	public static void main(String[] args) {
		try {
			// int alg = ALGORITHM_HEFT;
			// int alg = ALGORITHM_GREEDY;
			// int alg = ALGORITHM_RR;
			//int alg = ALGORITHM_WRR;
//			int alg = ALGORITHM_RANDOM;
			int alg = ALGORITHM_PSO;
			for (int pp = 0; pp < 30; pp++) {
				double totalCost = 0;
				double totalTime = 0; // makespan
				int v = 0;

				File[] taskFiles = Utility.getTaskFileList(1); // testing set
				File[] vmFiles = Utility.getVMFileList();
				for (File tf : taskFiles) {//taking multiple task sets
					ArrayList<Task> ls_tasks = Utility.getTaskList(tf.getPath());
					ArrayList<VirtualMachine> ls_vms = Utility.getVMList(vmFiles[v].getPath());
					v++;
					if (v == vmFiles.length)
						v = 0;
					Queue<Task> queue = new LinkedList<Task>();
					queue = Utility.setTaskPriorityQueue(ls_tasks);
					int j = 0;

					for (int i = 0; i < ls_tasks.size(); i++) {
						if (!queue.isEmpty()) {
							Task t = queue.poll();
							ArrayList<Task> parentTasks = Utility.getParentTasksById(ls_tasks, t.getId());
							ArrayList<Object> udpatedVal = new ArrayList<Object>();
							switch (alg) {
								case ALGORITHM_HEFT:
									HEFT heft = new HEFT();
									udpatedVal = heft.taskMapping(parentTasks, ls_vms, t, 0); // 0: no use
									break;
								case ALGORITHM_GREEDY:
									Greedy gd = new Greedy();
									udpatedVal = gd.taskMapping(parentTasks, ls_vms, t, 0); // 0: no use
									break;
								case ALGORITHM_RR:
									RR rr = new RR();
									udpatedVal = rr.taskMapping(parentTasks, ls_vms, t, i); // i no use
									break;
								case ALGORITHM_WRR:
									WRR wrr = new WRR();
									udpatedVal = wrr.taskMapping(parentTasks, ls_vms, t, j); // i no use
									j++;
									break;
								case ALGORITHM_RANDOM:
									Random r = new Random();
									udpatedVal = r.taskMapping(parentTasks, ls_vms, t, 0); // i no use
									break;
							}
//							//
//							//****************************
//							// PSO used to give a solution of scheduling, this is different to the greedy algorithms which schedule
//							// each task and routing to VM, so here I put it outside of the for loop which is iterate through the task set
//							if(alg == 5){//PSO
////								ArrayList<Object> udpatedVal = new ArrayList<Object>();// Here we store the solution which will be used to calculate
//								PSO_based_approach pso = new PSO_based_approach(ls_tasks, ls_vms, j);
//								new Comparison().setPso(pso);
//								udpatedVal = pso.taskMapping(0);
//							}
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
					//
					//****************************
					// PSO used to give a solution of scheduling, this is different to the greedy algorithms which schedule
					// each task and routing to VM, so here I put it outside of the for loop which is iterate through the task set
					if(alg == 5){//PSO
						ArrayList<Object> udpatedVal = new ArrayList<Object>();
						PSO_based_approach pso = new PSO_based_approach(ls_tasks, ls_vms, j);
						new Comparison().setPso(pso);
						udpatedVal = pso.taskMapping(0);
						Task t = queue.poll();
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
				//***************PSO
				if(alg == 5 ){// PSO
					if(pso.best_solution) {//already the best solution
						System.out.println(alg + " - average total cost of testing set is: " + average_total);
						System.out.println(alg + " - average makespan of testing set is: " + average_makespan);
					}
					else{
						pso.setCurrent_fitness(average_makespan);
						pso.Main_Procedure(pso.POP,pso.ETC);
					}
				}
				else {
					System.out.println(alg + " - average total cost of testing set is: " + average_total);
					System.out.println(alg + " - average makespan of testing set is: " + average_makespan);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PSO_based_approach getPso() {
		return pso;
	}

	public void setPso(PSO_based_approach pso) {
		this.pso = pso;
	}
}