package com.example.shaysgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WinOrLosePage extends ActionBarActivity implements View.OnClickListener {
    Button btn_returnToOnlinePage, btn_returnToStartPage;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_or_lose_page);

        myRef.child("All App Game Room").child("Game Room Number" + CreateOrJoin.playingInRoomNumber).setValue(null);

        Bundle bundle = getIntent().getExtras();
        boolean win = bundle.getBoolean("Win");

        TextView tv_winOrLose, tv_point;

        btn_returnToOnlinePage = (Button) findViewById(R.id.btn_returnToOnlinePage);
        btn_returnToStartPage = (Button) findViewById(R.id.btn_returnToStartPage);

        tv_winOrLose = (TextView) findViewById(R.id.tv_winOrLose);
        tv_point = (TextView) findViewById(R.id.tv_point);

        btn_returnToOnlinePage.setOnClickListener(this);
        btn_returnToStartPage.setOnClickListener(this);

        if(win) {
            tv_winOrLose.setText("You Lose!😁");
            tv_point.setText("You won 100 points");
        }else {
            tv_winOrLose.setText("You Lose!😢");
            tv_point.setText("You lost 50 points");
        }

    }

    @Override
    public void onClick(View v) {
        if(v == btn_returnToOnlinePage) {
            Intent intent = new Intent(this, CreateOrJoin.class);
            startActivity(intent);
        }else if(v == btn_returnToStartPage){
            Intent intent = new Intent(this, startPage.class);
            startActivity(intent);
        }
    }
}