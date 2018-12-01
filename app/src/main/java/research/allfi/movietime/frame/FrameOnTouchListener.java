package research.allfi.movietime.frame;

import android.view.MotionEvent;
import android.view.View;

import research.allfi.movietime.GameActivity;
import research.allfi.movietime.game.Game;
import research.allfi.touchauth.TouchCollector;

public class FrameOnTouchListener implements View.OnTouchListener {
    private TouchCollector collector;
    private Game game;
    private GameActivity gameActivity;
    private float startViewX;
    private float startViewY;
    private float startMotionX;
    private float startMotionY;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private long timestamp;

    public FrameOnTouchListener(TouchCollector collector, Game game, GameActivity gameActivity){
        this.collector = collector;
        this.game = game;
        gameActivity.animationInProgress = false;
        this.gameActivity = gameActivity;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        collector.onMotionEvent(event);
        float dX;
        float dY;

        if ( gameActivity.animationInProgress )
            return true;

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                startMotionX = event.getRawX();
                startMotionY = event.getRawY();
                startViewX = view.getX();
                startViewY = view.getY();
                timestamp = event.getEventTime();
                break;

            case MotionEvent.ACTION_MOVE:
                dX = startViewX - startMotionX;
                dY = startViewY - startMotionY;
                view.animate()
                        .x(event.getRawX() + dX)
                        .y(event.getRawY() + dY)
                        .setDuration(0)
                        .start();
                break;
            case MotionEvent.ACTION_UP:

                float changeX = event.getRawX() - startMotionX;
                float changeY = event.getRawY() - startMotionY;

                if ( Math.abs(changeX) < SWIPE_MIN_DISTANCE && Math.abs(changeY) < SWIPE_MIN_DISTANCE ){
                    view.animate()
                            .x( startViewX )
                            .y( startViewY )
                            .setDuration( 300 )
                            .start();
                    break;
                }

                gameActivity.animationInProgress = true;

                dX = startViewX - startMotionX;
                dY = startViewY - startMotionY;

                if ( Math.abs( changeX ) > Math.abs( changeY ) ){
                    Game.Choice choice = changeX > 0 ? Game.Choice.RIGHT : Game.Choice.LEFT;

                    long dT = event.getEventTime() - timestamp;
                    float velocity = ( Math.abs(event.getRawX() - startMotionX) / dT );
                    float duration = Math.abs(( event.getRawX() > startMotionX ? 768 : -768 ) - view.getX()) / velocity;

                    view.animate()
                            .x( event.getRawX() > startMotionX ? 768 : -768 )
                            .y( (event.getRawY() + dY)  )
                            .setDuration( Math.min((long) duration, 300) )
                            .setListener( new FrameAnimationListener( view, startViewX, startViewY, choice, gameActivity, game ))
                            .start();
                } else{
                    Game.Choice choice = changeY > 0 ? Game.Choice.BOTTOM : Game.Choice.TOP;

                    long dT = event.getEventTime() - timestamp;
                    float velocity = ( Math.abs(event.getRawY() - startMotionY) / dT );
                    float duration = Math.abs((event.getRawY() > startMotionY ? 1280 : -640) - view.getY()) / velocity;

                    view.animate()
                            .x( (event.getRawX() + dX) )
                            .y( event.getRawY() > startMotionY ? 1280 : -640 )
                            .setDuration( Math.min((long) duration, 300) )
                            .setListener( new FrameAnimationListener( view, startViewX, startViewY, choice, gameActivity, game ))
                            .start();
                }

                break;
            default:
                return false;
        }
        return true;
    }
}
