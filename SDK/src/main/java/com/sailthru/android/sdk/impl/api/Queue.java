package com.sailthru.android.sdk.impl.api;

/**
 * Created by Vijay Penemetsa on 5/27/14.
 */
public class Queue {

    private NetworkQueue.TaskType taskType;

    public NetworkQueue.TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(NetworkQueue.TaskType taskType) {
        this.taskType = taskType;
    }
}
