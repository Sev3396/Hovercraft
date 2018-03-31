package com.example.admin.hovercraft;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity {
    MqttHelper mqttHelper;
    String message;
    String[] seperated,sm,sa,ss,sd;
    String data1,data2,data3,data4,temp,hum,lum,ph,v1,v2,v3,v4,v5,pH,wt,lm,tp;
    TextView t1,t2,t3,t4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        t1=(TextView)findViewById(R.id.ph);
        t2=(TextView)findViewById(R.id.hum);
        t3=(TextView)findViewById(R.id.temp);
        t4=(TextView)findViewById(R.id.lum);
        startMqtt();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void startMqtt() {
        mqttHelper = new MqttHelper(getApplicationContext());
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {

            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                    if (topic.equals("ihovercraft/tet")) {

                    message = mqttMessage.toString();
                    Toast.makeText(MainActivity.this, "Received data", Toast.LENGTH_SHORT).show();
                    message = mqttMessage.toString();
                    seperated = message.split(",");
                    v1 = seperated[0];
                    v2=seperated[1];
                    v3=seperated[2];
                    v4=seperated[3];
                    sm=v1.split(":");
                    pH = sm[1];
                    sa=v2.split(":");
                    wt= sa[1];
                    ss=v3.split(":");
                    lm= ss[1];
                    sd=v4.split(":");
                    tp=sd[1];
                    Float acid =Float.parseFloat(pH);
                    Integer range = Integer.parseInt(wt);
                    Integer heat = Integer.parseInt(tp);
                    Integer lumi = Integer.parseInt(lm);
                    if(acid>7)
                    {
                        data1="Basic Nature";
                    }
                    else if(acid<7)
                    {
                        data1="Acidic Nature";
                    }
                    else if(acid == 7)
                    {
                        data1="Neutral";
                    }
                    if(range>5)
                    {
                        data2="Excess Water";
                    }
                    else if(range <= 2)
                    {
                        data2="Dry";
                    }
                    if(heat<25)
                    {
                        data3="Cold";
                    }
                    else if(heat>42)
                    {
                        data3="Sunny";
                    }
                    else if(heat < 36 && heat >25)
                    {
                        data3= "Normal";
                    }
                    if(lumi == 0)
                    {
                        data4="Clear";
                    }
                    else if(lumi > 0)
                    {
                        data4 = "Dark";
                    }
                    ph=v1+" pH -"+data1;
                    hum=v2+" Rh -"+data2;
                    temp=v3+" C -"+data3;
                    lum=v4+" cd/m2 -"+data4;
                    t1.setText(ph);
                    t2.setText(hum);
                    t3.setText(temp);
                    t4.setText(lum);
                }
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
            }



        });
    }
}
