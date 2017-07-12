package com.tristanhuang.sensorlogger;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * sensors listener
 * @author TristanHuang
 * 2017-6-28  p.m. 7:55:53
 */
public class MySensorListener implements SensorEventListener {
    private OutputStreamWriter writer;

    public MySensorListener(String dataFileName){
        super();
        try {
            writer = new OutputStreamWriter(new FileOutputStream(dataFileName, true));
        } catch (FileNotFoundException e) {
            Log.i("Text", "监听器构造器构造失败");
            e.printStackTrace();
        }
    }

    public void onSensorChanged(SensorEvent event) {
        try {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    writer.append("ACCE;"+System.currentTimeMillis()+";"+event.timestamp/1000000+";"+event.values[0]+";"+event.values[1]+";"+event.values[2]+";"+event.accuracy+";\n");
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    writer.append("GYRO;"+System.currentTimeMillis()+";"+event.timestamp/1000000+";"+event.values[0]+";"+event.values[1]+";"+event.values[2]+";"+event.accuracy+";\n");
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    writer.append("MAGN;"+System.currentTimeMillis()+";"+event.timestamp/1000000+";"+event.values[0]+";"+event.values[1]+";"+event.values[2]+";"+event.accuracy+";\n");
                    break;
                case Sensor.TYPE_PRESSURE:
                    writer.append("PRES;"+System.currentTimeMillis()+";"+event.timestamp/1000000+";"+event.values[0]+";"+event.accuracy+";\n");
                    break;
                case Sensor.TYPE_GRAVITY:
                    writer.append("GRAV;"+System.currentTimeMillis()+";"+event.timestamp/1000000+";"+event.values[0]+";"+event.values[1]+";"+event.values[2]+";"+event.accuracy+";\n");
                    break;
                case Sensor.TYPE_LINEAR_ACCELERATION:
                    writer.append("LACC;"+System.currentTimeMillis()+";"+event.timestamp/1000000+";"+event.values[0]+";"+event.values[1]+";"+event.values[2]+";"+event.accuracy+";\n");
                    break;
                case Sensor.TYPE_ROTATION_VECTOR:
                    writer.append("ROTA;"+System.currentTimeMillis()+";"+event.timestamp/1000000+";"+event.values[0]+";"+event.values[1]+";"+event.values[2]+";"+event.values[3]+";"+event.accuracy+";\n");
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            Log.i("Test", "监听器出现异常");
            e.printStackTrace();
        }
    }


    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    public void onFlushCompleted(Sensor sensor) {}

}
