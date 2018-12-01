package research.allfi.movietime.tips;

import android.graphics.Color;
import android.view.View;

import java.util.Random;

import research.allfi.movietime.GameActivity;
import research.allfi.movietime.game.Game;

public class FiftyTip implements View.OnClickListener {

    private Game game;
    private GameActivity activity;
    private int counter;

    public FiftyTip(Game game, GameActivity activity){
        this.game = game;
        this.activity = activity;
        this.counter = 0;
    }

    private void disable(){
        counter = 8;
        activity.fiftyTipButton.setEnabled(false);
    }

    public void tick(){
        counter = Math.max(--counter, 0);
        if ( counter == 0 )
            activity.fiftyTipButton.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        Game.Choice choice = game.getCorrectChoice();
        int ch = choice.toInt();
        int[] choices = new int[] {0,1,2,3};
        mix(choices);
        int count = 0;
        for (int i = 0; i < 4; i++) {
            if ( choices[i] != ch ){
                blur(Game.Choice.fromInt(choices[i]));
                count++;
            }
            if ( count == 2 ) {
                disable();
                return;
            }
        }
    }

    void mix(int[] a) {
        Random rnd = new Random();
        for (int i = 1; i < a.length; i++) {
            int j = rnd.nextInt(i);
            int temp = a[i];
            a[i] = a[j];
            a[j] = temp;
        }
    }

    public void blur( Game.Choice choice ){
        switch( choice ){
            case TOP:
                activity.movieTop.setTextColor(Color.GRAY);
                break;
            case BOTTOM:
                activity.movieBottom.setTextColor(Color.GRAY);
                break;
            case LEFT:
                activity.movieLeft.setTextColor(Color.GRAY);
                break;
            case RIGHT:
                activity.movieRight.setTextColor(Color.GRAY);
                break;
        }
    }
}
