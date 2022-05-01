package com.example.shaysgame;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class gamePlayPage extends ActionBarActivity implements View.OnClickListener {

    Random rand = new Random();

    VideoView vv_videos[][];
    VideoView vv_battle;

    ImageButton ibtn_rock, ibtn_paper, ibtn_scissors;
    TextView tv_result;
    final char[][] optionsTable = {{'T', 'W', 'L'}, {'L', 'T', 'W'}, {'W', 'L', 'T'}};

    int playerNum, rivalNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solo_page);


        ibtn_rock = (ImageButton) findViewById(R.id.ibtn_rock);
        ibtn_paper = (ImageButton) findViewById(R.id.ibtn_paper);
        ibtn_scissors = (ImageButton) findViewById(R.id.ibtn_scissors);

        tv_result = (TextView) findViewById(R.id.tv_result);

        vv_battle = (VideoView) findViewById(R.id.vv_battle);


        ibtn_rock.setOnClickListener(this);
        ibtn_paper.setOnClickListener(this);
        ibtn_scissors.setOnClickListener(this);


    }

    public void setRivalNum() {
        rivalNum = rand.nextInt(3);
    }

    @Override
    public void onClick(View v) {

        setRivalNum();

        if (v == ibtn_rock) {
            playerNum = 0;

        } else if (v == ibtn_paper) {
            playerNum = 1;

        } else if (v == ibtn_scissors) {
            playerNum = 2;

        }

        int videoLocation = setVv_battle(playerNum, rivalNum);
        vv_battle.setVideoPath("android.resource://" + getPackageName() + "/" + videoLocation);
        vv_battle.requestFocus();
        vv_battle.start();
        tv_result.setText("player: " + playerNum + ", rival: " + rivalNum);

    }

    public int setVv_battle(int playerNum, int rivalNum) {
        switch (playerNum) {
            case 0:
                switch (rivalNum) {
                    case 0:
                        return R.raw.rockrock;

                    case 1:
                        return R.raw.rockpaper;

                    case 2:
                        return R.raw.rockscissors;

                }

            case 1:
                switch (rivalNum) {
                    case 0:
                        return R.raw.paperrock;

                    case 1:
                        return R.raw.paperpaper;

                    case 2:
                        return R.raw.paperscissors;
                }

            case 2:
                switch (rivalNum) {
                    case 0:
                        return R.raw.scissorsrock;


                    case 1:
                        return R.raw.scissorspaper;


                    case 2:
                        return R.raw.scissorsscissors;

                }

        }

        return 0;

    }

}