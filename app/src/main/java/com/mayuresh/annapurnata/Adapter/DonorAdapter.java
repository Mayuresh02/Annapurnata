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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mayuresh.annapurnata.ModelClass.Donors;
import com.mayuresh.annapurnata.R;

import java.util.ArrayList;

public class DonorAdapter extends RecyclerView.Adapter<DonorAdapter.MyViewHolder>
{
    Context context;
    ArrayList<Donors> list;
    String receiver;

    public DonorAdapter(Context context, ArrayList<Donors> list, String receiver) {
        this.context = context;
        this.list = list;
        this.receiver = receiver;
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

        holder.sAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();

                if (adapterPosition != RecyclerView.NO_POSITION)
                {
                    Donors donations = list.get(adapterPosition);

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Donors");
                    reference.child(donations.getAadhar()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            if (checkPermission(Manifest.permission.SEND_SMS)) {
                                SmsManager smsManager = SmsManager.getDefault();
                                ArrayList<String> message = smsManager.divideMessage("Dear "+donors.description+". I "+receiver+" sending this message to you because I saw your donation on Annapurnata:- Food Donation Application and I am interested in taking your donation so kindly help me through it. Thank you for your support!!!");
                                smsManager.sendMultipartTextMessage(donors.phone, null, message, null, null);
                                Toast.makeText(context, "Message Sent", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Sorry!!! Try again after some time.", Toast.LENGTH_LONG).show();
                        }
                    });

                    list.remove(adapterPosition);
                    notifyDataSetChanged();
                    notifyItemRemoved(adapterPosition);
                }
            }
        });

        //Contact
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
        //Contact End

        //Fetch Location
        holder.sLocation.setOnClickListener(v -> {
            String coordinates = donors.getMap();
            String[] parts=coordinates.substring(1,coordinates.length()-1).split(",");
            double lat = Double.parseDouble(parts[0]);
            double lng = Double.parseDouble(parts[1]);

            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lng);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(mapIntent);
            } else {
                // Handle case when Google Maps is not installed
                Toast.makeText(context, "Please Install Google Maps", Toast.LENGTH_LONG).show();
            }
        });
        //Fetch Location End

        //OpenInfo
        holder.openinfo.setOnClickListener(v -> {
            showInfo(donors);
        });
    }

    void showInfo(Donors donors)
    {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.openinfolayout);

        TextView oname = dialog.findViewById(R.id.oname);
        TextView oaadhar = dialog.findViewById(R.id.oaadhar);
        TextView oquantity = dialog.findViewById(R.id.oquantity);
        TextView ophone = dialog.findViewById(R.id.ophone);

        oname.setText(donors.getDescription());
        oaadhar.setText(donors.getAadhar());
        oquantity.setText(donors.getQuantity());
        ophone.setText(donors.getPhone());

        dialog.show();
    }

    void removeDonationFromDatabase(String aadhar)
    {

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
        ConstraintLayout openinfo;
        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            name = itemView.findViewById(R.id.sName);
            aadhar = itemView.findViewById(R.id.sAadhar);

            openinfo = itemView.findViewById(R.id.openinfo);

            sAccept = itemView.findViewById(R.id.sAccept);
            sContact = itemView.findViewById(R.id.sContact);
            sLocation = itemView.findViewById(R.id.sLocation);
        }
    }
}