package com.example.musictab;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class Player extends AppCompatActivity {
    Bundle songExtraData;
    ArrayList<File> songFileList;
    SeekBar mSeekBar;
    TextView mSongTitle;
    ImageView playbtn;
    ImageView nextbtn;
    ImageView prevbtn;
    static MediaPlayer mMediaPlayer;
    int position;
    TextView currentTime;
    TextView totalTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        mSeekBar = findViewById(R.id.progressseekBar);
        mSongTitle= findViewById(R.id.songTitle);
        playbtn = findViewById(R.id.playBtn);
        prevbtn = findViewById(R.id.prevBtn);
        nextbtn = findViewById(R.id.nextBtn);
        currentTime = findViewById(R.id.currentTimer);
        totalTime = findViewById(R.id.totalTimer);

        //checking if media player is null or not
        if (mMediaPlayer != null){
            mMediaPlayer.stop();
        }

        Intent songdata= getIntent();
        songExtraData = songdata.getExtras();
        songFileList = (ArrayList) songExtraData.getParcelableArrayList("songFileList");
        position = songExtraData.getInt("position",0);
        initMusicPlayer(position); //start media player

        //set play pause button
        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //when play button is clicked
                play();
            }
        });

        //working of next button
       nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position < songFileList.size()-1){
                    position += 1;

                }else{
                       position = 0;
                }
                initMusicPlayer(position);
            }
        });

        //working of previous button
        prevbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position <= 0){
                     position = songFileList.size() - 1;
                }else{
                    position--;
                }
                initMusicPlayer(position);
            }
        });


    }
    private void initMusicPlayer(final int position){
        if (mMediaPlayer !=null && mMediaPlayer.isPlaying()){
            mMediaPlayer.reset();
        }
        String name = songFileList.get(position).getName();
        mSongTitle.setText(name);

        //song path
        Uri songResourcePath = Uri.parse(songFileList.get(position).toString());

        //create mediaplayer
        mMediaPlayer = MediaPlayer.create(getApplicationContext(),songResourcePath);

        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {

                //set seekbar max duration
                mSeekBar.setMax(mMediaPlayer.getDuration());

                //set total duration of song
                String totTime = createTimerLabel(mMediaPlayer.getDuration());
                totalTime.setText(totTime);

                //start player
                mMediaPlayer.start();

                //set icon to pause
                playbtn.setImageResource(R.drawable.pause_btn);
            }
        });

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                //do something when song is finished
                //abhi bs pause icon change kr rhe to play
                //playbtn.setImageResource(R.drawable.play_btn);

                //repeat song
                int curr_song_pos = position;
                if(curr_song_pos < songFileList.size()-1){
                    curr_song_pos += 1;

                }else{
                    curr_song_pos = 0;
                }
                initMusicPlayer(curr_song_pos);

            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    mMediaPlayer.seekTo(progress); //seek the song
                    mSeekBar.setProgress(progress); //set progress
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //set seekbar to change with song duration
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mMediaPlayer != null){
                   try{
                       if (mMediaPlayer.isPlaying()){
                           Message message = new Message();
                           message.what = mMediaPlayer.getCurrentPosition();
                           handler.sendMessage(message);
                           Thread.sleep(500);

                       }
                   }catch (InterruptedException e){
                       e.printStackTrace();
                   }
                }
            }
        }).start();
    }

    //create new handler
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            currentTime.setText(createTimerLabel(msg.what));
            mSeekBar.setProgress(msg.what);
        }
    };

    private void play(){
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()){
            mMediaPlayer.pause();
            playbtn.setImageResource(R.drawable.play_btn);
        }else{
            mMediaPlayer.start();
            playbtn.setImageResource(R.drawable.pause_btn);
        }
    }

    public  String createTimerLabel(int duration){
        String timerLabel = "";
        int min = duration / 1000/ 60;
        int sec = duration / 1000 % 60;
        timerLabel += min + ":";
        if (sec < 10) timerLabel += "0";
        timerLabel += sec;
        return timerLabel;
    }
}
