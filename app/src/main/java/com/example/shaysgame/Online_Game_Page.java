package com.example.shaysgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Online_Game_Page extends ActionBarActivity implements View.OnClickListener {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    ImageButton ibtn_rock, ibtn_paper, ibtn_scissors;
    TextView tv_result;

    LinearLayout rock_paper_scissors_layout;
    TextView tv_wait_for_rival_choice ,tv_victories_table;

    PlayerChoice player1Choice = PlayerChoice.NotSet, player2Choice = PlayerChoice.NotSet;
    static int round = 1;

    boolean theGameEnded = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_game_page);

        Intent intent;
        GameRoom gameRoom = CreateOrJoin.gameRoom;

        if(gameRoom.getP1_points() >= 3) {
            theGameEnded = true;
            gameRoom.getP1().addScore(100);
            gameRoom.getP2().removePoints(50);

            if(MainActivity.nowPlayingInThisPhone.getId() == gameRoom.getP1().getId()) {
                intent = new Intent(Online_Game_Page.this, WinOrLosePage.class);
                intent.putExtra("Win", true);
            }
            else {
                intent = new Intent(Online_Game_Page.this, WinOrLosePage.class);
                intent.putExtra("Win", false);
            }

            updateFaireBase(gameRoom);
            startActivity(intent);
        }
        else if(gameRoom.getP2_points() >= 3) {
            theGameEnded = true;
            gameRoom.getP2().addScore(100);
            gameRoom.getP1().removePoints(50);

            if(MainActivity.nowPlayingInThisPhone.getId() == gameRoom.getP2().getId()) {
                intent = new Intent(Online_Game_Page.this, WinOrLosePage.class);
                intent.putExtra("Win", true);
            }
            else {
                intent = new Intent(Online_Game_Page.this, WinOrLosePage.class);
                intent.putExtra("Win", false);
            }

            updateFaireBase(gameRoom);

            startActivity(intent);
        }

        myRef.child("My App Users:").child(gameRoom.getP1().getName()).setValue(gameRoom.getP1());
        myRef.child("My App Users:").child(gameRoom.getP2().getName()).setValue(gameRoom.getP2());

    }

    @Override
    public void onClick(View view) {
        if (CreateOrJoin.i_am_game_creator) {
            if (ibtn_rock == view) {
                player1Choice = PlayerChoice.Rock;

            } else if (ibtn_paper == view) {
                player1Choice = PlayerChoice.Paper;

            } else if (ibtn_scissors == view) {
                player1Choice = PlayerChoice.Scissors;

            }

        }else {
            if (ibtn_rock == view) {
                player2Choice = PlayerChoice.Rock;

            } else if (ibtn_paper == view) {
                player2Choice = PlayerChoice.Paper;

            } else if (ibtn_scissors == view) {
                player2Choice = PlayerChoice.Scissors;
            }
        }

        setChoice();
    }



    public void setChoice() {
        Query q_thisRoom = myRef.child("All App Game Room").child("Game Room Number" + CreateOrJoin.playingInRoomNumber).orderByValue();
        q_thisRoom.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GameRoom gameRoom = snapshot.getValue(GameRoom.class);


                if (CreateOrJoin.i_am_game_creator && player1Choice != PlayerChoice.NotSet) {
                    gameRoom.getP1().setChoice(player1Choice);
                    rock_paper_scissors_layout.setVisibility(View.GONE);
                    tv_wait_for_rival_choice.setVisibility(View.VISIBLE);
                    myRef.child("All App Game Room").child("Game Room Number" + gameRoom.getGameRoomNum()).setValue(gameRoom);
                }
                else if (!CreateOrJoin.i_am_game_creator && player2Choice != PlayerChoice.NotSet) {
                    gameRoom.getP2().setChoice(player2Choice);
                    rock_paper_scissors_layout.setVisibility(View.GONE);
                    tv_wait_for_rival_choice.setVisibility(View.VISIBLE);
                    myRef.child("All App Game Room").child("Game Room Number" + gameRoom.getGameRoomNum()).setValue(gameRoom);
                }
                CreateOrJoin.gameRoom = gameRoom;

                if (gameRoom.getP1().getChoice() != PlayerChoice.NotSet && gameRoom.getP2().getChoice() != PlayerChoice.NotSet){
                    moveToBattleShowPage();
                    q_thisRoom.removeEventListener(this);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void moveToBattleShowPage() {
        CreateOrJoin.gameRoom.setNextRound();

        Intent intent = new Intent(Online_Game_Page.this, Battle_Show_Page.class);
        startActivity(intent);
    }

    public Player getGameWinner() {
        Player p1 = CreateOrJoin.gameRoom.getP1(), p2 = CreateOrJoin.gameRoom.getP2();
        int p1_points = CreateOrJoin.gameRoom.getP1_points(), p2_points = CreateOrJoin.gameRoom.getP2_points();
        if(p1_points > p2_points) {
            return p1;
        } else if(p1_points < p2_points) {
            return p2;
        }
        return null;
    }

    public void updateFaireBase(GameRoom gameRoom) {
        Query q_thisRoom = myRef.child("All App Game Room").child("Game Room Number" + CreateOrJoin.playingInRoomNumber).orderByValue();
        q_thisRoom.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myRef.child("All App Game Room").child("Game Room Number" + CreateOrJoin.playingInRoomNumber).setValue(gameRoom);
                if(theGameEnded) {
                    q_thisRoom.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void deleteFromFireBase(GameRoom gameRoom) {
        myRef.child("All App Game Room").child("Game Room Number" + gameRoom.getGameRoomNum()).setValue(null);

    }

    @Override
    protected void onStart() {
        super.onStart();
        rock_paper_scissors_layout = (LinearLayout) findViewById(R.id.rock_paper_scissors_layout);
        tv_wait_for_rival_choice = (TextView) findViewById(R.id.tv_wait_for_rival_choice);
        ibtn_rock = (ImageButton) findViewById(R.id.ibtn_rock);
        ibtn_paper = (ImageButton) findViewById(R.id.ibtn_paper);
        ibtn_scissors = (ImageButton) findViewById(R.id.ibtn_scissors);
        tv_result = (TextView) findViewById(R.id.tv_result);
        tv_victories_table = (TextView) findViewById(R.id.tv_victories_table);

        tv_wait_for_rival_choice.setVisibility(View.GONE);
        rock_paper_scissors_layout.setVisibility(View.VISIBLE);

        if(MainActivity.nowPlayingInThisPhone.getId() == CreateOrJoin.gameRoom.getP1().getId())
            tv_victories_table.setText("My Score: " + CreateOrJoin.gameRoom.getP1_points() + "\n" + "Rival Score: " + CreateOrJoin.gameRoom.getP2_points());
        else
            tv_victories_table.setText("My Score: " + CreateOrJoin.gameRoom.getP2_points() + "\n" + "Rival Score: " + CreateOrJoin.gameRoom.getP1_points());

        ibtn_rock.setOnClickListener(this);
        ibtn_paper.setOnClickListener(this);
        ibtn_scissors.setOnClickListener(this);

    }
}