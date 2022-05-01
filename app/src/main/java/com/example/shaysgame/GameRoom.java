package com.example.shaysgame;


import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class GameRoom {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    boolean isExist = false;

    private int gameRoomNum;

    private Player p1, p2;
    private int p1_points, p2_points;

    private int round = 0;

    private int gameCode = 0;

    public GameRoom(){ }

    public GameRoom(int gameRoomNum) {
        this.gameRoomNum = gameRoomNum;
    }

    public GameRoom(Player p1) {
        this.p1 = p1;
    }


    public Player getP1() {
        return p1;
    }

    public Player getP2() {
        return p2;
    }

    public int getGameRoomNum() {
        return gameRoomNum;
    }

    public int getRound() {
        return round;
    }

    public int getGameCode() {return gameCode; }

    public int getP1_points() {
        return p1_points;
    }

    public int getP2_points() {
        return p2_points;
    }


    public void setP1(Player p1) {
        this.p1 = p1;
    }

    public void setP2(Player p2) {
        this.p2 = p2;
    }

    public void setGameRoomNum(int gameRoomNum) {this.gameRoomNum = gameRoomNum; }

    public void setRound(int round) {
        this.round = round;
    }

    public void setNextRound() {this.round++; }

    public void setGameCode() {
        Random rand = new Random();
        do {
            this.gameCode = rand.nextInt(10000) + 1000;
        }
        while (IsGameCodeExist(this.gameCode));

   }

    public void setP1_points(int p1_points) {
        this.p1_points = p1_points;
    }

    public void setP2_points(int p2_points) {
        this.p2_points = p2_points;
    }

    public void addP1Point() {this.p1_points++; }
    public void addP2Point() {this.p2_points++; }


    public boolean IsGameCodeExist(int gameCode) {

        Query q = myRef.child("All App Game Room").orderByKey();
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dst : snapshot.getChildren()) {
                    GameRoom gameRoom = dst.getValue(GameRoom.class);
                    if (gameRoom.getGameCode() == gameCode) {
                        isExist =  true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return isExist;
    }
}
