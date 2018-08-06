package timuruktus.ru.commerceproject1;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Use the {@link DevInfoFragment#getInstance} factory method to
 * create an instance of this fragment.
 */
public class DevInfoFragment extends Fragment implements BarcodeGeneratorListener {

    private Button copyButton;
    private Button shareButton;
    private TextView imeiText;
    private TextView imei2Text;
    private TextView snText;
    private TextView simidText;
    private ImageView imeiImage;
    private ImageView imei2Image;
    private ImageView snImage;
    private ImageView simidImage;
    private Context context;
    private final static int READ_PHONE_STATE_PERMISSION_REQUEST = 1;
    final static int IMEI_ID = 1;
    final static int IMEI2_ID = 2;
    final static int SN_ID = 3;
    final static int SIMID_ID = 4;


    public DevInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     * @return A new instance of fragment DevInfoFragment.
     */
    public static DevInfoFragment getInstance() {
        return new DevInfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.dev_info_fragment, container, false);
        context = view.getContext();
        copyButton = view.findViewById(R.id.copyBtn);
        shareButton = view.findViewById(R.id.shareBtn);
        imeiText = view.findViewById(R.id.imeiText);
        imei2Text = view.findViewById(R.id.imei2Text);
        snText = view.findViewById(R.id.snText);
        simidText = view.findViewById(R.id.simidText);

        imeiImage = view.findViewById(R.id.imeiImage);
        imei2Image = view.findViewById(R.id.imei2Image);
        snImage = view.findViewById(R.id.snImage);
        simidImage = view.findViewById(R.id.simidImage);
        if(isPermissionsGranted()){
            setDevInfo();
        }else{
            askForPermissions();
        }
        copyButton.setOnClickListener(v -> copyToClipboard());
        shareButton.setOnClickListener(v -> shareInfo());

        return view;

    }

    private boolean isPermissionsGranted(){
        int readPhoneStatePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_PHONE_STATE);
        if(readPhoneStatePermission != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(context, R.string.ask_for_permission, Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }

    private void askForPermissions(){
        requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},
                READ_PHONE_STATE_PERMISSION_REQUEST);
    }

    private void setDevInfo(){
        TelephonyManager tm = context.getSystemService(TelephonyManager.class);
        int height = imeiImage.getHeight();
        int width = imeiImage.getWidth();
        BarcodeGenerator barcodeGenerator = new BarcodeGenerator();
        try {
            imeiText.setText("" + tm.getDeviceId(1));
            barcodeGenerator.generateBarcode(height, width, tm.getDeviceId(1), this, IMEI_ID);
            imei2Text.setText("" + tm.getDeviceId(2));
            barcodeGenerator.generateBarcode(height, width, tm.getDeviceId(2), this, IMEI2_ID);
            snText.setText(android.os.Build.SERIAL);
            barcodeGenerator.generateBarcode(height, width, android.os.Build.SERIAL, this, SN_ID);
            simidText.setText("" + tm.getSimSerialNumber());
            barcodeGenerator.generateBarcode(height, width, tm.getSimSerialNumber(), this, SIMID_ID);
        }catch (SecurityException ex){
            ex.printStackTrace();
        }
    }

    private void shareInfo(){
        if(!isPermissionsGranted()){
            askForPermissions();
            return;
        }
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, buildTextInfo());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void copyToClipboard(){
        if(!isPermissionsGranted()){
            askForPermissions();
            return;
        }
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", buildTextInfo());
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
        }
        Toast.makeText(context, R.string.success_copy, Toast.LENGTH_SHORT).show();
    }

    private String buildTextInfo(){
        return "IMEI:" + imeiText.getText().toString()
                + ";IMEI2:" + imei2Text.getText().toString()
                + ";SN:" + snText.getText().toString()
                + ";SIMID:" + simidText.getText().toString() + ";";
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READ_PHONE_STATE_PERMISSION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setDevInfo();
                } else {
                    Toast.makeText(context, R.string.ask_for_permission, Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    @Override
    public void onBarcodeGenerated(Bitmap bitmap,@IdRes int imageId) {

        switch (imageId){
            case IMEI_ID:
                imeiImage.setImageBitmap(bitmap);
                break;
            case IMEI2_ID:
                imei2Image.setImageBitmap(bitmap);
                break;
            case SN_ID:
                snImage.setImageBitmap(bitmap);
                break;
            case SIMID_ID:
                simidImage.setImageBitmap(bitmap);
                break;

        }

    }
}
