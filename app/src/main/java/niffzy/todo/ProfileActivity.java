package niffzy.todo;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private TextView profileNameTextView, profileEmailTextView, profileUsernameTextView, profilePasswordTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize TextViews
        profileNameTextView = findViewById(R.id.profile_name);
        profileEmailTextView = findViewById(R.id.profile_email);
        profileUsernameTextView = findViewById(R.id.profile_username);
        profilePasswordTextView = findViewById(R.id.profile_password);

        // Retrieve user data from Intent extras
        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String username = getIntent().getStringExtra("username");
        String password = getIntent().getStringExtra("password");

        // Set retrieved data to TextViews
        profileNameTextView.setText(name);
        profileEmailTextView.setText(email);
        profileUsernameTextView.setText(username);
        profilePasswordTextView.setText(password);
    }
}
