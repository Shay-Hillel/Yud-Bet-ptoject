package com.example.shaysgame;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    static Player nowPlayingInThisPhone;
    Intent musicIntent;

    LowBatteryReceiver lowBatteryReceiver = new LowBatteryReceiver();
    InternetDisconnected internetDisconnected = new InternetDisconnected();

    Button log_in_btn, sign_up_btn, forgot_password_btn, continue_btn;
    ImageButton back_ibtn;

    EditText player_name_et, password_et, password_validation_et, id_et;
    TextView or_tv;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    int tap_log_in_btn_counter = 0;
    int tap_sign_up_btn_counter = 0;

    static boolean isMovedFromMainActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        musicIntent = new Intent(MainActivity.this, backGroundMusic_service.class);
        startService(musicIntent);

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(lowBatteryReceiver, filter);
        registerReceiver(internetDisconnected, filter);

        player_name_et = (EditText) findViewById(R.id.user_name_et);
        password_et = (EditText) findViewById(R.id.password_et);
        password_validation_et = (EditText) findViewById(R.id.password_validation_et);
        id_et = (EditText) findViewById(R.id.id_et);

        log_in_btn = (Button) findViewById(R.id.log_in_btn);
        log_in_btn.setOnClickListener(this);

        sign_up_btn = (Button) findViewById(R.id.sign_up_btn);
        sign_up_btn.setOnClickListener(this);

        forgot_password_btn = (Button) findViewById(R.id.forgot_password_btn);
        forgot_password_btn.setOnClickListener(this);

        continue_btn = (Button) findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(this);

        back_ibtn = (ImageButton) findViewById(R.id.back_to_start_btn);
        back_ibtn.setOnClickListener(this);

        or_tv = (TextView) findViewById(R.id.or_tv);

        player_name_et.setVisibility(View.GONE);
        password_et.setVisibility(View.GONE);
        password_validation_et.setVisibility(View.GONE);
        id_et.setVisibility(View.GONE);
        forgot_password_btn.setVisibility(View.GONE);
        continue_btn.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        if (view == back_ibtn){
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

        if (view == log_in_btn) {
            ActionBarActivity.userConnected = true;
            player_name_et.setVisibility(View.VISIBLE);
            password_et.setVisibility(View.VISIBLE);
            forgot_password_btn.setVisibility(View.VISIBLE);
            log_in_btn.setVisibility(View.VISIBLE);

            or_tv.setVisibility(View.GONE);
            sign_up_btn.setVisibility(View.GONE);
            password_validation_et.setVisibility(View.GONE);
            id_et.setVisibility(View.GONE);
            continue_btn.setVisibility(View.GONE);

            if (tap_log_in_btn_counter > 0){
                if (player_name_et.getText().toString().isEmpty() || password_et.getText().toString().isEmpty())
                    Toast.makeText(this, "Empty input field", Toast.LENGTH_SHORT).show();
                else {
                    tap_log_in_btn_counter = 0;
                    logIn();
                }
            }
            tap_log_in_btn_counter++;
        }

        if (view == sign_up_btn) {
            player_name_et.setVisibility(View.VISIBLE);
            password_et.setVisibility(View.VISIBLE);
            password_validation_et.setVisibility(View.VISIBLE);
            sign_up_btn.setVisibility(View.VISIBLE);
            id_et.setVisibility(View.VISIBLE);

            log_in_btn.setVisibility(View.GONE);
            or_tv.setVisibility(View.GONE);

            if (tap_sign_up_btn_counter > 0){
                if (player_name_et.getText().toString().isEmpty() || password_et.getText().toString().isEmpty() || (id_et.getVisibility() == View.VISIBLE && id_et.getText().toString().isEmpty()))
                    Toast.makeText(this, "Empty input field", Toast.LENGTH_SHORT).show();
                else {
                    tap_sign_up_btn_counter = 0;
                    signUp();
                }
            }
            tap_sign_up_btn_counter++;
        }

        if (view == forgot_password_btn) {
            player_name_et.setVisibility(View.VISIBLE);
            id_et.setVisibility(View.VISIBLE);
            continue_btn.setVisibility(View.VISIBLE);

            password_et.setVisibility(View.GONE);
            or_tv.setVisibility(View.GONE);
            sign_up_btn.setVisibility(View.GONE);
            log_in_btn.setVisibility(View.GONE);
            forgot_password_btn.setVisibility(View.GONE);
        }

        if (view == continue_btn){
            if (player_name_et.getText().toString().isEmpty() || id_et.getText().toString().isEmpty())
                Toast.makeText(this, "Empty input field", Toast.LENGTH_SHORT).show();
            else {
                forgotPassword();
            }
        }

    }

    public void logIn() {
        Query q = myRef.child("My App Users:").orderByKey();
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean user_already_exist = false;
                for (DataSnapshot dst : snapshot.getChildren()) {
                    Player tempPlayer = dst.getValue(Player.class);

                    String playerName = player_name_et.getText().toString();
                    String password = password_et.getText().toString();
                    if(tempPlayer.getName().equals(playerName) && tempPlayer.getPass().equals(password)) {
                        user_already_exist = true;
                        Toast.makeText(MainActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                        nowPlayingInThisPhone = tempPlayer;

                        Intent intent = new Intent(MainActivity.this, startPage.class);
                        startActivity(intent);
                    }

                }

                if(!user_already_exist) {
                    Toast.makeText(MainActivity.this, "The user name or password is incorrect.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void signUp() {
        Query q = myRef.child("My App Users:").orderByKey();
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean user_already_exist = false;
                for (DataSnapshot dst : snapshot.getChildren()) {
                    Player tempPlayer = dst.getValue(Player.class);

                    if(tempPlayer.getName().equals(player_name_et.getText().toString()) || tempPlayer.getId() == Integer.parseInt(id_et.getText().toString())) {
                        Toast.makeText(MainActivity.this, "Username or ID already in use", Toast.LENGTH_SHORT).show();
                        user_already_exist = true;
                    }
                }

                if(!user_already_exist) {
                    if (password_et.getText().toString().equals(password_validation_et.getText().toString())) {
                        Player p = new Player(player_name_et.getText().toString(), password_et.getText().toString(), Integer.valueOf(id_et.getText().toString()));
                        myRef.child("My App Users:").child(player_name_et.getText().toString()).setValue(p);
                        nowPlayingInThisPhone = p;

                        player_name_et.setText("");
                        password_et.setText("");
                        tap_log_in_btn_counter = 0;
                        onClick(log_in_btn);
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Validation Password Incorrect", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void forgotPassword() {
        Query q = myRef.child("My App Users:").orderByKey();
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean user_already_exist = false;

                for (DataSnapshot dst : snapshot.getChildren()) {
                    Player player = dst.getValue(Player.class);

                    if(player.getName().equals(player_name_et.getText().toString()) && player.getId() == Integer.parseInt(id_et.getText().toString())) {
                        Toast.makeText(MainActivity.this, "Your Password is: " + player.getPass(), Toast.LENGTH_SHORT).show();
                        user_already_exist = true;

                        player_name_et.setText("");
                        password_et.setText("");
                        tap_log_in_btn_counter = 0;
                        onClick(log_in_btn);
                    }
                }

                if(!user_already_exist) {
                    Toast.makeText(MainActivity.this, "This Player Is Not Exist!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        isMovedFromMainActivity = true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        isMovedFromMainActivity = false;
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(lowBatteryReceiver);
        unregisterReceiver(internetDisconnected);
        stopService(musicIntent);
        super.onDestroy();
    }
}