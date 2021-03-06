package com.example.aplikasikasir;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
//main activity nya ini adalah tampilan awal tetapi ini javanya atau untuk fungsi nya

public class MainActivity extends AppCompatActivity {
    String[]  daftar;
    ListView ListView01;
    Menu menu;
    protected Cursor cursor;
    DataHelper dbcenter;
    public static MainActivity ma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn=(Button)findViewById(R.id.button2);
        Button btn2 =(Button)findViewById(R.id.buttonkeluar);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent inte = new Intent(MainActivity.this, BuatBarang.class);
                //method untuk memindahkan user ke halaman layout buat barang..
                startActivity(inte);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                moveTaskToBack(true);

                //membuat method tombol keluar dari aplikasi

            }
        });


        ma = this;
        dbcenter = new DataHelper(this);
        RefreshList();
    }

    public void RefreshList(){
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM barang",null);
        daftar = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int cc=0; cc < cursor.getCount(); cc++){
            cursor.moveToPosition(cc);
            daftar[cc] = cursor.getString(1).toString();
    }
    ListView01 = (ListView)findViewById(R.id.listView1);
    ListView01.setAdapter(new ArrayAdapter<>( this, android.R.layout.simple_list_item_1, daftar));
    ListView01.setSelected(true);
    ListView01.setOnItemClickListener(new OnItemClickListener() {


        public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
            final String selection = daftar[arg2]; //.getItemAtPosition(arg2.toString();
            final CharSequence[] dialogitem = {"Lihat Barang", "Update Barang", "Hapus Barang"};
            AlertDialog.Builder builder = new AlertDialog.Builder( MainActivity.this);
            builder.setTitle("Pilihan");
            builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    switch (item){
                        //nah disini adalah fungsinya ada lihat barang,update barang dan hapus data
                        case 0  :
                            Intent i = new Intent(getApplicationContext(), LihatBarang.class);
                            i.putExtra("nama", selection);
                            startActivity(i);
                            break;
                            //kemudian fungsi melihat barang ada disini
                        case 1  :
                            Intent in = new Intent(getApplicationContext(), UpdateBarang.class);
                            //jika popup update barang di klik maka program akan meng eksekusi barisan intent yang artinya
                            //user akan dipindahkan ke layout update barang
                            in.putExtra("nama", selection);
                            startActivity(in);
                            break;
                            //dan ini adalah fungsi update data
                        case 2  :
                            SQLiteDatabase db = dbcenter.getWritableDatabase();
                            db.execSQL("delete from barang where nama = '"+selection+ "'");
                            //query hapus delete (nama database)
                            RefreshList();
                            break;
                        //dan ini fungsi untuk delete data

                         }
                    }
                });
                builder.create().show();
            }});
        ((ArrayAdapter)ListView01.getAdapter()).notifyDataSetInvalidated();

    }

}