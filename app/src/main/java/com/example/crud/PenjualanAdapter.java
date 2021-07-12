package com.example.crud;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class PenjualanAdapter extends ArrayAdapter<Penjualan> {

    Context mCtx;
    int listLayoutRes;
    List<Penjualan> penjualanList;
    SQLiteDatabase mDatabase;

    public PenjualanAdapter(Context mCtx, int listLayoutRes, List<Penjualan> penjualanList, SQLiteDatabase mDatabase) {
        super(mCtx, listLayoutRes, penjualanList);

        this.mCtx = mCtx;
        this.listLayoutRes = listLayoutRes;
        this.penjualanList = penjualanList;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(listLayoutRes, null);

        final Penjualan penjualan = penjualanList.get(position);

        TextView textViewTanggal = view.findViewById(R.id.textViewTanggal);
        TextView textViewSektor = view.findViewById(R.id.textViewSektorusaha);
        TextView textViewPendapatan = view.findViewById(R.id.textViewPendapatan);
        TextView textViewPengeluaran = view.findViewById(R.id.textViewPengeluaran);
        TextView textViewHasil = view.findViewById(R.id.textViewHasil);
        TextView textViewJoiningDate = view.findViewById(R.id.textViewJoiningDate);

        textViewTanggal.setText(penjualan.getTanggal());
        textViewSektor.setText(penjualan.getSektor());
        textViewPendapatan.setText(String.valueOf(penjualan.getPendapatan()));
        textViewPengeluaran.setText(String.valueOf(penjualan.getPengeluaran()));
        textViewHasil.setText(String.valueOf(penjualan.getPendapatan()-penjualan.getPengeluaran()));
        textViewJoiningDate.setText(penjualan.getJoiningDate());

        Button buttonDelete = view.findViewById(R.id.buttonDeletePenjualan);
        Button buttonEdit = view.findViewById(R.id.buttonEditPenjualan);

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePenjualan(penjualan);
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String sql = "DELETE FROM penjualans WHERE id = ?";
                        mDatabase.execSQL(sql, new Integer[]{penjualan.getId()});
                        reloadPenjualansFromDatabase();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        return view;
    }

    private void  updatePenjualan(final  Penjualan penjualan) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.dialog_update_penjualan,null);
        builder.setView(view);

        final EditText editTextTanggal = view.findViewById(R.id.editTextTanggal);
        final EditText editTextPendapatan = view.findViewById(R.id.editTextPendapatan);
        final EditText editTextPengeluaran = view.findViewById(R.id.editTextPengeluaran);
        final Spinner spinnerSektorusaha = view.findViewById(R.id.spinnerSektorusaha);

        editTextTanggal.setText(penjualan.getTanggal());
        editTextPendapatan.setText(String.valueOf(penjualan.getPendapatan()));
        editTextPengeluaran.setText(String.valueOf(penjualan.getPengeluaran()));

        final AlertDialog dialog = builder.create();
        dialog.show();

        view.findViewById(R.id.buttonUpdatePenjualan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tanggal = editTextTanggal.getText().toString().trim();
                String pendapatan = editTextPendapatan.getText().toString().trim();
                String pengeluaran = editTextPengeluaran.getText().toString().trim();
                String sektor = spinnerSektorusaha.getSelectedItem().toString();

                if (tanggal.isEmpty()) {
                    editTextTanggal.setError("tanggal can't be blank");
                    editTextTanggal.requestFocus();
                    return;
                }

                if (pendapatan.isEmpty()) {
                    editTextPendapatan.setError("pendapatan can't be blank");
                    editTextPendapatan.requestFocus();
                    return;
                }

                if (pengeluaran.isEmpty()) {
                    editTextPendapatan.setError("Pengeluaran can't be blank");
                    editTextPendapatan.requestFocus();
                    return;
                }

                String sql = "UPDATE penjualans \n" +
                        "SET tanggal = ?, \n" +
                        "sektorusaha = ?, \n" +
                        "pendapatan = ?, \n" +
                        "pengeluaran = ? \n" +
                        "WHERE id = ?;\n";

                mDatabase.execSQL(sql, new String[]{tanggal, sektor, pendapatan, pengeluaran, String.valueOf(penjualan.getId())});
                Toast.makeText(mCtx, "Penjualan Updated", Toast.LENGTH_SHORT).show();
                reloadPenjualansFromDatabase();

                dialog.dismiss();
            }
        });
    }

    private void reloadPenjualansFromDatabase() {
        Cursor cursorPenjualans = mDatabase.rawQuery("SELECT * FROM penjualans", null);
        if (cursorPenjualans.moveToFirst()) {
            penjualanList.clear();
            do {
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
        cursorPenjualans.close();
        notifyDataSetChanged();
    }
}