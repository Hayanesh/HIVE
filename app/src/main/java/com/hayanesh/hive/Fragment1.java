package com.hayanesh.hive;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.gregacucnik.EditableSeekBar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link Fragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment1 extends Fragment {
    View view;
    private ListView deptlist;
    private EditableSeekBar sem;
    private static FragmentManager fragmentManager;
    public Fragment1() {
        // Required empty public constructor
    }


    public static Fragment1 newInstance(String param1, String param2) {
        Fragment1 fragment = new Fragment1();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_fragment1, container, false);
        initViews();
        return view;
    }

    public void initViews()
    {
        fragmentManager = getActivity().getSupportFragmentManager();
        deptlist = (ListView)view.findViewById(R.id.depList);
        deptlist.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_single_choice,android.R.id.text1,
                getResources().getStringArray(R.array.dept)));
        sem = (EditableSeekBar)view.findViewById(R.id.seekbar1);
    }


}
