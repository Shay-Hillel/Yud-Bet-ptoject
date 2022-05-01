package com.example.shaysgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;


public class ActionBarActivity extends AppCompatActivity {
    public Menu option_Menu;
    MenuItem mi_goto;
    Intent musicIntent;
    public Switch actionView;
    public static boolean userConnected = false;
    public static boolean inTableRecord = false;
    static boolean rememberSwitchState ;

    private View main;
    private ImageView imageView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        option_Menu = menu;

        final MenuItem toggleservice = menu.findItem(R.id.mute_switch);
        actionView = (Switch) toggleservice.getActionView();
        actionView.setChecked(rememberSwitchState);

        actionView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //mMySwitch.setChecked(isChecked);
                rememberSwitchState = isChecked;
                if(isChecked) {
                    Toast.makeText(getApplicationContext(), "yes", Toast.LENGTH_SHORT).show();
                    backGroundMusic_service.stopMusic();
                }
                else{
                    Toast.makeText(getApplicationContext(), "no", Toast.LENGTH_SHORT).show();
                    backGroundMusic_service.startMusic();
                }

            }
        });

        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
            option_Menu.findItem(R.id.mi_goto).setVisible(ActionBarActivity.userConnected);


            option_Menu.findItem(R.id.mi_shareMyRecord).setVisible(ActionBarActivity.inTableRecord);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.mi_aboutTheGame){
            ActionBarActivity.inTableRecord = false;
            Intent intent = new Intent(this, AboutTheGame.class);
            startActivity(intent);
        }
        else if(id == R.id.mi_aboutMe) {
            ActionBarActivity.inTableRecord = false;
            Intent intent = new Intent(this, AboutMe.class);
            startActivity(intent);
        }else if(id == R.id.mi_table_record){
            ActionBarActivity.inTableRecord = true;
            Intent intent = new Intent(this, Table_Record_Activity.class);
            startActivity(intent);
        }
        else if(id == R.id.mi_play_solo){
            ActionBarActivity.inTableRecord = false;
            Intent intent = new Intent(this, soloPage.class);
            startActivity(intent);
        }else if(id == R.id.mi_play_online){
            ActionBarActivity.inTableRecord = false;
            Intent intent = new Intent(this, CreateOrJoin.class);
            startActivity(intent);
        }
        else if(id == R.id.mi_shareMyRecord){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String subject = "I have " + MainActivity.nowPlayingInThisPhone.getScore() + " points in the game Rock Paper Scissors\nCan you do better!?🤨\nIf you don't have the game this is the link\n -->https://thebestgameever.com<--\n🙄TAP IT🙄";
            intent.putExtra(Intent.EXTRA_TEXT, subject);
            startActivity(Intent.createChooser(intent, "Rock Paper Scissors Game"));
        }
        return super.onOptionsItemSelected(item);
    }




}


