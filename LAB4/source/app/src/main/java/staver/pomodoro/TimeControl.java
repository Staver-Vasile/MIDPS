package staver.pomodoro;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Alexandr on 25.04.16.
 */
public class TimeControl extends SurfaceView implements SurfaceHolder.Callback{
    public static TimeControl engine;
    public static Context context;
    public static int Width, Height;
    public static float Proportion;
    public static DrawThread drawThread;
    public static Paint paint, paint2;
    public static Path path;
    public static int noColor, workColor, restColor;
    public static int currentColor,progressColor, progressWorkColor, progressRestColor;
    public static float progress = 0.5f;
    public static RectF rect;
    public static String time;
    public static Typeface font;
    public static Typeface defaultFont, mainPageFont;
    public static long prev;
    public static String currentLabel;
    public static Bitmap pomodora, logo, workLogo, restLogo;
    public static float koef = 1f, inc = 0.01f;
    public static boolean growing;

    public TimeControl(Context context){
        super(context);
        engine = this;
        this.context = context;
        getHolder().addCallback(this);
        currentColor = noColor;
    }
    public TimeControl(Context context, AttributeSet attrs){
        super(context);
        if (pomodora == null)
            pomodora = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.logo);
        if (workLogo == null)
            workLogo = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.worklogo6);
        if (restLogo == null)
            restLogo = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.breaklogo);
        engine = this;
        this.context = context;
        getHolder().addCallback(this);
        currentColor = noColor;
        font= Typeface.createFromAsset(context.getAssets(),"alarm.ttf");
        defaultFont= Typeface.createFromAsset(context.getAssets(),"valuoldcaps.ttf");
        mainPageFont = Typeface.createFromAsset(context.getAssets(),"Surfing & Kiteboarding.ttf");
        currentLabel = PomodoroActivity.noLabel;
    }
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d("DRAWT","CREATED");
        paint = new Paint();
        paint2 = new Paint();
        paint.setTypeface(font);
        paint2.setTypeface(defaultFont);
        drawThread = new DrawThread(this.getHolder());
        drawThread.setRunning(true);
        drawThread.start();
        prev = System.currentTimeMillis();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        drawThread.setRunning(false);
    }

    class DrawThread extends Thread {

        private boolean running = false;
        private SurfaceHolder surfaceHolder;

        public DrawThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
        }

        public void setRunning(boolean running) {
            this.running = running;
        }

        @Override
        public void run() {
            while(running){
                Canvas canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas(null);
                    if (PomodoroActivity.state != PomodoroActivity.State.NONE &&  PomodoroActivity.state != PomodoroActivity.State.PAUSE) {
                        if (System.currentTimeMillis() - prev >= 1000){
                            prev = System.currentTimeMillis();
                            if (PomodoroActivity.timer<PomodoroActivity.timerLimit)
                                PomodoroActivity.timer += 1;
                            else {
                                switch (PomodoroActivity.state){
                                    case WORK:  PomodoroActivity.playSound(); PomodoroActivity.startRest(); break;
                                    case REST: PomodoroActivity.playSound(); PomodoroActivity.startWork(); break;
                                }
                            }
                        }
                    }
                    if (canvas == null)
                        continue;
                    Width = canvas.getWidth();
                    Height = canvas.getHeight();
                    Proportion = Height/768.0f;
                    drawTime(canvas);
                    //Log.d("DRAWT","DRAWING");

                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                        postInvalidate();
                    }
                }
            }
        }

    }
    public void drawImage(Canvas canvas,Bitmap bmp, float left, float top,float width, float height, Paint paint){
        left *= Width;
        top *= Height;
        width *= Width;
        height *= Height;
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setTranslate(left, top);
        matrix.preScale(width / bmp.getWidth(), height / bmp.getHeight());
        canvas.drawBitmap(bmp, matrix, paint);
    }

    public void drawImageCenter(Canvas canvas, Bitmap bmp, float x, float y, float w, float h, Paint paint){
        x *= Width;
        y *= Height;
        w *= Width;
        h *= Height;
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setTranslate(x - w / 2, y - h / 2);
        matrix.preScale(w / bmp.getWidth(), h / bmp.getHeight());
        canvas.drawBitmap(bmp, matrix, paint);
    }

    public static int getX(float x){
        return (int)(x*Width);
    }
    public static int getY(float y){
        return (int)(y*Height);
    }
    public static int getM(float m){
        return (int)(m*((Height+Width)/2));
    }
    public static int getCenterX(){
        return Width/2;
    }
    public static int getCenterY(){
        return Height/2;
    }
    public static int fromCenterX(float x){
        return getCenterX()+(int)(x*Width);
    }
    public static int fromCenterY(float y){
        return getCenterY()+(int)(y*Width);
    }
    public void drawTime(Canvas canvas){
        switch (PomodoroActivity.state){
            case NONE: inc = 0.005f; break;
            case PAUSE: inc = 0f; break;
            default: inc = 0.005f; break;
        }
        /*if (progress<1f)
            progress += 0.00f;
        else progress = 1f;*/
        if (growing){
            if (koef<1.5f)
                koef += inc;
            else
                growing = false;
        } else {
            if (koef>1.0f)
                koef -= inc;
            else
                growing = true;
        }
        time = System.currentTimeMillis()/1000+"";
        progress = (1f*PomodoroActivity.timer)/PomodoroActivity.timerLimit;
        canvas.drawColor(currentColor);
        if (PomodoroActivity.state == PomodoroActivity.State.NONE)
        drawImageCenter(canvas, pomodora,0.5f,0.6f,0.5f*koef,0.3f*koef,paint);

        if (PomodoroActivity.state == PomodoroActivity.State.REST)
            drawImageCenter(canvas, restLogo,0.5f,0.525f,0.35f*koef,0.20f*koef,paint);
        if (PomodoroActivity.state == PomodoroActivity.State.WORK){
          //  if (PomodoroActivity.state == PomodoroActivity.State.PAUSE)
              // drawImageCenter(canvas, workLogo,0.5f,0.525f,0.35f*koef,0.20f*koef,paint);
          //  else
                drawImageCenter(canvas, workLogo,0.5f,0.525f,0.35f*koef,0.20f*koef,paint);
        }
        if (PomodoroActivity.state == PomodoroActivity.State.PAUSE){
              if (PomodoroActivity.savedState == PomodoroActivity.State.WORK)
             drawImageCenter(canvas, workLogo,0.5f,0.525f,0.35f*koef,0.20f*koef,paint);
              else
            drawImageCenter(canvas, restLogo,0.5f,0.525f,0.35f*koef,0.20f*koef,paint);
        }
        paint.setColor(Color.WHITE);
        if (path == null) {
            path = new Path();
            rect = new RectF(fromCenterX(-0.35f), fromCenterY(-0.30f), fromCenterX(0.35f), fromCenterY(0.40f));
            path.addArc(rect, 0, 360);
        }
        paint.setStrokeWidth(32*Proportion);
        paint.setStyle(Paint.Style.STROKE);
        if (PomodoroActivity.state != PomodoroActivity.State.NONE)
        canvas.drawPath(path, paint);

        paint.setColor(progressColor);
        paint.setStrokeWidth(16*Proportion);
        Path path2 = new Path();
        if (PomodoroActivity.state != PomodoroActivity.State.NONE)
        path2.addArc(rect,0,360*progress);
        else
            path2.addArc(rect,0,360);
        if (PomodoroActivity.state != PomodoroActivity.State.NONE)
        canvas.drawPath(path2, paint);
        paint.setStrokeWidth(2);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.WHITE);
        paint.setTextSize(64*Proportion);
        time = PomodoroActivity.convertTime(PomodoroActivity.timer);
        if (PomodoroActivity.state != PomodoroActivity.State.NONE)
        canvas.drawText(time,getCenterX(),getCenterY()+getY(-0.22f*Proportion),paint);
        paint2.setStrokeWidth(4);
        paint2.setColor(Color.WHITE);
        paint2.setTextSize(50 *Proportion);
        paint2.setTextAlign(Paint.Align.CENTER);
        if (PomodoroActivity.state == PomodoroActivity.State.NONE){
            paint2.setTextSize(135*Proportion);
            paint2.setTypeface(mainPageFont);
            canvas.drawText(currentLabel,getCenterX(),getCenterY()+getY(-0.20f*Proportion),paint2);
        }

        else{
            paint2.setTypeface(defaultFont);
            canvas.drawText(currentLabel,getCenterX(),getCenterY()+getY(0.26f*Proportion),paint2);}
        //canvas.drawCircle(getX(0.5f),getY(0.5f),getM(0.4f),paint);
    }

}
