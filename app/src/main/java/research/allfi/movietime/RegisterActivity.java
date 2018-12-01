package research.allfi.movietime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import research.allfi.movietime.users.AppUsersInfo;
import research.allfi.movietime.users.Gender;

public class RegisterActivity extends AppCompatActivity {

    private Button saveButton = null;
    private EditText nameText = null;
    private EditText surnameText = null;
    private EditText nicknameText = null;
    private Spinner genderSpinner = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);

        nameText = findViewById(R.id.nameText);
        surnameText = findViewById(R.id.surnameText);
        nicknameText = findViewById(R.id.nicknameText);
        genderSpinner = findViewById(R.id.genderSpinner);

        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameText.getText().toString();
                String surname = surnameText.getText().toString();
                String nickname = nicknameText.getText().toString();
                String gender = genderSpinner.getSelectedItem().toString();

                if ( name.isEmpty() || surname.isEmpty() || nickname.isEmpty() ){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Not all fields are filled!", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                if ( !AppUsersInfo.checkNicknameUniqueness(nickname) ){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "User with such nickname already exist", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                if ( !AppUsersInfo.registerUser(name, surname, Gender.fromString(gender), nickname) ){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Could not register user!", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                Intent intent = new Intent( RegisterActivity.this, MainActivity.class );
                startActivity(intent);
            }
        });
    }
}
