package website.lizihanglove.xzing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Hashtable;

/**
 * 二维码查看界面
 * <p>
 * Created by lizihanglove on 2017/5/19.
 */

public class ZxingShowActivity extends AppCompatActivity {
    private static int IMAGE_HALFWIDTH = 80;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zxing);
        ImageView view = (ImageView) findViewById(R.id.show);
        Bitmap bitmap = null;
        String str = "www.lizihanglove.website";
        Bitmap logo = null;
        Bitmap bg = null;
        int order = getIntent().getIntExtra("generate", 0);
        switch (order) {
            case 1:
                bitmap = generateXzing(str);
                break;
            case 2:
                logo = BitmapFactory.decodeResource(getResources(), R.mipmap.logo);
                bitmap = generateXzingWithLogo(str, 400, logo);
                break;
            case 3:
                bg = BitmapFactory.decodeResource(getResources(), R.mipmap.bg);
                bitmap = QRCodeUtil.createQRCodeBitmap(str, 400, bg);
                break;
            case 4:
                logo = BitmapFactory.decodeResource(getResources(), R.mipmap.logo);
                bg = BitmapFactory.decodeResource(getResources(), R.mipmap.bg);
                bitmap = QRCodeUtil.createQRCodeBitmap(str, 400,bg, logo,50f);
                break;
        }
        view.setImageBitmap(bitmap);

    }

    /**
     * 生成默认宽高的二维码
     *
     * @param str
     * @return
     */
    private Bitmap generateXzing(String str) {
        Bitmap bitmap = null;
        BitMatrix result = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            result = multiFormatWriter.encode(str, BarcodeFormat.QR_CODE, 300, 300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(result);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException iae) {
            return null;
        }
        return bitmap;
    }

    /**
     * 生成自定义宽高的二维码
     *
     * @param str
     * @param size
     * @return
     */
    public Bitmap generateXzing(String str, int size) {
        Bitmap bitmap = null;
        BitMatrix result = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            result = multiFormatWriter.encode(str, BarcodeFormat.QR_CODE, size, size);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(result);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException iae) {
            return null;
        }
        return bitmap;
    }

    public Bitmap generateXzingWithLogo(String str, int size, Bitmap logo) {
        try {
            IMAGE_HALFWIDTH = size / 10;
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            /*
             * 设置容错级别，默认为ErrorCorrectionLevel.L
             * 因为中间加入logo所以建议你把容错级别调至H,否则可能会出现识别不了
             */
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            BitMatrix bitMatrix = new QRCodeWriter().encode(str,
                    BarcodeFormat.QR_CODE, size, size, hints);

            int width = bitMatrix.getWidth();//矩阵高度
            int height = bitMatrix.getHeight();//矩阵宽度
            int halfW = width / 2;
            int halfH = height / 2;

            Matrix m = new Matrix();
            float sx = (float) 2 * IMAGE_HALFWIDTH / logo.getWidth();
            float sy = (float) 2 * IMAGE_HALFWIDTH
                    / logo.getHeight();
            m.setScale(sx, sy);
            //设置缩放信息
            //将logo图片按martix设置的信息缩放
            logo = Bitmap.createBitmap(logo, 0, 0,
                    logo.getWidth(), logo.getHeight(), m, false);

            int[] pixels = new int[size * size];
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH
                            && y > halfH - IMAGE_HALFWIDTH
                            && y < halfH + IMAGE_HALFWIDTH) {
                        //该位置用于存放图片信息
                        //记录图片每个像素信息
                        pixels[y * width + x] = logo.getPixel(x - halfW
                                + IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH);
                    } else {
                        if (bitMatrix.get(x, y)) {
                            pixels[y * size + x] = 0xff000000;
                        } else {
                            pixels[y * size + x] = 0xffffffff;
                        }
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(size, size,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}