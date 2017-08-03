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
import android.provider.UserDictionary;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;

public class PhrasesActivity extends AppCompatActivity {
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
        final ArrayList<word> words=new ArrayList<word>();

        //creating an array list of words

        words.add(new word("Where are you going?","minto wuksus",R.raw.phrase_where_are_you_going));
        words.add(new word("What is your name","tinna oyaase`na",R.raw.phrase_what_is_your_name));
        words.add(new word("my namne is...","oyaaset...?",R.raw.phrase_my_name_is));
        words.add(new word("How are you feeling?","michksas?",R.raw.phrase_how_are_you_feeling));
        words.add(new word("I'm feeling good.","kuchi achit",R.raw.phrase_im_feeling_good));
        words.add(new word("Are you coming?","eenem aa",R.raw.phrase_are_you_coming));
        words.add(new word("yes,I'm coming.","hee`eenem",R.raw.phrase_yes_im_coming));
        words.add(new word("I'm coming","eenem",R.raw.phrase_im_coming));
        words.add(new word("Let's go","yoowutis",R.raw.phrase_lets_go));
        words.add(new word("Come here","enni`nem",R.raw.phrase_come_here));

        WordAdapter adapter=new WordAdapter(this, words,R.color.category_phrases);

        ListView listView=(ListView)findViewById(R.id.list);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                word Word=words.get(position);
                releaseMediaPlayer();

                //Request audio focus for playback
                int result = mAudiomanager.requestAudioFocus(mOnAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mMediaPlayer = MediaPlayer.create(PhrasesActivity.this, Word.getmAudioresourceId());
                    mMediaPlayer.start();
                    //setup the media on the media player,so that we can stop and release the
                    //media player once the sounds has finished playing
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }

            }
        });


    }
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
