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


public class Fragment2 extends Fragment {

    private View view;
    private FragmentManager fragmentManager;
    private ListView sub;
    private EditableSeekBar period;
    public Fragment2() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_fragment2, container, false);
        initViews();
        return view;
    }

    public void initViews()
    {
        fragmentManager = getActivity().getSupportFragmentManager();
        sub = (ListView)view.findViewById(R.id.subList);
        sub.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_single_choice,android.R.id.text1,
                getResources().getStringArray(R.array.sub)));
        period = (EditableSeekBar)view.findViewById(R.id.seekbar3);

    }


}
