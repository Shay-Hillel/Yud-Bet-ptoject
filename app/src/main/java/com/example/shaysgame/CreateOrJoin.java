package com.example.shaysgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CreateOrJoin extends ActionBarActivity{

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    static GameRoom gameRoom;
    static  int playingInRoomNumber = -1;
    Button createRoom_btn, enterFriendRoom_btn;
    int desiredRoomNum = 1;
    TextView wait_tv;
    static boolean i_am_game_creator = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_or_join);

        wait_tv = (TextView) findViewById(R.id.tv_wait);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        createRoom_btn = (Button) findViewById(R.id.createRoom_btn);

        enterFriendRoom_btn = (Button) findViewById(R.id.btn_enterFriendRoom);
        enterFriendRoom_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText inputEditTextField = new EditText(CreateOrJoin.this);
                inputEditTextField.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                AlertDialog dialog = new AlertDialog.Builder(CreateOrJoin.this)
                        .setTitle("Do You Want To Play With A Friend? ")
                        .setMessage("Enter The Number Of The Room! ")
                        .setView(inputEditTextField)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String editTextInput = inputEditTextField.getText().toString();
                                int gameCode = Integer.parseInt(editTextInput);


                                Query q = myRef.child("All App Game Room").orderByKey();
                                q.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        boolean gameExist = false;
                                        for (DataSnapshot dst : snapshot.getChildren()) {
                                            gameRoom = dst.getValue(GameRoom.class);
                                            if (gameRoom.getGameCode() == gameCode) {
                                                gameExist = true;
                                                if(gameRoom.getP2() != null){
                                                    Toast.makeText(CreateOrJoin.this, "The Room Is Already Full ", Toast.LENGTH_SHORT).show();
                                                }
                                                else {
                                                    gameRoom.setP2(MainActivity.nowPlayingInThisPhone);
                                                    CreateOrJoin.gameRoom = gameRoom;
                                                    playingInRoomNumber = gameRoom.getGameRoomNum();
                                                    myRef.child("All App Game Room").child("Game Room Number" + gameRoom.getGameRoomNum()).setValue(gameRoom);
                                                    Intent intent = new Intent(CreateOrJoin.this, Online_Game_Page.class);
                                                    startActivity(intent);
                                                }

                                            }
                                        }

                                        if(!gameExist) Toast.makeText(CreateOrJoin.this, "Wrong Game Number! ", Toast.LENGTH_SHORT).show();

                                   }

                                   @Override
                                   public void onCancelled(@NonNull DatabaseError error) {

                                   }
                               });
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
            }

        });
        createRoom_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query q = myRef.child("All App Game Room").orderByKey();
                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        gameRoom = new GameRoom(MainActivity.nowPlayingInThisPhone);
                        gameRoom.setGameCode();
                        playingInRoomNumber = gameRoom.getGameCode();
                        gameRoom.setGameRoomNum(playingInRoomNumber);
                        myRef.child("All App Game Room").child("Game Room Number" + playingInRoomNumber).setValue(gameRoom);


                        i_am_game_creator = true;
                        Toast.makeText(CreateOrJoin.this, "i am creator: " + i_am_game_creator, Toast.LENGTH_SHORT).show();
                        move_to_the_game();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }



    public void move_to_the_game() {
        Intent intent = new Intent(CreateOrJoin.this, Wait_P2.class);
        startActivity(intent);
    }

}