package com.xbw.youdao;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.xbw.youdao.service.YyService;

public class MainActivity extends AppCompatActivity implements OnCheckedChangeListener{
    private Switch mSwitch;
    private Switch switchProxy;
    private VideoEnabledWebView webView;
    private VideoEnabledWebChromeClient webChromeClient;
    public ProgressBar pb;
    private SwipeRefreshLayout swipeLayout;
    private String url = "file:///android_asset/wechat/indexs.html";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSwitch = (Switch)findViewById(R.id.switch1);
        mSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent intent = new Intent(MainActivity.this,YyService.class);
                if (isChecked) {
                    // 开启switch，设置提示信息
                    startService(intent);
                    Toast.makeText(MainActivity.this,"摇一摇开启桌面词典",Toast.LENGTH_LONG).show();
                } else {
                    // 关闭swtich，设置提示信息
                    stopService(intent);
                    Toast.makeText(MainActivity.this,"摇一摇关闭桌面词典",Toast.LENGTH_LONG).show();
                }
            }
        });
        url = "http://139.129.40.202/english/index.php";
        //进度条显示
        pb = (ProgressBar) findViewById(R.id.pb);
        pb.setMax(100);
        webView = (VideoEnabledWebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        View nonVideoLayout = findViewById(R.id.nonVideoLayout); // Your own view, read class comments
        ViewGroup videoLayout = (ViewGroup) findViewById(R.id.videoLayout); // Your own view, read class comments
        View loadingView = getLayoutInflater().inflate(R.layout.view_loading_video, null); // Your own view, read class comments
        webChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout, videoLayout, loadingView, webView) // See all available constructors...
        {
            // Subscribe to standard events, such as onProgressChanged()...
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // Your code...
                pb.setProgress(newProgress);
                if (newProgress == 100) {
                    //加载完成刷新图标消失
                    swipeLayout.setRefreshing(false);
                    pb.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }
        };
        webChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback() {
            @Override
            public void toggledFullscreen(boolean fullscreen) {
                // Your code to handle the full-screen change, for example showing and hiding the title bar. Example:
                if (fullscreen) {
                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 14) {
                        //noinspection all
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                    }
                } else {
                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 14) {
                        //noinspection all
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }

            }
        });
        webView.setWebChromeClient(webChromeClient);
        // Call private class InsideWebViewClient
        webView.setWebViewClient(new InsideWebViewClient());
        // Navigate anywhere you want, but consider that this classes have only been tested on YouTube's mobile site
        webView.loadUrl(url);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //重新刷新页面
                webView.loadUrl(webView.getUrl());
                pb.setVisibility(View.VISIBLE);
                webView.setWebViewClient(new InsideWebViewClient());
                webView.setWebChromeClient(webChromeClient);
            }
        });
        swipeLayout.setColorScheme(R.color.holo_blue_bright,
                R.color.holo_green_light, R.color.holo_orange_light,
                R.color.holo_red_light);
    }
    private class InsideWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            pb.setVisibility(View.VISIBLE);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            // TODO Auto-generated method stub
            super.onReceivedError(view, errorCode, description, failingUrl);
            Toast.makeText(MainActivity.this, "网络连接失败，请重试", Toast.LENGTH_SHORT).show();
            webView.loadUrl("file:///android_asset/wechat/indexs.html");
        }
    }

    @Override
    public void onBackPressed() {
        // Notify the VideoEnabledWebChromeClient, and handle it ourselves if it doesn't handle it
        if (!webChromeClient.onBackPressed()) {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                // Standard back button implementation (for example this could close the app)
                super.onBackPressed();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_actions, menu);

        MenuItem menuItem = menu.findItem(R.id.menu_item_switch);
        if (menuItem == null) {
            return false;
        }
        switchProxy = (Switch) menuItem.getActionView();
        if (switchProxy == null) {
            return false;
        }
        switchProxy.setChecked(false);
        switchProxy.setOnCheckedChangeListener(this);
        return true;
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Intent intent = new Intent(MainActivity.this,YyService.class);
        if (isChecked) {
            // 开启switch，设置提示信息
            startService(intent);
            Toast.makeText(MainActivity.this,"摇一摇开启桌面词典",Toast.LENGTH_LONG).show();
        } else {
            // 关闭swtich，设置提示信息
            stopService(intent);
            Toast.makeText(MainActivity.this,"摇一摇关闭桌面词典",Toast.LENGTH_LONG).show();
        }


    }
}
