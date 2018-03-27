package com.example.wb.testzxing;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wb.testzxing.android.CaptureActivity;
import com.example.wb.testzxing.bean.ZxingConfig;
import com.example.wb.testzxing.common.Constant;
import com.example.wb.testzxing.encode.CodeCreator;
import com.google.zxing.WriterException;

public class MainActivity extends AppCompatActivity {

    private int REQUEST_CODE_SCAN = 111;
    private ImageView contentIv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button scan = (Button) findViewById(R.id.scan);
        Button create = (Button) findViewById(R.id.create);
        contentIv = (ImageView) findViewById(R.id.contentIv);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    //权限还没有授予，需要在这里写申请权限的代码
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 60);
                } else {
                    Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                                /*ZxingConfig是配置类  可以设置是否显示底部布局，闪光灯，相册，是否播放提示音  震动等动能
                                * 也可以不传这个参数
                                * 不传的话  默认都为默认不震动  其他都为true
                                * */
                    ZxingConfig config = new ZxingConfig();
                    config.setPlayBeep(true);
                    config.setShake(true);
                    config.setShowbottomLayout(false);
                    intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                    startActivityForResult(intent, REQUEST_CODE_SCAN);
                }

            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contentEtString = "dfjdfkjdfkdjfkdjfdkjfkdfjd";
                if (TextUtils.isEmpty(contentEtString)) {
                    Toast.makeText(MainActivity.this, "contentEtString不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                Bitmap bitmap = null;
                try {
                    bitmap = CodeCreator.createQRCode(contentEtString, 1000, 1000, null);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                if (bitmap != null) {
                    contentIv.setImageBitmap(bitmap);
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                Toast.makeText(MainActivity.this,content, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
