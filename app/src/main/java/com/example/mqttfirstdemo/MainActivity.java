package com.example.mqttfirstdemo;

import androidx.annotation.LongDef;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    Switch simpleSwitch;
    Button simpleButton;

    //use "tccp://{ip}" for MQTTHOST proccess
    static String MQTTHOST ="tcp://{yourIpHere}";

    //I don't need to use userName and password for public ip, if you want you can activate the lines

//    static String USERNAME = "";
//    static String PASSWORD ="";
    String topicName ="{yourTopicHere}";


    MqttAndroidClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), MQTTHOST, clientId);

        MqttConnectOptions options = new MqttConnectOptions();

        simpleButton = findViewById(R.id.button);


        getValue();

//        options.setUserName(USERNAME);
//        options.setPassword(PASSWORD.toCharArray());

        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d(TAG, "onSuccess: starts");
                    Toast.makeText(MainActivity.this,"Connected",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    // We are connected
                    Log.d(TAG, "onFailure: starts");
                    Toast.makeText(MainActivity.this,"Failed",Toast.LENGTH_SHORT).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void getValue() {
        //default value
        simpleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pub("{yourDefaultMessage}");
            }
        });

        simpleSwitch = (Switch) findViewById(R.id.switch1);

        simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if(isChecked){

                    simpleButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            pub("{activatedMessage}");
                        }
                    });
                }else {

                    simpleButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            pub("{InactivateMessage}");
                        }
                    });

                }
            }
        });
    }


    public void pub(String message){
        String topic = topicName;
        try {
            client.publish(topic, message.getBytes(),0,false);
        } catch ( MqttException e) {
            e.printStackTrace();
        }
    }

}