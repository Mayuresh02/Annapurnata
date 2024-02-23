package com.mayuresh.annapurnata.Adapter;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mayuresh.annapurnata.ModelClass.Donors;
import com.mayuresh.annapurnata.R;

import java.util.ArrayList;

public class DonorAdapter extends RecyclerView.Adapter<DonorAdapter.MyViewHolder>
{
    Context context;
    ArrayList<Donors> list;
    final int SEND_SMS_CODE = 1;
    final int CALL_PHONE_CODE = 1;

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
        holder.sContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.contactlayout);

                dialog.show();

                Button sms, call;
                sms=dialog.findViewById(R.id.sms);
                call=dialog.findViewById(R.id.call);

                if (checkPermission(Manifest.permission.SEND_SMS)) {

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.SEND_SMS}, 1);
                }

                sms.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkPermission(Manifest.permission.SEND_SMS)) {
                            SmsManager smsManager = SmsManager.getDefault();
                            ArrayList<String> message = smsManager.divideMessage("Dear "+donors.description+". Thank you for donating food! One of the users of our app wants to receive it. So, Kindly communicate with them ! Again Thank you for Donating food on Annapurnata.");
                            smsManager.sendMultipartTextMessage(donors.phone, null, message, null, null);
                            Toast.makeText(context, "Message Sent", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intcall = new Intent(Intent.ACTION_CALL);
                        intcall.setData(Uri.parse("tel:" + donors.phone));
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(context, "Please Grant Permission", Toast.LENGTH_SHORT).show();
                            requestingPermission();
                        } else {
                            context.startActivity(intcall);
                        }
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public boolean checkPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(context, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    public void requestingPermission() {
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 1);
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