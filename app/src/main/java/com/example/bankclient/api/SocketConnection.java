package com.example.bankclient.api;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketConnection {
    public static void connectSocket(String a){
        try {

            InetAddress serverAddr = InetAddress.getByName("127.0.0.1");
            Socket socket = new Socket(serverAddr, 5049);
            String message = "1";
            String result = "";
            PrintWriter out = null;
            BufferedReader in = null;
            try {
                out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out.println(message);
                String text = "";
                String finalText = "";
                while ((text = in.readLine()) != null) {
                    finalText += text;
                }
                result = finalText;
            } catch(Exception e) {
                Log.e("TCP", "S: Error", e);
            } finally {
                socket.close();
            }
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            Log.e("TCP", "C: UnknownHostException", e);
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e("TCP", "C: IOException", e);
            e.printStackTrace();
        }
    }
}
