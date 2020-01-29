package inc.bado.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import inc.bado.app.welcome.WelcomeActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onStart() {
        super.onStart();

        navToWelcomeActivity();
    }

    private void navToWelcomeActivity() {

        //start the welcome page
        Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
        finishAffinity();
        startActivity(intent);
    }

}
