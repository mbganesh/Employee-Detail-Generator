# Employee-Detail-Generator

Simple Employee Details Generator App in Android


## Code:
### [MainActivity.java](https://github.com/mbganesh/Employee-Detail-Generator/blob/master/app/src/main/java/mb/ganesh/employeedetails/MainActivity.java)


## Screens and Outs

<img src="https://user-images.githubusercontent.com/51211116/147079086-f9cc9fde-9a95-44c7-90d3-83e834ab9ce0.jpg" width="200" height="400" />
<img src="https://user-images.githubusercontent.com/51211116/147079083-a18352cb-02f8-4e15-99e8-014f8c63ed27.jpg" width="200" height="400" />
<img src="https://user-images.githubusercontent.com/51211116/147079076-af2d22c7-2272-4dd6-bc4a-995e343b4d6c.jpg" width="200" height="400" />


## Sample Codes

```

        PdfWriter writer = new PdfWriter(file);
        com.itextpdf.kernel.pdf.PdfDocument pdfDocument = new com.itextpdf.kernel.pdf.PdfDocument(writer);
        Document document = new Document(pdfDocument);
```

<hr/>

```

        Drawable d = getDrawable(R.drawable.gdood);
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG , 45 , stream);
        byte[] bitmapData = stream.toByteArray();

        ImageData imageData = ImageDataFactory.create(bitmapData);
        Image image = new Image(imageData).setWidth(300).setHeight(300).setHorizontalAlignment(HorizontalAlignment.CENTER);
        
```

<hr/>

```
  Paragraph paragraph = new Paragraph("Employee Details").setBold().setFontSize(24).setTextAlignment(TextAlignment.CENTER);
```

<hr/>

```
        float[] width = {125f,250f};
        Table table = new Table(width);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);

        table.addCell(new Cell().add(new Paragraph("Name").setBold()));
        table.addCell(new Cell().add(new Paragraph(name)));
```

