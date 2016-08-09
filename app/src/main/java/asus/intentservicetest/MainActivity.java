package asus.intentservicetest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    Intent intent;
    Socket socket;
    EditText editText;
    static TextView textView;

    public static Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0x123){

            }else {
                textView.setText((CharSequence) msg.obj);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Test", NetWorkUtils.getLocalIpAddress(this));
        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);
        intent = new Intent(this, MyIntentServcie.class);
        startService(intent);
        Log.d("Test", NetWorkUtils.getLocalIpAddress(this));
    }

    public void go(View v){
        startActivity(new Intent(this, SecondActivity.class));
    }

    public void send(View v){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OutputStream os = null;
                try {
                    socket = new Socket("192.168.0.4", 55561);
                    os = socket.getOutputStream();
                    os.write(editText.getText().toString().getBytes("UTF-8"));
                    while (true){
                        InputStream inputStream = socket.getInputStream();
                        byte[] bytes = new byte[30];
                        while ((inputStream.read(bytes, 0, bytes.length))!=0){
                            Log.d("Test", new String(bytes));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
