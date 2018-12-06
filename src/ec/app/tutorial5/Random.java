package ec.app.tutorial5;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import ec.app.tutorial4.Task;
import ec.app.tutorial4.VirtualMachine;

public class Random implements IAlgorithm {

	public Random() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public ArrayList<Object> taskMapping(ArrayList<Task> parentTasks, ArrayList<VirtualMachine> vms, Task t, int j) {
		ArrayList<Object> updatedVals = new ArrayList<Object>();

		int index = ThreadLocalRandom.current().nextInt(0, vms.size());

		VirtualMachine vmSel = vms.get(index);
		t.setAllocation_time(Utility.getMaxFinishTime(parentTasks));
		t.setExe_time((double) t.getTask_size() / vmSel.getVelocity());

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

}
