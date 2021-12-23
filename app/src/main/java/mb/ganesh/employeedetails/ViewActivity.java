package mb.ganesh.employeedetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ProgressBar;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;

public class ViewActivity extends AppCompatActivity {
    MaterialButton sharePDF;
    PDFView pdfView;
    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        toolbar = findViewById(R.id.materialToolBar);
        pdfView = findViewById(R.id.viewPDF);
        sharePDF = findViewById(R.id.sharePDF);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/EmployeeDetails.pdf";
        File file = new File(pdfPath);

        if(!file.exists()){
            sharePDF.setVisibility(View.GONE);

            View view = findViewById(android.R.id.content);
            Snackbar.make(view ,"No File Founded Please Create at least one PDF" , Snackbar.LENGTH_LONG).setBackgroundTint(ContextCompat.getColor(ViewActivity.this , R.color.priCol)).show();
        }else {
            pdfView.fromFile(file)
                    .swipeHorizontal(true)
                    .enableDoubletap(true)
                    .enableAnnotationRendering(true)
                    .defaultPage(0)
                    .scrollHandle(null)
                    .password(null)
                    .load();
        }

        sharePDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharePdfFile();
            }
        });
    }

    private void sharePdfFile() {

        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/EmployeeDetails.pdf";
        File file = new File(pdfPath);

        if(file.exists()){
            Uri uri = FileProvider.getUriForFile(this, getPackageName(), file);
            Intent intent = ShareCompat.IntentBuilder.from(this)
                    .setStream(uri) // uri from FileProvider
                    .setType("*/*")
                    .getIntent()
                    .setAction(Intent.ACTION_SEND) //Change if needed
                    .setDataAndType(uri, "application/*")
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        }


    }
}