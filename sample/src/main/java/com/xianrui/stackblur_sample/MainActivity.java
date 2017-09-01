package com.xianrui.stackblur_sample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.xianrui.stackblur.StackBlurManager;

public class MainActivity extends AppCompatActivity {

    ImageView mImageView;
    SeekBar mSeekBar;

    Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mImageView = (ImageView) findViewById(R.id.image);
        mSeekBar = (SeekBar) findViewById(R.id.seek_bar);

        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.test_image);

        mImageView.setImageBitmap(mBitmap);


        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    mImageView.setImageBitmap(StackBlurManager.processNatively(mBitmap, i));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
