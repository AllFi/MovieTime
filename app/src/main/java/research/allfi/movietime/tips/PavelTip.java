package research.allfi.movietime.tips;

import android.animation.Animator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.Random;

import research.allfi.movietime.GameActivity;
import research.allfi.movietime.R;
import research.allfi.movietime.game.Game;

public class PavelTip implements View.OnClickListener, Animator.AnimatorListener {
    private Game game;
    private GameActivity activity;
    private int counter;

    public PavelTip(Game game, GameActivity activity){
        this.game = game;
        this.activity = activity;
        this.counter = 0;
    }

    private void disable(){
        counter = 8;
        activity.pavelTipButton.setEnabled(false);
    }

    public void tick(){
        counter = Math.max(--counter, 0);
        if ( counter == 0 )
            activity.pavelTipButton.setEnabled(true);
    }

    ImageView pavelAdvise = null;
    RelativeLayout layout = null;
    boolean started = false;

    @Override
    public void onClick(View v) {
        pavelAdvise = new ImageView( activity );
        pavelAdvise.setX(-500);
        pavelAdvise.setY(70);
        pavelAdvise.setImageBitmap(loadBitmap("advise"));
        layout = activity.findViewById(R.id.relativeLayout);
        layout.addView(pavelAdvise);
        started = true;
        setAdvise();
        pavelAdvise.animate().
                rotation(60).
                x( -220 ).
                setDuration(800).
                setListener(this).
                start();
        disable();
    }

    String adviseMessage = "";

    private void setAdvise(){
        Game.Choice choice = game.getCorrectChoice();
        int ch = choice.toInt();

        Random rand = new Random();
        int confidence = 50 + rand.nextInt(51);

        boolean correctAdvice = rand.nextInt(101) < confidence;
        int pavelAdv = ch;
        if ( !correctAdvice ) {
            while ( pavelAdv == ch )
                pavelAdv = rand.nextInt(4);
        }

        if ( confidence == 95 ){
            adviseMessage = "Я уверен, что тебе нужно смахнуть " + getStringChoice(pavelAdv);
        } else if ( confidence > 90 ) {
            adviseMessage = "Я почти уверн, что тебе нужно смахнуть " + getStringChoice(pavelAdv);
        } else if ( confidence > 80 ){
            adviseMessage = "Скорее всего тебе нужно смахнуть " + getStringChoice(pavelAdv);
        } else if ( confidence > 70 ){
            adviseMessage = "Думаю, тебе нужно смахнуть  " + getStringChoice(pavelAdv);
        }
        else if ( confidence > 60 ){
            adviseMessage = "Просто смахни  " + getStringChoice(pavelAdv);
        }
        else if ( confidence > 50 ){
            adviseMessage = "Я хз, если честно, попробуй смахнуть  " + getStringChoice(pavelAdv);
        }
    }

    private String getStringChoice(int ch ){
        switch (ch){
            case 0:
                return "наверх";
            case 1:
                return "вниз";
            case 2:
                return "налево";
            case 3:
                return "направо";
        }
        return null;
    }

    private Bitmap loadBitmap( String name ){
        File sdcard = Environment.getExternalStorageDirectory();
        File imgFile = new File(sdcard, "MovieTime/game/pavel/" + name + ".png");

        if ( !imgFile.exists() )
            return null;

        return BitmapFactory.decodeFile(imgFile.getAbsolutePath());
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    TextView advise;

    @Override
    public void onAnimationEnd(Animator animation) {
        if ( started ){
            started = false;

            advise = new TextView(activity);
            advise.setText(adviseMessage);
            advise.setBackgroundColor(Color.WHITE);
            advise.setTextSize(20);
            advise.setWidth(300);
            advise.setX(250);
            advise.setY(300);
            layout.addView(advise);

            pavelAdvise.animate().
                    setStartDelay(1500).
                    x( -500 ).
                    setDuration(600).
                    setListener(this).
                    start();
        }
        else{
            layout.removeView(advise);
            layout.removeView(pavelAdvise);
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
