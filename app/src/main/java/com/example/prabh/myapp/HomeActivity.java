package com.example.prabh.myapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.LightingColorFilter;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class HomeActivity extends AppCompatActivity {

    ImageView start, imageButton2;
    TextView highscore,scoretxt;
    MediaPlayer mp;
    ImageButton btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main);
        View imageButton = findViewById(R.id.imageButton);
        imageButton.bringToFront();
        View imageButton2 = findViewById(R.id.imageButton2);
        imageButton2.bringToFront();
        View imageButton4 = findViewById(R.id.imageButton4);
        imageButton4.bringToFront();

        YoYo.with(Techniques.Wobble)
                .duration(8500)
                .repeat(-1)
                .playOn(findViewById(R.id.cloud));

        YoYo.with(Techniques.Pulse)
                .duration(5500)
                .repeat(-1)
                .playOn(findViewById(R.id.cloud));

        YoYo.with(Techniques.Wobble)
                .duration(6900)
                .repeat(-1)
                .playOn(findViewById(R.id.cloud2));

        YoYo.with(Techniques.Swing)
                .duration(10000)
                .repeat(-1)
                .playOn(findViewById(R.id.appleRope));

        YoYo.with(Techniques.Swing)
                .duration(12000)
                .repeat(-1)
                .playOn(findViewById(R.id.pearRope));

        YoYo.with(Techniques.Swing)
                .duration(10000)
                .repeat(-1)
                .playOn(findViewById(R.id.bananaRope));

        // just for check

        final int screenWidth = getScreenDimensions(this).x;
        final int cloudAnimationImgWidth = getResources().getDrawable(R.drawable.cloud).getIntrinsicWidth();
        int animatedViewWidth = 0;
        while (animatedViewWidth < screenWidth) {
            animatedViewWidth += cloudAnimationImgWidth;
        }
        animatedViewWidth += cloudAnimationImgWidth;


//        View animatedView = findViewById(R.id.animated_view);
//        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) animatedView.getLayoutParams();
//        layoutParams.width = animatedViewWidth;
//        animatedView.setLayoutParams(layoutParams);


        Animation waveAnimation = new TranslateAnimation(0, -cloudAnimationImgWidth, 0, 0);
        waveAnimation.setInterpolator(new LinearInterpolator());
        waveAnimation.setRepeatCount(Animation.INFINITE);
        waveAnimation.setDuration(2500);

//        animatedView.startAnimation(waveAnimation);


        init();
        getSupportActionBar().hide();

        mp = MediaPlayer.create(this, R.raw.jumper);
        mp.setLooping(true);
        mp.start();


        start = (ImageView) findViewById(R.id.start);
        highscore = (TextView) findViewById(R.id.highscore);
        scoretxt = (TextView) findViewById(R.id.score);
        imageButton2 = (ImageView) findViewById(R.id.imageButton2);

        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(HomeActivity.this);
                dialog.setContentView(R.layout.custom);
                dialog.setTitle("Settings");

                Switch mySwitch = (Switch) dialog.findViewById(R.id.switch1);
                mySwitch.setChecked(mp.isPlaying());
                mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (!isChecked) {
                            mp.pause();
                        } else {
                            mp.start();
                        }
                    }
                });

                dialog.show();

            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, Level1Activity.class);
                startActivity(intent);

            }
        });

        SharedPreferences sharedPref = getDefaultSharedPreferences(getApplicationContext());

        final int highScore = sharedPref.getInt("highscore", 0);
        int score = sharedPref.getInt("score", 0);

        highscore.setText(String.format("Highscore : %03d", highScore));
        scoretxt.setText(String.format("Score : %03d", score));


        // share
        btn = (ImageButton) findViewById(R.id.imageButton4);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(android.content.Intent.ACTION_SEND);
                myIntent.setType("Text/plain");
                String shareBody = "Congratulation you have scored "+highScore+" in Touch Me Not Game";
                String shareSub = "Your subject";
                myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                myIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Touch Me Not Score!!!!");
                startActivity(Intent.createChooser(myIntent, "Share Using"));
            }
        });
    }

    // back exit
    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setMessage("Are you sure want to Exit ?");
        builder.setCancelable(true);
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int i) {
                finish();
            }
        });
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // Help Button
    public ImageButton imageButton;

    public void init() {
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
//                builder
                builder.setMessage(Html.fromHtml("<h1><font size='12' color='#ffffff' >When level 1 starts the user must click on the burger, <br/><br/>" +
                        "If user don't click the burger for 2 sec then the game will be over <br/><br/>" +
                        "In level 2 user must click on two sandwich randomly <br/><br/>" +
                        "If user don't click two sandwich within 2 sec then the game will be over. <br/><br/>"+
                        "In level 3 user must click on three birds randomly <br/><br/>" +
                        "If user don't click three birds within 2 sec then the game will be over. <br/><br/>"+
                        "User can gain 5 sec if they click on bonus icon, which will appear in every 20 second.<br/><br/></font></h1>"));

                AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().getDecorView().getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0x09f5b121));
                alertDialog.show();

            }

        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mp.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mp.pause();
    }


    public static Point getScreenDimensions(Context context) {
        int width = context.getResources().getDisplayMetrics().widthPixels;
        int height = context.getResources().getDisplayMetrics().heightPixels;
        return new Point(width, height);
    }


}