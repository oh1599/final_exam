package com.example.igx.problem1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener  {

    SensorManager mSensorManager;
    Sensor mAccelerometer;
    Sensor mGyro;
    Sensor mTemperature;
    Sensor mProx;
    Intent intent;

    int key = 0;
    double latitude=0;
    double longitude=0;

    EditText edit_phoneNumber;

    TextView text_selectedData;
    TextView text_selectedType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_getLocation = (Button) findViewById(R.id.btn_getLocation);
        Button btn_getSensors = (Button) findViewById(R.id.btn_getSensors);
        Button btn_sendMessage = (Button) findViewById(R.id.btn_sendMessage);

        text_selectedData = (TextView) findViewById(R.id.text_selectedData);
        text_selectedType = (TextView) findViewById(R.id.text_selectedType);
        edit_phoneNumber = (EditText) findViewById(R.id.edit_phoneNumber);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mTemperature = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        mProx = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);


        btn_getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_selectedType.setText("LOCATION");
                key = 1;
                if(key==1) {
                    text_selectedData.setText("Latitude 71.2132  Longitude 65.12331");
                }
                startLocationService();
            }
        });

        btn_getSensors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_selectedType.setText("SENSOR");
                key = 2;
            }
        });

        btn_sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNum = edit_phoneNumber.getText().toString();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mGyro, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mTemperature, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mProx, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (key == 2) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_PROXIMITY:
                    text_selectedData.setText("Distance는 : " + event.values[0]);
                    break;
                case Sensor.TYPE_AMBIENT_TEMPERATURE:
                    text_selectedData.setText("온도는 : " + event.values[0] + "C 이다.");
                    break;
                case Sensor.TYPE_ACCELEROMETER:
                    text_selectedData.setText("Acceleration force along the x axis (including gravity). : " + event.values[0]);
                    text_selectedData.setText("Acceleration force along the y axis (including gravity). : " + event.values[1]);
                    text_selectedData.setText("Acceleration force along the z axis (including gravity). : " + event.values[2]);//이부분만 출력된다ㅡ
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    text_selectedData.setText("Rate of rotation around the x axis. : " + event.values[0]);
                    text_selectedData.setText("Rate of rotation around the y axis. : " + event.values[1]);
                    text_selectedData.setText("Rate of rotation around the z axis. : " + event.values[2]);
                    break;
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    private class GPSListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            //위치정보 리스트에 저장
            latitude = location.getLatitude();
            longitude = location.getLongitude();

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    }

    private void startLocationService() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        GPSListener gpsListener = new GPSListener();


        try {
            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0, 0, gpsListener);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            manager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0, 0, gpsListener);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

}

