# SampleFacialRecognitionApp

I am using timertask to capture picture and each images is saved locally using AsyncTask. We can acheive this using Service also.

For storage I did research and I see that FileProvider is the best option. For now I am storing in external directory as storing 10 images in-memory within the app can lead to low resources and force kill the app.

References: 
https://developer.android.com/reference/android/hardware/Camera.html
https://developer.android.com/reference/android/support/v4/content/FileProvider.html
http://mobiledevtuts.com/android/android-sdk-how-to-make-an-automatic-snapshot-android-app/
https://developer.android.com/reference/android/os/AsyncTask.html
https://www.linux.com/learn/how-call-camera-android-apps-part-2-capture-and-store-photos
