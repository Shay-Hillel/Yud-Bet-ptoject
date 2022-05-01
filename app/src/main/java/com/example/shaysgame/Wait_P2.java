package com.example.shaysgame;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Wait_P2 extends ActionBarActivity implements View.OnClickListener {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    Button btn_shareGameCode;

    TextView wait_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_p2);

        wait_tv = (TextView) findViewById(R.id.tv_wait);
        btn_shareGameCode = (Button) findViewById(R.id.btn_shareGameCode);

        btn_shareGameCode.setOnClickListener(this);

        wait_tv.setText("Wait For The Secend  Player! \nGame Code: " + CreateOrJoin.gameRoom.getGameCode());

        if(CreateOrJoin.i_am_game_creator) {
            if(CreateOrJoin.gameRoom.getP2() == null) {
                Query q_thisRoom = myRef.child("All App Game Room").child("Game Room Number" + CreateOrJoin.playingInRoomNumber).orderByKey();
                q_thisRoom.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        CreateOrJoin.gameRoom = snapshot.getValue(GameRoom.class);
                        if(CreateOrJoin.gameRoom.getP2() != null) {
                            Toast.makeText(Wait_P2.this, "p2 = " + CreateOrJoin.gameRoom.getP2().getName(), Toast.LENGTH_SHORT).show();
                            q_thisRoom.removeEventListener(this);
                            move_to_the_game();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }

    }

    public void move_to_the_game(){
        Intent intent = new Intent(Wait_P2.this, Online_Game_Page.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String subject = "Enter this code to play with me RPS:\n" + CreateOrJoin.gameRoom.getGameCode();
        intent.putExtra(Intent.EXTRA_TEXT, subject);
        startActivity(Intent.createChooser(intent, "Rock Paper Scissors Game"));
    }
}