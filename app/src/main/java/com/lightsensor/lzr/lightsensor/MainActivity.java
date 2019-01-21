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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lightsensor.lzr.easypermission.EasyPermission;
import com.lightsensor.lzr.easypermission.GrantResult;
import com.lightsensor.lzr.easypermission.NextAction;
import com.lightsensor.lzr.easypermission.NextActionType;
import com.lightsensor.lzr.easypermission.Permission;
import com.lightsensor.lzr.easypermission.PermissionRequestListener;
import com.lightsensor.lzr.easypermission.RequestPermissionRationalListener;
import com.lightsensor.lzr.lightsensor.util.LightSensorUtils;
import com.lightsensor.lzr.lightsensor.util.OtherUtils;
import com.lightsensor.lzr.lightsensor.util.SaveUtil;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private MySensorEventListener mySensorEventListener;
    private TextView tvLight;
    private TextView tvMol;
    private Button btStart;
    private Button btCali;
    private EditText etTimer;
    private long time;
    private boolean isStarted;
    private Timer timerTask;
    private static String reactionTime;

    private ArrayList<String> timeList = new ArrayList<>();
    private ArrayList<String> nameList = new ArrayList<>();
    private ArrayList<String> aList = new ArrayList<>();
    private ArrayList<String> bList = new ArrayList<>();
    private ArrayList<String> xList = new ArrayList<>();
    private ArrayList<String> caliList = new ArrayList<>();
    private ArrayList<String> lightOrgList = new ArrayList<>();
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
        etTimer = findViewById(R.id.et_timer);
        initData();
        initListener();

        EasyPermission.with(this)
                .addPermissions(Permission.Group.STORAGE)
                .addRequestPermissionRationaleHandler(Permission.WRITE_EXTERNAL_STORAGE, new RequestPermissionRationalListener() {
                    @Override
                    public void onRequestPermissionRational(String permission, boolean requestPermissionRationaleResult, NextAction nextAction) {
                        nextAction.next(NextActionType.NEXT);
                    }
                })
                .request(new PermissionRequestListener() {
                    @Override
                    public void onGrant(Map<String, GrantResult> result) {

                    }

                    @Override
                    public void onCancel(String stopPermission) {
                        Toast.makeText(MainActivity.this, "请开启权限", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
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
                    if (!TextUtils.isEmpty(etTimer.getText().toString())) {
                        reactionTime = etTimer.getText().toString();
                        startTimer();
                    }
                    btStart.setText("Stop");
                } else {
                    stopAndRecord();
                }
                isStarted = !isStarted;
            }
        });
    }

    private void startTimer() {
        timerTask = new Timer();
        etTimer.setEnabled(false);
        timerTask.schedule(new TimerTask() {
            @Override
            public void run() {
                final String timer = etTimer.getText().toString();
                if (Integer.parseInt(timer) <= 0) {
                    stopAndRecord();
                    isStarted = false;
                } else {
                    etTimer.post(new Runnable() {
                        @Override
                        public void run() {
                            etTimer.setText((Integer.parseInt(timer) - 1) + "");
                        }
                    });
                }
            }
        }, 0, 1000);
    }

    private void stopAndRecord() {
        try {
            if (!TextUtils.isEmpty(reactionTime)) {
                etTimer.post(new Runnable() {
                    @Override
                    public void run() {
                        etTimer.setText(reactionTime);
                    }
                });
            }
            SaveUtil.saveDataToExcel(timeList, nameList, aList, bList, xList, caliList, lightOrgList, lightList, conList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        clearData();
        if (timerTask != null) {
            timerTask.cancel();
        }
        btStart.post(new Runnable() {
            @Override
            public void run() {
                btStart.setText("Start");
            }
        });

        etTimer.post(new Runnable() {
            @Override
            public void run() {
                etTimer.setEnabled(true);
            }
        });
    }

    private void clearData() {
        timeList.clear();
        nameList.clear();
        aList.clear();
        bList.clear();
        xList.clear();
        caliList.clear();
        lightOrgList.clear();
        lightList.clear();
        conList.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LightSensorUtils.registerSensor(this, mySensorEventListener);
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
                if (!isStarted) {
                    return;
                }
                try {
                    if (!TextUtils.isEmpty(GlobalData.dataX)) {
                        realBright = Integer.parseInt(GlobalData.dataX);
                    }
                    tvMol.setText(String.format(getString(R.string.mol_num),
                            OtherUtils.formatToSave2All(realBright * GlobalData.dataA + GlobalData.dataB)) + "");

                    timeList.add(OtherUtils.getCurrentTime());
                    nameList.add(TextUtils.isEmpty(GlobalData.name) ? "" : GlobalData.name);
                    aList.add(String.valueOf(GlobalData.dataA));
                    bList.add(String.valueOf(GlobalData.dataB));
                    xList.add(GlobalData.dataX);
                    lightOrgList.add(String.valueOf((int) event.values[0]));
                    caliList.add(String.valueOf(GlobalData.caliLight));
                    lightList.add(String.valueOf((int) event.values[0] - GlobalData.caliLight));
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
