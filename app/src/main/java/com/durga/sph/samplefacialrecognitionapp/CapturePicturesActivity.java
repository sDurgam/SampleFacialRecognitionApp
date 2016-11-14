package com.durga.sph.samplefacialrecognitionapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

import static com.durga.sph.samplefacialrecognitionapp.CapturePicturesActivity.mcount;

/**
 * Created by Durga on 11/14/16.
 */

public class CapturePicturesActivity extends Activity {

    private android.hardware.Camera camera; // camera object
    private TextView textTimeLeft; // time left field
    Timer timer;
    MyTimerTask myTimerTask;
    static int mcount = 0;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        textTimeLeft = (TextView) findViewById(R.id.textTimeLeft); // make time left object
        camera = Camera.open();
        SurfaceView view = new SurfaceView(this);

        try {
            camera.setPreviewDisplay(view.getHolder()); // feed dummy surface to surface
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        camera.startPreview();
        timer = new Timer();
        myTimerTask = new MyTimerTask(camera, jpegCallBack);
        timer.schedule(myTimerTask, 500, 500);
    }
        Camera.PictureCallback jpegCallBack = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                // set file destination and file name
                new SaveImagesAsyncTask(getApplicationContext()).execute(data);
                camera.stopPreview();
            }
        };


    class MyTimerTask extends TimerTask{
        Camera camera;
        Camera.PictureCallback callback;

        public MyTimerTask(Camera cam, Camera.PictureCallback callback)
        {
            this.camera = cam;
            this.callback = callback;
        }

        @Override
        public void run() {
            if(mcount == 10)
            {
                this.cancel();
            }
            else {
                mcount++;
                camera.startPreview();
                camera.takePicture(null, null, callback);
            }

        }
    }
}

