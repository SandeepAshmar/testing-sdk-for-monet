package com.monet.mylibrary.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.monet.mylibrary.R;
import com.monet.mylibrary.visionCamera.CameraSourcePreview;
import com.monet.mylibrary.visionCamera.FaceGraphic;
import com.monet.mylibrary.visionCamera.GraphicOverlay;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;

public class FaceDetectionScreen extends AppCompatActivity {

    private static final String TAG = "FaceTracker";
    private CameraSource mCameraSource = null;
    private CameraSourcePreview mPreview;
    private GraphicOverlay mGraphicOverlay;
    private static final int RC_HANDLE_GMS = 9001;
    private ImageView img_cornerAlignRightTop, img_cornerAlignLeftBottom, img_cornerAlignRightBottom, img_cornerAlignLeftTop;
    private TextView tv_notify;
    private Runnable runnable;
    private Handler handler;
    public boolean detecting = false;
    public boolean detecting1 = false;
    public boolean detecting2 = false;
    public boolean detecting3 = false;
    public int currnetTime, oldTime = 0 ,gap, counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_detection_screen);
        handler = new Handler();
        mPreview = (CameraSourcePreview) findViewById(R.id.preview);
        mGraphicOverlay = (GraphicOverlay) findViewById(R.id.faceOverlay);
        img_cornerAlignRightTop = findViewById(R.id.img_cornerAlignRightTop);
        img_cornerAlignLeftBottom = findViewById(R.id.img_cornerAlignLeftBottom);
        img_cornerAlignRightBottom = findViewById(R.id.img_cornerAlignRightBottom);
        img_cornerAlignLeftTop = findViewById(R.id.img_cornerAlignLeftTop);
        tv_notify = findViewById(R.id.tv_notify);

