package com.example.shaysgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class startPage extends ActionBarActivity implements View.OnClickListener {

    Button btn_play, btn_shop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        btn_play = (Button) findViewById(R.id.btn_play);
        btn_shop = (Button) findViewById(R.id.btn_tableRecord);

        btn_play.setOnClickListener(this);
        btn_shop.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if(view == btn_play) {
            Intent intent = new Intent(this, PlayPage.class);
            startActivity(intent);
        } else if(view == btn_shop){
            Intent intent = new Intent(this, Table_Record_Activity.class);
            startActivity(intent);
        }
    }
}