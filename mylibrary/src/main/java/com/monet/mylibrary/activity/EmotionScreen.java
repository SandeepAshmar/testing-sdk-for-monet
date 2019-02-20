package com.monet.mylibrary.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.monet.mylibrary.R;
import com.monet.mylibrary.adapter.ImageSliderAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import me.relex.circleindicator.CircleIndicator;

public class EmotionScreen extends AppCompatActivity {

    private ViewPager imageSliderViewPager;
    private ImageView img_toolbarBack;
    private TextView tv_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion_screen);

        img_toolbarBack = findViewById(R.id.img_toolbarBack);
        tv_next = findViewById(R.id.tv_next);
        img_toolbarBack.setVisibility(View.GONE);
        imageSliderViewPager = findViewById(R.id.imageSliderViewPager);
        if(checkCameraPermission())
        {
            Toast.makeText(this, "Permission granted oncreate", Toast.LENGTH_SHORT).show();
        }else{
            requestCameraPermission();
        }
        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(getSupportFragmentManager());
        imageSliderViewPager.setAdapter(imageSliderAdapter);

        CircleIndicator circleIndicator = findViewById(R.id.imageSliderCircleIndicator);
        circleIndicator.setViewPager(imageSliderViewPager);



        imageSliderViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position==2){
                    tv_next.setVisibility(View.VISIBLE);
                }
                else{
                    tv_next.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmotionScreen.this, FaceDetectionScreen.class));
                finish();
            }
        });
    }

    private boolean checkCameraPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale
                (this, Manifest.permission.CAMERA)) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.DialogTheme);
            alertDialog.setMessage("You Have To Give Permission for Camera From Your Device Setting To go in Setting Please Click on Settings Button");
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("Go To Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
            });
            alertDialog.show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, 1012);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.DialogTheme);
            alertDialog.setMessage("You Have To Give Permission From Your Device Setting To go in Setting Please Click on Settings Button");
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("Go To Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
            });
            alertDialog.show();
        }else{
             //take positive actions
            Toast.makeText(this, "Permission granted onRequestPermissionsResult", Toast.LENGTH_SHORT).show();
        }

    }


    //    private FrameLayout emotion_frame;
//    private LinearLayout btn_extll, btn_nextll;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_emotion_screen);
//
//        emotion_frame = findViewById(R.id.emotion_frame);
//        btn_extll = findViewById(R.id.faceIns_firstBtnll);
//        btn_nextll = findViewById(R.id.faceIns_secoundBtnll);
//        btn_exit = findViewById(R.id.btn_faceIns_exit);
//        btn_proceed = findViewById(R.id.btn_faceIns_proceed);
//        btn_next = findViewById(R.id.btn_faceIns_next);
//
//        setButtonClicks();
//    }
//
//
//    private void setButtonClicks(){
//        btn_exit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                btn_exit.setClickable(false);
//                finish();
//            }
//        });
//
//        btn_proceed.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                btn_proceed.setClickable(false);
//                setFragment(faceDetectionInsSecound);
//                btn_extll.setVisibility(View.GONE);
//                btn_nextll.setVisibility(View.VISIBLE);
//                if (checkCameraPermission()) {
//                    Log.e("Tag", "Camera and External storage Permission's are allowed");
//                } else {
//                    requestCameraPermission();
//                }
//            }
//        });
//
//        btn_next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(layoutShow.equals("first_layout")){
//                    setFragment(faceDetectionInsThird);
//                    layoutShow = "secoundLayout";
//                }
//                else if(layoutShow.equals("secoundLayout")){
//                    setFragment(faceDetectionInsForth);
//                    layoutShow = "thirdLayout";
//                }
//                else if(layoutShow.equals("thirdLayout")){
//                    btn_next.setClickable(false);
//                    startActivity(new Intent(EmotionScreen.this, FaceDetectionScreen.class));
//                    finish();
//                }
//            }
//        });
//    }
//





}
