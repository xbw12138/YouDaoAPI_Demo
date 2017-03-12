package com.xbw.youdao;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xbw.youdao.HttpUtils;
import com.google.gson.JsonSyntaxException;
import com.loopj.android.http.TextHttpResponseHandler;
import org.apache.http.Header;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author:Jack Tony
 * 
 * 重要：注意要申请权限！！！！
 *  <!-- 悬浮窗的权限 -->
    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
 * 
 * @tips  :思路：
 * 1.获得一个windowManager类
 * 2.通过wmParams设置好windows的各种参数
 * 3.获得一个视图的容器，找到悬浮窗视图的父控件，比如linearLayout
 * 4.将父控件添加到WindowManager中去
 * 5.通过这个父控件找到要显示的悬浮窗图标，并进行拖动或点击事件的设置
 * @date  :2014-9-25
 */
public class FloatingService extends Service{
	/**
	 * 定义浮动窗口布局
	 */
	LinearLayout mlayout;
	/**
	 * 悬浮窗控件
	 */
	EditText mfloatingIvs;
	Button mfloatingIv;
	Button search;
	/**
	 * 悬浮窗的布局
	 */
	LayoutParams wmParams;
	LayoutInflater inflater;
	/**
	 * 创建浮动窗口设置布局参数的对象
	 */
	WindowManager mWindowManager;

