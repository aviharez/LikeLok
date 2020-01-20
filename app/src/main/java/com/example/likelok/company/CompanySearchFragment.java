package com.example.likelok.company;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.likelok.R;
import com.example.likelok.adapter.CompanyLokerAdapter;
import com.example.likelok.models.Loker;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompanySearchFragment extends Fragment {

    private ProgressBar loading;
    private EditText et_search;
    private ImageButton bt_search;
    private ImageView iv_start;
    private TextView tv_total;
    private LinearLayout ll_no_data;
    private RecyclerView rv_loker;
    private CompanyLokerAdapter adapter;
    private ArrayList<Loker> lokerList;
    private DatabaseReference databaseReference;
    private String userId;


    public CompanySearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_company_search, container, false);
    }

}
