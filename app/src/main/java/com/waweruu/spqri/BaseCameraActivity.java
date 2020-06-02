package com.waweruu.spqri;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.waweruu.spqri.databinding.ActivityBarcodeScannerBinding;

public abstract class BaseCameraActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityBarcodeScannerBinding binding;
    BottomSheetBehavior<LinearLayout> sheetBehavior;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBarcodeScannerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.buttonRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.cameraView.getVisibility() == View.VISIBLE) showPreview();
                else hidePreview();
            }
        });

        binding.cameraView.setLifecycleOwner(this);

        binding.fabTakePhoto.setOnClickListener(this);

        sheetBehavior = BottomSheetBehavior.from(binding.layoutBottomSheet);
        sheetBehavior.setPeekHeight(224);
    }

    protected void showPreview() {
        binding.framePreview.setVisibility(View.VISIBLE);
        binding.cameraView.setVisibility(View.GONE);
    }

    protected void hidePreview() {
        binding.framePreview.setVisibility(View.GONE);
        binding.cameraView.setVisibility(View.VISIBLE);
    }
}
