package com.example.kasq;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kasq.Model.Transaksi;
import com.example.kasq.Model.User;

import java.util.ArrayList;
import java.util.Calendar;

public class editActivity extends AppCompatActivity {
    private DatePickerDialog datePickerDialog;
    ArrayList<Transaksi> transaksiArrayList;
    Button date;
    private Button updateButton,cancelButton;
    Spinner Category;
    RadioGroup type;
    RadioButton radioButton,rbPemasukan,rbPengeluaran;
    EditText Value,Description;
    DBHelper DB;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);


        // getdata from adapter
        Intent i = getIntent();
        String t0 = i.getStringExtra("id").toString();
        String t1 = i.getStringExtra("date").toString();
        String t2 = i.getStringExtra("type").toString();
        String t3 = i.getStringExtra("category").toString();
        String t4 = i.getStringExtra("value").toString();
        String t5 = i.getStringExtra("description").toString();
        user = (User) i.getSerializableExtra("user");



        //inisiasi
        Category = findViewById(R.id.spinnerCategory);//kategori
        updateButton = findViewById(R.id.btnEdit);//tombol edit
        cancelButton = findViewById(R.id.btnCancel);//tombol cancel
        date = findViewById(R.id.btnDatePicker);//date
        Value = findViewById(R.id.jumlahValue);//jumlah
        type = findViewById(R.id.rgTipe);//Tipe
        Description =findViewById(R.id.DescriptionValue);//deskripsi
        rbPemasukan = findViewById(R.id.rdPemasukan);
        rbPengeluaran = findViewById(R.id.rdPengeluaran);

        DB = new DBHelper(editActivity.this);


        initDatePicker();

        date.setText(t1);
        if(t2.equals("Pemasukan")){
            ((RadioButton)type.getChildAt(0)).setChecked(true);
        }
        else if(t2.equals("Pengeluaran")){
            ((RadioButton)type.getChildAt(1)).setChecked(true);
        }
        if(t2.equals("Pemasukan")){
            ArrayAdapter<CharSequence> adapterPemasukan = ArrayAdapter.createFromResource(getApplicationContext(),R.array.arrayPemasukan, android.R.layout.simple_spinner_item);
            adapterPemasukan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Category.setAdapter(adapterPemasukan);
            Category.setSelection(getIndex(Category,t3));
        }
        else{
            ArrayAdapter<CharSequence> adapterPengeluaran = ArrayAdapter.createFromResource(getApplicationContext(),R.array.arrayPengeluaran, android.R.layout.simple_spinner_item);
            adapterPengeluaran.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Category.setAdapter(adapterPengeluaran);
            Category.setSelection(getIndex(Category,t3));
        }
        Value.setText(t4);
        Description.setText(t5);

        type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(radioGroup.getCheckedRadioButtonId() == -1){
                    Toast.makeText(getApplicationContext(), "Please select Type", Toast.LENGTH_SHORT).show();
                }else if(radioGroup.getCheckedRadioButtonId()==R.id.rdPemasukan){
                    ArrayAdapter<CharSequence> adapterPemasukan = ArrayAdapter.createFromResource(getApplicationContext(),R.array.arrayPemasukan, android.R.layout.simple_spinner_item);
                    adapterPemasukan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Category.setAdapter(adapterPemasukan);
                    Category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String choice = adapterView.getItemAtPosition(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }else if(radioGroup.getCheckedRadioButtonId()==R.id.rdPengeluaran){
                    ArrayAdapter<CharSequence> adapterPengeluaran = ArrayAdapter.createFromResource(getApplicationContext(),R.array.arrayPengeluaran, android.R.layout.simple_spinner_item);
                    adapterPengeluaran.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Category.setAdapter(adapterPengeluaran);
                    Category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String choice = adapterView.getItemAtPosition(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            }
        });

        //onClick Event
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dateTXT ="",tipeTXT="",categoryTXT="",valueTXT="",descriptionTXT="";
                int selectedID = type.getCheckedRadioButtonId();
                radioButton = findViewById(selectedID);
                SQLiteDatabase sqLiteDatabase;

                if(type.getCheckedRadioButtonId()==-1){
                    Toast.makeText(editActivity.this, "please insert type", Toast.LENGTH_SHORT).show();
                }else if(Value.getText().toString().equals("")){
                    Toast.makeText(editActivity.this, "please insert value", Toast.LENGTH_SHORT).show();
                }else if(Description.getText().toString().equals("")){
                    Toast.makeText(editActivity.this, "please insert description", Toast.LENGTH_SHORT).show();
                }
                else{
                    ContentValues contentValues = new ContentValues();
                    dateTXT = date.getText().toString();
                    tipeTXT = radioButton.getText().toString();
                    categoryTXT = Category.getSelectedItem().toString();
                    valueTXT = Value.getText().toString();
                    descriptionTXT = Description.getText().toString();
                    contentValues.put("date", dateTXT);
                    contentValues.put("type",tipeTXT);
                    contentValues.put("category",categoryTXT);
                    contentValues.put("value",valueTXT);
                    contentValues.put("description",descriptionTXT);
                    sqLiteDatabase = DB.getWritableDatabase();
                    long recedit=sqLiteDatabase.update("transaksiDetails",contentValues,"id=?",new String[]{t0});
                    if(recedit!=-1){
                        Toast.makeText(editActivity.this, "Selected Entry Updated", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(editActivity.this, "Selected Entry not Updated", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(editActivity.this,MainActivity.class);
                i.putExtra("user",user);
                startActivity(i);
                finish();
            }
        });
    }

    //untuk mendapatkan index dari category
    private int getIndex(Spinner type,String s){
        for(int i = 0;i<type.getCount();i++){
            if(type.getItemAtPosition(i).toString().equalsIgnoreCase(s)){
                return i;
            }
        }
            return 0;
    }

    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month,year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month+1;
                String dateString = makeDateString(day,month,year);
                date.setText(dateString);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this,style,dateSetListener,year,month,day);
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        return "JAN";
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        i.putExtra("user",user);
        startActivity(i);
        finish();
    }

}
