package com.lightsensor.lzr.lightsensor;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lightsensor.lzr.lightsensor.util.LightSensorUtils;
import com.lightsensor.lzr.lightsensor.util.OtherUtils;
import com.lightsensor.lzr.lightsensor.util.SaveUtil;

import java.io.IOException;
import java.util.ArrayList;

import jxl.write.WriteException;

public class MainActivity extends AppCompatActivity {

    private MySensorEventListener mySensorEventListener;
    private TextView tvLight;
    private TextView tvMol;
    private Button btStart;
    private Button btCali;
    private long time;
    private boolean isStarted;
    private ArrayList<String> timeList = new ArrayList<>();
    private ArrayList<String> aList = new ArrayList<>();
    private ArrayList<String> bList = new ArrayList<>();
    private ArrayList<String> xList = new ArrayList<>();
    private ArrayList<String> lightList = new ArrayList<>();
    private ArrayList<String> conList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvLight = findViewById(R.id.tv_light);
        tvMol = findViewById(R.id.tv_mol);
        btCali = findViewById(R.id.bt_cali);
        btStart = findViewById(R.id.bt_start);
        initData();
        initListener();
    }

    private void initData() {
        time = System.currentTimeMillis();
        tvLight.setText(String.format(getString(R.string.light_num), 0 + ""));
        tvMol.setText(String.format(getString(R.string.mol_num), 0 + ""));
    }

    private void initListener() {
        mySensorEventListener = new MySensorEventListener();
        btCali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CalibrationActivity.class));
            }
        });

        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isStarted) {
                    LightSensorUtils.registerSensor(MainActivity.this, mySensorEventListener);
                    btStart.setText("Stop");
                } else {
                    LightSensorUtils.unRegisterSensor(MainActivity.this, mySensorEventListener);
                    try {
                        SaveUtil.saveDataToExcel(timeList, aList, bList, xList, lightList, conList);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (WriteException e) {
                        e.printStackTrace();
                    }
                    clearData();
                    btStart.setText("Start");
                }
                isStarted = !isStarted;
            }
        });
    }

    private void clearData() {
        timeList.clear();
        aList.clear();
        bList.clear();
        xList.clear();
        lightList.clear();
        conList.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LightSensorUtils.unRegisterSensor(this, mySensorEventListener);
    }

    private class MySensorEventListener implements SensorEventListener {
        @Override
        public void onSensorChanged(final SensorEvent event) {
            if (System.currentTimeMillis() - time < 1000) {
                return;
            } else {
                time = System.currentTimeMillis();
            }
            if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                int realBright = (int) event.values[0] - GlobalData.caliLight;
                tvLight.setText(String.format(getString(R.string.light_num), realBright + ""));
                try {
                    if (!TextUtils.isEmpty(GlobalData.dataX)) {
                        realBright = Integer.parseInt(GlobalData.dataX);
                    }
                    tvMol.setText(String.format(getString(R.string.mol_num),
                            OtherUtils.formatToSave2All(realBright * GlobalData.dataA + GlobalData.dataB)) + "");

                    timeList.add(OtherUtils.getCurrentTime());
                    aList.add(String.valueOf(GlobalData.dataA));
                    bList.add(String.valueOf(GlobalData.dataB));
                    xList.add(GlobalData.dataX);
                    lightList.add(String.valueOf(realBright));
                    conList.add(String.valueOf(OtherUtils.formatToSave2All(realBright * GlobalData.dataA + GlobalData.dataB)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }
}
