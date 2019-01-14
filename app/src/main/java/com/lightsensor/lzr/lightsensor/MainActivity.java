package com.lightsensor.lzr.lightsensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private boolean mIsContains;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //获取手机上支持的所有传感器
        if (mSensorManager != null) {
            List<Sensor> mList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
            for (Sensor sensor : mList) {
                Log.d("KeithXiaoY", "名字：" + sensor.getName());
                Log.d("KeithXiaoY", "type:" + sensor.getType());
                Log.d("KeithXiaoY", "vendor:" + sensor.getVendor());
                Log.d("KeithXiaoY", "version:" + sensor.getVersion());
                Log.d("KeithXiaoY", "resolution:" + sensor.getResolution());
                Log.d("KeithXiaoY", "max range:" + sensor.getMaximumRange());
                Log.d("KeithXiaoY", "power:" + sensor.getPower());
                if (Sensor.TYPE_LIGHT == sensor.getType()) {
                    mIsContains = true; //这是一个 Boolean 值，true 代表有这个传感器、false 代表没有
                    return;
                }
            }
        }

    }

    /*  第三个参数是传感器数据更新数据的速度
      有以下四个值可选，他们的速度是递增的
      SENSOR_DELAY_UI
      SENSOR_DELAY_NORMAL
      SENSOR_DELAY_GAME
      SENSOR_DELAY_FASTEST*/
    public void registerSensor() {
        // 获得光线传感器
        if (null != mSensorManager) {
            Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            if (null != sensor && mIsContains) {
                mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
            }
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        mLightSensorUtils.registerSensor();
//        Boolean isBright = mLightSensorUtils.getBright();
//        if (null != isBright) {
//            checkTheme();
//        }
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        mLightSensorUtils.unRegisterSensor();
//    }
}
