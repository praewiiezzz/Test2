package com.example.chuti.test;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by chuti on 4/23/2017.
 */
public class Rotate extends Activity implements SensorEventListener {

        // define the display assembly compass picture
        private ImageView image;
        private double calibrate;
        private double Heading;
        private double heading;
        // record the compass picture angle turned
        private float currentDegree = 0f;

        // device sensor manager
        private SensorManager mSensorManager;

        TextView tvHeading;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.rotation_page);
            receiveValue();  // add
            //
            image = (ImageView) findViewById(R.id.rotateImage);

            // TextView that will tell the user what degree is he heading
            tvHeading = (TextView) findViewById(R.id.Heading);

            // initialize your android device sensor capabilities
            mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

            // Test callDegree //
            ///

            double[] positionB = {13.776092, 100.513573};
            double[] positionA= {13.776827, 100.514619};

            //an = 120; //120 degree
            //bn = 30 ; //30 degree

            //double difX = latB - latA;
            //double difY = lonB - lonA;
            double difX = positionB[0] - positionA[0];
            double difY = positionB[1] - positionA[1];

            //rotAng = Math.toDegrees(Math.atan2(difX,difY));
            double rotAng = Math.toDegrees(Math.atan2(difX, difY));
            System.out.println(rotAng);
            calDegree(positionA[0],positionB[0],positionA[1],positionB[1],rotAng);

            ///
        }

        @Override
        protected void onResume() {
            super.onResume();

            // for the system's orientation sensor registered listeners
            mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                    SensorManager.SENSOR_DELAY_GAME);
        }

        @Override
        protected void onPause() {
            super.onPause();

            // to stop the listener and save battery
            mSensorManager.unregisterListener(this);
        }

        @Override
        public void onSensorChanged(SensorEvent event) {

            // get the angle around the z-axis rotated
            float degree = Math.round(event.values[0]);
            degree = calibrateDegree(degree);
            if( degree == 360 || degree == 0)
            {
                degree = 0;
                image.setImageResource(R.drawable.counterclockwise_green);
            }
            else
                image.setImageResource(R.drawable.counterclockwise_yellow);



            tvHeading.setText(Integer.toString((int)degree) + "°");

            // create a rotation animation (reverse turn degree degrees)
            RotateAnimation ra = new RotateAnimation(
                    -currentDegree,
                    degree,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f);

            // how long the animation will take place
            ra.setDuration(210);

            // set the animation after the end of the reservation status
            ra.setFillAfter(true);

            // Start the animation
            image.startAnimation(ra);
            currentDegree = -degree;

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // not in use
        }

        public void receiveValue()
        {
            calibrate = Double.valueOf(getIntent().getStringExtra("CalibrateVal"));
            Heading = Double.valueOf(getIntent().getStringExtra("CurrentHeading"));
            Log.v("Calibrate value 2", String.valueOf(calibrate));
            Log.v("oldHeading 2", String.valueOf(Heading));
        }


    //public void calDegree(double latA, double latB,double lonA,double lonB,double an,double bn,double z)
    public void calDegree(double latA, double latB,double lonA,double lonB,double z)
    {
        double an = Heading;
        System.out.println("an"+an);
        double a = 0;
        double pi = 180;
        System.out.println("z: "+z);
        if(latA > latB && lonA > lonB)
        {
            System.out.println("a left bot, b right top");
            a = (-an + pi + pi/2 - z);
            //b = (+bn - pi/2 + z);
        }
        else if(latA > latB && lonA < lonB)
        {	System.out.println("a left bot, b right top");
            a = (-an + pi/2 + z);
            //b = (+bn - pi - pi/2 - z);
        }
        else if(latA < latB && lonA > lonB)
        {
            System.out.println("a right bot, b left top");
            a = (-an + pi + pi/2 + z);
            //b = (+bn - pi/2 - z);
        }
        else if(latA < latB && lonA < lonB)
        {	System.out.println("a right top, b left bot");
            a = (-an + pi/2 - z);
            //b = (+bn - pi - pi/2 + z);
        }

        // - is counter-clockwise, + is clockwise
        a = a%360;
        System.out.println("a "+(a));
    }

    public float calibrateDegree(float degree)
    {
        if(degree < 360)
        {
            degree = degree +(360-(float)calibrate);
            degree %=360;
        }
        else if( degree == 360)
        {
            degree = degree-(float)calibrate;
        }

        return degree;
    }
}

