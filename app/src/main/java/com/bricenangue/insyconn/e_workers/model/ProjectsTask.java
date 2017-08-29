package com.bricenangue.insyconn.e_workers.model;

import java.util.ArrayList;

/**
 * Created by bricenangue on 28.08.17.
 */

public class ProjectsTask {
    private String title, taskID;
    private String description;
    private ArrayList<String> participantIDs;
    private String creatorID;
    private Long start , deadline; //as timestamp


    public ProjectsTask() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getParticipantIDs() {
        return participantIDs;
    }

    public void setParticipantIDs(ArrayList<String> participantIDs) {
        this.participantIDs = participantIDs;
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

}
