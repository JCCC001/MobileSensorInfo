package com.tristanhuang.sensorlogger;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;

/**
 * The 'SensorInfo' program stores information from Smartphone/Tablet internal sensors
 * (Accelerometers, Gyroscopes, Magnetometers, Pressure, Gravity, LinearAccelerometers, Rotation)
 * @author TristanHuang
 * 2017-6-28  p.m. 5:25:28
 */
public class MainActivity extends Activity {
    private TextView tv_info;
    private ToggleButton bt_log;
    private Button bt_mark;

    SensorManager smManager;
    SensorEventListener sListener2;

    /** Accelerometer sensor. */
    private Sensor acceSensor;
    /** Gyroscope sensor. */
    private Sensor gyroSensor;
    /** Magnetometer sensor. */
    private Sensor magnSensor;
    /** Pressure sensor. */
    private Sensor presSensor;
    /** 合成：gravity sensor. */
    private Sensor gravSensor;
    /** 合成：(Linear) Accl sensor. */
    private Sensor linearAcceSensor;
    /** 合成： Rotation sensor. */
    private Sensor rotaSensor;

    private String dataFileNameString;

    private int markCount;
    private int frequency;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        initViews();
        appendSensorInfo();
        initSensors();

        frequency = 20000;

        //建立根目录下的logfiles文件夹
        String dataDirString = Toolbox.getSDPath().toString()+File.separator+"logfiles";
        File dataDirFile = new File(dataDirString);
        dataDirFile.mkdir();