	//触摸监听器
	GestureDetector mGestureDetector;
	Example translation;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		initWindow();//设置窗口的参数
	}
	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		initFloating();//设置悬浮窗图标
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mlayout != null) {	
			// 移除悬浮窗口
			mWindowManager.removeView(mlayout);
		}
	}
	
	///////////////////////////////////////////////////////////////////////
	
	/**
	 * 初始化windowManager
	 */
	private void initWindow() {
		mWindowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
		wmParams = getParams(wmParams);//设置好悬浮窗的参数
		// 悬浮窗默认显示以左上角为起始坐标
		wmParams.gravity = Gravity.CENTER| Gravity.TOP;
		//悬浮窗的开始位置，因为设置的是从左上角开始，所以屏幕左上角是x=0;y=0		
		//wmParams.x = 0;
		//wmParams.y = 0;
		//得到容器，通过这个inflater来获得悬浮窗控件
		inflater = LayoutInflater.from(getApplication());
		// 获取浮动窗口视图所在布局
		mlayout = (LinearLayout) inflater.inflate(R.layout.floating_layout, null);
		// 添加悬浮窗的视图
		mWindowManager.addView(mlayout, wmParams);
	}
	/** 对windowManager进行设置
	 * @param wmParams
	 * @return
	 */
	public LayoutParams getParams(LayoutParams wmParams){
		wmParams = new LayoutParams();
		//设置window type 下面变量2002是在屏幕区域显示，2003则可以显示在状态栏之上
		//wmParams.type = LayoutParams.TYPE_PHONE; 
		//wmParams.type = LayoutParams.TYPE_SYSTEM_ALERT; 
		wmParams.type = LayoutParams.TYPE_TOAST;
		//设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888; 
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
       //wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE; 
        //设置可以显示在状态栏上
		//LayoutParams.FLAG_NOT_FOCUSABLE| 取消这个，可以弹出输入法
        wmParams.flags =  LayoutParams.FLAG_NOT_TOUCH_MODAL|
        LayoutParams.FLAG_LAYOUT_IN_SCREEN| LayoutParams.FLAG_LAYOUT_INSET_DECOR|
        LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        //设置悬浮窗口长宽数据  
        wmParams.width = LayoutParams.WRAP_CONTENT;
        wmParams.height = LayoutParams.WRAP_CONTENT;

        return wmParams;
	}
	/**
	 * 找到悬浮窗的图标，并且设置事件
	 * 设置悬浮窗的点击、滑动事件
	 */
	private void initFloating() {
		mfloatingIvs = (EditText) mlayout.findViewById(R.id.searchView);
		mfloatingIv = (Button) mlayout.findViewById(R.id.button);
		search = (Button) mlayout.findViewById(R.id.button1);
		mGestureDetector = new GestureDetector(this, new MyOnGestureListener());
		//设置监听器
		mfloatingIv.setOnTouchListener(new FloatingListener());
		search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mfloatingIvs.getText().toString().isEmpty()){
					Toast.makeText(getApplicationContext(),"您没有输入内容",Toast.LENGTH_LONG).show();
					return;
				}
				if (HttpUtils.isNetworkConnected(getApplicationContext())) {
					HttpUtils.get(mfloatingIvs.getText().toString(), new TextHttpResponseHandler() {
						@Override
						public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
							Toast.makeText(getApplicationContext(), "获取数据失败1002", Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onSuccess(int statusCode, Header[] headers, String responseString) {
							try {
								Gson gson = new Gson();
								translation = gson.fromJson(responseString, Example.class);
								if(translation.getErrorCode()==0){
									Log.d(responseString,translation.getTranslation()+"");
									Toast.makeText(getApplicationContext(), translation.getTranslation()+"", Toast.LENGTH_SHORT).show();
									HttpUtils.gets(Constant.QT+"?us="+translation.getTranslation()+"&zh="+mfloatingIvs.getText().toString(), new TextHttpResponseHandler() {
										@Override
										public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
										}
										@Override
										public void onSuccess(int statusCode, Header[] headers, String responseString) {
										}
									});
								}else{
									Toast.makeText(getApplicationContext(), "无结果", Toast.LENGTH_SHORT).show();
								}

							} catch (JsonSyntaxException e) {
								Toast.makeText(getApplicationContext(), "获取数据失败1003", Toast.LENGTH_SHORT).show();
								return;
							}
						}
					});
				}
			}
		});
	}
	//开始触控的坐标，移动时的坐标（相对于屏幕左上角的坐标）
	private int mTouchStartX,mTouchStartY,mTouchCurrentX,mTouchCurrentY;
	//开始时的坐标和结束时的坐标（相对于自身控件的坐标）
	private int mStartX,mStartY,mStopX,mStopY;
	private boolean isMove;//判断悬浮窗是否移动
	
	/**
	 * @author:金凯
	 * @tips  :自己写的悬浮窗监听器
	 * @date  :2014-3-28
	 */
	private class FloatingListener implements OnTouchListener{

		@Override
		public boolean onTouch(View arg0, MotionEvent event) {
			int action = event.getAction();
			switch(action){ 
				case MotionEvent.ACTION_DOWN:
					isMove = false;
					mTouchStartX = (int)event.getRawX();
					mTouchStartY = (int)event.getRawY();
					mStartX = (int)event.getX();
					mStartY = (int)event.getY();
					break; 
				case MotionEvent.ACTION_MOVE:  
					mTouchCurrentX = (int) event.getRawX();
					mTouchCurrentY = (int) event.getRawY();
					wmParams.x += mTouchCurrentX - mTouchStartX;
					wmParams.y += mTouchCurrentY - mTouchStartY;
					mWindowManager.updateViewLayout(mlayout, wmParams);
					mTouchStartX = mTouchCurrentX;
					mTouchStartY = mTouchCurrentY; 
		            break;
				case MotionEvent.ACTION_UP:
					mStopX = (int)event.getX();
					mStopY = (int)event.getY();
					//System.out.println("|X| = "+ Math.abs(mStartX - mStopX));
					//System.out.println("|Y| = "+ Math.abs(mStartY - mStopY));
					if(Math.abs(mStartX - mStopX) >= 1 || Math.abs(mStartY - mStopY) >= 1){
						isMove = true;
					}
		            break; 
			}
			return mGestureDetector.onTouchEvent(event);  //此处必须返回false，否则OnClickListener获取不到监听
		}

	}
	
	/**
	 * @author:金凯
	 * @tips  :自己定义的手势监听类
	 * @date  :2014-3-29
	 */
	class MyOnGestureListener extends SimpleOnGestureListener {
		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			if (!isMove) {
				Toast.makeText(getApplicationContext(), "戳我干啥", 0).show();
				System.out.println("onclick");
			}
			return super.onSingleTapConfirmed(e);
		}
	}

	
	
}
