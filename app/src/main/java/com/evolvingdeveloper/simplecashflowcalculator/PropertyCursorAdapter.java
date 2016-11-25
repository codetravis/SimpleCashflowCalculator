package com.evolvingdeveloper.simplecashflowcalculator;

import android.view.LayoutInflater;
import android.widget.Adapter;
import android.widget.CursorAdapter;
import android.database.Cursor;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import java.text.NumberFormat;

/**
 * Created by tcooper on 10/29/16.
 */

public class PropertyCursorAdapter extends CursorAdapter {
    Context context;

    public PropertyCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        this.context = context;

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.property_list_layout, parent, false);
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        TextView property_name = (TextView) view.findViewById(R.id.name_entry);
        TextView property_value = (TextView) view.findViewById(R.id.value_entry);

        DBHelper dbh = new DBHelper(context);
        String name = cursor.getString(cursor.getColumnIndexOrThrow(dbh.PROPERTY_COLUMN_NAME));
        String text_value = cursor.getString(cursor.getColumnIndexOrThrow(dbh.PROPERTY_COLUMN_PROPERTY_VALUE));
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(dbh.PROPERTY_COLUMN_ID));
        double money_value;
        try {
            money_value = Double.parseDouble(text_value);
        } catch (Exception except) {
            money_value = 0;
        }

        NumberFormat format = NumberFormat.getCurrencyInstance();
        property_name.setText(name);
        property_value.setText(format.format(money_value));

        Button delete_button = (Button) view.findViewById(R.id.delete_button);
        delete_button.setTag(id);
        delete_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = (long) v.getTag();
                DBHelper dbh = new DBHelper(v.getContext());
                dbh.deleteProperty(id);
                refreshCursor();
            }
        });
    }

    public void refreshCursor() {
        DBHelper dbh = new DBHelper(this.context);
        this.changeCursor(dbh.getAllProperties());
    }
}
