package com.example.paisehpay.mainActivityFragments;

import static android.app.Activity.RESULT_OK;

import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.paisehpay.R;
import com.example.paisehpay.activities.ReceiptOverview;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.FileUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class AddFragment extends Fragment {
    private String currentPhotoPath;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;
    private String responseBody;
    private Double[] receiptItemPrice;
    private String[] receiptItemName;

    public AddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add, container, false);

        // Set up buttons
        rootView.findViewById(R.id.btn_camera).setOnClickListener(v -> dispatchTakePictureIntent());
        rootView.findViewById(R.id.btn_gallery).setOnClickListener(v -> openGallery());


        // Request camera permission if not granted
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
        }

        return rootView;
    }

    // Permissions result callback
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(requireContext(), "Camera permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            File photoFile;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(requireContext(),
                        requireContext().getPackageName() + ".fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                processImage(new File(currentPhotoPath));
            } else if (requestCode == REQUEST_IMAGE_GALLERY && data != null) {
                Uri uri = data.getData();
                try {
                    InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
                    File tempFile = File.createTempFile("image", ".jpg", requireContext().getCacheDir());
                    FileUtils.copyInputStreamToFile(inputStream, tempFile);
                    processImage(tempFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void processImage(File imageFile) {
        if (isNetworkAvailable()) {
            new ReceiptOcrTask().execute(imageFile);
        } else {
            Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    private class ReceiptOcrTask extends AsyncTask<File, Void, String> {

        //for asprise later
        @Override
        protected String doInBackground(File... files) {

            Log.d("OCR_CALL", "Starting OCR request...");

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build();

            try {
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("api_key", "TEST")
                        .addFormDataPart("recognizer", "auto")
                        .addFormDataPart("ref_no", "ocr_android_123")
                        .addFormDataPart("file", files[0].getName(),
                                RequestBody.create(MediaType.parse("image/jpeg"), files[0]))
                        .build();

                Request request = new Request.Builder()
                        .url("https://ocr.asprise.com/api/v1/receipt")
                        .post(requestBody)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful() && response.body() != null) {
                        responseBody = response.body().string();
                        Log.d("OCR", responseBody);
                        return responseBody;
                    } else {
                        Log.e("OCR", "Response failed: Code = " + response.code() + ", Message = " + response.message());
                        return null;
                    }
                }

            } catch (IOException e) {
                Log.e("OCR", "IOException occurred: " + e.getMessage(), e);
                return null;
            } catch (Exception e) {
                Log.e("OCR", "Unexpected exception: " + e.getMessage(), e);
                return null;
            }
        }



        // for asprise later
        @Override
        protected void onPostExecute(String result) {
            // Log the full response for debugging
            Log.d("OCR", "Response: " + result);

            if (result != null) {
                try {
                    // Create a JSONObject from the response string
                    JSONObject json = new JSONObject(result);
// Check if the OCR request was successful
                    boolean success = json.optBoolean("success", false);
                    if (!success) {
                        Log.e("OCR", "OCR process failed.");
                        return;
                    }

                    // Get the "receipts" array from the JSON response
                    JSONArray receipts = json.getJSONArray("receipts");


                    // Loop through the receipts (we're assuming only one receipt in the "receipts" array)
                    for (int i = 0; i < receipts.length(); i++) {
                        JSONObject receipt = receipts.getJSONObject(i);

                        // Check if the "items" key exists and if it's not null
                        if (receipt.has("items") && !receipt.isNull("items")) {
                            // Get the "items" array from each receipt
                            JSONArray items = receipt.getJSONArray("items");
                            Log.d("gugu",items.toString() + items.length());

                            receiptItemPrice = new Double[items.length()];
                            receiptItemName = new String[items.length()];

                            // Loop through each item and extract the necessary data
                            for (int j = 0; j < items.length(); j++) {
                                JSONObject item = items.getJSONObject(j);
                                Log.d("bk",item.toString());

                                String itemName = item.getString("description");
                                receiptItemName[j] = itemName;
                                double itemPrice = item.getDouble("amount");
                                receiptItemPrice[j] = itemPrice;
                            }
                        } else {
                            Log.e("OCR", "No 'items' found for this receipt.");
                        }
                    }

                    // Log the number of parsed items for debugging
                    Log.d("OCR", "Parsed " + receiptItemName.length + " items.");


                    if (receiptItemName.length > 0) {
                        // Navigate to ReceiptOverview
                        Intent intent = new Intent(getActivity(), ReceiptOverview.class);
                        Log.d("sd",Arrays.toString(receiptItemName)+ Arrays.toString(receiptItemPrice));
                        intent.putExtra("itemName", receiptItemName);
                        intent.putExtra("itemPrice", receiptItemPrice);
                        startActivity(intent);
                        requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    } else {
                        Toast.makeText(requireContext(), "No items detected", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("OCR", "Error parsing the OCR response", e);
                }
            } else {
                Log.e("OCR", "OCR response is null");
            }
        }

    }
}

