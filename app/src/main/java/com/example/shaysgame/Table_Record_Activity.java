package com.example.shaysgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Table_Record_Activity extends ActionBarActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    ListView lv_players;
    ArrayList<Player> playersList;
    PlayerAdapter playerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_record);

        playersList = new ArrayList<>();

        setPlayersInList();
    }

    public void setPlayersInList() {
        Query q = myRef.child("My App Users:").orderByKey();
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dst : snapshot.getChildren()) {
                    Player tempPlayer = dst.getValue(Player.class);
                    playersList.add(tempPlayer);
                }

                if(playersList.size() == 1)
                    Toast.makeText(Table_Record_Activity.this, "You AreThe Only Player In The Game\n Bring Friends To Play!", Toast.LENGTH_SHORT).show();

                sortPlayersList();
                displayPlayers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void displayPlayers() {
        playerAdapter = new PlayerAdapter(this, 0, 0, playersList);

        lv_players = (ListView) findViewById(R.id.lv_players);
        lv_players.setAdapter(playerAdapter);
    }

    public void sortPlayersList() {
        Player[] playersArr = new Player[playersList.size()];
        int index = 0;
        for (Player p : playersList) {
            playersArr[index] = p;
            index++;
        }

        int size = playersArr.length;
        boolean run = true;

        for (int i = 0; i < (size-1) && run ;i++) {
            run = false;
            for (int j = 0; j < size - 1 - i; j++) {
                if (!comparePlayers(playersArr[j], playersArr[j + 1])) {
                    Player tmp = playersArr[j];
                    playersArr[j] = playersArr[j + 1];
                    playersArr[j + 1] = tmp;
                    run = true;
                }
            }
        }
        if(size == 1) {
            playersArr[0].setMedal(R.drawable.gold);
        }
        else if(size == 2) {
            playersArr[0].setMedal(R.drawable.gold);
            playersArr[1].setMedal(R.drawable.silver);
        }
        else if(size > 2) {
            playersArr[0].setMedal(R.drawable.gold);
            playersArr[1].setMedal(R.drawable.silver);
            playersArr[2].setMedal(R.drawable.arad);
            for (int i = 3; i < size; i++) {
                playersArr[i].setMedal(R.drawable.medal);
            }
        }

        ArrayList<Player> newPlayersList = new ArrayList<>();

        for (Player p : playersArr) {
            newPlayersList.add(p);
        }

        playersList = newPlayersList;

    }

    public boolean comparePlayers(Player p1, Player p2) {
        int p1_Score = p1.getScore();
        int p2_Score = p2.getScore();

        return p1_Score > p2_Score;
    }

    @Override
    protected void onStart() {
        super.onStart();
        ActionBarActivity.inTableRecord = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        ActionBarActivity.inTableRecord = false;
    }
}