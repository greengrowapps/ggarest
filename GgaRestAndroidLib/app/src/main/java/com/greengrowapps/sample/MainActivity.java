package com.greengrowapps.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.greengrowapps.ggarest.GgaRest;
import com.greengrowapps.ggarest.Response;
import com.greengrowapps.ggarest.listeners.OnObjResponseListener;
import com.greengrowapps.ggarest.listeners.OnResponseListener;

public class MainActivity extends AppCompatActivity {

    private TextView txtUrl;
    private TextView txtIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtUrl = (TextView)findViewById(R.id.url);
        txtIp = (TextView)findViewById(R.id.ip);


        GgaRest.init(this);
        GgaRest.addDefaulteader("Accept","application/json");

        findViewById(R.id.btnGet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doGet();
            }
        });
    }

    private void doGet() {

        GgaRest.ws().get("http://httpbin.org/get")
                .onSuccess(OriginAndUrl.class, new OnObjResponseListener<OriginAndUrl>() {
                    @Override
                    public void onResponse(int code, OriginAndUrl object, Response fullResponse) {
                        txtUrl.setText(object.url);
                        txtIp.setText(object.origin);
                    }
                })
                .onOther(new OnResponseListener() {
                    @Override
                    public void onResponse(int code, Response fullResponse, Exception e) {
                        Toast.makeText(MainActivity.this, "Get failed "+e, Toast.LENGTH_SHORT).show();
                    }
                })
                .execute();
    }
}
