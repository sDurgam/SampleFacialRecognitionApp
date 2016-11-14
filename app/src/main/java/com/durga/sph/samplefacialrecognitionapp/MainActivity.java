    package com.durga.sph.samplefacialrecognitionapp;

    import android.content.Context;
    import android.content.Intent;
    import android.graphics.Bitmap;
    import android.graphics.Camera;
    import android.media.Image;
    import android.net.Uri;
    import android.os.Bundle;
    import android.os.Environment;
    import android.provider.MediaStore;
    import android.support.design.widget.FloatingActionButton;
    import android.support.design.widget.Snackbar;
    import android.support.v4.content.FileProvider;
    import android.support.v7.app.AppCompatActivity;
    import android.support.v7.widget.Toolbar;
    import android.view.View;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.widget.Button;
    import android.widget.ImageView;

    import java.io.File;
    import java.io.IOException;
    import java.text.SimpleDateFormat;
    import java.util.Date;

    public class MainActivity extends AppCompatActivity {

        Button takePicturesBtnButton;
        private static final int REQUEST_OPEN_CAMERA = 1;
        private static final int REQUEST_TAKE_PHOTO = 2;
        private Camera mCamera;
        Context mContext;
        ImageView imageView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            mContext = getApplicationContext();
            //if(isExistsFrontCamera()) {
            //    mCamera = getCameraInstance();
                imageView = (ImageView) findViewById(R.id.imageView);
                takePicturesBtnButton = (Button) findViewById(R.id.takePicturesBtn);
                takePicturesBtnButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //open the camera intent
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if(cameraIntent.resolveActivity(getPackageManager()) != null) {
                            Uri photoURI = saveImage();
                            cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
                            cameraIntent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
                            cameraIntent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(cameraIntent, REQUEST_TAKE_PHOTO);
                        }

                        }
                });
            //}
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }



        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(resultCode == REQUEST_OPEN_CAMERA && resultCode == RESULT_OK) {

            }
            if(resultCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK)
            {
                Bundle extras = data.getExtras();
                Bitmap image = (Bitmap) extras.get("data");
                imageView.setImageBitmap(image);
            }
        }

        private Uri saveImage()
        {
            File photoFile = null;
            Uri photoURI = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI= FileProvider.getUriForFile(mContext,
                        "com.durga.sph.samplefacialrecognitionapp.fileprovider",
                        photoFile);
            }
            return photoURI;
        }

        String mCurrentPhotoPath;
        private File createImageFile() throws IOException {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = "file:" + image.getAbsolutePath();
            return image;
        }
    }
