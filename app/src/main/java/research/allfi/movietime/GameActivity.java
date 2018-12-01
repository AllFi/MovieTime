package research.allfi.movietime;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import research.allfi.movietime.game.Game;
import research.allfi.movietime.frame.FrameOnTouchListener;
import research.allfi.movietime.tips.FiftyTip;
import research.allfi.movietime.tips.PavelTip;
import research.allfi.movietime.users.AppUsersInfo;
import research.allfi.touchauth.TouchCollector;

public class GameActivity extends AppCompatActivity implements SensorEventListener {

    private ImageView frameView = null;
    public TextView movieTop = null;
    public TextView movieBottom = null;
    public TextView movieLeft = null;
    public TextView movieRight = null;
    public Button fiftyTipButton = null;
    public Button pavelTipButton = null;
    private FiftyTip fiftyTip = null;
    private PavelTip pavelTip = null;
    private Game game = null;
    private TouchCollector collector;
    public boolean animationInProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_game);

        String user = getIntent().getStringExtra("user");
        game = new Game( user );

        //touch auth
        collector = new TouchCollector( user +"/level" + AppUsersInfo.getUsersLevel(user)  );
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener( this, sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                SensorManager.SENSOR_DELAY_FASTEST );

        TextView nicknameText = findViewById(R.id.nicknameText);
        nicknameText.setText(user);

        TextView levelText = findViewById(R.id.levelText);
        levelText.setText("level: " + String.valueOf(AppUsersInfo.getUsersLevel(user)));

        frameView = findViewById(R.id.frameView);
        frameView.setZ( -1 );

        movieTop = findViewById(R.id.filmTitleTop);
        movieBottom = findViewById(R.id.filmTitleBottom);
        movieLeft = findViewById(R.id.filmTitleLeft);
        movieRight = findViewById(R.id.filmTitleRight);
        movieTop.setText( game.getMovieTopName() );
        movieBottom.setText( game.getMovieBottomName() );
        movieLeft.setText( game.getMovieLeftName() );
        movieRight.setText( game.getMovieRightName() );

        frameView.setOnTouchListener( new FrameOnTouchListener( collector, game, this ));

        fiftyTipButton = findViewById(R.id.fiftyTip);
        fiftyTip = new FiftyTip(game, this);
        fiftyTipButton.setOnClickListener( fiftyTip );

        pavelTipButton = findViewById(R.id.pavelTip);
        pavelTip = new PavelTip(game, this);
        pavelTipButton.setOnClickListener( pavelTip );

        renderLevel(true, true, null);
    }

    public void renderLevel(boolean first, boolean correct , final TextView movieView)
    {
        if ( !first ){
            movieView.setTextColor( correct ? Color.GREEN : Color.RED );
            movieView.setTextSize( 24 );
            setDefaultTextStyle();
        }

        if ( game.hasMore() ) {
            frameView.setImageBitmap(game.getNextFrame());
            fiftyTip.tick();
            pavelTip.tick();
        }
        else{
            collector.saveAll();
            Intent intent = new Intent(GameActivity.this, FinishActivity.class);
            String user = getIntent().getStringExtra("user");
            intent.putExtra("user", user );
            intent.putExtra("result", game.getCorrectCount());
            intent.putExtra("resultMax", game.getAllCount());
            intent.putExtra("level", AppUsersInfo.getUsersLevel(user));
            SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            sensorManager.unregisterListener(this);
            AppUsersInfo.inrementUsersLevel(user);
            startActivity(intent);
        }
    }

    void setDefaultTextStyle() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(600);
                } catch (Exception ex) {}
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        movieBottom.setTextColor(Color.WHITE);
                        movieBottom.setTextSize(20);
                        movieTop.setTextColor(Color.WHITE);
                        movieTop.setTextSize(20);
                        movieLeft.setTextColor(Color.WHITE);
                        movieLeft.setTextSize(20);
                        movieRight.setTextColor(Color.WHITE);
                        movieRight.setTextSize(20);
                    }
                });
            }
        }).start();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        collector.onSensorEvent(event);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }
}
