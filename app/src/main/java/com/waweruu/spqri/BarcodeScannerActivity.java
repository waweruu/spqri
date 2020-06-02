package com.waweruu.spqri;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.otaliastudios.cameraview.CameraListener;

import java.util.ArrayList;
import java.util.List;

public class BarcodeScannerActivity extends BaseCameraActivity {

    private ArrayList<QrCode> qrList = new ArrayList<>();
    private QrCodeAdapter adapter = new QrCodeAdapter(this, qrList);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.recyclerViewQrCode.setLayoutManager(linearLayoutManager);
        binding.recyclerViewQrCode.setAdapter(adapter);

        binding.cameraView.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(byte[] jpeg) {
                super.onPictureTaken(jpeg);
                if (jpeg != null && jpeg.length != 0) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(jpeg, 0, jpeg.length);
                    if (bitmap != null) {
                        runBarcodeScanner(bitmap);
                    }
                    showPreview();
                    binding.imagePreview.setImageBitmap(bitmap);
                }
            }
        });
    }

    private void runBarcodeScanner(final Bitmap bitmap) {
        //Create a FirebaseVisionImage
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);

        //Option of formats to scan
        FirebaseVisionBarcodeDetectorOptions options = new FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(
                        FirebaseVisionBarcode.FORMAT_ALL_FORMATS
                )
                .build();

        //Get access to an instance of FirebaseVisionBarcodeDetector
        FirebaseVisionBarcodeDetector detector = FirebaseVision.getInstance()
                .getVisionBarcodeDetector(options);

        //Use the detector to detect the labels inside the image
        detector.detectInImage(image)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {
                        qrList.clear();
                        adapter.notifyDataSetChanged();
                        //Task has been completed successfully
                        for (FirebaseVisionBarcode firebaseVisionBarcode : firebaseVisionBarcodes) {
                            switch (firebaseVisionBarcode.getValueType()) {
                                case FirebaseVisionBarcode.TYPE_URL:
                                    //Handle the URL here
                                    qrList.add(new QrCode("URL", firebaseVisionBarcode
                                            .getDisplayValue()));
                                    break;

                                case FirebaseVisionBarcode.TYPE_CONTACT_INFO:
                                    //Handling contact info here
                                    qrList.add(new QrCode("Contact", firebaseVisionBarcode
                                            .getContactInfo().getTitle()));
                                    break;

                                case FirebaseVisionBarcode.TYPE_WIFI:
                                    //Handling wifi here
                                    qrList.add(new QrCode("WiFi", firebaseVisionBarcode
                                            .getWifi().getSsid()));
                                    break;

                                case FirebaseVisionBarcode.TYPE_TEXT:
                                    //Handling text here
                                    qrList.add(new QrCode("Text", firebaseVisionBarcode
                                            .getDisplayValue()));
                                    break;

                                case FirebaseVisionBarcode.TYPE_EMAIL:
                                    //Handling email here
                                    qrList.add(new QrCode("Email", firebaseVisionBarcode
                                            .getEmail().getAddress()));
                                    break;

                                //TODO: Handle more types
                                default:
                                    //Data has no detected type
                                    qrList.add(new QrCode("Generic", firebaseVisionBarcode.getDisplayValue()));
                                    break;
                            }
                        }
                        adapter.notifyDataSetChanged();
                        binding.progressBar.setVisibility(View.GONE);
                        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Task has failed with an exception
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(getBaseContext(), "Sorry, something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<List<FirebaseVisionBarcode>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<FirebaseVisionBarcode>> task) {
                        binding.progressBar.setVisibility(View.GONE);
                        //
                    }
                });
    }

    @Override
    public void onClick(View v) {
        binding.progressBar.setVisibility(View.VISIBLE);

        binding.cameraView.captureSnapshot();
    }
}
