package com.hayanesh.hive;

import com.greasemonk.timetable.TimeRange;

import java.util.Date;

/**
 * Created by Hayanesh on 07-Jan-17.
 */

public class Schedule {
    private String employeeName, projectName;
    private TimeRange timeRange;

    public Schedule() {}

    public Schedule(String employeeName, String projectName, Date planStart, Date planEnd)
    {
        this.employeeName = employeeName;
        this.projectName = projectName;
        this.timeRange = new TimeRange(planStart, planEnd);
    }

    public TimeRange getTimeRange()
    {
        return timeRange;
    }


    public String getName()
    {
        return projectName;
    }


    public String getPersonName()
    {
        return employeeName;
    }
}
