package ec.app.tutorial4;

import java.util.ArrayList;

public class VirtualMachine {

	public VirtualMachine() {
		// TODO Auto-generated constructor stub
	}

	public String id;
	public String name;
	public double velocity;
	public double unit_cost_vm;
	public int vm_status;
	public double exe_time;
	public ArrayList<Task> priority_queue;
	public double fit_val;
	public double cost;
	public double relative_finish_time;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getVelocity() {
		return velocity;
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	public double getUnit_cost_vm() {
		return unit_cost_vm;
	}

	public void setUnit_cost_vm(double unit_cost_vm) {
		this.unit_cost_vm = unit_cost_vm;
	}

	public int getVm_status() {
		return vm_status;
	}

	public void setVm_status(int vm_status) {
		this.vm_status = vm_status;
	}

	public double getExecution_time() {
		return exe_time;
	}

	public void setExecution_time(double exe_time) {
		this.exe_time = exe_time;
	}

	public ArrayList<Task> getPriority_queue() {
		return priority_queue == null ? new ArrayList<Task>() : priority_queue;
	}

	public void setPriority_queue(Task t) {
		if (this.priority_queue == null)
			this.priority_queue = new ArrayList<Task>();
		this.priority_queue.add(t);
	}

	public double getFit_val() {
		return fit_val;
	}

	public void setFit_val(double fit_val) {
		this.fit_val = fit_val;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public double getRelativeFinish_time() {
		return relative_finish_time;
	}

	public void setRelativeFinish_time(double relative_finish_time) {
		this.relative_finish_time = relative_finish_time;
	}

	public VirtualMachine getVMById(ArrayList<VirtualMachine> ls_vms, String id) {
		VirtualMachine vm = new VirtualMachine();
		for (VirtualMachine v : ls_vms) {
			if (v.getId() == id) {
				vm = v;
			}
		}
		return vm;
	}

	public boolean equals(Object object) {
		boolean same = false;
		if (object != null && object instanceof VirtualMachine) {
			same = this.id == ((VirtualMachine) object).id;
		}

		return same;
	}
}
