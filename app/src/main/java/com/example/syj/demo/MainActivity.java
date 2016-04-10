package com.example.syj.demo;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import base.baseActivity;
import utils.StreamUtils;
import utils.fromJson;

public class MainActivity extends baseActivity {
     TextView tile;
     TextView context;


    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what){
                case 1:
                    fromJson response = (fromJson) msg.obj;
                    Log.d("e", "handleMessage: "+response.getContext());
         //setText---------------------------------------------------------
                    tile.setText(response.getTitle());
                    context.setText(response.getContext());
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tile=(TextView)findViewById(R.id.text_title);
       context=(TextView)findViewById(R.id.text_context);




        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                try {
                    URL url = new URL("http://10.0.2.2:8080/demo.json");
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.connect();
                    int responsecode = connection.getResponseCode();
                    if(responsecode==200){

                        InputStream inputStream=connection.getInputStream();
                        String result = StreamUtils.readFromStream(inputStream);
                        Log.d("e", "run: "+result);
          //use gson parse result
                         Gson gson =new Gson();
                         fromJson fromjson = gson.fromJson(result, fromJson.class);
                        Log.d("t", "run: "+fromjson.getTitle());
                        Log.d("c", "run: "+fromjson.getContext());
           //handlerMessage--------------------------------------
                        Message  message = new Message();
                        message.what=1;
                        message.obj=fromjson;
                        handler.sendMessage(message);
                    }
                } catch (MalformedURLException e) {
                   //url error
                    e.printStackTrace();
                }catch (IOException e){
                    //net error
                    e.printStackTrace();
                }
                finally {
                    if(connection!=null) {
                        connection.disconnect();
                   }
                }
            }
        }).start();
    }
}
