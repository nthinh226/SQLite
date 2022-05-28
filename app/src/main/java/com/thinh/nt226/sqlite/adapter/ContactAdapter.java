package com.thinh.nt226.sqlite.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.thinh.nt226.sqlite.R;
import com.thinh.nt226.sqlite.model.Contact;

import java.util.List;

public class ContactAdapter extends ArrayAdapter<Contact> {
    Activity context;
    int resource;
    List<Contact> objects;

    public ContactAdapter(@NonNull Activity context, int resource, @NonNull List<Contact> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View row = inflater.inflate(R.layout.item_danhba,null);

        TextView txtTen, txtSoDienThoai;
        txtTen = row.findViewById(R.id.txtTen);
        txtSoDienThoai = row.findViewById(R.id.txtSoDienThoai);

        Contact contact = objects.get(position);
        txtTen.setText(contact.getTen());
        txtSoDienThoai.setText(contact.getPhone());

        return row;
    }
}
