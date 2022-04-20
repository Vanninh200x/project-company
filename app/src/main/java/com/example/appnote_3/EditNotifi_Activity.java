package com.example.appnote_3;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class EditNotifi_Activity extends AppCompatActivity {
    ImageView imageView_back, imageView_uncheck_box_1, imageView_uncheck_box_2, imageView_uncheck_box_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notifi);
        init();

        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        imageView_uncheck_box_1.setOnClickListener(new View.OnClickListener() {

            boolean flag = false;
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            @Override
            public void onClick(View view) {
                if (!flag) {
                    r.play();
                    imageView_uncheck_box_1.setImageResource(R.drawable.ic_checkbox_checked);
                    flag = true;
                } else {
                    r.stop();
                    imageView_uncheck_box_1.setImageResource(R.drawable.ic_checkbox_uncheck);
                    flag = false;
                }
            }
        });

        imageView_uncheck_box_2.setOnClickListener(new View.OnClickListener() {

            boolean flag = false;

            @Override
            public void onClick(View view) {
                if (!flag) {
                    imageView_uncheck_box_2.setImageResource(R.drawable.ic_checkbox_checked);

                    flag = true;
                } else {
                    imageView_uncheck_box_2.setImageResource(R.drawable.ic_checkbox_uncheck);
                    flag = false;
                }
            }
        });

        imageView_uncheck_box_3.setOnClickListener(new View.OnClickListener() {

            boolean flag = false;

            @Override
            public void onClick(View view) {
                if (!flag) {
                    imageView_uncheck_box_3.setImageResource(R.drawable.ic_checkbox_checked);

                    flag = true;
                } else {
                    imageView_uncheck_box_3.setImageResource(R.drawable.ic_checkbox_uncheck);
                    flag = false;
                }
            }
        });


    }

    private void init() {
        imageView_back = findViewById(R.id.id_ivBack);
        imageView_uncheck_box_1 = findViewById(R.id.id_imgV_uncheck_1);
        imageView_uncheck_box_2 = findViewById(R.id.id_imgV_uncheck_2);
        imageView_uncheck_box_3 = findViewById(R.id.id_imgV_uncheck_3);


//        SETCOLOR
        imageView_back.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
        imageView_uncheck_box_1.setColorFilter(Color.parseColor("#686EFE"), PorterDuff.Mode.SRC_IN);
        imageView_uncheck_box_2.setColorFilter(Color.parseColor("#686EFE"), PorterDuff.Mode.SRC_IN);
        imageView_uncheck_box_3.setColorFilter(Color.parseColor("#686EFE"), PorterDuff.Mode.SRC_IN);
    }
}