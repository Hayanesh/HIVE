package com.hayanesh.hive;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SONU on 22/03/16.
 */
public class Toolbar_ActionMode_Callback implements ActionMode.Callback {

    private Context context;
    private StudentAdapter recyclerView_adapter;
    private List<Student> message_models;
    private boolean isListViewFragment;
    DatabaseHelper databaseHelper;
    private RecyclerView recyclerView;
    List<Student> students;

    public Toolbar_ActionMode_Callback(Context context,StudentAdapter recyclerView_adapter, List<Student> message_models,RecyclerView recyclerView, boolean isListViewFragment) {
        this.context = context;
        this.recyclerView_adapter = recyclerView_adapter;
        this.message_models = message_models;
        this.isListViewFragment = isListViewFragment;
        this.recyclerView = recyclerView;
        databaseHelper = new DatabaseHelper(context.getApplicationContext());
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.menu_actionbar, menu);//Inflate the menu over action mode
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

        //Sometimes the meu will not be visible so for that we need to set their visibility manually in this method
        //So here show action menu according to SDK Levels
        if (Build.VERSION.SDK_INT < 11) {
            MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_present), MenuItemCompat.SHOW_AS_ACTION_NEVER);
            MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_absent), MenuItemCompat.SHOW_AS_ACTION_NEVER);
           // MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_forward), MenuItemCompat.SHOW_AS_ACTION_NEVER);
        } else {
            menu.findItem(R.id.action_present).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.findItem(R.id.action_absent).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            //menu.findItem(R.id.action_forward).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }

        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_present:
                databaseHelper.AllPresent();
                databaseHelper.AllDeselected();
                students = databaseHelper.getStudents();
                recyclerView_adapter = new StudentAdapter(this.context,students);
                recyclerView.setAdapter(recyclerView_adapter);
                recyclerView_adapter.notifyDataSetChanged();
                mode.finish();
                break;
            case R.id.action_absent:
                databaseHelper.AllAbsent();
                databaseHelper.AllDeselected();
                students = databaseHelper.getStudents();
                recyclerView_adapter = new StudentAdapter(this.context,students);
                recyclerView.setAdapter(recyclerView_adapter);
                recyclerView_adapter.notifyDataSetChanged();
                mode.finish();//Finish action mode
                break;

            default:

        }
        return false;
    }


    @Override
    public void onDestroyActionMode(ActionMode mode) {

        databaseHelper.AllDeselected();
        students = databaseHelper.getStudents();
        recyclerView_adapter = new StudentAdapter(this.context,students);
        recyclerView.setAdapter(recyclerView_adapter);
        recyclerView_adapter.notifyDataSetChanged();
        ((Attendance_display)context).abs_stud.setText(String.valueOf(databaseHelper.getAbsent()));
        mode.finish();//Finish action mode

    }
}