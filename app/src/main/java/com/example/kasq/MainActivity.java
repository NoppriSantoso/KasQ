package com.example.kasq;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.kasq.Model.Transaksi;
import com.example.kasq.Model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private transaksiAdapter transaksiAdapter;
    ArrayList<Transaksi> transaksiArrayList;
    private FloatingActionButton add;
    private TextView tvPemasukan,tvPengeluaran,tvSaldo;
    DBHelper DB;
    User user;
    private Button btnUpdate,btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DB = new DBHelper(getApplicationContext());
        transaksiArrayList = new ArrayList<>();

        tvPemasukan = findViewById(R.id.tvPemasukantext);
        tvPengeluaran = findViewById(R.id.tvPengeluarantext);
        tvSaldo = findViewById(R.id.tvSaldoAkhirtext);

        btnDelete = findViewById(R.id.btnDelete);
        btnUpdate = findViewById(R.id.btnUpdate);

        add = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recycle_view);

        user = (User) getIntent().getSerializableExtra("user");
        displayDataSaldo();
        displayData();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),addActivity.class);
                i.putExtra("user",user);
                startActivity(i);
                transaksiAdapter.notifyDataSetChanged();//melihat perubahan
            }
        });
    }


    public ArrayList<Transaksi> displayData(){
        Cursor cursor = DB.getData(user.getUserId());
        while(cursor.moveToNext()){
            String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
            String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
            String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
            String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
            String value = cursor.getString(cursor.getColumnIndexOrThrow("value"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            String userId = cursor.getString(cursor.getColumnIndexOrThrow("UserID"));
            user = DB.getUser(userId);
            Transaksi transaksi = new Transaksi(id,date,type,category,value,description,user);
            transaksiArrayList.add(transaksi);
        }
        cursor.close();
        transaksiAdapter = new transaksiAdapter(this,R.layout.item_transaksi,transaksiArrayList,this);
        recyclerView.setAdapter(transaksiAdapter);
        return transaksiArrayList;
    }

    //format currency
    Locale locale = new Locale("in","ID");
    NumberFormat formatCurrency = NumberFormat.getCurrencyInstance(locale);

    double pemasukan,pengeluaran;
    public void displayDataSaldo(){
        pemasukan = DB.getPemasukanData(user.getUserId());
        pengeluaran = DB.getPengeluaranData(user.getUserId());
        tvPemasukan.setText(""+formatCurrency.format((double) pemasukan));
        tvPengeluaran.setText(""+formatCurrency.format((double) pengeluaran));
        Double saldo = pemasukan-pengeluaran;
        tvSaldo.setText(formatCurrency.format((double) saldo));
    }

    //menu pada action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drop_down_menu,menu);
        return true;
    }


    //onClicklistener menu action bar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.btnAbout:
                Intent i = new Intent (getApplicationContext(),AboutActivity.class);
                i.putExtra("user",user);
                startActivity(i);
                break;
            case R.id.btnExit:
                user = null;
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    boolean doubleBacktoExit = false;

    @Override
    public void onBackPressed() {
        if(doubleBacktoExit){
            super.onBackPressed();
            return;
        }
        this.doubleBacktoExit = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBacktoExit=false;
            }
        }, 2000);
    }
}
