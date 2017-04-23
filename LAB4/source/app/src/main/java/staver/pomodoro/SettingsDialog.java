package staver.pomodoro;

/**
 * Created by Alexandr on 27.04.16.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Alexandr on 27.12.15.
 */
public class SettingsDialog extends Dialog implements View.OnClickListener, View.OnTouchListener{
    private Button yes, no;
    private static int gift;
    public static boolean isClosing = true;
    public EditText worktime, resttime, ultraresttime;
    public Button saveButton, cancelButton;

    public SettingsDialog(Context context) {
        super(context);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.settings_dialog);
        worktime = (EditText)findViewById(R.id.worktime);
        resttime = (EditText)findViewById(R.id.resttime);
        ultraresttime = (EditText)findViewById(R.id.ultraresttime);
        worktime.setText((PomodoroActivity.workMax/60)+"");
        resttime.setText((PomodoroActivity.restMax/60)+"");
        ultraresttime.setText((PomodoroActivity.ultraRestMax/60)+"");
        worktime.setSelection(worktime.getText().length());
        resttime.setSelection(resttime.getText().length());
        ultraresttime.setSelection(ultraresttime.getText().length());
        saveButton = (Button)findViewById(R.id.saveButton);
        cancelButton = (Button)findViewById(R.id.cancelButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int work = 25;
                try{
                    work = Integer.parseInt(worktime.getText().toString());
                } catch (Exception e){

                }
                if (work<1) work = 1; else
                if (work>1000) work = 1000;
                int rest = 5;
                try{
                    rest = Integer.parseInt(resttime.getText().toString());
                } catch (Exception e){

                }
                if (rest<1) rest = 5; else
                if (rest>1000) rest = 1000;
                int ultra = 25;
                try{
                    ultra = Integer.parseInt(ultraresttime.getText().toString());
                } catch (Exception e){

                }
                if (ultra<1) ultra = 1; else
                if (ultra>1000) ultra = 1000;
                PomodoroActivity.workMax = work*60;
                PomodoroActivity.restMax = rest*60;
                PomodoroActivity.ultraRestMax = ultra*60;
                save(work*60, rest*60, ultra*60);
                dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }


    @Override
    public void onClick(View view) {
        dismiss();
    }
    public static void showDialog(){
        SettingsDialog giftDialog=new SettingsDialog(PomodoroActivity.MainScreen);
        giftDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        giftDialog.show();

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        return false;
    }

    public void save(int work, int rest, int ultra){
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("work", work+"");
        ed.putString("rest", rest+"");
        ed.putString("ultra", ultra+"");
        ed.commit();
    }
}

