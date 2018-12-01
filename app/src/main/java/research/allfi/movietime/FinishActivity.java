package research.allfi.movietime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import research.allfi.movietime.users.AppUsersInfo;

public class FinishActivity extends AppCompatActivity {

    Button returnButton = null;
    Button nextButton = null;
    TextView levelView = null;
    TextView resultView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_finish);

        final String user = getIntent().getStringExtra("user");
        int level = getIntent().getIntExtra( "level", 0 );
        int result = getIntent().getIntExtra("result", 0);
        int resultMax = getIntent().getIntExtra("resultMax", 0);

        levelView = findViewById(R.id.levelView);
        levelView.setText("Уровень " + level + " завершен!");

        resultView = findViewById(R.id.resultView);
        resultView.setText("Ваш результат: " + result + " из " + resultMax);

        returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinishActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        nextButton = findViewById( R.id.nextButton );
        nextButton.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinishActivity.this, GameActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        } );

    }
}
