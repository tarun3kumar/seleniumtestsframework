package com.seleniumtests.reporter;

public class ShortTestResult {

    private String name;
    private String id;

    private int totalMethod;

    private int instancesPassed;

    private int instancesFailed;

    private int instancesSkipped;

    public ShortTestResult(final String name) {
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

    public void setId(final String id) {
        this.id = id;
    }

    public void setInstancesFailed(final int instancesFailed) {
        this.instancesFailed = instancesFailed;
    }

    public void setInstancesPassed(final int instancesPassed) {
        this.instancesPassed = instancesPassed;
    }

    public void setInstancesSkipped(final int instancesSkipped) {
        this.instancesSkipped = instancesSkipped;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setTotalMethod(final int totalMethod) {
        this.totalMethod = totalMethod;
    }
}
