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

import java.util.ArrayList;

import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;

public class FamilyActivity extends AppCompatActivity {

    //handles playback of all the sounds

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

        //creating an arrayn list of words

        words.add(new word("father","apa",R.drawable.family_father,R.raw.family_father));
        words.add(new word("mother","ata",R.drawable.family_mother,R.raw.family_mother));
        words.add(new word("son","angsi",R.drawable.family_son,R.raw.family_son));
        words.add(new word("daughter","tune",R.drawable.family_daughter,
                R.raw.family_daughter));
        words.add(new word("older brother","taachi",R.drawable.family_older_brother,
                R.raw.family_older_brother));
        words.add(new word("younger brother","chalitti",R.drawable.family_younger_brother,
                R.raw.family_younger_brother));
        words.add(new word("older sister","tete",R.drawable.family_older_sister,
                R.raw.family_older_sister));
        words.add(new word("younger sister","kolliti",R.drawable.family_younger_sister,
                R.raw.family_younger_sister));
        words .add(new word("grand mother","ama",R.drawable.family_grandmother,
                R.raw.family_grandmother));
        words.add(new word("grandfather","paapa",R.drawable.family_grandfather,
                R.raw.family_grandfather));




        WordAdapter adapter=
                new WordAdapter(this, words,R.color.category_family);

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
                    mMediaPlayer = MediaPlayer.create(FamilyActivity.this, Word.getmAudioresourceId());
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
