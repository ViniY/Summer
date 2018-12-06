package ec.app.tutorial5;

import java.util.ArrayList;

import ec.app.tutorial4.Task;
import ec.app.tutorial4.VirtualMachine;

public interface IAlgorithm {
	//public ArrayList<Object> taskMapping(ArrayList<Task> ts, ArrayList<VirtualMachine> vms, Task t);

	public ArrayList<Object> taskMapping(ArrayList<Task> ts, ArrayList<VirtualMachine> vms, Task t, int j);
}
