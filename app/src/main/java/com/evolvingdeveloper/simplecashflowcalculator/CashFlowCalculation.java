package com.evolvingdeveloper.simplecashflowcalculator;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import java.text.NumberFormat;
import java.util.ArrayList;
import android.graphics.Color;
import android.database.Cursor;
import android.view.MenuItem;

public class CashFlowCalculation extends AppCompatActivity {

    long mPropertyID;
    String mName;
    EditText mEstimatedRent;
    EditText mPropertyValue;
    EditText mMortgage;
    EditText mPropertyManagement;
    EditText mRepair;
    EditText mVacancy;
    EditText mInsurance;
    EditText mPropertyTax;
    TextView mCashFlow;
    EditText mPropertyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_flow_calculation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mName = "";
        mEstimatedRent = (EditText) findViewById(R.id.estimated_rent);
        mMortgage = (EditText) findViewById(R.id.mortgage);
        mVacancy = (EditText) findViewById(R.id.vacancy);
        mRepair = (EditText) findViewById(R.id.repair);
        mInsurance = (EditText) findViewById(R.id.insurance);
        mPropertyManagement = (EditText) findViewById(R.id.property_management);
        mPropertyTax = (EditText) findViewById(R.id.property_tax);
        mPropertyValue = (EditText) findViewById(R.id.property_value);
        mCashFlow = (TextView) findViewById(R.id.cash_flow);


        TextWatcher cash_flow_watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateAndSetCashFlow();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        ArrayList<View> form_inputs = findViewById(R.id.content_cash_flow_calculation).getFocusables(View.FOCUS_DOWN);
        for(View v : form_inputs) {
            ((EditText)  v).addTextChangedListener(cash_flow_watcher);
        }

        mPropertyID = getIntent().getLongExtra("_id", -1);

        if (mPropertyID == -1) {
            resetAllFields(findViewById(R.id.reset_button));
        } else {
            loadSavedProperty();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cash_flow_calculation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_main_menu:
                Intent new_activity = new Intent(CashFlowCalculation.this, MainMenu.class);
                CashFlowCalculation.this.startActivity(new_activity);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void calculateAndSetCashFlow() {
        double rent_income = getFieldValue(mEstimatedRent);
        double mortgage_cost = getFieldValue(mMortgage);
        double vacancy_cost = getFieldValue(mVacancy);
        double repair_cost = getFieldValue(mRepair);
        double insurance_cost = getFieldValue(mInsurance);
        double property_management_cost = getFieldValue(mPropertyManagement);
        double property_tax_cost = getFieldValue(mPropertyTax);

        double cash_flow = rent_income - (mortgage_cost + vacancy_cost + repair_cost +
                                        insurance_cost + property_management_cost +
                                        property_tax_cost);


        TextView cash_flow_view = (TextView) findViewById(R.id.cash_flow);

        NumberFormat format = NumberFormat.getCurrencyInstance();
        cash_flow_view.setText(format.format(cash_flow));
        String background_color = "#147a1b";
        if (cash_flow < 0) {
            background_color = "#d11c04";
        }

        cash_flow_view.setBackgroundColor(Color.parseColor(background_color));
    }

    public double getFieldValue(EditText field) {
        String input = field.getText().toString();
        if (input.length() == 0) {
            return 0;
        }
        else {
            try {
                return Double.parseDouble(input);
            } catch (Exception except) {
                return 0;
            }
        }
    }

    public void resetAllFields(View view) {
        mEstimatedRent.setText("0");
        mMortgage.setText("0");
        mVacancy.setText("0");
        mRepair.setText("0");
        mInsurance.setText("0");
        mPropertyManagement.setText("0");
        mPropertyTax.setText("0");
        mPropertyValue.setText("0");
    }

    public void savePropertyDialog(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Property Name");

        final EditText name_input = new EditText(this);
        name_input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(name_input);
        name_input.setText(mName);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPropertyName = name_input;
                saveProperty(mPropertyName);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               dialog.cancel();
           }
        });

        builder.show();
    }

    public void saveProperty(View view) {
        DBHelper db = new DBHelper(this);

        if (mPropertyID == -1) {
            db.insertProperty(mPropertyName.getText().toString(),
                    mPropertyValue.getText().toString(),
                    mEstimatedRent.getText().toString(),
                    mRepair.getText().toString(),
                    mVacancy.getText().toString(),
                    mMortgage.getText().toString(),
                    mInsurance.getText().toString(),
                    mPropertyTax.getText().toString(),
                    mPropertyManagement.getText().toString());
        } else {
            db.updateProperty(mPropertyID,
                    mPropertyName.getText().toString(),
                    mPropertyValue.getText().toString(),
                    mEstimatedRent.getText().toString(),
                    mRepair.getText().toString(),
                    mVacancy.getText().toString(),
                    mMortgage.getText().toString(),
                    mInsurance.getText().toString(),
                    mPropertyTax.getText().toString(),
                    mPropertyManagement.getText().toString());
        }
    }

    public void loadSavedProperty() {
        DBHelper db = new DBHelper(this);

        Cursor property = db.getProperty(mPropertyID);
        property.moveToFirst();

        mEstimatedRent.setText(property.getString(property.getColumnIndex(db.PROPERTY_COLUMN_ESTIMATED_RENT)));
        mMortgage.setText(property.getString(property.getColumnIndex(db.PROPERTY_COLUMN_MORTGAGE)));
        mVacancy.setText(property.getString(property.getColumnIndex(db.PROPERTY_COLUMN_VACANCY)));
        mRepair.setText(property.getString(property.getColumnIndex(db.PROPERTY_COLUMN_REPAIR)));
        mInsurance.setText(property.getString(property.getColumnIndex(db.PROPERTY_COLUMN_INSURANCE)));
        mPropertyManagement.setText(property.getString(property.getColumnIndex(db.PROPERTY_COLUMN_PROPERTY_MANAGEMENT)));
        mPropertyTax.setText(property.getString(property.getColumnIndex(db.PROPERTY_COLUMN_PROPERTY_TAX)));
        mPropertyValue.setText(property.getString(property.getColumnIndex(db.PROPERTY_COLUMN_PROPERTY_VALUE)));
        mName = property.getString(property.getColumnIndex(db.PROPERTY_COLUMN_NAME));
    }
}
