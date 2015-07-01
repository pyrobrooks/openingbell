package com.example.laptop.settlersopeningbell;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;

import java.util.Random;

/**
 * Created by Laptop on 1/11/2015.
 */
public class RollResults {




    public Die RollResults (int position) {
        Die [] mRollResults;

        mRollResults = new Die[6];

        mRollResults[0] = new Die(
                 R.drawable.whiteredone,
                1
        );
        mRollResults[1] = new Die(
                R.drawable.whiteredtwo,
                2
                );
        mRollResults[2] = new Die(
                 R.drawable.whiteredthree,
                3
                );
        mRollResults[3] = new Die(
                R.drawable.whiteredfour,
                4
                );
        mRollResults[4] = new Die(
              R.drawable.whiteredfive,
                5
                );
        mRollResults[5] = new Die(
               R.drawable.whiteredsix,
                6
                );
        return mRollResults[position];
    }

        public Die RollResultsYellow(int position) {
            Die [] mRollResultsYellow;
            mRollResultsYellow = new Die[6];

            mRollResultsYellow[0] = new Die(
                    R.drawable.yellowone,
                    1
            );
            mRollResultsYellow[1] = new Die(
                    R.drawable.yellowtwo,
                    2
            );
            mRollResultsYellow[2] = new Die(
                    R.drawable.yellowthree,
                    3
            );
            mRollResultsYellow[3] = new Die(
                    R.drawable.yellowfour,
                    4
            );
            mRollResultsYellow[4] = new Die(
                    R.drawable.yellowfive,
                    5
            );
            mRollResultsYellow[5] = new Die(
                    R.drawable.yellowsix,
                    6
            );
            return mRollResultsYellow[position];


    }

    public Die RollResultsRed (int position) {
        Die [] mRollResultsRed;

        mRollResultsRed = new Die[6];

        mRollResultsRed[0] = new Die(
                R.drawable.redone,
                1
        );
        mRollResultsRed[1] = new Die(
                R.drawable.redtwo,
                2
        );
        mRollResultsRed[2] = new Die(
                R.drawable.redthree,
                3
        );
        mRollResultsRed[3] = new Die(
                R.drawable.redfour,
                4
        );
        mRollResultsRed[4] = new Die(
                R.drawable.redfive,
                5
        );
        mRollResultsRed[5] = new Die(
                R.drawable.redsix,
                6
        );
        return mRollResultsRed[position];
    }
    public Die getResults(int position, boolean check) {  //This grabs a random roll result from the table above

        Die results= new Die(0,0);
                Random randomGenerator = new Random();

        int randomNumber = randomGenerator.nextInt(6);

        if (position == InGame.LEFTPOSITION) {

            if (check == true) {
                results = RollResultsRed(randomNumber);
            }else {
                results = RollResults(randomNumber);
            }


        }
        if (position == InGame.RIGHTPOSITION) {

            if (check == true) {
                results = RollResultsYellow(randomNumber);
            }else {
                results = RollResults(randomNumber);
            }


        }
        return results;
    }

}
