package com.durga.sph.samplefacialrecognitionapp;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Durga on 11/14/16.
 */

public class SaveImagesAsyncTask extends AsyncTask<byte[], Void, Uri>{

    Context mcontext;
    SaveImagesAsyncTask(Context ctx)
    {
        mcontext = ctx;
    }


    private Uri saveImage(byte[] data)
    {
        File photoFile = null;
        Uri photoURI = null;
        try {
            photoFile = createImageFile(data);
        } catch (IOException ex) {
            // Error occurred while creating the File
        }
        // Continue only if the File was successfully created
        if (photoFile != null && mcontext != null) {
            photoURI= FileProvider.getUriForFile(mcontext,
                    "com.durga.sph.samplefacialrecognitionapp.fileprovider",
                    photoFile);
        }
        return photoURI;
    }

    String mCurrentPhotoPath;
    private File createImageFile(byte[] data) throws IOException {
        // Create an image file name
        File image = null;
        if (mcontext != null) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = mcontext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
            try {
                FileOutputStream outputStream = new FileOutputStream(imageFileName);
                outputStream.write(data);
                outputStream.close();
            }
            catch (FileNotFoundException fex)
            {

            }

            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        }
        return image;
    }

    @Override
    protected Uri doInBackground(byte[]... bytes) {
        return saveImage(bytes[0]);
    }
}

