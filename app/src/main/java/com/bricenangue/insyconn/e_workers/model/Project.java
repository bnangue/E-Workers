package com.bricenangue.insyconn.e_workers.model;

import java.util.ArrayList;

/**
 * Created by bricenangue on 28.08.17.
 */

public class Project {
    private String title, projectAndcreatorID;
    private String description;
    private int priority, numberParticipants, numberTasks; //0 normal /-/ 1 high
    private ArrayList<String> participantIDs, taskListIDs;
    private String projectStartAndEnd;
    public  int DEFAULT_PRIORITY=0;

    public Project() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    //concatenated String with the following ProjectID-creatorID

    public String getProjectAndcreatorID() {
        return projectAndcreatorID;
    }

    public void setProjectAndcreatorID(String projectAndcreatorID) {
        this.projectAndcreatorID = projectAndcreatorID;
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

    //as concatenate string of timestamps start and end
    //format start-end

    public String getProjectStartAndEnd() {
        return projectStartAndEnd;
    }

    public void setProjectStartAndEnd(String projectStartAndEnd) {
        this.projectStartAndEnd = projectStartAndEnd;
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

    public int getNumberParticipants() {
        return numberParticipants;
    }

    public void setNumberParticipants(int numberParticipants) {
        this.numberParticipants = numberParticipants;
    }

    public int getNumberTasks() {
        return numberTasks;
    }

    public void setNumberTasks(int numberTasks) {
        this.numberTasks = numberTasks;
    }
    public String getProjectID(String projectAndcreatorID){
        String projectID = null;
        if (projectAndcreatorID!=null && !projectAndcreatorID.isEmpty()){
            projectID = projectAndcreatorID.split("-")[0];
        }
        return projectID;
    }
    public String getCreatorID(String projectAndcreatorID){
        String CreatorID = null;
        if (projectAndcreatorID!=null && !projectAndcreatorID.isEmpty()){
            CreatorID = projectAndcreatorID.split("-")[1];
        }
        return CreatorID;
    }

    public Long getProjectStart(String projectStartAndEnd){
        long projectStart=0;
        if (projectStartAndEnd!=null && !projectStartAndEnd.isEmpty()){
            String projectstart =projectStartAndEnd.split("-")[0];
            projectStart=Long.valueOf(projectstart);
        }
       return projectStart;
    }

    public Long getProjectEnd(String projectStartAndEnd){
        long projectEnd = 0;
        if (projectStartAndEnd!=null && !projectStartAndEnd.isEmpty()){
            String projectend =projectStartAndEnd.split("-")[1];
            projectEnd= Long.valueOf(projectend);
        }
        return projectEnd;
    }


}
