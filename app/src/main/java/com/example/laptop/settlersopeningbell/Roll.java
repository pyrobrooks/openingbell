package com.example.laptop.settlersopeningbell;

import java.util.Random;

/**
 * Created by Laptop on 12/30/2014.
 */
public class Roll {

    private int mRoll1;
    //private int mRoll2;

    public Roll(int roll1 /*,int roll2*/){
        mRoll1= roll1;
      //  mRoll2= roll2;
    }


    Random randomGenerator = new Random();
    int dieRoll = randomGenerator.nextInt(5);

    /*public int getRoll2() {
        return mRoll2;
    }*/
    public int getRoll1() {
        mRoll1= 2;
        return mRoll1;
    }

}
