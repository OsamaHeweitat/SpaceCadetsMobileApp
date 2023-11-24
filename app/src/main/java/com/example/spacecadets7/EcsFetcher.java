package com.example.spacecadets7;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class EcsFetcher extends AppCompatActivity {

    private Button fetchButton;
    private EditText idTextBox;
    private TextView result;
    public String resultText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecs_fetcher);

        idTextBox = findViewById(R.id.idTextBox);
        fetchButton = findViewById(R.id.fetch);
        result = findViewById(R.id.resultText);

        Handler handler = new Handler();

        fetchButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v){
                String id = idTextBox.getText().toString();
                new Thread(new Runnable(){
                    public void run() {
                        try {
                            URL ecsURL = new URL("https://www.ecs.soton.ac.uk/people/" + id);
                            BufferedReader webReader = new BufferedReader(new InputStreamReader(ecsURL.openStream()));
                            String line;

                            while ((line = webReader.readLine()) != null) {
                                if (line.contains("<title>")) {
                                    String name = line.substring(line.indexOf("<title>") + 7, line.indexOf("|"));
                                    if (name.contains("People")) {
                                        handler.post(new Runnable() {
                                            public void run() {
                                                result.setText("No user found with such ID.");
                                            }
                                        });

                                    } else {
                                        handler.post(new Runnable() {
                                            public void run() {
                                                result.setText(name);
                                            }
                                        });

                                    }
                                }
                            }
                        } catch(Exception e){
                            result.setText(e.toString());
                        }
                    }
            }).start();
        }
    });
}
}