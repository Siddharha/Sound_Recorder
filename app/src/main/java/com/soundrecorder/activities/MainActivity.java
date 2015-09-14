package com.soundrecorder.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.soundrecorder.R;
import com.soundrecorder.beans.Items;
import com.soundrecorder.utilities.DatabaseHandler;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private CardView card,card_menu;
    private Chronometer timer;
    private TextView time_v,name;
    private MediaRecorder mRecorder = null;
    private Integer start;
    private static String mFileName = null;
    private MediaPlayer   mPlayer = null;
    private String Rec_file_name;
    private Integer i;
    private SharedPreferences pref;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(card.getVisibility() == View.VISIBLE)
        {
            fab.performClick();

        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialization();
        click();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

            i = pref.getInt("rc_name",0);
            Log.e("Val of i : ", String.valueOf(i));


    }

    @Override
    protected void onResume() {
        super.onResume();
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


    private void click() {

toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onMenuItemClick(MenuItem item) {

                menu_animation();


        return false;
    }


});
        //


        fab.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                if (start == 0) {
                    start = 1;
                    // Log.e("pref Val: ", String.valueOf(pref.getInt("rc_name", 0)));
                    if (pref.getInt("rc_name", 0) != 0) {
                        i = pref.getInt("rc_name", 0);
                        Log.e("Val of i : ", String.valueOf(i));
                    }

                    time_v.setText("00:00");
                    fab.setBackgroundTintList(ColorStateList.valueOf(Color.argb(225, 127, 0, 17)));
//                    fab.animate().translationY(-card.getHeight() -  (fab.getHeight() * 3) - 40).setInterpolator(new AccelerateInterpolator(2)).start();
                    fab.animate().translationY(-fab.getY() + card.getY() - (card.getHeight() / 2)).setInterpolator(new AccelerateInterpolator(2)).start();
                    CircularReveal_in();
                } else {
                    start = 0;

                    fab.setBackgroundTintList(ColorStateList.valueOf(Color.argb(225, 0, 127, 17)));
//                    fab.animate().translationY(fab.getHeight() - (fab.getHeight())).setInterpolator(new AccelerateInterpolator(2)).start();
                    fab.animate().translationY(+fab.getY() - card.getY() + (card.getHeight() / 2)).setInterpolator(new AccelerateInterpolator(2)).start();
                    CircularReveal_out();
                    addToDB();
                    timer.stop();
                    timer.setBase(SystemClock.elapsedRealtime());

                }
                Log.e("start : ", String.valueOf(start));


                //----------------------------------------------------------------------------


                //----------------------------------------------------------------------------
                onRecord();
            }


            private void addToDB() {
                DatabaseHandler db = new DatabaseHandler(getBaseContext());
                db.addItem(new Items(name.getText().toString(), timer.getText().toString(), 0));
                // db.addItem(new Items("Hi", "abc",0));
                db.close();
            }

        });





timer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
    @Override
    public void onChronometerTick(Chronometer chronometer) {

        time_v.setText(timer.getText());

    }
});

    }


