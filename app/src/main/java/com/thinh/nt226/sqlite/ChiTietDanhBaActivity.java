package com.thinh.nt226.sqlite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.thinh.nt226.sqlite.model.Contact;

import java.util.Locale;

public class ChiTietDanhBaActivity extends AppCompatActivity {
    TextView txtEdit, txtSave;
    EditText txtTen, txtSoDienThoai;
    ImageButton btnCall, btnSms;
    Intent intent;
    Contact contact;
    Button btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_danh_ba);

        addControls();
        addEvents();
    }

    private void addEvents() {
        txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtEdit.setVisibility(View.GONE);
                txtSave.setVisibility(View.VISIBLE);
                txtTen.setEnabled(true);
                txtSoDienThoai.setEnabled(true);
            }
        });
        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtEdit.setVisibility(View.VISIBLE);
                txtSave.setVisibility(View.GONE);

                String ma = contact.getMa().toString();
                String ten = txtTen.getText().toString();
                String phone = txtSoDienThoai.getText().toString();

                txtTen.setEnabled(false);
                txtSoDienThoai.setEnabled(false);

                intent.putExtra("MA", ma);
                intent.putExtra("EDIT_TEN", ten);
                intent.putExtra("EDIT_PHONE", phone);
                setResult(55, intent);
                finish();
            }
        });
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xyLyCall();
            }
        });
        btnSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xyLyNhanTin();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("MA", contact.getMa());
                setResult(44, intent);
                finish();
            }
        });
    }


    private void xyLyNhanTin() {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", txtSoDienThoai.getText().toString());
        smsIntent.putExtra("sms_body", "");
        startActivity(smsIntent);
    }

    private void xyLyCall() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri uri = Uri.parse("tel:" + txtSoDienThoai.getText().toString());
        intent.setData(uri);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);

    }

    private void addControls() {
        txtEdit = (TextView) findViewById(R.id.txtEdit);
        txtSave = (TextView) findViewById(R.id.txtSave);
        txtTen = (EditText) findViewById(R.id.txtTen);
        txtSoDienThoai = (EditText) findViewById(R.id.txtSoDienThoai);
        btnCall = (ImageButton) findViewById(R.id.btnCall);
        btnSms = (ImageButton) findViewById(R.id.btnSms);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        intent = getIntent();
        Bundle bundle = intent.getBundleExtra("CHI_TIET_DANH_BA");

        contact = (Contact) bundle.getSerializable("DANH_BA");

        txtTen.setText(contact.getTen());
        txtSoDienThoai.setText(contact.getPhone());
    }
}