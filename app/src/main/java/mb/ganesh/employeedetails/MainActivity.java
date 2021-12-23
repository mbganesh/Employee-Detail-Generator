package mb.ganesh.employeedetails;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MainActivity extends AppCompatActivity {

    TextInputEditText nameField , mobNoField , mailIdField , addressField , cityField , stateField,qualificationField, genderField , skillsField;
    MaterialButton generatePdf , viewPdf;
    Document document;
    ProgressBar homeLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        nameField = findViewById(R.id.nameField);
        mobNoField = findViewById(R.id.mobNoField);
        mailIdField = findViewById(R.id.mailIdField);
        addressField = findViewById(R.id.addressField);
        cityField = findViewById(R.id.cityField);
        stateField = findViewById(R.id.stateField);
        qualificationField = findViewById(R.id.qualificationField);
        genderField = findViewById(R.id.genderField);
        skillsField = findViewById(R.id.skillsField);
        generatePdf = findViewById(R.id.generatePdf);
        viewPdf = findViewById(R.id.viewPdf);
        homeLoader = findViewById(R.id.homeLoader);


        if (checkPermission()) {
//            remove while build apk.
//            View v = findViewById(android.R.id.content);
//            Snackbar.make(v , "Permission Granted" ,Snackbar.LENGTH_LONG).show();
        } else {
            requestPermission();
        }

        generatePdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameField.getText().toString();
                String mobno = mobNoField.getText().toString();
                String mail = mailIdField.getText().toString();
                String address = addressField.getText().toString();
                String city = cityField.getText().toString();
                String state = stateField.getText().toString();
                String qualifi = qualificationField.getText().toString();
                String gender = genderField.getText().toString();
                String skills = skillsField.getText().toString();

                if(!name.isEmpty() || !mobno.isEmpty() ||  !mail.isEmpty() ||  !address.isEmpty() ||  !city.isEmpty() ||  !state.isEmpty() ||  !qualifi.isEmpty() ||  !gender.isEmpty() ||  !skills.isEmpty() ){
                    try {
                        homeLoader.setVisibility(View.VISIBLE);
                        getPdf(name , mobno , mail , address , city , state , qualifi , gender , skills);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    View v = findViewById(android.R.id.content);
                    Snackbar.make(v , "Field Count not be empty." , Snackbar.LENGTH_LONG).setBackgroundTint(ContextCompat.getColor(MainActivity.this , R.color.red)).show();
                }
            }
        });

        viewPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this , ViewActivity.class));
            }

        });
    }

    private void getPdf(String name, String mobno, String mail, String address, String city, String state, String qualifi, String gender, String skills) throws Exception{
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath , "EmployeeDetails.pdf");
//        OutputStream outputStream = new FileOutputStream(file);



        PdfWriter writer = new PdfWriter(file);
        com.itextpdf.kernel.pdf.PdfDocument pdfDocument = new com.itextpdf.kernel.pdf.PdfDocument(writer);
         document = new Document(pdfDocument);

        Drawable d = getDrawable(R.drawable.gdood);
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG , 45 , stream);
        byte[] bitmapData = stream.toByteArray();

        ImageData imageData = ImageDataFactory.create(bitmapData);
        Image image = new Image(imageData).setWidth(150).setHeight(150).setHorizontalAlignment(HorizontalAlignment.CENTER);

        Paragraph paragraph = new Paragraph("\nEmployee Details\n").setBold().setFontSize(24).setTextAlignment(TextAlignment.CENTER);

        float[] width = {125f,250f};
        Table table = new Table(width);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);

        table.addCell(new Cell().add(new Paragraph("Name").setBold()));
        table.addCell(new Cell().add(new Paragraph(name)));

        table.addCell(new Cell().add(new Paragraph("Mob No").setBold()));
        table.addCell(new Cell().add(new Paragraph("+91"+mobno)));

        table.addCell(new Cell().add(new Paragraph("Mail ID").setBold()));
        table.addCell(new Cell().add(new Paragraph(mail+"@gmail.com")));

        table.addCell(new Cell().add(new Paragraph("Address").setBold()));
        table.addCell(new Cell().add(new Paragraph(address)));

        table.addCell(new Cell().add(new Paragraph("City").setBold()));
        table.addCell(new Cell().add(new Paragraph(city)));

        table.addCell(new Cell().add(new Paragraph("State").setBold()));
        table.addCell(new Cell().add(new Paragraph(state)));

        table.addCell(new Cell().add(new Paragraph("Qualification").setBold()));
        table.addCell(new Cell().add(new Paragraph(qualifi)));

        table.addCell(new Cell().add(new Paragraph("Gender").setBold()));
        table.addCell(new Cell().add(new Paragraph(gender)));

        table.addCell(new Cell().add(new Paragraph("Skills").setBold()));
        table.addCell(new Cell().add(new Paragraph(skills)));

        Paragraph qrHead  = new Paragraph("\nScan to get Employee Details\n").setBold().setFontSize(16).setHorizontalAlignment(HorizontalAlignment.CENTER).setTextAlignment(TextAlignment.CENTER);

        String qrData = "Name : " + name +"\nMobNo : " + mobno +"\nMailId : " + mail + "\nAddress : " + address + "\nCity : " + city + "\nState : " + state + "\nQualification : " + qualifi + "\nGender : " + gender + "\nSkills : " + skills ;
        BarcodeQRCode qrCode = new BarcodeQRCode(qrData);
        PdfFormXObject qrCodeObj = qrCode.createFormXObject(ColorConstants.BLACK , pdfDocument);
        Image qrCodeImage = new Image(qrCodeObj).setWidth(85).setHorizontalAlignment(HorizontalAlignment.CENTER).setWidth(100).setHeight(100);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Paragraph dateHead  = new Paragraph("Date").setBold().setTextAlignment(TextAlignment.LEFT).setMarginTop(50);
        Paragraph date  = new Paragraph(LocalDate.now().format(dateTimeFormatter)).setTextAlignment(TextAlignment.LEFT);
        Paragraph sign  = new Paragraph("Employee Sign" ).setBold().setTextAlignment(TextAlignment.RIGHT);

        document.add(image);
        document.add(paragraph);
        document.add(table);
        document.add(qrHead);
        document.add(qrCodeImage);
        document.add(dateHead).setFixedPosition(25,25,200);
        document.add(date).setFixedPosition(25,25,200);
        document.add(sign);

        document.close();

        View v = findViewById(android.R.id.content);
        Snackbar.make(v , "PDF Generator" , Snackbar.LENGTH_SHORT).setBackgroundTint(ContextCompat.getColor(MainActivity.this , R.color.priCol)).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                homeLoader.setVisibility(View.GONE);
            }
        },850);

    }
    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 200);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200) {
            if (grantResults.length > 0) {
                View v = findViewById(android.R.id.content);

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Snackbar.make(v , "Permission Granted" ,Snackbar.LENGTH_LONG).setBackgroundTint(ContextCompat.getColor(MainActivity.this , R.color.green)).show();
                } else {
                    Snackbar.make(v , "Permission Denied! Allow to download PDF" ,Snackbar.LENGTH_LONG).setBackgroundTint(ContextCompat.getColor(MainActivity.this , R.color.red)).show();
                    finish();
                }
            }
        }
    }
}