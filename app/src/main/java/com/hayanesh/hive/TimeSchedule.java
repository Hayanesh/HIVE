package com.hayanesh.hive;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.greasemonk.timetable.TimeTable;

import java.util.ArrayList;
import java.util.List;

public class TimeSchedule extends AppCompatActivity {
    private static final int GENERATED_AMOUNT = 20;
    private TimeTable timeTable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        timeTable = (TimeTable) findViewById(R.id.time_table);
        timeTable.setItems(generateSamplePlanData(this));
    }

    private static List<EmployeePlanItem> generateSamplePlanData(Context context)
    {
        List<EmployeePlanItem> planItems = new ArrayList<>();
        for(int i = 0; i < GENERATED_AMOUNT; i++)
            planItems.add(EmployeePlanItem.generateSample(context));

        return planItems;
    }

}
