package com.example.likelok.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.likelok.R;
import com.example.likelok.models.Loker;

import java.util.ArrayList;

public class CompanyLokerAdapter extends RecyclerView.Adapter<CompanyLokerAdapter.CompanyLokerViewHolder> {

    private ArrayList<Loker> lokerList;
    private Context context;

    public CompanyLokerAdapter(ArrayList<Loker> lokerList, Context context) {
        this.lokerList = lokerList;
        this.context = context;
    }

    @NonNull
    @Override
    public CompanyLokerAdapter.CompanyLokerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_company_loker, parent, false);
        return new CompanyLokerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CompanyLokerAdapter.CompanyLokerViewHolder holder, int position) {
        final CompanyLokerViewHolder recyclerViewHolder = (CompanyLokerViewHolder) holder;

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.recycler_view_anim);
        recyclerViewHolder.view.startAnimation(animation);

        AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
        aa.setDuration(400);

        Log.d("YANGINI", lokerList.toString());
        if (!lokerList.get(position).getPic().equals("-"))
            Glide.with(context).load(Uri.parse(lokerList.get(position).getPic())).into(holder.iv_loker);

        holder.tv_nama_loker.setText(lokerList.get(position).getNama_loker());
        holder.tv_fee.setText("Rp " + lokerList.get(position).getGaji());
        holder.tv_jenis.setText(lokerList.get(position).getJenis());
        holder.tv_tanggal.setText(lokerList.get(position).getDeadline());

    }

    @Override
    public int getItemCount() {
        return lokerList.size();
    }

    public class CompanyLokerViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_nama_loker, tv_fee, tv_tanggal, tv_jenis;
        private ImageView iv_loker;
        private View view;

        public CompanyLokerViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            tv_nama_loker = itemView.findViewById(R.id.tv_nama_loker);
            tv_fee = itemView.findViewById(R.id.tv_fee);
            tv_tanggal = itemView.findViewById(R.id.tv_tanggal);
            tv_jenis = itemView.findViewById(R.id.tv_jenis);

            iv_loker = itemView.findViewById(R.id.iv_loker);

        }
    }

}
