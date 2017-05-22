package website.lizihanglove.xzing;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.android.Intents;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.Hashtable;

import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {
    private static int IMAGE_HALFWIDTH = 40;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.generate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ZxingShowActivity.class);
                intent.putExtra("generate",1);
                startActivity(intent);
            }
        });
        findViewById(R.id.generate_logo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ZxingShowActivity.class);
                intent.putExtra("generate",2);
                startActivity(intent);
            }
        });
        findViewById(R.id.generate_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ZxingShowActivity.class);
                intent.putExtra("generate",3);
                startActivity(intent);
            }
        });
        findViewById(R.id.generate_bg_logo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ZxingShowActivity.class);
                intent.putExtra("generate",4);
                startActivity(intent);
            }
        });
        findViewById(R.id.scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan();
            }
        });
    }

    private void scan() {
        RxPermissions.getInstance(MainActivity.this)
                .request(Manifest.permission.CAMERA
                       )//多个权限用","隔开
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            //当所有权限都允许之后，返回true
                            Log.i("permissions", "btn_more_sametime：" + aBoolean);
                            ZxingUtil.getInstance().decode(MainActivity.this);
                        } else {
                            //只要有一个权限禁止，返回false，
                            //下一次申请只申请没通过申请的权限
                            Log.i("permissions", "btn_more_sametime：" + aBoolean);
                            Toast.makeText(MainActivity.this,"没有权限！",Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
}

