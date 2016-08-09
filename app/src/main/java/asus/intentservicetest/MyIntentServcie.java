package asus.intentservicetest;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * Created by asus on 2016/8/9.
 */
public class MyIntentServcie extends Service {
    private static Vector<Socket> list = new Vector<>();
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    loop();
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    private void loop() {
        try {
            ServerSocket serverSocket = new ServerSocket(55561);
            Socket socket = serverSocket.accept();
            Thread workThread = new Thread(new MyHandler(socket));
            workThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Test", "销毁了");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class MyHandler implements Runnable {
        private Socket socket;
        public MyHandler(Socket socket) {
            this.socket = socket;
            if(list.size()==0){
                list.add(socket);
                Log.d("Test","New+"+socket.getInetAddress());
            } else {
                boolean needAdd = true;
                for (int i = 0; i < list.size(); i++) {
                    if(list.get(i).getInetAddress().equals(socket.getInetAddress())){
                       needAdd = false;
                    }
                }
                if(needAdd){
                    list.add(socket);
                    Log.d("Test", "加入了");
                }
            }
        }

        @Override
        public void run() {
            InputStream is = null;
            try {
                is = socket.getInputStream();
                byte[] bytes = new byte[100];
                if((is.read(bytes, 0, bytes.length))!=0){
                    String str = new String(bytes);
                    Message message = Message.obtain();
                    message.obj = str;
                    MainActivity.handler.sendMessage(message);
                    for (int i = 0; i < list.size(); i++) {
                        //if(!list.get(i).getInetAddress().equals(socket.getInetAddress())){
                            OutputStream os = list.get(i).getOutputStream();
                            os.write("Hello, This Msg is From Server!".getBytes());
                            os.flush();
                        //}
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
