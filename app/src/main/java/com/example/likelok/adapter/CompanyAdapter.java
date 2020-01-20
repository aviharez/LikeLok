package com.example.likelok.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.likelok.R;
import com.example.likelok.models.Company;

import java.util.ArrayList;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder> {

    private ArrayList<Company> companyList;
    private Context context;

    public CompanyAdapter(ArrayList<Company> companyList, Context context) {
        this.companyList = companyList;
        this.context = context;
    }

    @NonNull
    @Override
    public CompanyAdapter.CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_company, parent, false);
        return new CompanyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CompanyAdapter.CompanyViewHolder holder, int position) {
        if (!companyList.get(position).getPic().equals("-")) {
            Glide.with(context).load(companyList.get(position).getPic()).into(holder.iv_pic);
        }

        holder.tv_company_name.setText(companyList.get(position).getName());
        holder.tv_alamat.setText(companyList.get(position).getAlamat());
        holder.tv_email.setText(companyList.get(position).getEmail());
        holder.tv_pegawai.setText(companyList.get(position).getPegawai());
        holder.tv_industri.setText(companyList.get(position).getIndustri());
    }

    @Override
    public int getItemCount() {
        return companyList.size();
    }

    public class CompanyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_company_name, tv_alamat, tv_email, tv_pegawai, tv_industri;
        private ImageView iv_pic;

        public CompanyViewHolder(View itemView) {
            super(itemView);

            tv_company_name = itemView.findViewById(R.id.tv_company_name);
            tv_alamat = itemView.findViewById(R.id.tv_alamat);
            tv_email = itemView.findViewById(R.id.tv_email);
            tv_pegawai = itemView.findViewById(R.id.tv_pegawai);
            tv_industri = itemView.findViewById(R.id.tv_industri);
            iv_pic = itemView.findViewById(R.id.iv_pic);

        }
    }

}
