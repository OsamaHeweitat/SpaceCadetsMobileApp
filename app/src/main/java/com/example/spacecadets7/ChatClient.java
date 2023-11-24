package com.example.spacecadets7;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient extends AppCompatActivity {

    private Button connectButton;
    private Button sendButton;
    private EditText messageBox;
    private EditText hostBox;
    private String host;
    private EditText portBox;
    private int port;
    private EditText userBox;
    private String username;
    private LooperThread thread;
    private Boolean timeToRun = false;
    private TextView chatBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Handler handler = new Handler();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_client);

        connectButton = findViewById(R.id.connectButton);
        sendButton = findViewById(R.id.sendButton);
        messageBox = findViewById(R.id.messageBox);
        chatBox = findViewById(R.id.chatbox);
        chatBox.setMovementMethod(new ScrollingMovementMethod());

        connectButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                hostBox = findViewById(R.id.IPBox);
                host = hostBox.getText().toString();
                portBox = findViewById(R.id.portBox);
                port = Integer.parseInt(portBox.getText().toString());
                userBox = findViewById(R.id.usernameBox);
                username = userBox.getText().toString();
                thread = new LooperThread(host, port, username);
                thread.start();
                timeToRun = true;
                new Thread(new Runnable(){
                    public void run(){
                        Log.d("RUNNINGLOL", "I tried.");
                        while(timeToRun){
                            SystemClock.sleep(100);
                            handler.post(new Runnable() {
                                public void run() {
                                    Log.d("RUNNINGLOL", "run: ");
                                    chatBox.setText(thread.client.chat);
                                }});
                        }
                    }
                }).start();
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                thread.sendMsg(messageBox.getText().toString());
            }
        });


    }
}