package com.example.shaysgame;

public class Player {
    private String name, pass;
    private int score, medal, id;
    private PlayerChoice choice;

    public Player() {}

    public Player(String name, String pass, int id){
        this.name = name;
        this.pass = pass;
        this.score = 0;
        this.medal = R.drawable.medal;
        this.id = id;
        this.choice  = PlayerChoice.NotSet;
    }


    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public void addScore(int score) {this.score += score; }
    public void removePoints(int score) {
        this.score -= score;
        if(this.score <= 0) this.score = 0;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getPass() {
        return pass;
    }
    public void setPass(String pass) { //למקרה שאני אכין פ' לשינוי סיסמה
        this.pass = pass;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getMedal() {
        return medal;
    }
    public void setMedal(int medal) {
        this.medal = medal;
    }

    public PlayerChoice getChoice() {
        return choice;
    }
    public void setChoice(PlayerChoice choice) {
        this.choice = choice;
    }

}
