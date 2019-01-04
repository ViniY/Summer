package ec.app.tutorial5;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import ec.app.tutorial4.Task;
import ec.app.tutorial4.VirtualMachine;

public class Utility {

//	private static String trainingPath = "config/training"; // for deployment
//	private static String testingPath = "config/testing"; // for deployment
//	private static String vmPath = "config/vm"; // for deployment

	private static final int TRAINING_SET = 0;
	private static final int TESTING_SET = 1;

	private static String trainingPath = "/home/yuyong1/Desktop/Summer_project/src/ec/app/tutorial4/config/training";
	private static String testingPath = "/home/yuyong1/Desktop/Summer_project/src/ec/app/tutorial4/config/run_test";
	private static String vmPath = "/home/yuyong1/Desktop/Summer_project/src/ec/app/tutorial4/config/vm";

	public static ArrayList<Task> getTaskList(String filePath) { // task list in one file
		ArrayList<Task> ls_tasks = new ArrayList<Task>();

		try {
			JSONParser parser = new JSONParser();
			byte[] encoded = Files.readAllBytes(Paths.get(filePath));
			String content = new String(encoded, StandardCharsets.UTF_8);

			Object obj_tasks = parser.parse(content);

			JSONObject jsonObject = (JSONObject) obj_tasks;

			JSONArray tasks = (JSONArray) jsonObject.get("tasks");
			Iterator<Object> iterator_tasks = tasks.iterator();

			while (iterator_tasks.hasNext()) {
				Object it = iterator_tasks.next();
				JSONObject data = (JSONObject) it;
				Task t = new Task();
				t.setId((String) data.get("id"));
				t.setTask_size((long) data.get("task_size"));
				t.setParent_id((ArrayList<String>) data.get("parent_id"));

				ls_tasks.add(t);
			}

			// set child list
			ArrayList<String> childList = null;
			HashMap<String, ArrayList<String>> hmap = new HashMap<String, ArrayList<String>>();

			for (Task t : ls_tasks) {
				hmap.put(t.getId(), t.getParent_id());
			}

			for (Task t : ls_tasks) {
				childList = new ArrayList<String>();
				for (java.util.Map.Entry<String, ArrayList<String>> entry : hmap.entrySet()) {
					if (entry.getValue().contains(t.getId())) {
						childList.add(entry.getKey());
					}
				}
				t.setChild_id(childList);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ls_tasks;

	}

	public static File[] getTaskFileList(int set) {
		String path = set == TRAINING_SET ? trainingPath : testingPath;

		File fd = new File(path);
		return fd.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".json");
			}
		});
	}

	public static ArrayList<VirtualMachine> getVMList(String fvm) { // vm list in one file
		ArrayList<VirtualMachine> ls_vms = new ArrayList<VirtualMachine>();

		try {
			JSONParser parser = new JSONParser();
			byte[] encoded = Files.readAllBytes(Paths.get(fvm));
			String content = new String(encoded, StandardCharsets.UTF_8);

			Object obj_vm = parser.parse(content);
			JSONObject jsonObject = (JSONObject) obj_vm;

			JSONArray vms = (JSONArray) jsonObject.get("virtualmachine");
			Iterator<Object> iterator_vms = vms.iterator();

			while (iterator_vms.hasNext()) {
				Object it = iterator_vms.next();
				JSONObject data = (JSONObject) it;
				VirtualMachine vm = new VirtualMachine();
				vm.setId((String) data.get("id"));
				vm.setName((String) data.get("name"));
				vm.setVelocity((double) data.get("velocity"));
				vm.setUnit_cost_vm(Double.valueOf(data.get("unit_cost_vm").toString()));

				ls_vms.add(vm);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ls_vms;
	}

	public static File[] getVMFileList() {
		File fd = new File(vmPath);
		return fd.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".json");
			}
		});

	}

	public static Queue<Task> setTaskPriorityQueue(ArrayList<Task> ts) {
		// TODO Auto-generated method stub
		Queue<Task> queue = new LinkedList<Task>();
		ArrayList<Task> p = new ArrayList<Task>(); // parent
		ArrayList<Task> c = new ArrayList<Task>(); // child

		for (Task t : ts) { // get entry tasks
			if (t.getParent_id().size() == 0) {
				p.add(t);
			}
		}

		while (queue.size() < ts.size()) {
			c = new ArrayList<Task>();
			// Order tasks by the number of child
			orderTasksByNumberOfChild(p);

			for (Task t : p) {
				if (!queue.contains(t)) {
					queue.add(t); // add parent tasks into priority queue

				}
			}

			if (queue.size() != ts.size()) {
				HashSet<Task> hs = new HashSet<Task>();
				for (Task t : p) {
					ArrayList<Task> childTasks = getTasksByIds(t.getChild_id(), ts);
					hs.addAll(childTasks); // avoid duplicates
				}
				c.addAll(hs);

				if (c.size() == 0)
					p.clear();
				else
					p = c;
			}
		}

		return queue;
	}

	private static void orderTasksByNumberOfChild(ArrayList<Task> p) {
		p.sort((lp, rp) -> {
			return rp.getChild_id().size() - lp.getChild_id().size(); // sort by descending
		});

		p.sort((lp, rp) -> {
			return rp.getId().compareToIgnoreCase(lp.getId());
		});
	}

	private static ArrayList<Task> getTasksByIds(ArrayList<String> idList, ArrayList<Task> ts) {

		ArrayList<Task> tasks = new ArrayList<Task>();
		if (idList != null)
			for (Task t : ts) {
				if (idList.contains(t.getId()))
					tasks.add(t);
			}

		return tasks;
	}

	public static ArrayList<Task> getParentTasksById(ArrayList<Task> ls, String id) {
		ArrayList<Task> ptask = new ArrayList<Task>();
		// get parent id list of child id
		ArrayList<String> pid = new ArrayList<String>();
		for (Task t : ls) {
			if (t.getId().equals(id))
				pid.addAll(t.getParent_id());
		}

		for (int i = 0; i < pid.size(); i++) {
			for (Task t : ls) {
				if (pid.get(i).equals(t.getId()))
					ptask.add(t);
			}
		}
		return ptask;
	}

	public static double getTasksMaxSpan(ArrayList<Task> ts) { //
		return (double) (getMaxFinishTime(ts) - getMinStartTime(ts));
	}

	public static double getMaxFinishTime(ArrayList<Task> ts) { // max finish time of task list
		if (ts.size() > 0) {
			double[] ft = new double[ts.size()];
			for (int i = 0; i < ts.size(); i++) {
				ft[i] = ts.get(i).getFinish_time();
			}

			Arrays.sort(ft);
			return ft[ft.length - 1];
		} else
			return 0;
	}

	public static double getMinStartTime(ArrayList<Task> ts) { // minimum start time of task list
		if (ts.size() > 0) {
			double[] ft = new double[ts.size()];
			for (int i = 0; i < ts.size(); i++) {
				ft[i] = ts.get(i).getStart_time();
			}

			Arrays.sort(ft);
			return ft[0];
		} else
			return 0;
	}

	public static double getMaxStartTime(double a, double b) {
		return a >= b ? a : b;
	}

	public static Object DeepClone_Seializable(Object o){
		try{
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ObjectOutputStream outputStrm = new ObjectOutputStream(outputStream);
			outputStrm.writeObject(o);
			ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
			ObjectInputStream objectOutputStream = new ObjectInputStream(inputStream);
			return objectOutputStream.readObject();
		}
		catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	public static ArrayList<Task> TaskListCloning(ArrayList<Task> list){
		ArrayList<Task> cloned = new ArrayList<>();
		for(Task t : list){
			Task n = (Task) t.clone();
			cloned.add(n);
		}
		if(cloned.size() == list.size()) {
			System.out.println("list cloned");
			return cloned;
		}
		return null;//cloned wrong



	}


}