//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                callSetView();
//            }
//        };
//        handler.postDelayed(runnable, 1000);
    }


    @Override
    protected void onResume() {
        super.onResume();
        createCameraSource();
        startCameraSource();
    }

    private void createCameraSource() {

        Context context = getApplicationContext();
        FaceDetector detector = new FaceDetector.Builder(context)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        detector.setProcessor(
                new MultiProcessor.Builder<>(new GraphicFaceTrackerFactory())
                        .build());

        if (!detector.isOperational()) {
            Log.w(TAG, "Face detector dependencies are not yet available.");
        }

        mCameraSource = new CameraSource.Builder(context, detector)
//                .setRequestedPreviewSize(640, 480)
                .setRequestedPreviewSize(840, 680)
                .setFacing(CameraSource.CAMERA_FACING_FRONT)
                .setRequestedFps(30.0f)
                .build();
    }

    private void startCameraSource() {

        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

    private class GraphicFaceTrackerFactory implements MultiProcessor.Factory<Face> {
        @Override
        public Tracker<Face> create(Face face) {
            return new GraphicFaceTracker(mGraphicOverlay);
        }
    }

    private class GraphicFaceTracker extends Tracker<Face> {
        private GraphicOverlay mOverlay;
        private FaceGraphic mFaceGraphic;

        GraphicFaceTracker(GraphicOverlay overlay) {
            mOverlay = overlay;
            mFaceGraphic = new FaceGraphic(overlay);
        }

        @Override
        public void onNewItem(int faceId, Face item) {
            mFaceGraphic.setId(faceId);
        }

        @Override
        public void onUpdate(FaceDetector.Detections<Face> detectionResults, Face face) {
            mOverlay.add(mFaceGraphic);
            mFaceGraphic.updateFace(face);
            currnetTime = (int) (detectionResults.getFrameMetadata().getTimestampMillis() / 1000);
            gap = currnetTime - oldTime;
            Log.d(TAG, "Face Detections Gap ="+ gap + " counter =" + counter + " currntTime "+ currnetTime);

            if (gap == 1) {
                counter = counter++;
                oldTime = currnetTime;
            } else if (gap > 1) {
                counter = 0;
                oldTime = currnetTime;
            } else {
                oldTime = currnetTime;
            }
            setScreen();
//            detecting = true;
        }

        @Override
        public void onMissing(FaceDetector.Detections<Face> detectionResults) {
            mOverlay.remove(mFaceGraphic);
            counter = 0;
            setScreen();
//            detecting = false;
        }

        @Override
        public void onDone() {
            mOverlay.remove(mFaceGraphic);
        }
    }

    private void setScreen() {
        switch (counter) {
            case 0:
                img_cornerAlignRightBottom.setImageResource(R.drawable.ic_corner_align_right_bottom);
                img_cornerAlignRightTop.setImageResource(R.drawable.ic_corner_align_right_top);
                img_cornerAlignLeftBottom.setImageResource(R.drawable.ic_corner_align_left_bottom);
                img_cornerAlignLeftTop.setImageResource(R.drawable.ic_corner_align_left_top);
                tv_notify.setBackgroundColor(Color.parseColor("#192557"));
                tv_notify.setText("Adjust your face");
                break;
            case 1:
                tv_notify.setText("Wait a second");
                break;
            case 2:
                img_cornerAlignRightBottom.setImageResource(R.drawable.ic_corner_align_right_bottom_green);
                img_cornerAlignRightTop.setImageResource(R.drawable.ic_corner_align_right_top_green);
                img_cornerAlignLeftBottom.setImageResource(R.drawable.ic_corner_align_left_bottom_green);
                img_cornerAlignLeftTop.setImageResource(R.drawable.ic_corner_align_left_top_green);
                tv_notify.setBackgroundColor(Color.parseColor("#226501"));
                tv_notify.setText("Detected");
                Toast.makeText(this, "Intent Will be call here ", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Intent will be call here");
                break;
        }

    }

//    private void callSetView() {
//        if (detecting) {
//            runnable = new Runnable() {
//                @Override
//                public void run() {
//                    if (detecting) {
//                        if (!detecting1) {
//                            detectFirst();
//                        } else if (!detecting2) {
//                            detectSecond();
//                        } else if (!detecting3) {
//                            detecting3 = true;
//                        }
//                        if (detecting3) {
//                            detectThird();
//                            handler.removeCallbacks(runnable);
//                        } else {
//                            callSetView();
//                        }
//                    }else{
//                        setNotDetectView();
//                    }
//                }
//            };
//            handler.postDelayed(runnable, 1000);
//        } else {
//            setNotDetectView();
//        }
//    }

//    private void setNotDetectView() {
//            img_cornerAlignRightBottom.setImageResource(R.drawable.ic_corner_align_right_bottom);
//            img_cornerAlignRightTop.setImageResource(R.drawable.ic_corner_align_right_top);
//            img_cornerAlignLeftBottom.setImageResource(R.drawable.ic_corner_align_left_bottom);
//            img_cornerAlignLeftTop.setImageResource(R.drawable.ic_corner_align_left_top);
//            tv_notify.setBackgroundColor(Color.parseColor("#192557"));
//            tv_notify.setText("Adjust your face");
//            detecting1 = false;
//            detecting2 = false;
//            detecting3 = false;
//            callSetView();
//    }

//    private void detectThird() {
//        detecting1 = true;
//        detecting2 = true;
//        detecting3 = true;
//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(FaceDetectionScreen.this, PlayVideoAndRecordScreen.class));
//                handler.removeCallbacks(runnable);
//                mCameraSource.stop();
//                finish();
//            }
//        };
//        handler.postDelayed(runnable, 1000);
//    }

//    private void detectSecond() {
//        img_cornerAlignRightBottom.setImageResource(R.drawable.ic_corner_align_right_bottom_green);
//        img_cornerAlignRightTop.setImageResource(R.drawable.ic_corner_align_right_top_green);
//        img_cornerAlignLeftBottom.setImageResource(R.drawable.ic_corner_align_left_bottom_green);
//        img_cornerAlignLeftTop.setImageResource(R.drawable.ic_corner_align_left_top_green);
//        tv_notify.setBackgroundColor(Color.parseColor("#226501"));
//        tv_notify.setText("Detected");
//        detecting1 = true;
//        detecting2 = true;
//        detecting3 = false;
//    }

//    private void detectFirst() {
//        tv_notify.setText("Wait a second");
//        detecting1 = true;
//        detecting2 = false;
//        detecting3 = false;
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
