package timuruktus.ru.commerceproject1;

import android.graphics.Bitmap;

public interface BarcodeGeneratorListener {

    void onBarcodeGenerated(Bitmap bitmap, int imageId);
}
