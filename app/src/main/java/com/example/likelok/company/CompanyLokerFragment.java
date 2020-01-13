package com.example.likelok.company;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.likelok.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompanyLokerFragment extends Fragment {

    private ImageButton ib_add;


    public CompanyLokerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_company_loker, container, false);

        ib_add = v.findViewById(R.id.ib_add);

        loadData();

        ib_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CreateLokerActivity.class));
            }
        });

        return v;
    }

    private void loadData() {

    }

}
