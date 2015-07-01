package com.example.laptop.settlersopeningbell;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class MainActivity extends ActionBarActivity {

    private Button mStartButton;
    private EditText mTimeDelay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStartButton = (Button) findViewById(R.id.startGameButton);
        mTimeDelay = (EditText) findViewById(R.id.timeDelay);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int delay = new Integer (mTimeDelay.getText().toString());
                startGame(delay);
            }
        });


            }

    public void startGame(int delay){
        Intent intent = new Intent(this, InGame.class);
        intent.putExtra("key_delay", delay);

        startActivity(intent);
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent intent = new Intent (this, SettingsActivity.class);
            startActivity(intent);
        }
        if (id== R.id.instructions){
            Intent intent = new Intent(this, InstructionsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
