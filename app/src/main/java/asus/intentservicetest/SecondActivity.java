package asus.intentservicetest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class SecondActivity extends AppCompatActivity {

    EditText editText;
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        editText = (EditText) findViewById(R.id.editText);
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
