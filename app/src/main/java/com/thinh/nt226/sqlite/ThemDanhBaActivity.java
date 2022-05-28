package com.thinh.nt226.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ThemDanhBaActivity extends AppCompatActivity {
    TextView txtCancel, txtDone;
    EditText txtTen, txtHo, txtPhone;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_danh_ba);

        addControls();
        addEvents();
    }

    private void addEvents() {

        txtTen.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                txtDone.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    txtDone.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hoTen = txtHo.getText().toString() + " " + txtTen.getText().toString();
                String phone = txtPhone.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("HO_TEN", hoTen);
                bundle.putString("PHONE", phone);
                intent = new Intent();
                intent.putExtra("NEW_CONTACT",bundle);
                setResult(33, intent);
                finish();
            }
        });
    }

    private void addControls() {
        txtCancel = (TextView) findViewById(R.id.txtCancel);
        txtDone = (TextView) findViewById(R.id.txtDone);
        txtTen = (EditText) findViewById(R.id.txtTen);
        txtHo = (EditText) findViewById(R.id.txtHo);
        txtPhone = (EditText) findViewById(R.id.txtPhone);


    }
}