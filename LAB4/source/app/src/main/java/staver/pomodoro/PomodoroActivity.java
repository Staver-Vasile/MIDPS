package staver.pomodoro;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

public class PomodoroActivity extends Activity {
    public enum State {WORK, REST, NONE, PAUSE};
    public static State savedState;
    public static State state;
    public static int timer;
    public static int workMax = 1500;
    public static int restMax = 300;
    public static int ultraRestMax = 900;
    public static int timerLimit;
    public static int workPeriod = 0;
    public static View root;
    public static Handler handler;
    public static ImageView playButton, stopButton, settingsButton, facebookButton;
    public static String workLabel = "Working...", restLabel="Relaxing...", noLabel="Pomodoro Timer", pauseLabel="Paused...";
    public static MediaPlayer mp;
    public static PomodoroActivity MainScreen;
   // CallbackManager callbackManager;
    ShareDialog shareDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        state = State.NONE;
        MainScreen = this;
        TimeControl.noColor = Color.BLACK;
        TimeControl.workColor = Color.parseColor("BLACK");
        TimeControl.restColor = Color.parseColor("BLACK");
        TimeControl.progressWorkColor = Color.parseColor("#800000");
        TimeControl.progressRestColor = Color.parseColor("#20B2AA");
        setContentView(R.layout.activity_pomodoro);
        root = findViewById(R.id.root);
        root.setBackgroundColor(TimeControl.noColor);
        initInterface();
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                root.setBackgroundColor(TimeControl.currentColor);
                return false;
            }
        });
        load();
       // FacebookSdk.sdkInitialize(getApplicationContext());
       // callbackManager = CallbackManager.Factory.create();
        //shareDialog = new ShareDialog(this);
      /* shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });*/

    }
    public static String convertTime(int time){
        String min = time/60+"";
        String sec = time %60+"";
        if (min.length()<2) min = "0"+min;
        if (sec.length()<2) sec = "0"+sec;
        return min+":"+sec;
    }
    public void initInterface(){
        playButton = (ImageView) findViewById(R.id.play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(state){
                    case WORK: pause(); break;
                    case REST: pause(); break;
                    case NONE: startWork(); playButton.setImageResource(R.drawable.pause); break;
                    case PAUSE: resume(); break;
                }
            }
        });
        stopButton = (ImageView) findViewById(R.id.stop);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state = State.NONE;
                TimeControl.currentLabel = noLabel;
                playButton.setImageResource(R.drawable.play);
                TimeControl.currentColor = TimeControl.noColor;
                root.setBackgroundColor(TimeControl.currentColor);
            }
        });
        settingsButton = (ImageView) findViewById(R.id.settings);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsDialog.showDialog();
            }
        });
      /*facebookButton = (ImageView) findViewById(R.id.facebook);
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentTitle("Pomodoro App")
                            .setContentDescription(
                                    "Download right now pomodoro app and change your life!")
                            .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
                            .build();

                    shareDialog.show(linkContent);
                }
            }
        });*/
    }

    public static void pause(){
        Log.d("STATECHANGE","PAUSE");
        savedState = state;
        state = State.PAUSE;
        TimeControl.currentLabel = pauseLabel;
        playButton.setImageResource(R.drawable.play);
    }

    public static void resume(){
        Log.d("STATECHANGE","RESUME");
        state = savedState;
        playButton.setImageResource(R.drawable.pause);
        switch (state){
            case WORK: TimeControl.currentLabel = workLabel; break;
            case REST: TimeControl.currentLabel = restLabel; break;
        }
    }
    public static void startWork(){
        Log.d("STATECHANGE","WORK");
        timer = 0;
        timerLimit = workMax;
        TimeControl.time = convertTime(timer);
        state = State.WORK;
        TimeControl.currentColor = TimeControl.workColor;
        TimeControl.progressColor = TimeControl.progressWorkColor;
        handler.sendEmptyMessage(0);
        TimeControl.currentLabel = workLabel;
        workPeriod++;
    }
    public static void startRest(){
        Log.d("STATECHANGE","REST");
        timer = 0;
        timerLimit = workPeriod<4?restMax:ultraRestMax;
        if (workPeriod >= 4)
            workPeriod = 0;
        TimeControl.time = convertTime(timer);
        state = State.REST;
        TimeControl.currentColor = TimeControl.restColor;
        TimeControl.progressColor = TimeControl.progressRestColor;
        TimeControl.currentLabel = restLabel;
        handler.sendEmptyMessage(0);
    }
    public static void playSound(){
        mp = MediaPlayer.create(TimeControl.context, R.raw.ring);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                mp.reset();
                mp.release();
               mp = null;
                mp=null;
            }

        });
        mp.start();
    }

    public void load(){
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(this);
        workMax = Integer.parseInt(sPref.getString("work", "1500"));
        restMax = Integer.parseInt(sPref.getString("rest", "300"));
        ultraRestMax = Integer.parseInt(sPref.getString("ultra", "900"));
    }
}
