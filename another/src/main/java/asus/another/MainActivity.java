package asus.another;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;
    private Socket socket;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            textView.setText((CharSequence) msg.obj);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);
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
