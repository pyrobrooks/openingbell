package com.example.laptop.settlersopeningbell;

/**
 * Created by Laptop on 1/11/2015.
 */
public class Die {
   private int mValue;
   private int mImageId;

    public Die (int imageId, int value){
      mValue= value;
       mImageId = imageId;
    }

    public int getImageId(){
        return mImageId;
    }

   public int getValue(){
        return mValue;
    }
}
