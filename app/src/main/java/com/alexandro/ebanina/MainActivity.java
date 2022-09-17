package com.alexandro.ebanina;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button b1, b2, b3;
    TextView tHi;
    ConstraintLayout lay;
    //DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = findViewById(R.id.b1);
        b1.setOnClickListener(this);
        b2 = findViewById(R.id.b2);
        b2.setOnClickListener(this);
        b3 = findViewById(R.id.b3);
        b3.setOnClickListener(this);
        tHi = findViewById(R.id.tHi);
        lay = findViewById(R.id.mainMenu);

        Animation a1 = AnimationUtils.loadAnimation(this, R.anim.greeting);
        tHi.startAnimation(a1);
        a1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                tHi.setVisibility(View.GONE);
                b1.setVisibility(View.VISIBLE);
                b2.setVisibility(View.VISIBLE);
                b3.setVisibility(View.VISIBLE);
                Animation a2 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in);
                lay.startAnimation(a2);
            }
        });
        /*dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("mytable", null, null);*/
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.b1:
                intent = new Intent(this, act1.class);
                startActivity(intent);
                break;
            case R.id.b2:
                intent = new Intent(this, act2.class);
                startActivity(intent);
                break;
            case R.id.b3:
                intent = new Intent(this, act3.class);
                startActivity(intent);
                break;
        }
    }

    /*class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, "myDB", null, 1);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            // создаем таблицу с полями
            db.execSQL("create table mytable ("
                    + "id integer primary key autoincrement, "
                    + "name text, " + "amount double, " + "measure text);");
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }*/
}