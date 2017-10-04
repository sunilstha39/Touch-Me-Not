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
import java.util.List;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class Level3Activity extends AppCompatActivity {


    private Button buttons[];
    private GridLayout gl;
    private LinearLayout ll;
    private TextView countDowntv,timeTv,scoreTv;
    private int currentIndex = 0;
    private int currentIndex1 = 0;
    private int currentIndex2 =0;
    private int countdown = 4;
    private int score;
    private int timeLimit = 2;
    private long t1;
    private CountDownTimer gameStater;
    private boolean playing = true;
    private ArrayList<Integer> fruits = new ArrayList<>();
    private boolean btnclicked,btn1clicked,btn2clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_3_main);

        Intent intent = getIntent();
        int s = 0;
        if(intent.getStringExtra("HIGH_SCORE") == null){
            s = 0;
        }else{
            s = Integer.parseInt(intent.getStringExtra("HIGH_SCORE"));
        }

        score = s;

        TextView tx = (TextView)findViewById(R.id.instruction);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/JombloNgenes.ttf");
        tx.setTextSize(20);
        tx.setTypeface(custom_font);
        tx.setText(Html.fromHtml("<h1><font size='12' color='#ea624c' >When level 3 starts the user must click on the all of the birds," +
                "with in 3 sec otherwise game will be over <br/><br/></font></h1>"));

        fruits.add(R.drawable.btn_animal1);
        fruits.add(R.drawable.btn_animal2);
        fruits.add(R.drawable.btn_animal3);
        fruits.add(R.drawable.btn_animal4);
        fruits.add(R.drawable.btn_animal5);
        fruits.add(R.drawable.btn_animal6);
        fruits.add(R.drawable.btn_animal7);
        fruits.add(R.drawable.btn_animal8);
        fruits.add(R.drawable.btn_animal9);
        fruits.add(R.drawable.btn_animal8);
        fruits.add(R.drawable.btn_animal7);
        fruits.add(R.drawable.btn_animal6);

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

        setActiveButton(buttons[currentIndex],buttons[currentIndex1],buttons [currentIndex2]);

        gameStater = new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {
                timeTv.setText(timeConverter(millisUntilFinished / 1000));
                if((System.currentTimeMillis() - t1) > timeLimit * 1000 && playing){
                    dialogBox();
                    gameStater.cancel();
                    playing = false;
                }
            }

            public void onFinish() {
                timeTv.setText("Done");
                playing = false;
                finishGame();
                gameStater.cancel();
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

    private void finishGame() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Congratulation Game Completed!!!");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Game Over",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent i = new Intent(Level3Activity.this,HomeActivity.class);
                        i.putExtra("HIGH_SCORE", score+"");
                        startActivity(i);
                        Level3Activity.this.finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void setActiveButton(final Button btn,final Button btn1, final Button btn2) {
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
            if(buttons[i] == btn || buttons[i] == btn1 || buttons[i] == btn2)
                continue;
            buttons[i].setBackgroundResource(fruits.get(counter++));
        }

        btn.setBackgroundResource(R.drawable.btn_bird1);
        btn1.setBackgroundResource(R.drawable.btn_bird2);
        btn2.setBackgroundResource(R.drawable.btn_bird3);

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
                if(btnclicked&&btn1clicked&&btn2clicked){
                    setActiveButton(buttons[currentIndex],buttons[currentIndex1],buttons[currentIndex2]);
                    btnclicked = false;
                    btn1clicked = false;
                    btn2clicked =false;
                }else{
                    btn.setBackgroundResource(R.drawable.btn_bird1_clicked);
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
                if(btnclicked&&btn1clicked&&btn2clicked) {
                    setActiveButton(buttons[currentIndex], buttons[currentIndex1],buttons[currentIndex2]);
                    btnclicked = false;
                    btn1clicked = false;
                    btn2clicked =false;

                }else{
                    btn1.setBackgroundResource(R.drawable.btn_bird2_clicked);


                }
                mp.start();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!playing)
                    return;
                updateScore();
                getRandomInt();
                btn2clicked = true;
                if(btnclicked&&btn1clicked&&btn2clicked) {
                    setActiveButton(buttons[currentIndex], buttons[currentIndex1],buttons[currentIndex2]);
                    btnclicked = false;
                    btn1clicked = false;
                    btn2clicked =false;

                }else{
                    btn2.setBackgroundResource(R.drawable.btn_bird3_clicked);
                }
                mp.start();
            }
        });
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

        editor.apply();

    }

    private String convertScore(int score) {
        return (score<10 ? "0" : "")+score;
    }

    public void getRandomInt(){
        Integer[] randomArray = getRandomArray();
        currentIndex = randomArray[0];
        currentIndex1 = randomArray[1];
        currentIndex2 = randomArray[2];
    }

    public Integer[] getRandomArray(){
        List<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < 12; i++){
            list.add(i);
        }

        Collections.shuffle(list);
        Integer[] randomArray = list.subList(0, 3).toArray(new Integer[3]);

        return randomArray;
    }

    public void dialogBox() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Game over");
        alertDialogBuilder.setPositiveButton("Exit",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent i = new Intent(Level3Activity.this,HomeActivity.class);
                        Level3Activity.this.finish();
                        startActivity(i);
                    }
                });

        alertDialogBuilder.setNegativeButton("Reset",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        playing = true;
                        score = -1;
                        gameStater.cancel();
                        gameStater.start();
                        updateScore();
                        t1 = System.currentTimeMillis();
                        Intent i = new Intent(Level3Activity.this,Level1Activity.class);
                        Level3Activity.this.finish();
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
    }

    @Override
    protected void onStop(){
        super.onStop();
        gameStater.cancel();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        gameStater.cancel();
        Intent i = new Intent(Level3Activity.this,HomeActivity.class);
        Level3Activity.this.finish();
        startActivity(i);
    }

}
