package ec.app.tutorial5;

import java.util.ArrayList;

import ec.app.tutorial4.Task;
import ec.app.tutorial4.VirtualMachine;

public class RR implements IAlgorithm {

	public RR() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public ArrayList<Object> taskMapping(ArrayList<Task> parentTasks, ArrayList<VirtualMachine> vms, Task t, int j) {
		ArrayList<Object> updatedVals = new ArrayList<Object>();

		int index = j % vms.size();

		VirtualMachine vmSel = vms.get(index);

		t.setAllocation_time(Utility.getMaxFinishTime(parentTasks));
		t.setExe_time(t.getTask_size() / (double) vmSel.getVelocity());

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
