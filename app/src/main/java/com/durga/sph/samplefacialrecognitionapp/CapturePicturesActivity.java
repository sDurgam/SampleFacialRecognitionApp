package com.durga.sph.samplefacialrecognitionapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
    public static int REQUEST_CAMERA_PERMISSION = 1;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        textTimeLeft = (TextView) findViewById(R.id.textTimeLeft); // make time left object
        //check for camera permission
        if (!isCameraPermission()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
        } else {
            startCamera();
        }
    }

    public boolean isCameraPermission()
    {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
            return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && isWritePermissio();
        return true;
    }

    public boolean isWritePermissio()
    {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CAMERA_PERMISSION)
        {
            if(grantResults != null && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                startCamera();
            }
            else
            {
                Toast.makeText(this, R.string.cannotstartcamera, Toast.LENGTH_LONG).show();
            }
        }
    }



    public void startCamera()
    {
        camera = Camera.open();
        setCameraInPreview();
        mcount = 0;
        timer = new Timer();
        myTimerTask = new MyTimerTask(camera, jpegCallBack);
        timer.schedule(myTimerTask, 0, 500);
    }


    public void setCameraInPreview()
    {
        SurfaceView view = new SurfaceView(this);
        try {
            SurfaceTexture st = new SurfaceTexture(MODE_PRIVATE);
            camera.setPreviewTexture(st);
            camera.startPreview();
            camera.setPreviewDisplay(view.getHolder()); // feed dummy surface to surface
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    Camera.PictureCallback jpegCallBack = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            // set file destination and file name
            new SaveImagesAsyncTask(getApplicationContext()).execute(data);
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
                    camera.startPreview();
                    camera.takePicture(null, null, callback);
                    camera.stopPreview();
                    mcount++;
            }

        }
    }
}

