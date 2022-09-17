package com.alexandro.ebanina;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.view.View;
import android.view.View.OnClickListener;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import android.os.Bundle;

import java.util.ArrayList;

public class act1 extends AppCompatActivity implements OnClickListener {

    Button bAdd, bRest, bDel, bCancel, bOk;
    EditText etNameAdd, etAmountAdd, etMeasureAdd, etPriceBuy, etAmountBuy;
    TextView etAmount, etMeasure, etName, tPrice, tMeasure, tName;
    Spinner spin;
    ConstraintLayout layMain;
    LinearLayout layDialog;
    final String TAG = "myTag";
    DBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act1);

        bAdd = findViewById(R.id.bAdd);
        bAdd.setOnClickListener(this);
        bRest = findViewById(R.id.bRest);
        bRest.setOnClickListener(this);
        bDel = findViewById(R.id.bDel);
        bDel.setOnClickListener(this);
        bCancel = findViewById(R.id.bCancel);
        bCancel.setOnClickListener(this);
        bOk = findViewById(R.id.bOk);
        bOk.setOnClickListener(this);

        etNameAdd = findViewById(R.id.etNameAdd);
        etAmountAdd = findViewById(R.id.etAmountAdd);
        etMeasureAdd = findViewById(R.id.etMeasureAdd);
        tPrice = findViewById(R.id.tPrice);
        etPriceBuy = findViewById(R.id.etPriceBuy);
        etAmountBuy = findViewById(R.id.etAmountBuy);
        tMeasure = findViewById(R.id.tMeasure);
        tName = findViewById(R.id.tName);
        etName = findViewById(R.id.etName);
        etAmount = findViewById(R.id.etAmount);
        etMeasure = findViewById(R.id.etMeasure);

        layMain = findViewById(R.id.layMain);
        layDialog = findViewById(R.id.layDialog);
        spin = findViewById(R.id.spinner);
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        spin.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateView();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        addToSpin();
        spin.setSelection(0);
    }

    public void updateView() {
        Cursor c = db.query("mytable", null, "name = ?", new String[]{spin.getSelectedItem().toString()}, null, null, null);
        if (c.moveToFirst()) {
            int nID = c.getColumnIndex("name");
            int aID = c.getColumnIndex("amount");
            int mID = c.getColumnIndex("measure");
            int pID = c.getColumnIndex("price");
            etName.setText(c.getString(nID));
            etAmount.setText("" + c.getDouble(aID));
            etMeasure.setText(c.getString(mID));
            tPrice.setText("" + c.getDouble(pID));
        }
    }

    public int addToSpin() {
        ArrayList<String> arr = new ArrayList<>();
        Cursor c = db.query("mytable", null, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                int nID = c.getColumnIndex("name");
                arr.add(c.getString(nID));
            } while (c.moveToNext());
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arr);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin.setAdapter(adapter);
            return 1;
        }
        else {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[] {"no materials"});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin.setAdapter(adapter);
            return 0;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bAdd:
                ContentValues cv = new ContentValues();
                cv.put("name", etNameAdd.getText().toString());
                cv.put("amount", Double.parseDouble(etAmountAdd.getText().toString()));
                cv.put("measure", etMeasureAdd.getText().toString());
                cv.put("price", 0);
                db.insert("mytable", null, cv);
                addToSpin();
                etNameAdd.setText("");
                etAmountAdd.setText("");
                etMeasureAdd.setText("");
                break;

            case R.id.bRest:
                etPriceBuy.setText("");
                etAmountBuy.setText("");
                turnOn(false);
                Animation a1 = AnimationUtils.loadAnimation(this, R.anim.alpha_out);
                layMain.startAnimation(a1);
                a1.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}
                    @Override
                    public void onAnimationRepeat(Animation animation) {}

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        layDialog.setVisibility(View.VISIBLE);
                        tMeasure.setText(etMeasure.getText().toString() + " of");
                        tName.setText(spin.getSelectedItem().toString());
                        Animation a2 = AnimationUtils.loadAnimation(act1.this, R.anim.dialog_in);
                        layDialog.startAnimation(a2);
                    }
                });
                break;

            case R.id.bCancel:
                dialogOut();
                break;

            case R.id.bOk:
                Double price = Double.parseDouble(etPriceBuy.getText().toString());
                Double amount = Double.parseDouble(etAmountBuy.getText().toString());
                String name = spin.getSelectedItem().toString();
                Cursor c = db.query("mytable", null, "name = ?", new String[]{spin.getSelectedItem().toString()}, null, null, null);
                c.moveToFirst();
                int n = c.getColumnIndex("price");
                Double priceOld = c.getDouble(n);
                ContentValues rcv = new ContentValues();
                rcv.put("name", name);
                rcv.put("amount", amount + Double.parseDouble(etAmount.getText().toString()));
                rcv.put("measure", etMeasure.getText().toString());
                rcv.put("price", amount == priceOld ? 0 : price / amount);
                db.update("mytable", rcv, "name = ?", new String[] {name});
                updateView();
                dialogOut();
                break;

            case R.id.bDel:
                db.delete("mytable", "name = ?", new String[] {spin.getSelectedItem().toString()});
                etName.setText("");
                etAmount.setText("");
                etMeasure.setText("");
                tPrice.setText("");
                if (addToSpin() == 1) updateView();
                break;
        }
    }

    public void turnOn(Boolean b) {
        etNameAdd.setEnabled(b);
        etAmountAdd.setEnabled(b);
        etMeasureAdd.setEnabled(b);
        bRest.setClickable(b);
        bDel.setClickable(b);
    }

    public void dialogOut() {
        Animation a3 = AnimationUtils.loadAnimation(this, R.anim.dialog_out);
        layDialog.startAnimation(a3);
        a3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                layDialog.setVisibility(View.GONE);
                Animation a4 = AnimationUtils.loadAnimation(act1.this, R.anim.alpha_in);
                layMain.startAnimation(a4);
            }
        });
        layMain.setAlpha(1);
        turnOn(true);
    }

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, "myDB", null, 1);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(TAG, "--- onCreate database ---");
            // создаем таблицу с полями
            db.execSQL("create table mytable ("
                    + "id integer primary key autoincrement, "
                    + "name text, " + "amount double, " + "measure text, "
                    + "price double);");
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}