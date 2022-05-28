package com.thinh.nt226.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.thinh.nt226.sqlite.adapter.ContactAdapter;
import com.thinh.nt226.sqlite.model.Contact;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ImageButton btnThemDanhBa;
    ListView lvDanhBa;
    ArrayList<Contact> dsDanhBa;
    ContactAdapter contactAdapter;

    String DATABASE_NAME = "dbContact.sqlite";
    private static final String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xuLySaoChepCSDLTuAssetsVaoHeThongMobile();

        addControls();
        addEvents();
    }

    private void addEvents() {
        btnThemDanhBa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void addControls() {
        btnThemDanhBa = (ImageButton) findViewById(R.id.btnThemDanhBa);
        lvDanhBa = (ListView) findViewById(R.id.lvDanhBa);
        dsDanhBa = new ArrayList<>();
        dsDanhBa.add(new Contact("1","Thinh","038"));
        contactAdapter = new ContactAdapter(MainActivity.this, android.R.layout.simple_list_item_1, dsDanhBa);
        lvDanhBa.setAdapter(contactAdapter);

        xuLyCSDL();
    }

    private void xuLyCSDL() {
        database = openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);

        Cursor cursor = database.query("Contact",null,null,null,null,null,null);
        dsDanhBa.clear();
        while (cursor.moveToNext()){
            int ma = cursor.getInt(0);
            String ten = cursor.getString(1);
            String phone = cursor.getString(2);
            dsDanhBa.add(new Contact(String.valueOf(ma),ten,phone));
        }
        cursor.close();
        contactAdapter.notifyDataSetChanged();
    }

    private void xuLySaoChepCSDLTuAssetsVaoHeThongMobile() {
        File dbFile = getDatabasePath(DATABASE_NAME);

        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAsset();
                Toast.makeText(this, "Copying sucess from Assets folder", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void CopyDataBaseFromAsset() {
        try {
            InputStream myInput = getAssets().open(DATABASE_NAME);

            // Path to the just created empty db
            String outFileName = layDuongDanLuuTru();

            // if the path doesn't exist first, create it
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists())
                f.mkdir();

            // Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);

            // transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            // Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();

        } catch (Exception ex) {
            Log.e("LOI_SAO_CHEP", ex.toString());
        }
    }

    private String layDuongDanLuuTru() {
        //tra ve data/data/APP_NAME/databases/FILENAME
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }
}