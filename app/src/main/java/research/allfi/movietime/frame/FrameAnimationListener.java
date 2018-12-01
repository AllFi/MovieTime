package research.allfi.movietime.frame;

import android.animation.Animator;
import android.view.View;
import android.widget.TextView;

import research.allfi.movietime.GameActivity;
import research.allfi.movietime.game.Game;

public class FrameAnimationListener implements Animator.AnimatorListener {

    View view = null;
    float x;
    float y;
    Game.Choice choice;
    GameActivity activity;
    Game game;

    public FrameAnimationListener(View view, float x, float y, Game.Choice choice, GameActivity activity, Game game){
        this.view = view;
        this.x = x;
        this.y = y;
        this.game = game;
        this.choice = choice;
        this.activity = activity;
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {

        view.animate()
                .x(x)
                .y(y)
                .setDuration(0)
                .setListener(null)
                .start();
        boolean correct = game.answerIsCorrect( choice);
        TextView movieText = null;
        switch (choice) {
            case TOP:
                movieText = activity.movieTop;
                break;
            case BOTTOM:
                movieText = activity.movieBottom;
                break;
            case LEFT:
                movieText = activity.movieLeft;
                break;
            case RIGHT:
                movieText = activity.movieRight;
                break;
        }
        activity.renderLevel( false, correct, movieText );
        activity.animationInProgress = false;
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
