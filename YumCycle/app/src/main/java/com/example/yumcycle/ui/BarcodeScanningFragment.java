package com.example.yumcycle.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.yumcycle.R;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BarcodeScanningFragment extends Fragment {
    private CodeScanner mCodeScanner;
    private boolean isFrontCamera = false;

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final String API_KEY = "478edq3yiz4w42sh2i5feirqm5kxhm"; // Your API key

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final Activity activity = getActivity();
        View root = inflater.inflate(R.layout.fragment_barcode_scanning, container, false);

        initializeCodeScanner(root);

        return root;
    }

    private void initializeCodeScanner(View root) {
        CodeScannerView scannerView = root.findViewById(R.id.scanner_view);
        Button switchCameraButton = root.findViewById(R.id.switch_camera_button);

        mCodeScanner = new CodeScanner(requireActivity(), scannerView);

        // Default to back camera
        mCodeScanner.setCamera(CodeScanner.CAMERA_BACK);

        mCodeScanner.setDecodeCallback(result -> requireActivity().runOnUiThread(() -> {
            //String barcode = result.getText();
            String barcode = "59032823";
            // Log the detected barcode
            Log.d("BarcodeScanningFragment", "Detected Barcode: " + barcode);

            // Fetch product details based on the barcode
            fetchProductDetails(barcode);
        }));

        scannerView.setOnClickListener(view -> mCodeScanner.startPreview());

        // Handle camera switch
        switchCameraButton.setOnClickListener(v -> {
            isFrontCamera = !isFrontCamera;
            mCodeScanner.setCamera(isFrontCamera ? CodeScanner.CAMERA_FRONT : CodeScanner.CAMERA_BACK);
            mCodeScanner.startPreview();
        });
    }

    private void fetchProductDetails(String barcode) {
        // API URL for product lookup
        String url = "https://api.barcodelookup.com/v3/products?barcode=" + barcode + "&key=478edq3yiz4w42sh2i5feirqm5kxhm";

        // Perform the network request in a background thread
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseData = response.body().string();
                    parseProductResponse(responseData);
                } else {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Product not found or API request failed.", Toast.LENGTH_SHORT).show()
                    );
                }
            } catch (Exception e) {
                Log.e("BarcodeScanningFragment", "Error fetching product details", e);
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }

    private void parseProductResponse(String responseData) {
        try {
            JSONObject jsonResponse = new JSONObject(responseData);
            JSONArray products = jsonResponse.optJSONArray("products");

            if (products != null && products.length() > 0) {
                JSONObject product = products.getJSONObject(0);
                String productName = product.optString("product_name", "Unknown");
                String brand = product.optString("brand", "Unknown");
                String category = product.optString("category", "Unknown");

                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(),
                                "Product Name: " + productName + "\nBrand: " + brand + "\nCategory: " + category,
                                Toast.LENGTH_LONG).show()
                );
            } else {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "No product details available.", Toast.LENGTH_SHORT).show()
                );
            }
        } catch (Exception e) {
            Log.e("BarcodeScanningFragment", "Error parsing product response", e);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCodeScanner != null) {
            mCodeScanner.startPreview();
        }
    }

    @Override
    public void onPause() {
        if (mCodeScanner != null) {
            mCodeScanner.releaseResources();
        }
        super.onPause();
    }
}