package com.evolvingdeveloper.simplecashflowcalculator;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.content.Intent;

public class SavedProperties extends ListActivity {

    PropertyCursorAdapter mAdapter;
    DBHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_properties);

        dbh = new DBHelper(this.getApplicationContext());
        //String[] from = new String[] {dbh.PROPERTY_COLUMN_NAME, dbh.PROPERTY_COLUMN_PROPERTY_VALUE};
        //int[] to = new int[] {R.id.name_entry, R.id.value_entry};
        Cursor cursor = dbh.getAllProperties();

        mAdapter = new PropertyCursorAdapter(this, cursor);

        ListView propertyList = getListView();

        propertyList.setAdapter(mAdapter);
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        Intent intent = new Intent(getApplicationContext(), CashFlowCalculation.class);
        intent.putExtra("_id", id);
        startActivity(intent);
    }
}
