package com.example.crud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String DATABASE_NAME = "penjualann";

    TextView textViewViewPenjualans;
    EditText editTextTanggal, editTextPendapatan, editTextPengeluaran;
    Spinner spinnerSektorusaha;

    SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewViewPenjualans = (TextView) findViewById(R.id.textViewViewPenjualans);
        editTextTanggal = (EditText) findViewById(R.id.editTextTanggal);
        editTextPendapatan = (EditText) findViewById(R.id.editTextPendapatan);
        editTextPengeluaran = (EditText) findViewById(R.id.editTextPengeluaran);
        spinnerSektorusaha = (Spinner) findViewById(R.id.spinnerSektorusaha);

        findViewById(R.id.buttonAddPenjualan).setOnClickListener(this);
        textViewViewPenjualans.setOnClickListener(this);

        //creating a database
        mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        createPenjualanTable();

    }

    //this method will create the table
    //as we are going to call this method everytime we will launch the application
    //I have added IF NOT EXISTS to the SQL
    //so it will only create the table when the table is not already created


    //this method will validate the name and Pendapatan
    //dept does not need validation as it is a spinner and it cannot be empty
    private boolean inputsAreCorrect(String tanggal, String pendapatan, String pengeluaran) {
        if (tanggal.isEmpty()) {
            editTextTanggal.setError("Please enter a Tanggal");
            editTextTanggal.requestFocus();
            return false;
        }

        if (pendapatan.isEmpty() || Integer.parseInt(pendapatan) <= 0) {
            editTextPendapatan.setError("Please enter Pendapatan");
            editTextPendapatan.requestFocus();
            return false;
        }

        if (pengeluaran.isEmpty() || Integer.parseInt(pengeluaran) <= 0) {
            editTextPengeluaran.setError("Please enter pengeluaran");
            editTextPengeluaran.requestFocus();
            return false;
        }

        return true;
    }

    //In this method we will do the create operation
    private void addPenjualan() {
        String tanggal = editTextTanggal.getText().toString().trim();
        String pendapatan = editTextPendapatan.getText().toString().trim();
        String pengeluaran = editTextPengeluaran.getText().toString().trim();
        String sektor = spinnerSektorusaha.getSelectedItem().toString();

        //getting the current time for joining date
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String joiningDate = sdf.format(cal.getTime());

        //validating the inptus
        if (inputsAreCorrect(tanggal, pendapatan, pengeluaran)) {

            String insertSQL = "INSERT INTO Penjualans \n" +
                    "(tanggal, sektorusaha, joiningdate, pendapatan, pengeluaran)\n" +
                    "VALUES \n" +
                    "(?, ?, ?, ?, ?);";

            //using the same method execsql for inserting values
            //this time it has two parameters
            //first is the sql string and second is the parameters that is to be binded with the query
            mDatabase.execSQL(insertSQL, new String[]{tanggal, sektor, joiningDate, pendapatan, pengeluaran});

            Toast.makeText(this, "Penjualan Added Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonAddPenjualan:

                addPenjualan();

                break;
            case R.id.textViewViewPenjualans:

                startActivity(new Intent(this, PenjualanActivity.class));

                break;
        }
    }

    private void createPenjualanTable() {
        mDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS Penjualans (\n" +
                        "    id INTEGER NOT NULL CONSTRAINT Penjualans_pk PRIMARY KEY AUTOINCREMENT,\n" +
                        "    tanggal varchar(200) NOT NULL,\n" +
                        "    sektorusaha varchar(200) NOT NULL,\n" +
                        "    joiningdate datetime NOT NULL,\n" +
                        "    pendapatan double NOT NULL,\n" +
                        "    pengeluaran double NOT NULL\n" +
                        ");"
        );
    }

}
