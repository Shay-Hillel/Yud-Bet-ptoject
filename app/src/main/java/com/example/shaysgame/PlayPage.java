package com.example.shaysgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PlayPage extends ActionBarActivity implements View.OnClickListener {

    Button btn_online, btn_solo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_page);

        btn_online = (Button) findViewById(R.id.btn_online);
        btn_solo = (Button) findViewById(R.id.btn_solo);

        btn_online.setOnClickListener(this);
        btn_solo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btn_online){
            Intent intent = new Intent(this, CreateOrJoin.class);
            startActivity(intent);

        }else if(v == btn_solo){
            Intent intent = new Intent(this, soloPage.class);
            startActivity(intent);
        }
    }
}