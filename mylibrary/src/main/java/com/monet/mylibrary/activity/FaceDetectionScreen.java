package com.monet.mylibrary.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

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

import static com.monet.mylibrary.utils.SdkUtils.sendStagingData;

public class FaceDetectionScreen extends AppCompatActivity {

    private static final String TAG = "FaceTracker";
    private CameraSource mCameraSource = null;
    private CameraSourcePreview mPreview;
    private GraphicOverlay mGraphicOverlay;
    private static final int RC_HANDLE_GMS = 9001;
    private ImageView img_cornerAlignRightTop, img_cornerAlignLeftBottom, img_cornerAlignRightBottom, img_cornerAlignLeftTop;
    private TextView tv_notify;
    public short currnetTime, oldTime = 0 , counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_detection_screen);
        mPreview = (CameraSourcePreview) findViewById(R.id.preview);
        mGraphicOverlay = (GraphicOverlay) findViewById(R.id.faceOverlay);
        img_cornerAlignRightTop = findViewById(R.id.img_cornerAlignRightTop);
        img_cornerAlignLeftBottom = findViewById(R.id.img_cornerAlignLeftBottom);
        img_cornerAlignRightBottom = findViewById(R.id.img_cornerAlignRightBottom);
        img_cornerAlignLeftTop = findViewById(R.id.img_cornerAlignLeftTop);
        tv_notify = findViewById(R.id.tv_notify);

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
                .setRequestedPreviewSize(640, 480)
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
            currnetTime = (short) (detectionResults.getFrameMetadata().getTimestampMillis() / 1000);
//            Log.d(TAG, "Face Detections Gap ="+ gap + " counter =" + counter + " currntTime "+ currnetTime);

            if ((currnetTime - oldTime) == 1) {
                counter++;
                oldTime = currnetTime;
            } else if ((currnetTime - oldTime) > 1) {
                counter = 0;
                oldTime = currnetTime;
            } else {
                oldTime = currnetTime;
            }
            setScreen();
        }

        @Override
        public void onMissing(FaceDetector.Detections<Face> detectionResults) {
            mOverlay.remove(mFaceGraphic);
            counter = 0;
            setScreen();
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
                break;
            case 3:
                startActivity(new Intent(FaceDetectionScreen.this, PlayVideoAndRecordScreen.class));
                finish();
                mPreview.stop();
                break;
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        sendStagingData(this);
        finish();
        super.onBackPressed();
    }
}
