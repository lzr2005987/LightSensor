package com.lightsensor.lzr.lightsensor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by lizheren on 2019/1/15.
 */

public class CalibrationActivity extends AppCompatActivity {

    private EditText etLight;
    private EditText etA;
    private EditText etB;
    private Button btReturn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);
        etLight = findViewById(R.id.et_cali_light);
        etA = findViewById(R.id.et_cali_a);
        etB = findViewById(R.id.et_cali_b);
        btReturn = findViewById(R.id.bt_return);
        initData();
        initListener();
    }

    private void initData() {
        etLight.setText(GlobalData.caliLight + "");
        etA.setText(GlobalData.dataA + "");
        etB.setText(GlobalData.dataB + "");
    }

    private void initListener() {
        btReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = etA.getText().toString();
                String b = etB.getText().toString();
                String light = etLight.getText().toString();
                try {
                    GlobalData.caliLight = Integer.parseInt(light);
                    GlobalData.dataA = Double.parseDouble(a);
                    GlobalData.dataB = Double.parseDouble(b);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
    }
}