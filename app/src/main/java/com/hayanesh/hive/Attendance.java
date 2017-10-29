package com.hayanesh.hive;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fontometrics.Fontometrics;
import com.facebook.stetho.Stetho;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.github.silvestrpredko.dotprogressbar.DotProgressBar;
import com.tuyenmonkey.mkloader.MKLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

import static android.R.attr.id;
import static android.R.attr.label;
import static android.R.attr.onClick;
import static android.R.attr.startYear;
import static android.R.attr.visibility;

public class Attendance extends AppCompatActivity{

    TextView att_date,title;
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


    RequestQueue requestQueue;
    StringRequest stringRequest;
    DatabaseHelper databaseHelper;

    private String URL = "http://192.168.1.3/hive/retriveDetails.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Stetho.initializeWithDefaults(this);
        requestQueue = Volley.newRequestQueue(this);
        databaseHelper = new DatabaseHelper(getApplicationContext());


        title = (TextView)findViewById(R.id.textView);
        title.setTypeface(Fontometrics.safira_shine(this));
        viewPager = (CustomViewPager)findViewById(R.id.viewPager1);
        ok = (Button)findViewById(R.id.ok_button);
        back = (Button)findViewById(R.id.back_button);
        dotProgressBar = (DotProgressBar)findViewById(R.id.dot_progress_bar);


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
        title.startAnimation(slideInDown);


        viewPager.setPagingEnabled(false);
        setupViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 1){
                    //last page make button text to GOT IT
                    ok.setText("OK");
                    back.setText("BACK");

                }else {
                    //still pages are left
                    ok.setText("NEXT");
                    back.setText("CANCEL");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //viewPager.setPagingEnabled(true);
                if(viewPager.getCurrentItem() == 1)
                {

                    new StudentAsync().execute();
                    //change the settings

                }
                else
                {

                    viewPager.setCurrentItem(1);
                    viewPager.startAnimation(slideLeft);

                }

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // viewPager.setPagingEnabled(false);
                if(viewPager.getCurrentItem() == 0)
                {

                    viewPager.setVisibility(View.INVISIBLE);
                    viewPager.startAnimation(slideUp);
                    back.startAnimation(fadeOut);
                    ok.startAnimation(fadeOut);
                }
                else
                {

                    viewPager.setCurrentItem(0);
                    viewPager.startAnimation(slideRight);
                }

            }
        });




    }

      public void showCheck()
    {
        isSelected = true ? isSelected == false : isSelected == true;
        studentAdapter.notifyDataSetChanged();
    }

    public void showOptionDialog()
    {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.fragment_fragment1);
        dialog.setTitle("Change details");
        Window window = dialog.getWindow();
        window.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        // set the custom dialog components - text, image and button

        dialog.show();
       // Button dialogButton = (Button) dialog.findViewById(R.id.cancel_button);
        // if button is clicked, close the custom dialog
        //dialogButton.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        dialog.dismiss();
        //    }
        //});


    }
    public void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new Fragment1(),"Select Dept");
        viewPagerAdapter.addFragment(new Fragment2(),"Select Sub");
        viewPager.setAdapter(viewPagerAdapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<android.support.v4.app.Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(android.support.v4.app.Fragment fragment, String title)
        {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

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
        }
        else
        {
            footerLayout.setVisibility(View.VISIBLE);
            dateCard.startAnimation(slideDown);
            dateCard.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.GONE);
            viewPager.startAnimation(slideUp);
            dotProgressBar.setVisibility(View.GONE);
            ok.startAnimation(slideInDown);
            back.startAnimation(slideInDown);
            ok.setVisibility(View.GONE);
            back.setVisibility(View.GONE);
        }
    }

    public void retrieveNames()
    {

        stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                List<Student> students = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.names().get(0).equals("success"))
                    {
                        JSONArray jsonArray = jsonObject.getJSONArray("success");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jo =  jsonArray.getJSONObject(i);
                            int no = jo.getInt("no");
                            String id = jo.getString("id");
                            String name = jo.getString("name");
                            Student s = new Student(no,name,id,false,false);
                            students.add(s);
                        }
                        databaseHelper.DeleteDetails();
                        databaseHelper.CreateTableStudent(students);
                    }

                }catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return super.getParams();
            }
        };

        requestQueue.add(stringRequest);

    }


    class StudentAsync extends AsyncTask<Void,Void,Void>
    {
        final ProgressDialog progressDialog = new ProgressDialog(Attendance.this);
        boolean isretrieved = false;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("retrieving");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            Intent toDisplay = new Intent(Attendance.this,Attendance_display.class);
            startActivity(toDisplay);
        }

        @Override
        protected Void doInBackground(Void... params) {

            stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    List<Student> students = new ArrayList<>();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if(jsonObject.names().get(0).equals("success"))
                        {
                            JSONArray jsonArray = jsonObject.getJSONArray("success");
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jo =  jsonArray.getJSONObject(i);
                                int no = jo.getInt("no");
                                String id = jo.getString("id");
                                String name = jo.getString("name");
                                Student s = new Student(no,name,id,false,false);
                                students.add(s);
                            }
                            databaseHelper.DeleteDetails();
                            databaseHelper.CreateTableStudent(students);
                        }

                    }catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return super.getParams();
                }
            };

            requestQueue.add(stringRequest);
            return null;
        }

    }

}
