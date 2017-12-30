package com.example.android.javaandroidlibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class JokeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);

        Intent intent = getIntent();
        String joke = intent.getStringExtra(getString(R.string.joke));
        TextView textViewJoke = findViewById(R.id.text_view_joke);
        textViewJoke.setText(joke);
    }
}
