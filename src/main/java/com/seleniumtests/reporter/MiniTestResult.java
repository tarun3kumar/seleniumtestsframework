package com.seleniumtests.reporter;

public class MiniTestResult {

	private String name;
	private String id;

	private int totalMethod;

	private int instancesPassed;

	private int instancesFailed;

	private int instancesSkipped;

	public MiniTestResult(String name) {
		this.name = name;
		this.id = name.toLowerCase().replaceAll(" ", "_");
	}

	public String getId() {
		return id;
	}

	public int getInstancesFailed() {
		return instancesFailed;
	}

	public int getInstancesPassed() {
		return instancesPassed;
	}

	public int getInstancesSkipped() {
		return instancesSkipped;

	}

	public String getName() {
		return name;
	}

	public int getTotalMethod() {
		return totalMethod;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setInstancesFailed(int instancesFailed) {
		this.instancesFailed = instancesFailed;
	}

	public void setInstancesPassed(int instancesPassed) {
		this.instancesPassed = instancesPassed;
	}

	public void setInstancesSkipped(int instancesSkipped) {
		this.instancesSkipped = instancesSkipped;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTotalMethod(int totalMethod) {
		this.totalMethod = totalMethod;
	}
}

