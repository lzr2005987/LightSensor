package com.lightsensor.lzr.lightsensor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by lizheren on 2019/1/15.
 */

public class CalibrationActivity extends AppCompatActivity {

    private EditText etLight;
    private EditText etA;
    private EditText etB;
    private EditText etX;
    private Button btReturn;
    private EditText etName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);
        etLight = findViewById(R.id.et_cali_light);
        etA = findViewById(R.id.et_cali_a);
        etB = findViewById(R.id.et_cali_b);
        etX = findViewById(R.id.et_cali_x);
        etName = findViewById(R.id.et_name);
        btReturn = findViewById(R.id.bt_return);
        initData();
        initListener();
    }

    private void initData() {
        etLight.setText(GlobalData.caliLight + "");
        etName.setText(GlobalData.name);
        etA.setText(GlobalData.dataA + "");
        etB.setText(GlobalData.dataB + "");
        etX.setText(GlobalData.dataX);
    }

    private void initListener() {
        btReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String a = etA.getText().toString();
        String b = etB.getText().toString();
        String x = etX.getText().toString();
        String name = etName.getText().toString();
        String light = etLight.getText().toString();
        try {
            GlobalData.caliLight = Integer.parseInt(light);
            GlobalData.dataA = Double.parseDouble(a);
            GlobalData.dataB = Double.parseDouble(b);
            if (!TextUtils.isEmpty(x)) {
                GlobalData.dataX = x;
            } else {
                GlobalData.dataX = "";
            }

            if (!TextUtils.isEmpty(name)) {
                GlobalData.name = name;
            } else {
                GlobalData.name = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
