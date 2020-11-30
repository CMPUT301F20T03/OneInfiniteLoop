package com.example.booksies.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.example.booksies.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

/**
 * Scanner opens up camera to scan ISBN code and return it to Scan Fragment which displays result
 * on dialog box. Also Scanner extends to AppCompatActivity
 */
public class Scanner extends AppCompatActivity {

    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private ToneGenerator toneGen1; // Class gives methods for playing DTMF tones
    private String barcodeData;
    public static final int SCAN = 4;

    /**
     * Set content view and use ToneGenerator before scanning
     * @param savedInstanceState: saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scancamera);
        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC,100);
        surfaceView = findViewById(R.id.surface_view);
    }

    /**
     * Initialise detectors and sources
     */
    private void initialiseDetectorsAndSources() {
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {

            /**
             * Get holder for surface view
             * @param holder: an instance of SurfaceHolder
             */
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(Scanner.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(Scanner.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            /**
             * Works when surface is changed
             * @param holder: an instance of SurfaceHolder
             * @param format: format
             * @param width: width
             * @param height: height
             */
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            /**
             * Works when surface is destroyed
             * @param holder: an instance of SurfaceHolder
             */
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            /**
             * Works on release
             */
            @Override
            public void release() {
                // Toast.makeText(getApplicationContext(), "Prevent memory leaks and barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            /**
             * Works when ISBN is detected
             * @param detections: detections to get ISBN code
             */
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    surfaceView.post(new Runnable() {

                        /**
                         * Gets ISBN code
                         */
                        @Override
                        public void run() {
                            if (barcodes.valueAt(0).email != null) {
                                barcodeData = barcodes.valueAt(0).email.address;
                                Intent intent = new Intent();
                                intent.putExtra("ISBN", barcodeData);
                                setResult(Activity.RESULT_OK, intent);
                                Toast.makeText(Scanner.this, "Barcode: "+barcodeData, Toast.LENGTH_SHORT).show();
                                finish();
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                                return;
                            }
                            else {
                                barcodeData = barcodes.valueAt(0).displayValue;
                                Intent intent = new Intent();
                                intent.putExtra("ISBN", barcodeData);
                                setResult(Activity.RESULT_OK, intent);
                                Toast.makeText(Scanner.this, "Barcode: "+barcodeData, Toast.LENGTH_SHORT).show();
                                finish();
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                                return;
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * Works on pause
     */
    @Override
    protected void onPause() {
        super.onPause();
        getSupportActionBar().hide();
        cameraSource.release();
    }

    /**
     * Works on resume
     */
    @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().hide();
        initialiseDetectorsAndSources();
    }
}