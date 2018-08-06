package timuruktus.ru.commerceproject1;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

/**
 * This class allows you to create barcodes from String. Also it has inner listener.
 */
public class BarcodeGenerator {

    /**
     * Use this method to generate your custom barcode
     * @param text - basic text on which barcode is generated
     * @param listener - listener to handle method's callbacks
     * @param imageId - used to differentiate barcode bitmaps
     */
    public void generateBarcode(String text, BarcodeGeneratorListener listener,
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

    /**
     * Implement this class to handle BarcodeGenerator callbacks
     */
    public interface BarcodeGeneratorListener {

        /**
         * BarcodeGenerator callback method
         * @param bitmap - barcode bitmap
         * @param imageId - used to differentiate barcode bitmaps
         */
        void onBarcodeGenerated(Bitmap bitmap, int imageId);
    }
}
