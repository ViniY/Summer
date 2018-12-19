package ec.app.tutorial4;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.*;
// Generate JSon data
import java.util.*;
import ec.util.*;
import ec.*;
import ec.gp.*;
import ec.simple.*;
///
public class GenerateJSON {

	public GenerateJSON() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		try {
//			/ for now just testing the program to run propperly
//			File folder = new File("/home/yuyong1/Desktop/Summer_project/src/ec/app/tutorial4/config/run_test");
//			File[] listOfTestCases = Utility.getTestCaseFolders(folder);
//
//			for (File tc : listOfTestCases) {// test cases
//				File[] taskFiles = Utility.getTaskFiles(tc.getPath());
//				TaskList tlist = new TaskList();
//				TaskFile tfile = new TaskFile();
//				ArrayList<TaskFile> tflist = new ArrayList<TaskFile>();
//				for (File tf : taskFiles) { // task files
//					ArrayList<Task> tasks = Utility.getAllTasks(tf.getPath());
//
//					for (Task t : tasks) {
//						tfile = new TaskFile();
//						tfile.setId(t.getId());
//						tfile.setTask_size(t.getTask_size());
//						tfile.setParent_id(t.getParent_id());
//						tflist.add(tfile);
//					}
//				}
//				tlist.setTasks(tflist);
//
//				ObjectMapper mapper = new ObjectMapper();
//				 Object to JSON in file
//				mapper.writeValue(
//						new File("/Users/yalian/Desktop/testing/"
//								+ tc.getName() + ".json"),
///						tlist);
//				System.out.println("Completed!");
//			}
//
//		} catch (Exception ex) {
//			System.out.println(ex.getMessage());
//		}
//	}
	}

	class TaskList {
		public ArrayList<TaskFile> tasks;

		public ArrayList<TaskFile> getTasks() {
			return tasks;
		}

		public void setTasks(ArrayList<TaskFile> tasks) {
			this.tasks = tasks;
		}

	}

	class TaskFile {
		public String id;
		public long task_size;
		public ArrayList<String> parent_id;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public long getTask_size() {
			return task_size;
		}

		public void setTask_size(long task_size) {
			this.task_size = task_size;
		}

		public ArrayList<String> getParent_id() {
			return parent_id;
		}

		public void setParent_id(ArrayList<String> parent_id) {
			this.parent_id = parent_id;
		}
	}
}
