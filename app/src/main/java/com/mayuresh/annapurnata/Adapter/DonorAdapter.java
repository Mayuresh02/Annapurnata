package com.mayuresh.annapurnata.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mayuresh.annapurnata.ModelClass.Donors;
import com.mayuresh.annapurnata.R;

import java.util.ArrayList;

public class DonorAdapter extends RecyclerView.Adapter<DonorAdapter.MyViewHolder>
{

    Context context;
    ArrayList<Donors> list;

    public DonorAdapter(Context context, ArrayList<Donors> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.donor_recycler_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonorAdapter.MyViewHolder holder, int position) {
        Donors donors=list.get(position);
        holder.name.setText(donors.getDescription());
        holder.aadhar.setText(donors.getAadhar());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView name, aadhar;
        ImageView sAccept, sContact, sLocation;
        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            name = itemView.findViewById(R.id.sName);
            aadhar = itemView.findViewById(R.id.sAadhar);

            sAccept = itemView.findViewById(R.id.sAccept);
            sContact = itemView.findViewById(R.id.sContact);
            sLocation = itemView.findViewById(R.id.sLocation);
        }
    }
}