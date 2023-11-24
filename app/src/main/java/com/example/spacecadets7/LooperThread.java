package com.example.spacecadets7;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class LooperThread extends Thread{
    private String host;
    private String username;
    private int port;
    public Client client;
    public LooperThread(String h, int p, String u){
        this.host = h;
        this.port = p;
        this.username = u;
    }
    public void run(){
        Log.d("DEBUG", username);
        Socket socket = null;
        try {
            socket = new Socket(host, port);
            client = new Client(socket, username);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendMsg(String msg){
        client.sendMsg(msg);
    }
}

class Client{
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public String chat = "";

    private final String[] ANSI_COLORS = {
            "\u001B[0m", // ANSI_RESET
            "\u001B[33m", // ANSI_YELLOW
            "\u001B[34m", // ANSI_BLUE
            "\u001B[32m", // ANSI_GREEN
            "\u001B[31m", // ANSI_RED
            "\u001B[35m", // ANSI_PURPLE
            "\u001B[36m"  // ANSI_CYAN
    };
    private final String ANSI_CURSOR_UP = "\u001B[1A";
    private String chosenColor;

    public Client(Socket socket, String username){
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.username = username;
            do{
                this.chosenColor = this.ANSI_COLORS[(int)(Math.random() * this.ANSI_COLORS.length)];
            }while(this.chosenColor.equals(this.ANSI_COLORS[0]));
            bufferedWriter.write(username + "[MOBILE]");
            bufferedWriter.newLine();
            bufferedWriter.flush();
            this.listenForMsg();
        } catch (Exception e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try {
            if(socket != null){socket.close();}
            if(bufferedReader != null){bufferedReader.close();}

            if(bufferedWriter != null){bufferedWriter.close();}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {
        chat = chat + ("\n" + username + ": " + msg);
        new Thread(new Runnable() {
            public void run() {
                try {
                    bufferedWriter.write(chosenColor + username + ": " + ANSI_COLORS[0] + msg);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                } catch (IOException e) {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                }
            }
        }).start();
    }

    public void listenForMsg(){
        new Thread(new Runnable(){
            public void run(){
                String msgFromServer;

                while(socket.isConnected()){
                    try {
                        msgFromServer = bufferedReader.readLine();
                        chat = chat + ("\n" + msgFromServer);
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }
}
