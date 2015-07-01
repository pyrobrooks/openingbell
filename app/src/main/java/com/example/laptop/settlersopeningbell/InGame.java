package com.example.laptop.settlersopeningbell;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;


public class InGame extends ActionBarActivity {
    private static final String TAG = "InGame";
    private Roll mRoll = new Roll(1);
    private Button mRollDice;
    private Button mStopButton;
    private RollResults mRollResults = new RollResults();
    private RollResults mRollResultsAnimate = new RollResults();
    private ImageView imageViewDie1;
    private ImageView imageViewDie2;
    private ImageView plus1;
    private ImageView neg1;
    private TextView graphView;
    private ScrollView graphContainer;
    private Button mClearHistory;
    private RelativeLayout sevensLayout;
    public static final int LEFTPOSITION = 1;
    public static final int RIGHTPOSITION = 2;

    private Boolean inGame = true;
    private Boolean inSeven = false;
    private Handler mHandler = new Handler();
    private Handler rollHandler = new Handler();
    private int delay = 0;
    private int timeLeft;
    private TextView timer;
    private Toast mToast;
    private int loopCount= 10;
    private int animateDelay;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private HashMap<String, Integer> resultMap = new HashMap<String, Integer>();// create hashmap to store roll frequency
    private String allTimeString = "\nAll Time Roll History: \n";
    private String inGameString = "Rolls this game: \n";



    //added for sound
    private SoundPool soundPool;
    private int soundID;
    boolean loaded = false;
    float actVolume, maxVolume, volume;
    AudioManager audioManager;
    int counter;

    private Runnable animateEvent= new Runnable(){
        @Override
    public void run() {

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(InGame.this);
            boolean citiesCheck = prefs.getBoolean("citiesAndKnights", false);
            Die results1animate = mRollResultsAnimate.getResults(LEFTPOSITION, citiesCheck);
            Die results2animate = mRollResultsAnimate.getResults(RIGHTPOSITION, citiesCheck);
            imageViewDie1.setImageResource(results1animate.getImageId());
            imageViewDie2.setImageResource(results2animate.getImageId());

            if (loopCount > 1){
                loopCount--;
                animateDelay = animateDelay+20;
                rollHandler.postDelayed(animateEvent, animateDelay);
            }
            if (loopCount <= 1) {
                Log.e (TAG, "loop");
                finishRoll(imageViewDie1, imageViewDie2);

            }

        }
    };
    private Runnable showSeven = new Runnable() {
        @Override
        public void run() {
            mRollDice.setText("Done");
            sevensLayout.setVisibility(View.VISIBLE);
        }
    };

    private Runnable rollEvent = new Runnable() {
        @Override
        public void run() {


            timer.setText(String.valueOf(timeLeft));
            if (inGame) {
                timeLeft = timeLeft - 1;
            }
            if (timeLeft < 5){
                Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                v.vibrate(100);
            }
            if (timeLeft < 0){
                startRoll(imageViewDie1, imageViewDie2);
                timeLeft = delay;
            }

            if (inGame) {
                mHandler.postDelayed(rollEvent,  1000);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_game);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
         imageViewDie1 = (ImageView) findViewById(R.id.die1);
         imageViewDie2 = (ImageView) findViewById(R.id.die2);
        sevensLayout = (RelativeLayout) findViewById(R.id.sevensLayout);
        timer = (TextView) findViewById(R.id.timerCountdown);
        plus1 = (ImageView) findViewById(R.id.plus1);
        neg1 = (ImageView) findViewById(R.id.neg1);
        graphContainer = (ScrollView) findViewById(R.id.graphContainer);
        graphView = (TextView) findViewById(R.id.graphView);
        mClearHistory = (Button) findViewById(R.id.clearHistory);
        mSharedPreferences= getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
         //this was used to clear the data in sharedpreferences, but you can use it later to commit data on pause




        for (int i=2; i<=12; i++){
            int value = mSharedPreferences.getInt(""+i, 0);
            resultMap.put("allTimeTotal"+ i, value);
            resultMap.put("inGameTotal" +i,0);
        }

        String space = "   ";
        for(int i=2; i<=12; i++) {
            int value = resultMap.get("allTimeTotal" +i);
            if (i<10){
                space = "   ";
            }else {
                space = " ";
            }
            allTimeString += "" + i + space + "= " + value + "\n";
            int inGameValue = resultMap.get("inGameTotal" +i);
            inGameString += "" + i + space + "= " + inGameValue + "\n";
        }
        updateGraph();







        //added for sound
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        actVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume= actVolume / maxVolume;
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        counter = 0;
        soundPool = new SoundPool(10,AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
                soundPool.play(soundID, volume, volume, 1, 0, 1f);
                counter = counter++;

            }
        });
        soundID = soundPool.load(this, R.raw.shakedice, 1);



        mToast = Toast.makeText(this, "Turn time set to: "+ delay +" seconds.",
                Toast.LENGTH_SHORT);
        startRoll(imageViewDie1, imageViewDie2);
        mRollDice = (Button) findViewById(R.id.rollButton);
        final Button mStopButton = (Button) findViewById(R.id.stopButton);

        Intent intent = getIntent();
        delay = intent.getIntExtra("key_delay", 0);
        timeLeft = delay;






