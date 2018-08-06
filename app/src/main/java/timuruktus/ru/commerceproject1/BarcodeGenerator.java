package timuruktus.ru.commerceproject1;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class BarcodeGenerator {

    public void generateBarcode(int height, int width, String text, BarcodeGeneratorListener listener,
                                int imageId){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.CODE_128, 600, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            listener.onBarcodeGenerated(bitmap, imageId);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
