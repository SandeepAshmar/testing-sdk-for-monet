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

import com.monet.mylibrary.R;
import com.monet.mylibrary.adapter.EmotionImageSliderAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import me.relex.circleindicator.CircleIndicator;

import static com.monet.mylibrary.utils.SdkPreferences.getPageStage;
import static com.monet.mylibrary.utils.SdkPreferences.setPageStage;
import static com.monet.mylibrary.utils.SdkUtils.sendStagingData;

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

        EmotionImageSliderAdapter emotionImageSliderAdapter = new EmotionImageSliderAdapter(getSupportFragmentManager());
        imageSliderViewPager.setAdapter(emotionImageSliderAdapter);

        CircleIndicator circleIndicator = findViewById(R.id.imageSliderCircleIndicator);
        circleIndicator.setViewPager(imageSliderViewPager);

        imageSliderViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    tv_next.setVisibility(View.VISIBLE);
                } else {
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
                String pageStage = getPageStage(EmotionScreen.this);
                pageStage = pageStage.replace("emotion=q-card-start", "emotion=q-card-complete");
                setPageStage(EmotionScreen.this, pageStage);
                sendStagingData(EmotionScreen.this, 0);
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

    @Override
    protected void onResume() {
        super.onResume();
        setPageStage(this, getPageStage(this)+",emotion=q-card-start");
        sendStagingData(this, 0);
        if (checkCameraPermission()) {
         //   Toast.makeText(this, "Permission granted onResume", Toast.LENGTH_SHORT).show();
        } else {
            requestCameraPermission();
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
        } else {
            //take positive actions
          //  Toast.makeText(this, "Permission granted onRequestPermissionsResult", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        String pageStage = getPageStage(EmotionScreen.this);
        pageStage = pageStage.replace("emotion=q-card-start", "emotion=q-card-exit");
        setPageStage(EmotionScreen.this, pageStage);
        sendStagingData(this, 0);
        finish();
        super.onBackPressed();
    }
}
