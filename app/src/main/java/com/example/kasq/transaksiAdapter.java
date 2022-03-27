package com.example.kasq;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kasq.Model.Transaksi;
import com.example.kasq.Model.User;

import java.util.ArrayList;

public class transaksiAdapter extends RecyclerView.Adapter<transaksiAdapter.transaksiViewHolder>{
    private Context context;
    ArrayList<Transaksi> transaksiArrayList;
    Activity activity;

    public transaksiAdapter(Context context, int item_transaksi,ArrayList<Transaksi> transaksiArrayList,Activity activity) {
        this.context = context;
        this.activity = activity;
        this.transaksiArrayList = transaksiArrayList;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public transaksiAdapter.transaksiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.item_transaksi,parent,false);
        return new transaksiViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull transaksiViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Transaksi transaksi  = transaksiArrayList.get(position);
        holder.setData(transaksi,position);
        holder.setListeners();
    }

    @Override
    public int getItemCount() {

        return transaksiArrayList.size();
    }

    public class transaksiViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        SQLiteDatabase DB;
        private TextView date_id,type_id,category_id,value_id,description_id;
        private Button btnUpdate,btnDelete;
        private int position;
        private Transaksi currentObject;
        private MainActivity main;
        public transaksiViewHolder(View v){
            super(v);
            date_id = v.findViewById(R.id.tvTanggal);
            type_id = v.findViewById(R.id.tvTipe);
            category_id = v.findViewById(R.id.tvKategori);
            value_id = v.findViewById(R.id.tvJumlah);
            description_id = v.findViewById(R.id.tvJudul);
            btnDelete = v.findViewById(R.id.btnDelete);
            btnUpdate = v.findViewById(R.id.btnUpdate);
        }

        public void setData(Transaksi currentObject, int position){
            this.date_id.setText(String.valueOf(currentObject.getDate()));
            this.type_id.setText(String.valueOf(currentObject.getType()));
            this.category_id.setText(String.valueOf(currentObject.getCategory()));
            this.value_id.setText(String.valueOf(currentObject.getValue()));
            this.description_id.setText(String.valueOf(currentObject.getDescription()));
            this.position = position;
            this.currentObject = currentObject;
        }
        public void setListeners(){
            btnUpdate.setOnClickListener(transaksiViewHolder.this);
            btnDelete.setOnClickListener(transaksiViewHolder.this);
        }

        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.btnUpdate:
                    Intent i = new Intent(view.getContext(),editActivity.class);
                    i.putExtra("id",transaksiArrayList.get(position).getId());
                    i.putExtra("date",transaksiArrayList.get(position).getDate());
                    i.putExtra("type",transaksiArrayList.get(position).getType());
                    i.putExtra("category",transaksiArrayList.get(position).getCategory());
                    i.putExtra("value",transaksiArrayList.get(position).getValue());
                    i.putExtra("description",transaksiArrayList.get(position).getDescription());
                    i.putExtra("user",transaksiArrayList.get(position).getUser());
                    view.getContext().startActivity(i);
                    break;

                case R.id.btnDelete:
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(context);
                    DBHelper dbHelper = new DBHelper(context);
                    Transaksi transaksi = transaksiArrayList.get(position);

                    builder.setTitle("Delete Data")
                            .setMessage("Do you want to Delete this transaction Data from database?").
                            setCancelable(true).
                            setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int a) {

                                    DB = dbHelper.getReadableDatabase();
                                    long delete = DB.delete("transaksiDetails","id=?",new String[]{transaksi.getId()});
                                    if (delete!=-1){
                                        Toast.makeText(context, "deleted data successfully", Toast.LENGTH_SHORT).show();
                                        transaksiArrayList.remove(position);
                                        notifyDataSetChanged();
                                        Intent i = new Intent(view.getContext(),MainActivity.class);
                                        i.putExtra("user",transaksi.getUser());
                                        view.getContext().startActivity(i);
                                        activity.overridePendingTransition(0,0);
                                    }
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int a) {
                            dialogInterface.cancel();
                        }
                    }).show();
                    break;
            }
        }



    }


}