//-------------------------------Version Check-----------------------------//



    //--------------------------------------------------------------------------
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void menu_animation() {

        if (card.isShown()) {
            fab.performClick();
        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


            //Toast.makeText(getBaseContext(),"You Clicked",Toast.LENGTH_SHORT).show();
            int cx = card_menu.getWidth();
            int cy = 0;
            // int cy =0;
            // get the final radius for the clipping circle
            int finalRadius = Math.max(card_menu.getWidth(), card_menu.getHeight());

            if (card_menu.getVisibility() == View.INVISIBLE) {
                // create the animator for this view (the start radius is zero)
                Animator anim = ViewAnimationUtils.createCircularReveal(card_menu, cx, cy, 0, finalRadius);

                // make the view visible and start the animation
                card_menu.setVisibility(View.VISIBLE);
                anim.start();
            } else if (card_menu.getVisibility() == View.VISIBLE) {
                // create the animator for this view (the start radius is zero)
                Animator anim = ViewAnimationUtils.createCircularReveal(card_menu, cx, cy, finalRadius, 0);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        card_menu.setVisibility(View.INVISIBLE);
                    }
                });
                anim.start();
            }

        }

        else
        {
            if(card_menu.getVisibility() == View.INVISIBLE) {
                card_menu.setVisibility(View.VISIBLE);
            }
            else
            {
                card_menu.setVisibility(View.INVISIBLE);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void CircularReveal_in()
    {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


            int cx = card.getWidth() / 2;
            int cy = card.getHeight() / 2;
            // int cy =0;
            // get the final radius for the clipping circle
            int finalRadius = Math.max(card.getWidth(), card.getHeight());


            // create the animator for this view (the start radius is zero)
            Animator anim = ViewAnimationUtils.createCircularReveal(card, cx, cy, 0, finalRadius);


            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    indicatorPlaySound();
                    timer.start();
                }
            });
            // make the view visible and start the animation
            card.setVisibility(View.VISIBLE);
            anim.start();
        }

        else
        {
                card.setVisibility(View.VISIBLE);
            indicatorPlaySound();
            timer.start();
        }

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void CircularReveal_out()
    {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int cx = card.getWidth() / 2;
            int cy = card.getHeight() / 2;
            // int cy =0;
            // get the final radius for the clipping circle
            int finalRadius = Math.max(card.getWidth(), card.getHeight());


            // create the animator for this view (the start radius is zero)
            Animator anim = ViewAnimationUtils.createCircularReveal(card, cx, cy, finalRadius, 0);

            // make the view visible and start the animation
            // card.setVisibility(View.VISIBLE);

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    card.setVisibility(View.INVISIBLE);

                }
            });

            anim.start();

        }

        else
        {
            card.setVisibility(View.INVISIBLE);
        }
    }

    private void indicatorPlaySound()
    {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onRecord() {
        if (start == 1) {

            Rec_file_name = "/record"+i+".3gp";
            i++;
            shaveToPref();
            audioInitialize();
            Log.e("File Name : ",Rec_file_name);
            startRecording();

        } else if (start == 0){
            stopRecording();

           // startPlaying();
        }
    }

    private void shaveToPref() {
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("rc_name", i);

        editor.commit();
    }


    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        try {
            mRecorder.prepare();
            mRecorder.start();
            Log.e("Status :", "Started Recording!");
        } catch (IOException e) {
            Log.e("Status :","Not Recording!");
        }

    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        Log.e("Status :", "Stop Recording!");
    }

    private void initialization() {
        i = 0;
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
         Rec_file_name = "record"+".3gp";
        start = 0;
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        card = (CardView)findViewById(R.id.card);
        timer = (Chronometer)findViewById(R.id.timer);
        time_v = (TextView)findViewById(R.id.time_v);
        name = (TextView)findViewById(R.id.name);
        audioInitialize();
        card_menu = (CardView)findViewById(R.id.card_menu);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.argb(225, 0, 127, 17)));


    }

    private void audioInitialize() {
       // mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
       // mFileName += "/audiorecordtest.3gp";

        String fileDIr = Environment.getExternalStorageDirectory()
                + File.separator + getPackageName();
        File f = new File(fileDIr);
        if (!f.exists()) {
            f.mkdirs();
        }

        mFileName = f.getAbsolutePath() + File.separator
                +"/"+ Rec_file_name;
        name.setText(Rec_file_name);

    }

    public void rec_clk(View view)
    {

      /*  new Thread(){
            public void run(){
                try {
                    sleep(900);*/
                    menu_animation();
                    //Do Pass Intend for another Activity...
                    Intent I = new Intent(getBaseContext(),RecordListActivity.class);
                    startActivity(I);

             /*   } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }.start();*/


    }

    public void abt_clk(View view)
    {
       /* new Thread(){
            public void run(){
                try {
                    sleep(900);*/
                    menu_animation();
                    //Do Pass Intend for another Activity...
                    Intent I = new Intent(getBaseContext(), AboutUsActivity.class);
                    startActivity(I);

              /*  } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }.start();*/

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
