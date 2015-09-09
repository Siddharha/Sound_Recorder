package com.soundrecorder.activities;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.soundrecorder.R;
import com.soundrecorder.adapters.MyRecyclerAdapter;
import com.soundrecorder.beans.Items;
import com.soundrecorder.utilities.DatabaseHandler;
import com.soundrecorder.utilities.RecyclerItemClickListener;
import com.soundrecorder.utilities.SwipeableRecyclerViewTouchListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.logging.FileHandler;

public class RecordListActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    private Toolbar toolbar;
    List<Items> list;
    MyRecyclerAdapter myAdapter;
    RecyclerView R_view;
    TextView txt_plr_name;
    SeekBar SS_Bar;
    CardView plr;
    Button plr_x;
    FrameLayout shadder;
    MediaPlayer  mPlayer = null;
    String p,Current_file;
    Chronometer timer_2;
    int x,mFileDuration;
    SharedPreferences pref;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
       // overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);
        initialize();

        loadData();
        displayItems();
        onTouchFunction();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AdView mAdView = (AdView) findViewById(R.id.adView_2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void onTouchFunction() {
        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(R_view,
                        new SwipeableRecyclerViewTouchListener.SwipeListener()
                        {

                            @Override
                            public boolean canSwipe(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {

                                    Current_file = list.get(position).getRecord_name();
                                    String fileDIr = Environment.getExternalStorageDirectory()
                                            + File.separator + getPackageName();
                                    File f = new File(fileDIr);

                                    String MFlie = f.getAbsolutePath() + File.separator
                                            +Current_file;
                                    Log.e("Path : ", MFlie);
                                    File p = new File(MFlie);
                                    p.delete();


                                    DatabaseHandler db = new DatabaseHandler(getBaseContext());
                                    if(list.size()<2) {

                                        db.deleteItems();
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.remove("rc_name");
                                        editor.commit();
                                        Log.e("pref Val: ", String.valueOf(pref.getInt("rc_name", 0)));


                                    }
                                    else
                                    {
                                      db.deleteItem(list.get(position));
                                    }
                                    db.close();
                                    list.remove(position);
                                    myAdapter.notifyItemRemoved(position);
                                }
                                myAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {

                                    Current_file = list.get(position).getRecord_name();
                                    String fileDIr = Environment.getExternalStorageDirectory()
                                            + File.separator + getPackageName();
                                    File f = new File(fileDIr);

                                    String MFlie = f.getAbsolutePath() + File.separator
                                            +Current_file;
                                    Log.e("Path : ", MFlie);
                                    File p = new File(MFlie);
                                    p.delete();



                                    DatabaseHandler db = new DatabaseHandler(getBaseContext());


                                    if(list.size()<2) {
                                        db.deleteItems();
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.remove("rc_name");
                                        editor.commit();
                                        Log.e("pref Val: ", String.valueOf(pref.getInt("rc_name", 0)));

                                    }
                                    else
                                    {
                                        db.deleteItem(list.get(position));
                                    }
                                    db.close();
                                    list.remove(position);
                                    Log.e("Item : ",String.valueOf(list.size()));
                                    myAdapter.notifyItemRemoved(position);
                                }
                                myAdapter.notifyDataSetChanged();
                            }


                        });
        R_view.addOnItemTouchListener(swipeTouchListener);


        R_view.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        p = list.get(position).getRecord_name();
//                        PTime =(Integer.parseInt(list.get(position).getDuration()));
                        // PTime = mPlayer.getDuration();
                        Toast.makeText(getBaseContext(), p, Toast.LENGTH_SHORT).show();
                        plr.setVisibility(View.VISIBLE);
                        shadder.setVisibility(View.VISIBLE);
                        txt_plr_name.setText(p);
                        startPlaying();
                        timer_2.start();
                        timer_2.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                            @Override
                            public void onChronometerTick(Chronometer chronometer) {
                                if(mPlayer != null){
                                    int mCurrentPosition = mPlayer.getCurrentPosition() / 1000;
                                   SS_Bar.setProgress(mCurrentPosition);

                                    Log.e("Current Position : ",String.valueOf(mCurrentPosition));
                                }

                            }
                        });

                        // SS_Bar.setProgress(Integer.parseInt(list.get(position).getDuration()));


                    }


                })
        );


        plr_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plr.setVisibility(View.INVISIBLE);
                shadder.setVisibility(View.INVISIBLE);
                timer_2.stop();
                if (mPlayer.isPlaying()) {
                    mPlayer.stop();
                    mPlayer.release();
                }

            }
        });

    }


    private void startPlaying() {

        String fileDIr = Environment.getExternalStorageDirectory()
                + File.separator + getPackageName();
        File f = new File(fileDIr);

        String mFileName = f.getAbsolutePath() + File.separator
                +p;
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();

            mFileDuration = (mPlayer.getDuration()/1000);
            SS_Bar.setMax(mFileDuration);
            Log.e("Duration : ", String.valueOf(mFileDuration));
        } catch (IOException e) {
        }


    }



    private void initialize() {
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_small);
        toolbar.setTitle("Record List");
        toolbar.setPadding(10, 10, 10, 10);
        setSupportActionBar(toolbar);
        list = new ArrayList<>();
        SS_Bar = (SeekBar)findViewById(R.id.SS_Bar);
        R_view = (RecyclerView)findViewById(R.id.R_view);
        R_view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        myAdapter = new MyRecyclerAdapter(list, R.layout.table_layout);
        txt_plr_name = (TextView) findViewById(R.id.txt_plr_name);
        plr = (CardView)findViewById(R.id.plr);
        plr_x = (Button)findViewById(R.id.plr_x);
        shadder = (FrameLayout)findViewById(R.id.shadder);
        timer_2 = (Chronometer)findViewById(R.id.timer_2);
        x = 0;
        click();
    }

    private void click() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void displayItems() {
        Log.e("List Items: ", list.toString());
        R_view.setAdapter(myAdapter);
    }

    private void loadData() {

        DatabaseHandler db = new DatabaseHandler(this);
        for(int i=0;i<db.getItems().size();i++)
        {
            // items P = new items();
            list.add(db.getItems().get(i));
        }

        Log.e("List Items --> ",list.toString());
        db.close();
//        for(int i = 0;i<100;i++)
//        {
//            Items item = new Items();
//            item.setRecord_name("Siddhartha " + i);
//            item.setDuration("9732533406 " + i);
//            item.setSize(i);
//            list.add(item);
//        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_record_list, menu);
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

public void ShdClk(View view)
{
    plr.setVisibility(View.INVISIBLE);
    shadder.setVisibility(View.INVISIBLE);
    timer_2.stop();
    if (mPlayer.isPlaying()) {
        mPlayer.stop();
        mPlayer.release();

    }

}

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(mPlayer != null && fromUser){
            mPlayer.seekTo(progress * 1000);
            timer_2.setBase(SystemClock.elapsedRealtime());
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

}


