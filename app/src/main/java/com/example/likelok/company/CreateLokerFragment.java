package com.example.likelok.company;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.likelok.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateLokerFragment extends Fragment {


    public CreateLokerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_loker, container, false);
    }

}
