package com.example.prabh.myapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class Level2Activity extends AppCompatActivity {

    private Button buttons[];
    private GridLayout gl;
    private LinearLayout ll;
    private TextView countDowntv,timeTv,scoreTv;
    private int currentIndex = 0;
    private int currentIndex1 = 0;
    private int countdown = 4;
    private int score;
    private int timeLimit = 2;
    private long t1,bonusTime;
    private int bonusTimeLimiter = 20;
    private CountDownTimer gameStater;
    private CountDownTimer bonusTimer;
    private boolean playing = true;
    private boolean showBonus = false;
    private ArrayList<Integer> fruits = new ArrayList<>();
    private boolean btnclicked,btn1clicked = false;
    private int workingTimeSec = 120000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_2_main);

        Intent intent = getIntent();

        int s = Integer.parseInt(intent.getStringExtra("HIGH_SCORE"));
        score = s;

        TextView tx = (TextView)findViewById(R.id.instruction);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/JombloNgenes.ttf");
        tx.setTextSize(20);
        tx.setTypeface(custom_font);
        tx.setText(Html.fromHtml("<h1><font size='12' color='#ea624c' >When level 2 starts the user must click on the both of the sandwich," +
                "with in 2 sec otherwise game will be over <br/><br/></font></h1>"));

        fruits.add(R.drawable.btn_veg1);
        fruits.add(R.drawable.btn_veg2);
        fruits.add(R.drawable.btn_veg3);
        fruits.add(R.drawable.btn_veg4);
        fruits.add(R.drawable.btn_veg5);
        fruits.add(R.drawable.btn_veg6);
        fruits.add(R.drawable.btn_veg7);
        fruits.add(R.drawable.btn_veg8);
        fruits.add(R.drawable.btn_veg9);
        fruits.add(R.drawable.btn_strawberry);

        gl = (GridLayout) findViewById(R.id.gl);
        ll = (LinearLayout) findViewById(R.id.countdownHolder);
        countDowntv = (TextView) findViewById(R.id.countdown);
        timeTv = (TextView) findViewById(R.id.timeTv);
        scoreTv = (TextView) findViewById(R.id.scoreTv);
        scoreTv.setText(convertScore(s));

        buttons = new Button[gl.getChildCount()];

        for (int i = 0; i < gl.getChildCount(); i++) {
            buttons[i] = (Button) gl.getChildAt(i);
        }

        getRandomInt();

        setActiveButton(buttons[currentIndex],buttons[currentIndex1]);

        workingTimeSec = 120000;

        gameStater = new CountDownTimer(1200000, 1000) {

            public void onTick(long millisUntilFinished) {
                if(workingTimeSec <= 1){
                    this.onFinish();
                }
                workingTimeSec -= 1000;
                timeTv.setText(timeConverter(workingTimeSec / 1000));
                if((System.currentTimeMillis() - t1) > timeLimit * 1000 && playing){
                    dialogBox();
                    gameStater.cancel();
                    playing = false;
                }
            }

            public void onFinish() {
                timeTv.setText("Done");
                playing = false;
                level3();
                gameStater.cancel();

            }

        };

        bonusTimer = new CountDownTimer(1200000, 1000) {

            public void onTick(long millisUntilFinished) {
                if((System.currentTimeMillis() - bonusTime) > bonusTimeLimiter * 1000 && playing){
                    bonusTime = System.currentTimeMillis();
                    Random r = new Random();
                    int coin = r.nextInt(100) + 1;
                    if(coin % 2 == 0){
                        showBonus = true;
                    }else{
                        showBonus = false;
                    }
                }
            }

            public void onFinish() {
                bonusTimer.cancel();
            }

        };

        final Handler h = new Handler();
        final int delay = 0; //milliseconds

        final Runnable countDownRun = new Runnable(){
            public void run(){
                if(countdown < 1){
                    gl.setVisibility(View.VISIBLE);
                    ll.setVisibility(View.INVISIBLE);
                    gameStater.start();
                    bonusTimer.start();
                    t1 = System.currentTimeMillis();
                }else{
                    countDowntv.setText(countdown+"");
                    countdown--;
                    h.postDelayed(this, 1000);
                }

            }
        };

        h.postDelayed(countDownRun, delay);


    }

    private void setActiveButton(final Button btn,final Button btn1) {
        for (int i=0; i< gl.getChildCount(); i++){
            buttons[i] = (Button) gl.getChildAt(i);
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogBox();
                }
            });
        }

        Collections.shuffle(fruits);
        int counter = 0;
        for (int i=0; i< gl.getChildCount(); i++){
            buttons[i] = (Button) gl.getChildAt(i);
            if(buttons[i] == btn)
                continue;
            if(buttons[i] == btn1)
                continue;
            buttons[i].setBackgroundResource(fruits.get(counter++));
        }

        btn.setBackgroundResource(R.drawable.btn_sand);
        btn1.setBackgroundResource(R.drawable.btn_sand);

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.click);
        mp.setLooping(false);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!playing)
                    return;
                updateScore();
                getRandomInt();
                btnclicked = true;
                if(btnclicked&&btn1clicked){
                    setActiveButton(buttons[currentIndex],buttons[currentIndex1]);
                    btnclicked = false;
                    btn1clicked = false;
                }else{
                    btn.setBackgroundResource(R.drawable.btn_sand_clicked);
                }
                mp.start();
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!playing)
                    return;
                updateScore();
                getRandomInt();
                btn1clicked = true;
                if(btnclicked&&btn1clicked) {
                    setActiveButton(buttons[currentIndex], buttons[currentIndex1]);
                    btnclicked = false;
                    btn1clicked = false;
                }else{
                    btn1.setBackgroundResource(R.drawable.btn_sand_clicked);
                }
                mp.start();
            }
        });

        if(showBonus){
            Button bonusBtn = buttons[(new Random()).nextInt(buttons.length)];
            while(bonusBtn == btn){
                bonusBtn = buttons[(new Random()).nextInt(buttons.length)];
            }
            bonusBtn.setBackgroundResource(R.drawable.btn_bonus);
            bonusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!playing)
                        return;
                    workingTimeSec += 5 * 1000;
                    updateScore();
                    getRandomInt();
                    setActiveButton(buttons[currentIndex], buttons[currentIndex1]);
                    mp.start();
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {

                    }
                    mp.stop();
                    mp.release();
                }
            });

            showBonus = false;
            bonusTime = System.currentTimeMillis();
        }

    }

    private void updateScore() {
        score++;
        scoreTv.setText(convertScore(score));
        t1 = System.currentTimeMillis();

        SharedPreferences sharedPref = getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();

        int highScore = sharedPref.getInt("highscore", 0);

        if(score > highScore){
            editor.putInt("highscore", score);
        }

        editor.putInt("score", score);

        editor.commit();

    }

    private String convertScore(int score) {
        return (score<10 ? "0" : "")+score;
    }

    public void getRandomInt(){
        int min = 0,max = (gl.getChildCount() - 1);
        Random rand;
        rand = new Random();
        currentIndex = rand.nextInt((max - min) + 1) + min;
        while (true){
            currentIndex1 = rand.nextInt((max - min) + 1) + min;
            if(currentIndex != currentIndex1)
                break;
        }
    }



    public void level3() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Continue to Level 3");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Next",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent i = new Intent(Level2Activity.this,Level3Activity.class);
                        i.putExtra("HIGH_SCORE", score+"");
                        startActivity(i);
                        Level2Activity.this.finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public void dialogBox() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Game over");
        alertDialogBuilder.setPositiveButton("Exit",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent i = new Intent(Level2Activity.this,HomeActivity.class);
                        Level2Activity.this.finish();
                        startActivity(i);
                    }
                });

        alertDialogBuilder.setNegativeButton("Reset",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        playing = true;
                        score = -1;
                        workingTimeSec = 120000;
                        gameStater.cancel();
                        gameStater.start();
                        bonusTimer.cancel();
                        bonusTimer.start();
                        updateScore();
                        t1 = System.currentTimeMillis();
                        Intent i = new Intent(Level2Activity.this,Level1Activity.class);
                        Level2Activity.this.finish();
                        startActivity(i);
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private String timeConverter(long seconds){
        long hr = seconds/3600;
        long rem = seconds%3600;
        long mn = rem/60;
        long sec = rem%60;
        String hrStr = (hr<10 ? "0" : "")+hr;
        String mnStr = (mn<10 ? "0" : "")+mn;
        String secStr = (sec<10 ? "0" : "")+sec;
        return mnStr+":"+secStr;
    }

    @Override
    protected void onPause(){
        super.onPause();
        gameStater.cancel();
        bonusTimer.cancel();
    }

    @Override
    protected void onStop(){
        super.onStop();
        gameStater.cancel();
        bonusTimer.cancel();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        gameStater.cancel();
        bonusTimer.cancel();
        Intent i = new Intent(Level2Activity.this,HomeActivity.class);
        Level2Activity.this.finish();
        startActivity(i);
    }

}
