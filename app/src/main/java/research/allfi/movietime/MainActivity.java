package research.allfi.movietime;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import research.allfi.movietime.users.AppUsersInfo;

public class MainActivity extends AppCompatActivity {

    private Button registerButton = null;
    private Button startButton = null;
    private Spinner usersSpinner = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        int check = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (check != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1024);

        registerButton  = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        }) ;

        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra( "user", usersSpinner.getSelectedItem().toString() );
                startActivity( intent );
            }
        });

        AppUsersInfo.init();
        updateUsersList();
    }

    private void updateUsersList(){
        String users[] = AppUsersInfo.getUsers();
        usersSpinner = findViewById(R.id.usersSpinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter(this,   android.R.layout.simple_spinner_item, users);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        usersSpinner.setAdapter(spinnerArrayAdapter);
    }
}
