package com.thinh.nt226.sqlite;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
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
    TextView txtAddContactFromDevice;
    ListView lvDanhBa;
    ArrayList<Contact> dsDanhBa;
    ContactAdapter contactAdapter;

    private static final String DATABASE_NAME = "dbContact.sqlite";
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1011 && resultCode == 33) {

            Bundle bundle = new Bundle();
            bundle = data.getBundleExtra("NEW_CONTACT");
            String hoTen = bundle.getString("HO_TEN");
            String phone = bundle.getString("PHONE");
            xyLyThemDanhBa(hoTen, phone);
        }
        if (requestCode == 1002) {
            if (resultCode == 44) {
                String ma = data.getStringExtra("MA");
                database.delete("Contact", "Ma=?", new String[]{ma});
                hienThiDanhSachDanhBa();
            } else if (resultCode == 55) {
                String ma = data.getStringExtra("MA");
                String ten = data.getStringExtra("EDIT_TEN");
                String phone = data.getStringExtra("EDIT_PHONE");
                xyLySuaThongTin(ma, ten, phone);
            }

        }

    }

    private void xyLySuaThongTin(String ma, String ten, String phone) {
        ContentValues row = new ContentValues();

        row.put("Ten", ten);
        row.put("Phone", phone);
        database.update("Contact", row, "ma=?", new String[]{ma});
        hienThiDanhSachDanhBa();
    }

    private void xyLyThemDanhBa(String hoTen, String phone) {
        ContentValues row = new ContentValues();
        int ma = Integer.parseInt(dsDanhBa.get(dsDanhBa.size() - 1).getMa() + 1);

        row.put("Ma", ma);
        row.put("Ten", hoTen);
        row.put("Phone", phone);
        long r = database.insert("Contact", null, row);
        Toast.makeText(this, "Thêm danh bạ thành công", Toast.LENGTH_SHORT).show();
        hienThiDanhSachDanhBa();
    }

    private void addEvents() {
        btnThemDanhBa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, ThemDanhBaActivity.class), 1011);
            }
        });
        txtAddContactFromDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllContactFromDevice(dsDanhBa);
            }
        });

    }

    private void showAllContactFromDevice(ArrayList<Contact> dsDanhBa) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        } else {
            Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            //trả về cursor quản lý danh bạ của chúng ta
            while (cursor.moveToNext()) {
                String tenCotName = ContactsContract.Contacts.DISPLAY_NAME;
                String tenCotPhone = ContactsContract.CommonDataKinds.Phone.NUMBER;

                int viTriTenCotName = cursor.getColumnIndex(tenCotName);
                int viTriTenCotPhone = cursor.getColumnIndex(tenCotPhone);

                String name = cursor.getString(viTriTenCotName);
                String phone = cursor.getString(viTriTenCotPhone);

                xyLyThemDanhBa(name,phone);
            }
        }
    }

    private void addControls() {
        txtAddContactFromDevice = (TextView) findViewById(R.id.txtAddContactFromDevice);
        btnThemDanhBa = (ImageButton) findViewById(R.id.btnThemDanhBa);
        lvDanhBa = (ListView) findViewById(R.id.lvDanhBa);
        hienThiDanhSachDanhBa();
    }

    private void hienThiDanhSachDanhBa() {
        dsDanhBa = new ArrayList<>();
        contactAdapter = new ContactAdapter(MainActivity.this, android.R.layout.simple_list_item_1, dsDanhBa);
        lvDanhBa.setAdapter(contactAdapter);
        lvDanhBa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact contact = dsDanhBa.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("DANH_BA", contact);
                Intent intent = new Intent(MainActivity.this, ChiTietDanhBaActivity.class);
                intent.putExtra("CHI_TIET_DANH_BA", bundle);
                startActivityForResult(intent, 1002);
            }
        });

        xuLyCSDL();
    }

    private void xuLyCSDL() {
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

        Cursor cursor = database.query("Contact", null, null, null, null, null, null);
        dsDanhBa.clear();
        while (cursor.moveToNext()) {
            int ma = cursor.getInt(0);
            String ten = cursor.getString(1);
            String phone = cursor.getString(2);
            dsDanhBa.add(new Contact(String.valueOf(ma), ten, phone));
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