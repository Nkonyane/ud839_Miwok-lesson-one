/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RemoteController;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.media.AudioManager.AUDIOFOCUS_LOSS;
import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;
import static android.media.CamcorderProfile.get;
import static android.media.MediaPlayer.create;

public class NumbersActivity extends AppCompatActivity {
    private MediaPlayer mMediaPlayer;
    private AudioManager mAudiomanager;
    private RemoteController RemoteControlReceiver;
    AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener=
            new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange==AUDIOFOCUS_LOSS_TRANSIENT||
                            focusChange== AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
                        mMediaPlayer.pause();
                        mMediaPlayer.seekTo(0);

                }else if(focusChange==AudioManager.AUDIOFOCUS_GAIN) {
                    mMediaPlayer.start();

                        //resume play back
                }else if (focusChange==AudioManager.AUDIOFOCUS_LOSS){
                        releaseMediaPlayer();
                    }

                    }

};


     private MediaPlayer.OnCompletionListener mCompletionListener=new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
        };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);
        //create and setup the audi manager to request audio focus
        mAudiomanager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);

        //creating a list of words
        final ArrayList<word>words=new ArrayList<word>();

        //creating an arraylist of words

        words.add(new word("one","lutti",R.drawable.number_one,R.raw.number_one));
        words.add(new word("two","otiiko",R.drawable.number_two,R.raw.number_two));
        words.add(new word("three","tolookosu",R.drawable.number_three,R.raw.number_three));
        words.add(new word("four","oyyisa",R.drawable.number_four,R.raw.number_four));
        words.add(new word("five","massokka",R.drawable.number_five,R.raw.number_five));
        words.add(new word("six","temmokka",R.drawable.number_six,R.raw.number_six));
        words.add(new word("seven","kenekaku,",R.drawable.number_seven,R.raw.number_seven));
        words.add(new word("eight","kawinta",R.drawable.number_eight,R.raw.number_eight));
        words.add(new word("nine","wo`e",R.drawable.number_nine,R.raw.number_nine));
        words.add(new word("ten","na` achaar",R.drawable.number_ten,R.raw.number_ten));



        WordAdapter adapter= new WordAdapter(this, words,R.color.category_numbers);

        ListView listView=(ListView)findViewById(R.id.list);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                word Word = words.get(position);
                //release the media player if it currently exist because we are about to
                //play a different sound
                releaseMediaPlayer();

                //Request audio focus for playback
                int result = mAudiomanager.requestAudioFocus(mOnAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {


                    //create and setup the mediaplayer for the audio resource associated
                    //with the current word
                    mMediaPlayer = MediaPlayer.create(NumbersActivity.this, Word.getmAudioresourceId());
                    //start the audi file
                    mMediaPlayer.start();
                    //setup the media on the media player,so that we can stop and release the
                    //media player once the sounds has finished playing
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });

    }
@Override
protected void onStop(){
        super.onStop();
        releaseMediaPlayer();
        }



    //clean the media player by releasing its resources
    private void releaseMediaPlayer(){
        //
        if (mMediaPlayer!=null){
            mMediaPlayer.release();
            mMediaPlayer=null;
            mAudiomanager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }

}
