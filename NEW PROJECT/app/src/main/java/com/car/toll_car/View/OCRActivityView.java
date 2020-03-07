package com.car.toll_car.View;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.car.toll_car.R;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint("Registered")
public class OCRActivityView extends AppCompatActivity {

    ImageView imageView;
    LinearLayout linearLayout;
    private static  final  int CAMERA_REQUEST_CODE  = 200;
    private static  final  int STORAGE_REQUEST_CODE  = 400;
    private static  final  int IMAGE_PICK_GALLERY_CODE  = 1000;
    private static  final  int IMAGE_PICK_CAMERA_CODE  = 1001;
    String[] cameraPermission;
    String[] storagePermission;
    Uri imageUri;
    final static String TAG = "OCRActivityView";
    String result;
    Button submit;
    EditText oner_et;
    EditText vehicle_number_et, chesis_number_et;
    private String mVehicle= null;


     String[] mVehicleClassList = {"MOTOR CYCLE"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_ocr_view);

         setTitle("ETC Toll Plaza");

         oner_et = findViewById(R.id.woner_name);
         vehicle_number_et = findViewById(R.id.vehicle_number);
         chesis_number_et = findViewById(R.id.chasis_number);

        submit = findViewById(R.id.submite_btn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean connection =  isNetworkConnected();
                Log.e("Submit ", String.valueOf(connection));
                boolean internet = internetConnectionAvailable(3);
                Log.e("Internet ", String.valueOf(internet));

            }
        });
        imageView = findViewById(R.id.imageView);
        linearLayout =findViewById(R.id.camera_layout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageDialog();
            }
        });

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageDialog();
            }
        });

    }

    private void showImageDialog() {
        String[] items = {"Camera", "Gallery"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Select Image");
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which ==0){
                    if (!checkCamerapermission()){
                        requestCameraPermission();
                    }else {
                        pickCamera();
                    }
                }
                if (which ==1){
                    if (!checkStoragepermission()){
                        requestStoragePermission();
                    }else {
                        pickGalary();
                    }
                }
            }
        });
        dialog.create().show();

    }

    private void pickGalary() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);

    }

    private void pickCamera() {
        Log.e(TAG, "pick camera");
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "NewPIC");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image to text");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraInternt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraInternt.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraInternt, IMAGE_PICK_CAMERA_CODE);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkStoragepermission() {

        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    private boolean checkCamerapermission() {

        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUEST_CODE:
                if (grantResults.length>0){
                boolean cameraAccepted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                boolean writeStorageAccepted = grantResults[0]== PackageManager.PERMISSION_GRANTED;

                if (cameraAccepted && writeStorageAccepted){
                    pickCamera();
                }else {
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
            }

                break;
            case  STORAGE_REQUEST_CODE:
                if (grantResults.length>0){
                boolean writeStorageAccepted = grantResults[0]== PackageManager.PERMISSION_GRANTED;

                if (writeStorageAccepted){
                    pickGalary();
                }else {
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
            }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

      //  if (requestCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                //get gallery image and crop
                assert data != null;
                if (!(data.getData() ==null)){

                    CropImage.activity(data.getData()).setGuidelines(CropImageView.Guidelines.ON).start(this);
                }
            }

            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                //get image from camera and crop
                CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON).start(this);
            }

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    assert result != null;
                    Uri resulturi = result.getUri();
                    imageView.setImageURI(resulturi);
                    Log.e(TAG, "Imageview Set");

                    //get drable bitmap for text recognition
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                    if (!recognizer.isOperational()) {
                        Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "Frame Set");

                        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                        SparseArray<TextBlock> items = recognizer.detect(frame);
                        StringBuilder sb = new StringBuilder();
                        ArrayList<String> list = new ArrayList<>();

                        for (int i = 0; i < items.size(); i++) {        //

                            TextBlock myItem = items.valueAt(i);
                            sb.append(myItem.getValue());
                            sb.append("\n");
                            Log.e("Get text ", myItem.getValue());
                            list.add(myItem.getValue());
                        }
                        isContaintWord(list, "Vehicle Description");//take Vehicle classification

                        //isContaintWordLoad(list, "Unladen");//take Vehicle weight without unload
                        //isContaintWordUnload(list, "Laden");//take Vehicle weight with unload
                    }

                } else {
                    Toast.makeText(this, "Result not ok", Toast.LENGTH_SHORT).show();
                }
            }

    }

    private void isContaintWordUnload(ArrayList<String> list, String laden) {   // vehicle weight detect

        for (int i=0; list.size()>i;i++){
            String fullString = list.get(i);

            // Log.e("FullString", fullString);
            String pattern = "\\b"+laden+"\\b";
            Pattern p=Pattern.compile(pattern);
            Matcher m=p.matcher(fullString);
            boolean b =m.find();
            Log.e("match laden", String.valueOf(b));
            if (b==true){
                Log.e("FullString laden", fullString);

                String[] phones = fullString.split("\\s+");
                Log.e("Array ", Arrays.toString(phones));

                break;
            }
        }
    }

    private void isContaintWordLoad(ArrayList<String> list, String unladen) {    //vehicle weight  detect

        for (int i=0; list.size()>i;i++){
            String fullString = list.get(i);

            // Log.e("FullString", fullString);
            String pattern = "\\b"+unladen+"\\b";
            Pattern p=Pattern.compile(pattern);
            Matcher m=p.matcher(fullString);
            boolean b =m.find();
            Log.e("match UNladen", String.valueOf(b));
            if (b==true){
                Log.e("FullString UNladen", fullString);

                String[] phones = fullString.split("\\s+");
                Log.e("Array ", Arrays.toString(phones));

                break;
            }
        }


    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    private boolean internetConnectionAvailable(int timeOut) {
        InetAddress inetAddress = null;
        try {
            Future<InetAddress> future = Executors.newSingleThreadExecutor().submit(new Callable<InetAddress>() {
                @Override
                public InetAddress call() {
                    try {
                        return InetAddress.getByName("google.com");
                    } catch (UnknownHostException e) {
                        return null;
                    }
                }
            });
            inetAddress = future.get(timeOut, TimeUnit.MILLISECONDS);
            future.cancel(true);
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        } catch (TimeoutException e) {
        }
        return inetAddress!=null && !inetAddress.equals("");
    }

    private void isContaintWord(ArrayList<String> list, String partWord){   // vehicle class detect

        for (int i=0; list.size()>i;i++){
            String fullString = list.get(i);

           // Log.e("FullString", fullString);
            String pattern = "\\b"+partWord+"\\b";
            Pattern p=Pattern.compile(pattern);
            Matcher m=p.matcher(fullString);
            boolean b =m.find();
            Log.e("match", String.valueOf(b));
            if (b==true){
                Log.e("FullString", fullString);
                getVehicleClass(fullString);
                break;
            }
        }
    }

    private void getVehicleClass(String fullString) {

        for (int i=0; i<mVehicleClassList.length;i++){ //get all vehicle class and match with blue book
            String vehicle = mVehicleClassList[i];
            boolean b = check(fullString, vehicle);
            Log.e("Last", String.valueOf(b));
            if (b == true){
                Log.e("Vehicle class", "This car is "+vehicle);
               // serilizeData(vehicle);
                mVehicle = vehicle;

                break;  //if find the class then break the loop
            }
        }
    }

    private boolean check(String fullString, String vehicle) {
        String pattern = "\\b"+vehicle+"\\b";
        Pattern p=Pattern.compile(pattern);
        Matcher m=p.matcher(fullString);
        return m.find();
    }

}
