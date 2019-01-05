package ec.app.tutorial4;

import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Task implements Cloneable{
	public String id;
	public long task_size;
	public ArrayList<String> parent_id;
	public ArrayList<String> child_id;

	public double start_time;
	public double exe_time;
	public double waiting_time;
	public double relative_finish_time;
	public double allocation_time;

	public Task() {
	}
	public Task clone(){
		try{
			Task clonedObj = (Task) (super.clone());
			return clonedObj;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	public double getWaiting_time() {
		return waiting_time;
	}

	public void setWaiting_time() {
		this.waiting_time = this.getStart_time() - this.getAllocation_time();
	}

	public double getRelative_finish_time() {
		return relative_finish_time;
	}

	public void setRelative_finish_time() {
		this.relative_finish_time = this.getWaiting_time() + this.getExe_time();
	}

	public double getAllocation_time() {
		return allocation_time;
	}

	public void setAllocation_time(double allocation_time) {
		this.allocation_time = allocation_time;
	}

	public double finish_time;
	public int task_status;


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

	public double getStart_time() {
		return start_time;
	}

	public void setStart_time(double start_time) {
		this.start_time = start_time;
	}

	public double getExe_time() {
		return exe_time;
	}

	public void setExe_time(double exe_time) {
		this.exe_time = exe_time;
	}

	public double getFinish_time() {
		return finish_time;
	}

	public void setFinish_time() {
		this.finish_time = this.getStart_time() + this.getExe_time();
	}

	public int getTask_status() {
		return task_status;
	}

	public void setTask_status(int task_status) {
		this.task_status = task_status;
	}

	public ArrayList<String> getChild_id() {
		return child_id;
	}

	public void setChild_id(ArrayList<String> child_id) {
		this.child_id = child_id;
	}

	public String toString() {
		return "id = :" + id;
	}

	public boolean equals(Object object) {
		boolean same = false;
		if (object != null && object instanceof Task) {
			same = this.id == ((Task) object).id;
		}

		return same;
	}
}
