package com.example.crud;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class PenjualanActivity extends AppCompatActivity {

    List<Penjualan> penjualanList;
    SQLiteDatabase mDatabase;
    ListView listViewPenjualans;
    PenjualanAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penjualan);

        listViewPenjualans = (ListView) findViewById(R.id.listViewPenjualan);
        penjualanList = new ArrayList<>();

        //opening the database
        mDatabase = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);

        //this method will display the Penjualans in the list
        showPenjualansFromDatabase();
    }

    private void showPenjualansFromDatabase() {

        //we used rawQuery(sql, selectionargs) for fetching all the Penjualans
        Cursor cursorPenjualans = mDatabase.rawQuery("SELECT * FROM penjualans", null);

        //if the cursor has some data
        if (cursorPenjualans.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the Penjualan list
                penjualanList.add(new Penjualan(
                        cursorPenjualans.getInt(0),
                        cursorPenjualans.getString(1),
                        cursorPenjualans.getString(2),
                        cursorPenjualans.getString(3),
                        cursorPenjualans.getDouble(4),
                        cursorPenjualans.getDouble(5)
                ));
            } while (cursorPenjualans.moveToNext());
        }
        //closing the cursor
        cursorPenjualans.close();

        //creating the adapter object
        adapter = new PenjualanAdapter(this, R.layout.list_layout_penjualan, penjualanList, mDatabase);

        //adding the adapter to listview
        listViewPenjualans.setAdapter(adapter);
    }

}

