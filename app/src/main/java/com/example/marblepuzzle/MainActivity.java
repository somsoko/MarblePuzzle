package com.example.marblepuzzle;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import androidx.fragment.app.FragmentActivity;

public class MainActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, DifficultySelectActivity.class);
            startActivity(intent);
        });
    }
}
