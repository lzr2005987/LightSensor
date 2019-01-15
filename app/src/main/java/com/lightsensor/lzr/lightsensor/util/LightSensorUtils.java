package com.lightsensor.lzr.lightsensor.util;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.List;

/**
 * Created by lizheren on 2019/1/14.
 */

public class LightSensorUtils {
    private static SensorManager mSensorManager;

    private static boolean isSensorExist(Activity activity) {
        mSensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        //获取手机上支持的所有传感器
        if (mSensorManager != null) {
            List<Sensor> mList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
            for (Sensor sensor : mList) {
//                Log.d("KeithXiaoY", "名字：" + sensor.getName());
//                Log.d("KeithXiaoY", "type:" + sensor.getType());
//                Log.d("KeithXiaoY", "vendor:" + sensor.getVendor());
//                Log.d("KeithXiaoY", "version:" + sensor.getVersion());
//                Log.d("KeithXiaoY", "resolution:" + sensor.getResolution());
//                Log.d("KeithXiaoY", "max range:" + sensor.getMaximumRange());
//                Log.d("KeithXiaoY", "power:" + sensor.getPower());
                if (Sensor.TYPE_LIGHT == sensor.getType()) {
                    return true;
                }
            }
        }
        return false;
    }

    /*  第三个参数是传感器数据更新数据的速度
  有以下四个值可选，他们的速度是递增的
  SENSOR_DELAY_UI
  SENSOR_DELAY_NORMAL
  SENSOR_DELAY_GAME
  SENSOR_DELAY_FASTEST*/
    public static void registerSensor(Activity activity, SensorEventListener listener) {
        // 获得光线传感器
        if (!isSensorExist(activity)) {
            return;
        }
        if (null != mSensorManager) {
            Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            if (null != sensor) {
                mSensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_FASTEST);
            }
        }

    }

    public static void unRegisterSensor(Activity activity, SensorEventListener listener) {
        if (null != mSensorManager) {
            Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            if (null != sensor && isSensorExist(activity)) {
                mSensorManager.unregisterListener(listener);
            }
        }

    }
}
