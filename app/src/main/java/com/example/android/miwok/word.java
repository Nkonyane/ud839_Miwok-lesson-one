package com.example.android.miwok;

/**
 * Created by Wendy on 2017/07/10.
 */

public class word {
    //default translation for the word
    private String mDefaultTranslation;
    //Miwok translation for the word
    private String mMiwokTranslation;
    //Image resource ID for the word
    private int mImageResourceId= NO_IMAGE_PROVIDED;
    private static final int NO_IMAGE_PROVIDED=-1;
    //audio resource ID for the word
    private int mAudioresourceId;



    public word (String defaulttranslation,String MiwokTranslation,int audioResourceId){
        mDefaultTranslation=defaulttranslation;
        mMiwokTranslation=MiwokTranslation;
        mAudioresourceId= audioResourceId;

    }

    public word (String defaulttranslation,String MiwokTranslation,int imageResourceId,int audioResourceId ){
        mDefaultTranslation=defaulttranslation;
        mMiwokTranslation=MiwokTranslation;
        //Image resource ID for the word
        mImageResourceId=imageResourceId;
        mAudioresourceId= audioResourceId;

    }

    //Get default translation of the word
    public String getDefaultTranslation(){

        return mDefaultTranslation;
    }
    //Get Miwok translation of the word
    public String getMiwokTranslation(){

        return mMiwokTranslation;
    }

    //Return the image resource ID of the word
    public int getImageResourceId(){
        return mImageResourceId;
    }

    public boolean hasImage() { return mImageResourceId != NO_IMAGE_PROVIDED;}

   public int getmAudioresourceId()
   {return mAudioresourceId;}
}
