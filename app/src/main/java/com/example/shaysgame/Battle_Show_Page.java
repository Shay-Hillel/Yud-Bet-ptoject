package com.example.shaysgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.VideoView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Battle_Show_Page extends ActionBarActivity {
    static VideoView vv_battleOnline;
    PlayerChoice player1Choice = PlayerChoice.NotSet, player2Choice = PlayerChoice.NotSet;
    int videoLocation;
    //int p1_points = CreateOrJoin.gameRoom.getP1().getPoints(), p2_points = CreateOrJoin.gameRoom.getP2().getPoints();
    static Player winnerPlayer;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle_show_page);


    }

    public int setVv_battleOnline(PlayerChoice player1Choice, PlayerChoice player2Choice) {
        switch (player1Choice) {
            case Rock:
                switch (player2Choice) {
                    case Rock:
                        return R.raw.rockrock;

                    case Paper:
                        return R.raw.rockpaper;

                    case Scissors:
                        return R.raw.rockscissors;

                }

            case Paper:
                switch (player2Choice) {
                    case Rock:
                        return R.raw.paperrock;

                    case Paper:
                        return R.raw.paperpaper;

                    case Scissors:
                        return R.raw.paperscissors;
                }

            case Scissors:
                switch (player2Choice) {
                    case Rock:
                        return R.raw.scissorsrock;


                    case Paper:
                        return R.raw.scissorspaper;


                    case Scissors:
                        return R.raw.scissorsscissors;
                }
        }

        return -1;

    }

    public Player getWinner(PlayerChoice player1Choice, PlayerChoice player2Choice) {
        switch (player1Choice) {
            case Rock:
                switch (player2Choice) {
                    case Rock:
                        return null;

                    case Paper:
                        return CreateOrJoin.gameRoom.getP2();

                    case Scissors:
                        return CreateOrJoin.gameRoom.getP1();

                }

            case Paper:
                switch (player2Choice) {
                    case Rock:
                        return CreateOrJoin.gameRoom.getP1();

                    case Paper:
                        return null;

                    case Scissors:
                        return CreateOrJoin.gameRoom.getP2();
                }

            case Scissors:
                switch (player2Choice) {
                    case Rock:
                        return CreateOrJoin.gameRoom.getP2();


                    case Paper:
                        return CreateOrJoin.gameRoom.getP1();


                    case Scissors:
                        return null;
                }
        }

        return null;

    }

    /*
     בדקתי אם הוא היוצר של המשחק בגלל שכל פעם זה מעולה נקודה לשחקן שצריך ומעדכן בפיירבייס
     ואני רוצה שרק אחד יעדכן כי אז זה מעדכן פעמים וזה נותן כל ניצחון שני ניצחונות
    */

    public void setPlayersChoice() {
        Query q_thisRoom = myRef.child("All App Game Room").child("Game Room Number" + CreateOrJoin.playingInRoomNumber);
        q_thisRoom.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                player1Choice = CreateOrJoin.gameRoom.getP1().getChoice();
                player2Choice = CreateOrJoin.gameRoom.getP2().getChoice();

                if(CreateOrJoin.i_am_game_creator){
                    setVv_battleOnline(player1Choice, player2Choice);
                    videoLocation = setVv_battleOnline(player1Choice, player2Choice);
                }
                else {
                    setVv_battleOnline(player2Choice, player1Choice);
                    videoLocation = setVv_battleOnline(player2Choice, player1Choice);
                }

                winnerPlayer = getWinner(player1Choice,player2Choice);

                if(winnerPlayer != null) {
                    if (winnerPlayer.equals(CreateOrJoin.gameRoom.getP1())) {
                        CreateOrJoin.gameRoom.addP1Point();
                    } else {
                        CreateOrJoin.gameRoom.addP2Point();
                    }
                }

                vv_battleOnline.setVideoPath("android.resource://" + getPackageName() + "/" + videoLocation);
                vv_battleOnline.requestFocus();
                q_thisRoom.removeEventListener(this);
                myRef.child("All App Game Room").child("Game Room Number" + CreateOrJoin.playingInRoomNumber).setValue(CreateOrJoin.gameRoom);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        player1Choice = PlayerChoice.NotSet;
        player2Choice = PlayerChoice.NotSet;

        vv_battleOnline = (VideoView) findViewById(R.id.vv_battleOnline);
        setPlayersChoice();

        vv_battleOnline.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if(!vv_battleOnline.isPlaying()) {
                    CreateOrJoin.gameRoom.getP1().setChoice(PlayerChoice.NotSet);
                    CreateOrJoin.gameRoom.getP2().setChoice(PlayerChoice.NotSet);
                    myRef.child("All App Game Room").child("Game Room Number" + CreateOrJoin.gameRoom.getGameRoomNum()).setValue(CreateOrJoin.gameRoom);

                    Online_Game_Page.round++;

                    Intent intent = new Intent(Battle_Show_Page.this, Online_Game_Page.class);
                    startActivity(intent);
                    vv_battleOnline = null;
                }
            }
        });
        vv_battleOnline.start();
    }
}