        bt_log.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    markCount = 0;
                    dataFileNameString = Toolbox.getFileNameString();
                    try {
                        writeExperimentInfo(dataFileNameString);
                    } catch (IOException e) {
                        Log.i("Test", "写入说明文件失败");
                        e.printStackTrace();
                    }
                    sListener2 = new MySensorListener(dataFileNameString);
                    smManager.registerListener(sListener2, acceSensor, frequency);
                    smManager.registerListener(sListener2, gyroSensor, frequency);
                    smManager.registerListener(sListener2, magnSensor, frequency);
                    smManager.registerListener(sListener2, presSensor, frequency);
                    smManager.registerListener(sListener2, gravSensor, frequency);
                    smManager.registerListener(sListener2, linearAcceSensor, frequency);
                    smManager.registerListener(sListener2, rotaSensor, frequency);
                    Log.i("Test", "成功注册监听器");
                    bt_mark.setEnabled(true);
                } else {
                    bt_mark.setEnabled(false);
                    smManager.unregisterListener(sListener2);
                    Log.i("Test", "关闭监听");
                    bt_mark.setBackgroundColor(Color.GRAY);
                }
            }
        });

        bt_mark.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                try {
                    OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(dataFileNameString,true));
                    osw.append("\n% **********************POSI; "+markCount+"; "+System.currentTimeMillis()
                            +"**********************\n");
                    osw.close();
                } catch (FileNotFoundException e) {
                    Log.i("Test", "没有找到对应的数据文件");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.i("Test", "标记POSI写入出现问题");
                    e.printStackTrace();
                }
                bt_mark.setText("Mark Count : "+markCount);
                bt_mark.setBackgroundColor(Color.YELLOW);
                markCount++;
            }
        });

    }

    private void writeExperimentInfo(String fileNameString) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileNameString, true);
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        osw.append("% LogFile created by the 'SensorInfo' App for Android.\n");
        osw.append("% Date of creation: "+new Date()+"\n");
        osw.append("% Developed by Tristan in ISMC research group at ZJU, China.\n");
        osw.append("% The 'SensorInfo' program stores information from Smartphone/Tablet internal sensors " +
                "(Accelerometers, Gyroscopes, Magnetometers, Pressure, Gravity, LinearAccelerometers, Rotation) .\n");
        osw.append("% \n");
        osw.append("% The model of device:  "+android.os.Build.MODEL+"\n");
        osw.append("% The Manufacturer:     "+android.os.Build.MANUFACTURER+"\n");
        osw.append("% \n");
        osw.append("% LogFile Data format:\n");
        osw.append("% Accelerometer data: 	'ACCE;AppTimestamp(s);SensorTimestamp(s);Acc_X(m/s^2);Acc_Y(m/s^2);Acc_Z(m/s^2);Accuracy(integer)'\n");
        osw.append("% Gyroscope data:     	'GYRO;AppTimestamp(s);SensorTimestamp(s);Gyr_X(rad/s);Gyr_Y(rad/s);Gyr_Z(rad/s);Accuracy(integer)'\n");
        osw.append("% Magnetometer data:  	'MAGN;AppTimestamp(s);SensorTimestamp(s);Mag_X(uT);;Mag_Y(uT);Mag_Z(uT);Accuracy(integer)'\n");
        osw.append("% Pressure data:      	'PRES;AppTimestamp(s);SensorTimestamp(s);Pres(mbar);Accuracy(integer)'\n");
        osw.append("% Gravity data:     	'GRAV;AppTimestamp(s);SensorTimestamp(s);Gra_X(m/s^2);Gra_Y(m/s^2);Gra_Z(m/s^2);Accuracy(integer)'\n");
        osw.append("% LinearAcc data:   	'LACC;AppTimestamp(s);SensorTimestamp(s);Lacc_X(m/s^2);Lacc_Y(m/s^2);Lacc_Z(m/s^2);Accuracy(integer)'\n");
        osw.append("% \n");
        osw.append("% The info of Sensors:\n");
        if (!(acceSensor == null)) {
            osw.append("% Accelerometer Sensor:\n");
            osw.append("% 			Name:       " + acceSensor.getName() + "\n");
            osw.append("% 			Vendor:     " + acceSensor.getVendor() + "\n");
            osw.append("% 			Power:      " + acceSensor.getPower() + "\n");
            osw.append("% 			MaxRange:   " + acceSensor.getMaximumRange() + "\n");
            osw.append("% 			Resolution: " + acceSensor.getResolution() + "\n");
        }
        if (!(gyroSensor == null)) {
            osw.append("% Gyroscope Sensor:\n");
            osw.append("% 			Name:       " + gyroSensor.getName() + "\n");
            osw.append("% 			Vendor:     " + gyroSensor.getVendor() + "\n");
            osw.append("% 			Power:      " + gyroSensor.getPower() + "\n");
            osw.append("% 			MaxRange:   " + gyroSensor.getMaximumRange() + "\n");
            osw.append("% 			Resolution: " + gyroSensor.getResolution() + "\n");
        }
        if (!(magnSensor == null)) {
            osw.append("% Magnetometer Sensor:\n");
            osw.append("% 			Name:       " + magnSensor.getName() + "\n");
            osw.append("% 			Vendor:     " + magnSensor.getVendor() + "\n");
            osw.append("% 			Power:      " + magnSensor.getPower() + "\n");
            osw.append("% 			MaxRange:   " + magnSensor.getMaximumRange() + "\n");
            osw.append("% 			Resolution: " + magnSensor.getResolution() + "\n");
        }
        if (!(presSensor == null)) {
            osw.append("% Pressure Sensor:\n");
            osw.append("% 			Name:       " + presSensor.getName() + "\n");
            osw.append("% 			Vendor:     " + presSensor.getVendor() + "\n");
            osw.append("% 			Power:      " + presSensor.getPower() + "\n");
            osw.append("% 			MaxRange:   " + presSensor.getMaximumRange() + "\n");
            osw.append("% 			Resolution: " + presSensor.getResolution() + "\n");
        }
        if (!(gravSensor == null)) {
            osw.append("% Gravity Sensor:\n");
            osw.append("% 			MaxRange:   " + gravSensor.getMaximumRange() + "\n");
            osw.append("% 			Resolution: " + gravSensor.getResolution() + "\n");
        }
        if (!(linearAcceSensor == null)) {
            osw.append("% LinearAcc Sensor:\n");
            osw.append("% 			MaxRange:   " + linearAcceSensor.getMaximumRange() + "\n");
            osw.append("% 			Resolution: " + linearAcceSensor.getResolution() + "\n");
        }
        if (!(rotaSensor == null)) {
            osw.append("% Rotation Sensor:\n");
            osw.append("% 			MaxRange:   " + rotaSensor.getMaximumRange() + "\n");
            osw.append("% 			Resolution: " + rotaSensor.getResolution() + "\n");
        }
        osw.append("\n");
        osw.close();
        fos.close();
    }


    /** initial views. */
    private void initViews() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);  //防止休眠
        tv_info = (TextView) findViewById(R.id.tv_info);
        tv_info.setMovementMethod(ScrollingMovementMethod.getInstance());
        bt_log = (ToggleButton) findViewById(R.id.bt_log);
        bt_mark = (Button) findViewById(R.id.bt_mark);
        bt_mark.setEnabled(false);
    }

    /** initial the content of 'tv_info'-TextView. */
    private void appendSensorInfo() {
        tv_info.append("Model:\n"+"           "+android.os.Build.MODEL+"\n");
        tv_info.append("Manufacturer:\n"+"           "+android.os.Build.MANUFACTURER+"\n");
        tv_info.append("The list of sensor in this device: \n");
        List<Sensor> sensors  = smManager.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sensor : sensors) {
            tv_info.append("           "+sensor.getName()+"\n");
        }
    }

    /** initial the references to variables of different sensors */
    private void initSensors(){
        acceSensor = smManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroSensor = smManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        magnSensor = smManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        presSensor = smManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        gravSensor = smManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        linearAcceSensor = smManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        rotaSensor = smManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    }


    @Override
    protected void onDestroy() {
        //TODO 注销监听器的注册
    };

}
