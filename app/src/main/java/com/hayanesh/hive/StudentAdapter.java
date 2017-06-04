package com.hayanesh.hive;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.example.fontometrics.Fontometrics;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Hayanesh on 26-May-17.
 */

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder> {

    public Context mcontext;
    public List<Student> students;
    DatabaseHelper databaseHelper;
    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView sname,sid,sno;
        CheckBox status;
        FloatingActionButton fab;
        public MyViewHolder(View itemView) {
            super(itemView);
            sname = (TextView)itemView.findViewById(R.id.stud_name);
            sid = (TextView)itemView.findViewById(R.id.stud_id);
            sno = (TextView)itemView.findViewById(R.id.no);
            status = (CheckBox)itemView.findViewById(R.id.check_present);
            fab = (FloatingActionButton)itemView.findViewById(R.id.fab1);
        }
    }

    public StudentAdapter(Context context,List<Student>students) {
        this.mcontext = context;
        this.students = students;
        databaseHelper = new DatabaseHelper(context.getApplicationContext());
    }
    @Override
    public void onBindViewHolder(final StudentAdapter.MyViewHolder holder, int position) {
        final Student student = students.get(position);
        holder.sno.setText(String.valueOf(student.getNo()));
        holder.sname.setText(student.getName());
        holder.sid.setText(student.getId());
        holder.status.setChecked(student.getPresent());
        Log.d("TAG",""+student.getSelected());

        if(student.getSelected())
        {
            holder.sno.setVisibility(View.INVISIBLE);
            holder.fab.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.sno.setVisibility(View.VISIBLE);
            holder.fab.setVisibility(View.INVISIBLE);
        }
      /*  holder.status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                holder.status.setChecked(isChecked);
                student.setPresent(isChecked);
                databaseHelper.updateStudent(student);
                ((Attendance_display)mcontext).abs_stud.setText(String.valueOf(databaseHelper.getAbsent()));
            }
        });*/
      holder.status.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              CheckBox c = (CheckBox)v;
              student.setPresent(c.isChecked());
              databaseHelper.updateStudent(student);
              ((Attendance_display)mcontext).abs_stud.setText(String.valueOf(databaseHelper.getAbsent()));
          }
      });
    }

    @Override
    public StudentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list_element,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return students.size();
    }
}
