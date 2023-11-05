package ecse429.todoManager.model;

import java.util.List;

public class Todo {
    private String title;
    private String description;
    private boolean doneStatus;
    private List<Id> taskof;
    private List<Id> category;
    private int id;

    public Todo() {
    }

    public Todo(String title, String description, boolean doneStatus, List<Id> taskof, List<Id> category, int id) {
        this.title = title;
        this.description = description;
        this.doneStatus = doneStatus;
        this.taskof = taskof;
        this.category = category;
        this.id = id;
    }

    public Todo(String title, String description, boolean doneStatus, List<Id> taskof, List<Id> category) {
        this.title = title;
        this.description = description;
        this.doneStatus = doneStatus;
        this.taskof = taskof;
        this.category = category;
    }

    public Todo(String title, String description, boolean doneStatus) {
        this.title = title;
        this.description = description;
        this.doneStatus = doneStatus;
    }

    public Todo(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Todo(String title) {
        this.title = title;
    }

}
