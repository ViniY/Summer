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
	@SuppressWarnings("Duplicates")
	public static void main(String[] args) {
		try {
			// int alg = ALGORITHM_HEFT;
			// int alg = ALGORITHM_GREEDY;
			// int alg = ALGORITHM_RR;
			//int alg = ALGORITHM_WRR;
			int alg = ALGORITHM_RANDOM;

			for (int pp = 0; pp < 30; pp++) {
				double totalCost = 0;
				double totalTime = 0; // makespan
				int v = 0;

				File[] taskFiles = Utility.getTaskFileList(1); // testing set
				File[] vmFiles = Utility.getVMFileList();
				for (File tf : taskFiles) {
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

				System.out.println(alg + " - average total cost of testing set is: " + average_total);
				System.out.println(alg + " - average makespan of testing set is: " + average_makespan);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
