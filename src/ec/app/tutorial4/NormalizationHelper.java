package ec.app.tutorial4;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import ec.app.tutorial5.Greedy;
import ec.app.tutorial5.HEFT;
import ec.app.tutorial5.MaxCost;
import ec.app.tutorial5.MaxTime;
import ec.app.tutorial5.MinCost;
import ec.app.tutorial5.MinTime;
import ec.app.tutorial5.RR;
import ec.app.tutorial5.Random;
import ec.app.tutorial5.Utility;
import ec.app.tutorial5.WRR;

public class NormalizationHelper {

	public static final int RULE_MAXCOST = 1;
	public static final int RULE_MINCOST = 2;
	public static final int RULE_MAXMAKESPAN = 3;
	public static final int RULE_MINMAKESPAN = 4;

	public static HashMap<String, Double> helper(int alg, int set) { // from all test cases
		HashMap<String, Double> hmap = new HashMap<String, Double>();

		try {
			ArrayList<Object> udpatedVal = new ArrayList<Object>();
			double totalCost = 0;
			double totalTime = 0; // makespan
			int v = 0;

			File[] taskFiles = Utility.getTaskFileList(set);
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

						switch (alg) {
						case RULE_MAXCOST:
							MaxCost max_cost = new MaxCost();
							udpatedVal = max_cost.taskMapping(parentTasks, ls_vms, t, 0); // 0: no use
							break;
						case RULE_MINCOST:
							MinCost min_cost = new MinCost();
							udpatedVal = min_cost.taskMapping(parentTasks, ls_vms, t, 0); // 0: no use
							break;
						case RULE_MAXMAKESPAN:
							MaxTime max_time = new MaxTime();
							udpatedVal = max_time.taskMapping(parentTasks, ls_vms, t, 0); // 0 no use
							break;
						case RULE_MINMAKESPAN:
							HEFT heft = new HEFT();
							udpatedVal = heft.taskMapping(parentTasks, ls_vms, t, 0); // 0: no use
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

			switch (alg) {
			case RULE_MAXCOST:
				hmap.put("maxcost", average_total);
				break;
			case RULE_MINCOST:
				hmap.put("mincost", average_total);
				break;
			case RULE_MAXMAKESPAN:
				hmap.put("maxtime", average_makespan);
				break;
			case RULE_MINMAKESPAN:
				hmap.put("mintime", average_makespan);
				break;

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return hmap;
	}

	public static double getMaxCost(int set) { // from all test cases
		HashMap<String, Double> hmap = helper(RULE_MAXCOST, set);
		return hmap.get("maxcost");
	}

	public static double getMinCost(int set) { // from all test cases
		HashMap<String, Double> hmap = helper(RULE_MINCOST, set);
		return hmap.get("mincost");
	}

	public static double getMaxMakeSpan(int set) { // from all test cases
		HashMap<String, Double> hmap = helper(RULE_MAXMAKESPAN, set);
		return hmap.get("maxtime");
	}

	public static double getMinMakeSpan(int set) { // from all test cases
		HashMap<String, Double> hmap = helper(RULE_MINMAKESPAN, set);
		return hmap.get("mintime");
	}

}
