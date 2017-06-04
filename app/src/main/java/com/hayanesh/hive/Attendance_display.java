package com.hayanesh.hive;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fontometrics.Fontometrics;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.github.silvestrpredko.dotprogressbar.DotProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInLeftAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class Attendance_display extends AppCompatActivity {

    TextView att_date,title,total_stud,abs_stud;
    CustomViewPager viewPager;
    DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    Button ok,back;
    CardView dateCard;
    RecyclerView recyclerView;
    CoordinatorLayout footerLayout;
    DotProgressBar dotProgressBar;
    FloatingActionButton fab;
    FABProgressCircle fabProgressCircle;
    boolean isSelected = false;
    Animation fadeIn,fadeOut,slideUp,slideDown,slideRight,slideLeft,leftOut,rightOut,slideInDown,slideInleft,slideOut;
    StudentAdapter studentAdapter;
    List<Student> students;
    ActionMode actionMode;


    DatabaseHelper databaseHelper;

    private String URL = "http://192.168.1.3/hive/retriveDetails.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseHelper = new DatabaseHelper(getApplicationContext());


        title = (TextView)findViewById(R.id.textView);
        title.setTypeface(Fontometrics.safira_shine(this));
        att_date = (TextView)findViewById(R.id.date_card);
        dateCard = (CardView)findViewById(R.id.dateCardview);
        footerLayout = (CoordinatorLayout)findViewById(R.id.footer);
        fabProgressCircle = (FABProgressCircle)findViewById(R.id.fabProgressCircle);
        total_stud = (TextView)findViewById(R.id.total);
        abs_stud = (TextView)findViewById(R.id.abs);

        recyclerView = (RecyclerView)findViewById(R.id.stud_recyclerView);


        slideUp = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up);
        fadeOut = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);
        slideDown = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_down);
        fadeIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);

        slideRight = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.left_enter);
        slideLeft = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.right_enter);

        leftOut = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.left_out);
        rightOut = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.right_out);
        slideInDown = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_done);
        slideInleft = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_from_left);
        slideOut = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_done);
        title.startAnimation(slideDown);
        dateCard.setVisibility(View.VISIBLE);
        dateCard.startAnimation(slideDown);

        dateFormatter = new SimpleDateFormat("dd-MM-yy", Locale.US);

        Calendar c = Calendar.getInstance();
        att_date.setText(dateFormatter.format(c.getTime()));
        att_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                toDatePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        att_date.setText(dateFormatter.format(newDate.getTime()));
                        toDatePickerDialog.dismiss();
                    }

                },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                toDatePickerDialog.show();
            }
        });
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Snackbar.make((View)findViewById(R.id.dateCardview), "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                fabProgressCircle.show();
            }
        });

        loadData();
        recyclerView.startAnimation(slideInDown);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new SlideInLeftAnimator());
        total_stud.setText(String.valueOf(students.size()));
        abs_stud.setText(String.valueOf(databaseHelper.getAbsent()));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0) {
                    fab.hide();
                }
                else if (dy < 0)
                {

                    fab.show();
                }
            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_att, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.action_change)
        {
            // showOptionDialog();
            //show viewPager
            Intent toChange = new Intent(Attendance_display.this,Attendance.class);
            startActivity(toChange);
        }
        if(id == R.id.action_select)
        {
            databaseHelper.AllSelected();
            loadData();
            actionMode = this.startSupportActionMode(new Toolbar_ActionMode_Callback(this,studentAdapter,students,recyclerView, true));
            actionMode.setTitle(String.valueOf(students.size())+" selected");
        }

        return true;
    }
    public void showCheck()
    {
        isSelected = true ? isSelected == false : isSelected == true;
        loadData();
        studentAdapter.notifyDataSetChanged();
    }



    public void isChangeEnabled(boolean choice)
    {
        if(choice)
        {
            dotProgressBar.setVisibility(View.VISIBLE);

            viewPager.setVisibility(View.VISIBLE);
            back.setVisibility(View.VISIBLE);
            ok.setVisibility(View.VISIBLE);
            footerLayout.setVisibility(View.GONE);
            viewPager.setVisibility(View.VISIBLE);
            viewPager.startAnimation(slideDown);
            //Show the buttons
            back.startAnimation(slideLeft);
            ok.startAnimation(slideRight);
            //hide the date
            dateCard.setVisibility(View.INVISIBLE);
            dateCard.startAnimation(slideUp);
        }
        else
        {
            footerLayout.setVisibility(View.VISIBLE);
            dateCard.startAnimation(slideDown);
            dateCard.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.GONE);
            viewPager.startAnimation(slideUp);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.startAnimation(slideDown);
            dotProgressBar.setVisibility(View.GONE);
            ok.startAnimation(slideInDown);
            back.startAnimation(slideInDown);
            ok.setVisibility(View.GONE);
            back.setVisibility(View.GONE);
        }
    }


    public void loadData()
    {

        students = databaseHelper.getStudents();
        studentAdapter = new StudentAdapter(this,students);
        recyclerView.setAdapter(new SlideInLeftAnimationAdapter(studentAdapter));
        studentAdapter.notifyDataSetChanged();
    }



}