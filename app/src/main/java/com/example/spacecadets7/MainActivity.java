package com.example.spacecadets7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button ecsFetchButton;
    private Button chatClientButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ecsFetchButton = findViewById(R.id.ecsfetch);
        chatClientButton = findViewById(R.id.chatClient);

        ecsFetchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, EcsFetcher.class));
            }
        });
        chatClientButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, ChatClient.class));
            }
        });
    }
}