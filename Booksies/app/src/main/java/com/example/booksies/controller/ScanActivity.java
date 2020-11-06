package com.example.booksies.controller;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;
import android.database.Cursor;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.booksies.R;

import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

/**
 * Refer to https://firebase.google.com/docs/ml-kit/android/read-barcodes for further information about
 * any parts of the code.
 */

/**
 * Scanner activity opens up camera to scan ISBN code and return it to Scan Fragment which displays result
 * on dialog box. Also ScanActivity extends to AppCompatActivity
 */
public class ScanActivity extends AppCompatActivity {
    public static final String TAG = ScanActivity.class.getSimpleName();
    public static final int SCAN = 4;
    private Uri imageUri;

    /**
     * Set content view and use potrait orientation before scanning
     * @param savedInstanceState: saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        scanImage();
    }

    /**
     * Start the scan
     */
    public void scanImage() {
        ContentValues values = new ContentValues();
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, ScanActivity.SCAN);
    }

    /**
     * Scan Bitmap for any ISBN codes using firebase ml vision and use retrieveISBN to get ISBN code in such a case.
     * If ISBN code is available, then return it to parent Fragment
     * @param bitmap: the bitmap to process
     */
    private void processBitMap(Bitmap bitmap){
        Log.d(TAG, "processBitMap: PROCESS");
        FirebaseVisionImage img = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionBarcodeDetectorOptions options = new FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_ALL_FORMATS).build();
        FirebaseVisionBarcodeDetector isbnDetector = FirebaseVision.getInstance()
                .getVisionBarcodeDetector(options);
        isbnDetector.detectInImage(img).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
            @Override
            public void onSuccess(List<FirebaseVisionBarcode> isbnBarcodes) {
                for (FirebaseVisionBarcode barcode : isbnBarcodes){
                    if (barcode.getValueType() == FirebaseVisionBarcode.TYPE_ISBN){
                        Intent intent = new Intent();
                        intent.putExtra("ISBN", barcode.getDisplayValue());
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                        return;
                    }
                }
                Toast.makeText(ScanActivity.this, "Incorrect. Try again", Toast.LENGTH_SHORT).show();
                scanImage();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ScanActivity.this, "Try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Process the result of the barcode, if the ISBN if found return the ISBN code to the calling
     * activity. Otherwise, display the correct error message. Adapted from
     * https://firebase.google.com/docs/ml-kit/android/read-barcodes
     * @param firebaseVisionBarcodes: the list of barcodes
     */
    private void processResult(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {
        if (firebaseVisionBarcodes.size() == 0){
            Toast.makeText(this, "Nothing found to scan. Please try again", Toast.LENGTH_SHORT).show();
        }

        for (FirebaseVisionBarcode barcode : firebaseVisionBarcodes){
//            Log.d(TAG, "onSuccess: take it NICE");
            if (barcode.getValueType() == FirebaseVisionBarcode.TYPE_ISBN){
//                Log.d(TAG, "onSuccess: NICE");
                Toast.makeText(this, "processing   " + barcode.getDisplayValue(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.putExtra("ISBN", barcode.getDisplayValue());
                setResult(Activity.RESULT_OK, intent);
                finish();
                return;
            }
        }
        Toast.makeText(this, "Found improper barcode. Please try again", Toast.LENGTH_SHORT).show();
        scanImage();
    }

    /**
     * Run when Scanner activity returns result
     * @param requestCode: requested Code to know about the request made
     * @param resultCode: result code which shows if a result was OK or NOT or CANCELLED
     * @param data: intent given
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // If scanner requests and result is ok
        if (requestCode == ScanActivity.SCAN & resultCode == RESULT_OK) {
            try {
                // Get bitmap ready and process it through processBitMap
                Bitmap bit_map = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                String[] mediaData = { MediaStore.Images.Media.DATA };
                Cursor cursor = managedQuery(imageUri, mediaData, null, null, null);
                int colIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String getRealPathFromURI = cursor.getString(colIndex);
                getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        MediaStore.Images.ImageColumns.DATA + "=?" , new String[]{ getRealPathFromURI });
                processBitMap(bit_map);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Log.d(TAG, "Scanning faced issues");
            finish();
        }
    }

    /**
     * When back button is clicked, go to ScanFragment.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ScanFragment.class);
        startActivity(intent);
    }
}