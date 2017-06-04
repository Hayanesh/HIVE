package com.hayanesh.hive.store;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hayanesh.hive.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link notes_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link notes_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class notes_fragment extends Fragment {

    public notes_fragment() {
        // Required empty public constructor
    }

    public static notes_fragment newInstance(String param1, String param2) {
        notes_fragment fragment = new notes_fragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes_fragment, container, false);
    }

}
