package com.gungoronline.photto;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.gungoronline.photto.Model.BitmapType;
import com.gungoronline.photto.Model.Photto;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        File[] files = new File[]{new File("/storage/emulated/0/picture1.jpg"),new File("/storage/emulated/0/picture2.jpg")};
        Bitmap b = new Photto.BitmapBuilder(getApplicationContext(),files,"", BitmapType.TYPE_3).build();
        ImageView iv = new ImageView(getApplicationContext());
        iv.setImageBitmap(b);

        setContentView(iv);//R.layout.activity_main
    }
}
