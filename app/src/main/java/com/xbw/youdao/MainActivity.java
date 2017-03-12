package com.xbw.youdao;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void buttonListener(View v) {
        Intent intent = new Intent(MainActivity.this,FloatingService.class);
        switch (v.getId()) {
            case R.id.open_button:
                startService(intent);
                break;
            case R.id.close_button:
                stopService(intent);
                break;
            default:
                break;
        }
    }
}
