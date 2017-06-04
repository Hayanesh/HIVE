package com.hayanesh.hive.store;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fontometrics.Fontometrics;
import com.github.florent37.arclayout.ArcLayout;
import com.hayanesh.hive.R;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

public class Book_upload extends AppCompatActivity implements SingleUploadBroadcastReceiver.Delegate{

    TextView u_title;

    EditText b_name,author,year,b_sub;
    Button choose,upload;
    ImageButton close;
    TextView textView;
    LinearLayout u_details;
    String path;

    Animation fadeIn;
    static final String UPLOAD_URL = "http://192.168.1.4/hive/upload.php";

    CardView p_card,p_status;
    TextView p_title,p_author,p_year,p_complete;
    ProgressBar progressBar;
    FloatingActionButton fab;
    ArcLayout arcLayout;

    ImageView pdfView;

    Bitmap bitmap;

    //Pdf request code
    private int PICK_PDF_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Uri to store the image uri
    private Uri filePath;


    private final SingleUploadBroadcastReceiver uploadReceiver =
            new SingleUploadBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_upload);

        requestStoragePermission();

        u_title = (TextView)findViewById(R.id.title_upload);
        final Typeface customtypeface = Typeface.createFromAsset(this.getAssets(),"fonts/DancingScript-Bold.ttf");
        final Typeface customtypeface1 = Typeface.createFromAsset(this.getAssets(),"fonts/Lobster-Regular.ttf");
        final Typeface customtypeface2 = Typeface.createFromAsset(this.getAssets(),"fonts/KaushanScript-Regular.ttf");
        final Typeface customtypeface3 = Typeface.createFromAsset(this.getAssets(),"fonts/CormorantGaramond-Regular.ttf");
        u_title.setTypeface(customtypeface2);

        b_sub = (EditText)findViewById(R.id.subject);
        b_name = (EditText)findViewById(R.id.book_name);
        author = (EditText)findViewById(R.id.book_author);
        year = (EditText)findViewById(R.id.book_year);
        textView = (TextView)findViewById(R.id.file_name);
        u_details = (LinearLayout)findViewById(R.id.upload_details);
        close = (ImageButton)findViewById(R.id.close_btn);

          p_card = (CardView)findViewById(R.id.book_card);

       // p_author = (TextView)findViewById(R.id.author);
        //p_year = (TextView)findViewById(R.id.year);
        arcLayout = (ArcLayout)findViewById(R.id.upload_status);
        progressBar = (ProgressBar)findViewById(R.id.progress);
        p_complete = (TextView)findViewById(R.id.upload_text);
        fab = (FloatingActionButton)findViewById(R.id.floatingActionButton3);
        pdfView = (ImageView)findViewById(R.id.pdfView);

        //p_title.setTypeface(customtypeface3);
        //p_author.setTypeface(customtypeface3);
        //p_year.setTypeface(customtypeface3);
        //textView.setTypeface(customtypeface);

        b_sub.setTypeface(customtypeface3);
        b_name.setTypeface(customtypeface3);
        author.setTypeface(customtypeface3);
        year.setTypeface(customtypeface3);
        //p_complete.setTypeface(customtypeface);

        fadeIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);

        choose = (Button)findViewById(R.id.choose);
        upload = (Button)findViewById(R.id.upload);

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arcLayout.setVisibility(View.GONE);
                upload.setEnabled(true);
                showFileChooser();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload.setEnabled(false);
                arcLayout.setVisibility(View.VISIBLE);
              //  p_card.setVisibility(View.VISIBLE);
               // p_card.startAnimation(fadeIn);
                //initProgressView();
                uploadMultipart();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void openPDF() throws IOException {
        File file = new File(filePath.getPath());

        ParcelFileDescriptor fileDescriptor = null;
        fileDescriptor = ParcelFileDescriptor.open(
                file, ParcelFileDescriptor.MODE_READ_ONLY);

        //min. API Level 21
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            PdfRenderer pdfRenderer = null;
            pdfRenderer = new PdfRenderer(fileDescriptor);


            //Display page 0
            PdfRenderer.Page rendererPage = pdfRenderer.openPage(0);
            int rendererPageWidth = rendererPage.getWidth();
            int rendererPageHeight = rendererPage.getHeight();
            bitmap = Bitmap.createBitmap(
                    rendererPageWidth,
                    rendererPageHeight,
                    Bitmap.Config.ARGB_8888);
            rendererPage.render(bitmap, null, null,
                    PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

            pdfView.setImageBitmap(bitmap);

            rendererPage.close();

            pdfRenderer.close();
            fileDescriptor.close();
        } else{
            Toast.makeText(this, "API level not supported", Toast.LENGTH_SHORT).show();
        }

    }


     /*
    * This is the method responsible for pdf upload
    * We need the full pdf path and the name for the pdf in this method
    * */
   /* public void initProgressView()
    {
        p_title.setText(b_name.getText());
        p_author.setText(author.getText());
        p_year.setText(year.getText());
    }*/


    public void uploadMultipart() {
        //getting name for the image
        String name = b_name.getText().toString().trim();
        path = FilePath.getPath(this, filePath);


        if (path == null) {

            Toast.makeText(this, "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
        } else {
            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();
                uploadReceiver.setDelegate(this);
                uploadReceiver.setUploadID(uploadId);
                //Creating a multi part request
                new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                        .addFileToUpload(path, "pdf") //Adding file
                        .addParameter("name", name)
                        .addParameter("author",author.getText().toString())
                        .addParameter("year",year.getText().toString())
                        .addParameter("subject",b_sub.getText().toString())
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload

            } catch (Exception exc) {
                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }


    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            textView.setText(filePath.getLastPathSegment());
            p_card.setVisibility(View.VISIBLE);
            try {
                openPDF();
            }catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        uploadReceiver.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        uploadReceiver.unregister(this);
    }


    @Override
    public void onProgress(int progress) {
        progressBar.setProgress(progress);
    }

    @Override
    public void onProgress(long uploadedBytes, long totalBytes) {

    }

    @Override
    public void onError(Exception exception) {

    }

    @Override
    public void onCompleted(int serverResponseCode, byte[] serverResponseBody) {
        uploadImage();
        progressBar.setVisibility(View.GONE);
       // p_complete.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCancelled() {

    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    private void uploadImage(){
        //Showing the progress dialog
       // final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                       // loading.dismiss();
                        //Showing toast message of the response
                        p_complete.setText("upload complete...");
                        progressBar.setVisibility(View.INVISIBLE);
                        fab.setImageResource(R.drawable.ic_present);
                        Toast.makeText(Book_upload.this, s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        p_complete.setText("upload Failed...");
                        progressBar.setVisibility(View.INVISIBLE);
                        fab.setImageResource(R.drawable.ic_absent);
                        //Showing toast
                        Toast.makeText(Book_upload.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = BitMapToString(bitmap);


                //Getting Image Name


                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("img", image);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }


}
