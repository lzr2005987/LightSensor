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

public class MainActivity extends AppCompatActivity {

    private MySensorEventListener mySensorEventListener;
    private TextView tvLight;
    private TextView tvMol;
    private Button btStart;
    private Button btCali;

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
    }

    @Override
    public void onResume() {
        super.onResume();
        LightSensorUtils.registerSensor(this, mySensorEventListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        LightSensorUtils.unRegisterSensor(this, mySensorEventListener);
    }

    private class MySensorEventListener implements SensorEventListener {
        @Override
        public void onSensorChanged(final SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                int realBright = (int) event.values[0] - GlobalData.caliLight;
                tvLight.setText(String.format(getString(R.string.light_num), realBright + ""));
                try {
                    if (!TextUtils.isEmpty(GlobalData.dataX)) {
                        realBright = Integer.parseInt(GlobalData.dataX);
                    }
                    tvMol.setText(String.format(getString(R.string.mol_num), (realBright * GlobalData.dataA + GlobalData.dataB) + ""));
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
