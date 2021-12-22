package mb.ganesh.employeedetails;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
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
    MaterialButton generatePdf , sharePdf;
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
        sharePdf = findViewById(R.id.sharePdf);

        if (checkPermission()) {
//            remove while build apk.
            View v = findViewById(android.R.id.content);
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
                        getPdf(name , mobno , mail , address , city , state , qualifi , gender , skills);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    View v = findViewById(android.R.id.content);
                    Snackbar.make(v , "Enter All the Above Details" , Snackbar.LENGTH_LONG).show();
                }
            }
        });

        sharePdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = findViewById(android.R.id.content);
                Snackbar.make(v , "Under Processing" , Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void getPdf(String name, String mobno, String mail, String address, String city, String state, String qualifi, String gender, String skills) throws Exception{
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath , name+"sDetails.pdf");
//        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter writer = new PdfWriter(file);
        com.itextpdf.kernel.pdf.PdfDocument pdfDocument = new com.itextpdf.kernel.pdf.PdfDocument(writer);
        Document document = new Document(pdfDocument);

        Drawable d = getDrawable(R.drawable.gdood);
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG , 45 , stream);
        byte[] bitmapData = stream.toByteArray();

        ImageData imageData = ImageDataFactory.create(bitmapData);
        Image image = new Image(imageData).setWidth(300).setHeight(300).setHorizontalAlignment(HorizontalAlignment.CENTER);

        Paragraph paragraph = new Paragraph("Employee Details").setBold().setFontSize(24).setTextAlignment(TextAlignment.CENTER);

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

        Paragraph qrHead  = new Paragraph("Scan to get Employee Details").setBold().setFontSize(16).setHorizontalAlignment(HorizontalAlignment.CENTER).setTextAlignment(TextAlignment.CENTER);

        String qrData = "Name : " + name +"\nMobNo : " + mobno +"\nMailId : " + mail + "\nAddress : " + address + "\nCity : " + city + "\nState : " + state + "\nQualification : " + qualifi + "\nGender : " + gender + "\nSkills : " + skills ;
        BarcodeQRCode qrCode = new BarcodeQRCode(qrData);
        PdfFormXObject qrCodeObj = qrCode.createFormXObject(ColorConstants.BLACK , pdfDocument);
        Image qrCodeImage = new Image(qrCodeObj).setWidth(85).setHorizontalAlignment(HorizontalAlignment.CENTER);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Paragraph dateHead  = new Paragraph("Date").setBold().setTextAlignment(TextAlignment.LEFT);
        Paragraph date  = new Paragraph(LocalDate.now().format(dateTimeFormatter)).setTextAlignment(TextAlignment.LEFT);
        Paragraph sign  = new Paragraph("Employee Sign" ).setBold().setTextAlignment(TextAlignment.RIGHT);

        document.add(image);
        document.add(paragraph);
        document.add(table);
        document.add(qrHead);
        document.add(qrCodeImage);
        document.add(dateHead);
        document.add(date);
        document.add(sign);

        document.close();
        Toast.makeText(MainActivity.this, "Pdf Created", Toast.LENGTH_SHORT).show();
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
                    Snackbar.make(v , "Permission Granted" ,Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(v , "Permission Denied" ,Snackbar.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }
}