       /* new CountDownTimer(delay * 1000, 1000) {  //Starts a timer that counts down the delay number of seconds

            public void onTick(long millisUntilFinished) {
                //add countdown display here
            }

            public void onFinish() {
                startRoll(imageViewDie1, imageViewDie2);

            }
        }.start();*/

        plus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delay = delay +1;
                timeLeft = timeLeft +2;
                timer.setText(String.valueOf(timeLeft));
                mToast.setText ( "Turn time set to: "+ delay +" seconds.");
                mToast.show();
            }
        });

        neg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delay = delay -1;
                mToast.setText ( "Turn time set to: "+ delay +" seconds.");
                mToast.show();
            }
        });


        mRollDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mHandler.removeCallbacks(rollEvent);
                startRoll(imageViewDie1, imageViewDie2);
                if (inGame) {
                    timeLeft = delay;
                    mHandler.postDelayed(rollEvent, 1000);

                }
                if (inSeven){

                    mRollDice.setText("Roll");
                    sevensLayout.setVisibility(View.INVISIBLE);
                    inSeven = false;
                }


                //do the startRoll and change dice image
            }
        });

        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inGame = !inGame;
                mHandler.removeCallbacks(rollEvent);
                if (inGame) {
                    mStopButton.setText("Pause");


                    graphContainer.setVisibility(View.INVISIBLE);
                    graphView.setVisibility(View.INVISIBLE);
                    mClearHistory.setVisibility(View.INVISIBLE);


                    mHandler.postDelayed(rollEvent,  100);
                }else
                {
                    mStopButton.setText("Resume");
                    updateGraph();
                    graphContainer.setVisibility(View.VISIBLE);
                    graphView.bringToFront();
                    graphView.setVisibility(View.VISIBLE);
                    mClearHistory.setVisibility(View.VISIBLE);

                }


                //do the startRoll and change dice image
            }
        });

       /* View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int dieRoll = mRoll.getRoll1();
                imageViewDie1.setImageDrawable(RollResults[dieRoll]);
            }
        }*/

        mHandler.postDelayed(rollEvent, 1000);
    }

    private void startRoll(ImageView imageViewDie1, ImageView imageViewDie2) {
       //Added for creating sound


        if (loaded){
            soundPool.play(soundID, volume, volume, 1, 0, 1f);
            counter = counter++;

        }
        loopCount= 4;
        animateDelay = 100;
        rollHandler.postDelayed(animateEvent, animateDelay);





    }
    private void finishRoll(ImageView imageViewDie1, ImageView imageViewDie2){
        rollHandler.removeCallbacks(animateEvent);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(InGame.this);
        boolean citiesCheck = prefs.getBoolean("citiesAndKnights", false);
        Die results1 = mRollResults.getResults(LEFTPOSITION, citiesCheck);
        Die results2 = mRollResults.getResults(RIGHTPOSITION, citiesCheck);
        int total = results1.getValue()+ results2.getValue();
        int tempTotal = resultMap.get("inGameTotal" + total)+1;
        int tempAllTimeTotal = resultMap.get ("allTimeTotal" + total) +1;

        resultMap.put("inGameTotal" +total, tempTotal);
        resultMap.put("allTimeTotal"+total, tempAllTimeTotal);


        /*String totalString = "" + total;
        int tempTotal = mSharedPreferences.getInt (totalString, 0); // git test

        mEditor.putInt(totalString, ++tempTotal);
        mEditor.commit();

        int testTotal= mSharedPreferences.getInt(totalString, 0);

        mToast.setText ( "The total of " +totalString + " is: " +testTotal );
        mToast.show();*/



        // Update the die image with new number

        imageViewDie1.setImageResource(results1.getImageId());
        imageViewDie2.setImageResource(results2.getImageId());
        if (total == 7){
            inSeven = true;
            mHandler.removeCallbacks(rollEvent);
            mHandler.postDelayed(showSeven, 1000);


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_in_game, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        if (id== R.id.instructions){
            Intent intent = new Intent(this, InstructionsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        inGame= false;
        mHandler.removeCallbacks(rollEvent);
        mEditor= mSharedPreferences.edit();
        for (int i=2; i<=12; i++){
            System.out.println (i + " = " + resultMap.get ("allTimeTotal" +i));
        }

        for (int i=2; i<=12; i++){

            mEditor.putInt(""+i, resultMap.get ("allTimeTotal" +i));


        }

       mEditor.commit();

        }

    @Override
    protected void onResume() {
        super.onResume();
        if (!inGame){
            ((Button) findViewById(R.id.stopButton)).setText("Resume");


        }


        //mHandler.postDelayed(rollEvent, 1000);

    }

    private void updateGraph (){
        allTimeString = "\nAll Time Roll History: \n";
        inGameString = "Rolls this game: \n";
        String space = "   ";
        for(int i=2; i<=12; i++) {
            int value = resultMap.get("allTimeTotal" +i);
            if (i<10){
                space = "   ";
            }else {
                space = " ";
            }
            allTimeString += "" + i + space + "= " + value + "\n";
            int inGameValue = resultMap.get("inGameTotal" +i);
            inGameString += "" + i + space + "= " + inGameValue + "\n";
        }
        graphView.setText(inGameString + allTimeString);




        }
    }



