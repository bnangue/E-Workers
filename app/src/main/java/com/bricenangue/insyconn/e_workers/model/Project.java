package com.bricenangue.insyconn.e_workers.model;

import java.util.ArrayList;

/**
 * Created by bricenangue on 28.08.17.
 */

public class Project {
    private String title, projectID;
    private String description;
    private int priority; //0 normal - 1 high
    private ArrayList<String> participantIDs, taskListIDs;
    private String creatorID;
    private Long start , deadline; //as timestamp

    public Project() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public ArrayList<String> getParticipantIDs() {
        return participantIDs;
    }

    public void setParticipantIDs(ArrayList<String> participantIDs) {
        this.participantIDs = participantIDs;
    }

    public ArrayList<String> getTaskListIDs() {
        return taskListIDs;
    }

    public void setTaskListIDs(ArrayList<String> taskListIDs) {
        this.taskListIDs = taskListIDs;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getDeadline() {
        return deadline;
    }

    public void setDeadline(Long deadline) {
        this.deadline = deadline;
    }

    public void addParticipantId(String participantID){
        if (participantIDs==null){
            participantIDs=new ArrayList<>();
            participantIDs.add(participantID);
        }
        participantIDs.add(participantID);
    }

    public void removeParticipantId(String participantID){
        if (participantIDs!=null){
            participantIDs.remove(participantID);
        }

    }
    public void addTaskId(String taskID){
        if (taskListIDs==null){
            taskListIDs=new ArrayList<>();
            taskListIDs.add(taskID);
        }
        taskListIDs.add(taskID);
    }

    public void removeTaskId(String taskID){
        if (taskListIDs!=null){
            taskListIDs.remove(taskID);
        }

    }
}
