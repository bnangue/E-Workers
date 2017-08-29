package com.bricenangue.insyconn.e_workers.model;

import java.util.ArrayList;

/**
 * Created by bricenangue on 28.08.17.
 */

public class ProjectsTask {
    private String title, taskAndcreatorID;
    private String description;
    private ArrayList<String> participantIDs;
    private String taskStartAndEnd;//as timestamp
    private int numberParticipants;


    public ProjectsTask() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    //see project: format TaskID-CreatorID
    public String getTaskAndcreatorID() {
        return taskAndcreatorID;
    }

    public void setTaskAndcreatorID(String taskAndcreatorID) {
        this.taskAndcreatorID = taskAndcreatorID;
    }

    public String getTaskStartAndEnd() {
        return taskStartAndEnd;
    }

    public void setTaskStartAndEnd(String taskStartAndEnd) {
        this.taskStartAndEnd = taskStartAndEnd;
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


    public int getNumberParticipants() {
        return numberParticipants;
    }

    public void setNumberParticipants(int numberParticipants) {
        this.numberParticipants = numberParticipants;
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

    public String getTaskID(String taskAndcreatorID){
        String taskID = null;
        if (taskAndcreatorID!=null && !taskAndcreatorID.isEmpty()){
            taskID = taskAndcreatorID.split("-")[0];
        }
        return taskID;
    }
    public String getCreatorID(String taskAndcreatorID){
        String CreatorID = null;
        if (taskAndcreatorID!=null && !taskAndcreatorID.isEmpty()){
            CreatorID = taskAndcreatorID.split("-")[1];
        }
        return CreatorID;
    }

    public Long getTaskStart(String taskStartAndEnd){
        long taskStart=0;
        if (taskStartAndEnd!=null && !taskStartAndEnd.isEmpty()){
            String taskstart =taskStartAndEnd.split("-")[0];
            taskStart=Long.valueOf(taskstart);
        }
        return taskStart;
    }

    public Long getTaskEnd(String taskStartAndEnd){
        long taskEnd=0;
        if (taskStartAndEnd!=null && !taskStartAndEnd.isEmpty()){
            String taskend =taskStartAndEnd.split("-")[1];
            taskEnd=Long.valueOf(taskend);
        }
        return taskEnd;
    }

}
