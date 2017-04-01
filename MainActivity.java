package com.rangermerah.recyletor;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kosalgeek.android.photoutil.CameraPhoto;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class MainActivity extends AppCompatActivity implements ProgressRequestBody.UploadCallbacks {


    public static final int REQUEST_CAMERA = 212;

    private final String TAG = this.getClass().getName();

    ImageButton snapPhoto;

    String checkWelcome;
    private Service service;
    private Call<com.rangermerah.recyletor.Response> call;
    private Retrofit retrofit;
    CameraPhoto cameraPhoto;
    private Uri filePhotoBangsat;

    boolean showMinMax = true;
    boolean candak = false;
    MaterialDialog dialogUpload;
    private final static int ALL_PERMISSIONS_RESULT = 107;
    String[] permissions = {"android.permission.CAMERA", "android.permission.INTERNET", "android.permission.WRITE_EXTERNAL_STORAGE","android.permission.READ_EXTERNAL_STORAGE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar;


        setContentView(R.layout.activity_main);


        if(isReadStorageAllowed()){
            //If permission is already having then showing the toast
            //Toast.makeText(MainActivity.this,"You already have the permission",Toast.LENGTH_LONG).show();
            //Existing the method with return
            //return;
        }
        else {

            if (Build.VERSION.SDK_INT > 22) {
                ActivityCompat.requestPermissions(this, permissions, ALL_PERMISSIONS_RESULT);
                Log.e("Android ","M");
            }

        }



        toolbar = (Toolbar) findViewById(R.id.toolbar_top);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.hijauDasar));
        toolbar.setTitle("Face Detector");

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title("Uploading")
                .cancelable(false)
                .progress(false, 100, showMinMax);

        dialogUpload = builder.build();


        snapPhoto = (ImageButton) findViewById(R.id.launchCamera);
        //result_photo = (ImageView)findViewById(R.id.imageView);

        cameraPhoto = new CameraPhoto(getApplicationContext());


        Gson gsonRegister = new GsonBuilder().create();
        OkHttpClient client = new MyLog().getLog();
        retrofit = new Retrofit.Builder()
                .baseUrl("https://myfvckingann.mybluemix.net/")
                .addConverterFactory(GsonConverterFactory.create(gsonRegister))
                .client(client)
                .build();



        if (!hasCamera()) {
            // snapPhoto.setEnabled(false);
        }


    }

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == ALL_PERMISSIONS_RESULT) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // save file

            } else {
                Toast.makeText(getApplicationContext(), "PERMISSION_DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //We are calling this method to check the permission status
    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this,  Manifest.permission.CAMERA);
        //int result = ContextCompat.checkSelfPermission(this,  Manifest.permission.READ_EXTERNAL_STORAGE);
        Log.e("Permission ",""+result);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    public boolean hasCamera() {

        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    public void launchCamera(View v) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_CAMERA);

    }

    public void requestFail() {
        snapPhoto.setEnabled(true);
        dialogUpload.dismiss();
        Toast.makeText(getApplicationContext(), "Image process has failed", Toast.LENGTH_SHORT).show();
    }

    public void startResult(View view) {


    }




    public void finalResult2(String result, String recycle, String[] tag, Bitmap photobitmap){
        Intent intent = new Intent(this, Result.class);
        intent.putExtra("Result", result);
        intent.putExtra("recycle", recycle);
        intent.putExtra("tag", tag);
        intent.putExtra("bitmap", photobitmap);
        startActivity(intent);

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        snapPhoto.setEnabled(false);


        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            final String photoPath = cameraPhoto.getPhotoPath();
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Uri tempUri = getImageUri(getApplicationContext(), photo);

            //final String photoPath = "/storage/emulated/0/Download/images-3.jpg";
            dialogUpload.show();


            File file = new File(getRealPathFromURI(tempUri));
            filePhotoBangsat = Uri.fromFile(file);


//            Log.e("rotate ",""+getRotation(photoPath));
//            rotatebitmap(photoPath, getRotation(photoPath));




            //RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);

            long sizeFile = file.length()/1024;
            Log.e("Image Size 1 " ,""+sizeFile +" KB");

            int sizeDefault = 1000;
            int size = (int) sizeFile;

            int percent = (int)((float)sizeDefault/size*100);


            if (size > 1000){


                Log.e("Compress Image ",":"+percent+"%");
                compressbitmap(photoPath,percent);

            }



            Log.e("Image Size 2 " ,""+sizeFile +" KB");


            final ProgressRequestBody reqFile = new ProgressRequestBody(file, this);

            MultipartBody.Part body = MultipartBody.Part.createFormData("images_file", file.getName(), reqFile);
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");


            service = retrofit.create(Service.class);
            call = service.submitImage(body, name);

            Random rand = new Random();

            int n = rand.nextInt(12) + 1;

            dialogUpload.incrementProgress(n);



            call.enqueue(new Callback<com.rangermerah.recyletor.Response>() {

                @Override
                public void onResponse(Call<com.rangermerah.recyletor.Response> call, Response<com.rangermerah.recyletor.Response> response) {


                    int status = response.code();

                    if (status == 200) {

                        String tempResult = String.valueOf(response.body().getResult());
                        float acuracy=response.body().getResultscore();
                        String result = new String();
                        String gender;
                        String acuracyResult = new String();

                        if (tempResult != null){
                            if(tempResult.equals("asiatenggara")){
                                result = "Indonesia/Malaysia";
                            }
                            else if(tempResult.equals("asia")){
                                result= "Japan/Korea";
                            }
                            else if(tempResult.equals("european")){
                                result="European/American";
                            }

                            if (acuracy>0.5){
                                acuracyResult = "Strong";
                            }
                            else {
                                acuracyResult = "Weak";
                            }
                        }
                        else {
                            result = "Unclassify";
                        }

                        List<Integer> tempUmur = response.body().getAge();
                        String rataUmur = "";
                        if (tempUmur != null){
                            int[] umur= new int[2];
                            umur[0]= tempUmur.get(0);
                            umur[1]= tempUmur.get(1);
                            rataUmur = String.valueOf((umur[0]+umur[1]/2));
                            gender = response.body().getGender();
                        }
                        else {
                            rataUmur = "Unclassify";
                            gender = "Unclassify";
                        }

                        List<String> tag = response.body().getTag();
                        int tagsize = tag.size();
                        String[] hashtag = new String[tagsize];
                        for (int i=0; i<tagsize;i++){
                            hashtag[i] = tag.get(i);
                        }


//                        String resultImage = String.valueOf(response.body().getResult());
//                        String imageUpload = String.valueOf(response.body().getImage());
//                        String statusUpload = String.valueOf(response.body().getStatus());
//                        String success = String.valueOf(response.body().getSuccess());


                        Log.e("Networking ", "" + status);
//                        Log.e("Upload Status ", "" + statusUpload);
//                        Log.e("Message ", "" + success);
//                        Log.e("Recycle ", "" + recycle);
//                        Log.e("image URL ", "" + imageUpload);
//                        Log.e("Result Image ", "" + resultImage);





                        dialogUpload.incrementProgress(100);



                            //Toast.makeText(getApplicationContext(), " " + response.body().toString(), Toast.LENGTH_LONG).show();
                            snapPhoto.setEnabled(true);


                            dialogUpload.setProgress(0);
                            dialogUpload.dismiss();

                            //                                Bitmap bitmap_photo = ImageLoader.init().from(photoPath).getBitmap();
                            // finalResult2(resultImage,recycle,response.body().getTagOthers(), bitmap_photo);
                            finalResult(result, hashtag, filePhotoBangsat, gender, rataUmur, acuracyResult);


                        } else {
                        requestFail();

                    }


                }

                @Override
                public void onFailure(Call<com.rangermerah.recyletor.Response> call, Throwable t) {
                    t.printStackTrace();
                    requestFail();

                }
            });



            /*
            try {
                Bitmap bitmap_photo = ImageLoader.init().from(photoPath).getBitmap();
                result_photo.setImageBitmap(bitmap_photo);

            }catch (FileNotFoundException e){
                Toast.makeText(getApplicationContext(), "Load image wrong",Toast.LENGTH_SHORT).show();

            }
            */


            /*
            Bundle extras = data.getExtras();
            Bitmap bitmap_photo = (Bitmap) extras.get("data");
            result_photo.setImageBitmap(bitmap_photo);
            */
        } else if (resultCode == Activity.RESULT_CANCELED) {
            snapPhoto.setEnabled(true);
        }


    }


    public void finalResult(String result, String[] tag, Uri photobitmap, String gender, String umur, String acuracy){
        Intent intent = new Intent(this, Result.class);
        intent.putExtra("Acuracy", acuracy);
        intent.putExtra("Umur", umur);
        intent.putExtra("Result", result);
        intent.putExtra("Gender", gender);
        //intent.putExtra("recycle", recycle);
        intent.putExtra("tag", tag);
        intent.putExtra("bitmap", photobitmap);
        startActivity(intent);

    }

    private File compressbitmap(String photoPath, int size) {

        File file = new File(photoPath);
        OutputStream outStream = null;
        try {
            // make a new bitmap from your file
            Bitmap bmp = BitmapFactory.decodeFile(photoPath);
            outStream = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, size, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("file", "" + file);
        return file;

    }

    @Override
    public void onProgressUpdate(final int percentage) {

        //Log.e("Progress ",""+percentage+"%");

        int a = percentage % 10;
        if (a == 0) {

            if (percentage % 2 == 0) {

               // Log.e("Progress ", "" + percentage + "%");

               // Log.e("Current ",""+dialogUpload.getCurrentProgress());
                if (dialogUpload.getCurrentProgress() < percentage){
                   // Log.e("Current ",""+dialogUpload.getCurrentProgress()+": Current "+percentage);
                    dialogUpload.incrementProgress(1);
                    if (dialogUpload.getCurrentProgress() == percentage){
                        candak = true;

                    }


                }





            }
        }
        if (candak == true) {
            dialogUpload.incrementProgress(10);
            candak = false;

        }

        if (dialogUpload.getCurrentProgress() > 75) {
            dialogUpload.setTitle("Processing Image");
          //  dialogUpload.incrementProgress(1);

        }


    }

    @Override
    public void onError() {
        Log.e("Rampung ", "Error");
    }

    @Override
    public void onFinish() {
        dialogUpload.dismiss();
        Log.e("Rampung ", "OK");


    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }


    private File rotatebitmap(String photoPath, float size) {

        File file = new File(photoPath);
        OutputStream outStream = null;
        try {
            // make a new bitmap from your file
            Bitmap bmp = BitmapFactory.decodeFile(photoPath);
            outStream = new FileOutputStream(file);
            Matrix matrix = new Matrix();
            matrix.postRotate(size);
            bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return file;

    }

    private int getRotation(String photoPath)
    {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(photoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        int rotationInDegrees = exifToDegrees(rotation);
        return rotationInDegrees;
    }





}
