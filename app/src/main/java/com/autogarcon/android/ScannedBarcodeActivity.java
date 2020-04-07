package com.autogarcon.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.Bundle;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import androidx.annotation.NonNull;

import java.io.IOException;

public class ScannedBarcodeActivity extends AppCompatActivity {

    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
    }

    /**
     * Creates a barcode detector and processes results. Results including restaurant name and table number from
     * barcode scanning are passed back to the landing page intent.
     * @author Mitchell Nelson
     */
    private void initialiseDetectorsAndSources() {
        Log.d("CREATION", "in INIT");

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        if (!barcodeDetector.isOperational()){
            Log.d("CREATION", "DETECTOR IS NOT OPERATIONAL!");
        }

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                //.setRequestedPreviewSize(1080, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            /**
             * Starts the camera when a surface is created
             * @param holder
             * @author Mitchell Nelson
             */
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScannedBarcodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScannedBarcodeActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            /**
             * Override method that does not have necessary functionality for current requirements
             * @param holder
             * @param format
             * @param width
             * @param height
             * @author Mitchell Nelson
             */
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            /**
             * Stops the camera when a surface is destroyed
             * @param holder
             * @author Mitchell Nelson
             */
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {

            /**
             * Override method that does not have necessary functionality for current requirements
             * @author Mitchell Nelson
             */
            @Override
            public void release() {
            }

            /**
             * Processes detected barcode and calls the inner run method.
             * @param detections list of all detected barcodes
             * @author Mitchell Nelson
             */
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {

                    txtBarcodeValue.post(new Runnable() {

                        /**
                         * Parses a single detected barcode and passes the results back to the landing
                         * page intent. The ScannedBarcodeActivity is then finished.
                         * @author Mitchell Nelson
                         */
                        @Override
                        public void run() {
                            String qrText = barcodes.valueAt(0).displayValue;
                            Intent intent = new Intent();
                            intent.putExtra("URL",qrText);
                            setResult(2,intent);
                            finish();
                        }
                    });
                }
            }
        });
    }

    /**
     *  This method will run the first time that user uses the app. After granting the application
     *  to access the camera, this method reloads this activity so that it may start clean with
     *  proper permissions
     * @param requestCode Request code of permissions
     * @param permissions List of the permissions requested
     * @param grantResults Results of the granted or denied permissions
     * @author Mitchell Nelson
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted - reload the page
                    recreate();
                }
            }
        }
    }

    /**
     * Stops the camera when the app is closed or paused otherwise.
     * @author Mitchell Nelson
     */
    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    /**
     * Starts the barcode detector when the scanning process begins or resumes.
     * @author Mitchell Nelson
     */
    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }
}
