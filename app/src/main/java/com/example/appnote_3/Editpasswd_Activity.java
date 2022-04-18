package com.example.appnote_3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class Editpasswd_Activity extends AppCompatActivity {

    private ImageView imageView_back, imageView_done;
    private EditText editText_passwd1, editText_passwd2;
    private TextInputLayout textInputLayout_1, textInputLayout_2;
    SharedPreferences sharedPreferences;
//    private int id = -1;
    private static final String SHARE_PRE_NAME = "mypref";
    private static final String KEY_PASS = "passwd";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpasswd);
        init();

        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        imageView_done.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                validatePassword();
            }
        });


    }

    private void validatePassword() {
        String passwd1 = editText_passwd1.getText().toString().trim();
        String passwd2 = editText_passwd2.getText().toString().trim();

        if (passwd1.isEmpty() && passwd2.isEmpty()) {
            textInputLayout_1.setError("Đề nghị nhập mật khẩu");
            textInputLayout_2.setError("Đề nghị nhập mật khẩu");
        }

        if (passwd1.isEmpty() && !passwd2.isEmpty()) {
            textInputLayout_1.setError("Đề nghị nhập mật khẩu");
            textInputLayout_2.setErrorEnabled(false);
        }

        if (!passwd1.equals(passwd2) && !passwd1.isEmpty()) {
            textInputLayout_1.setErrorEnabled(false);
            textInputLayout_2.setError("Đề nghị nhập mật khẩu giống nhau");
        }

        if (passwd1.equals(passwd2) && !passwd1.isEmpty()) {
            textInputLayout_1.setErrorEnabled(false);
            textInputLayout_2.setErrorEnabled(false);
        }

        if (passwd1.equals(passwd2) && !passwd1.isEmpty()) {
            sharedPreferences = getSharedPreferences(SHARE_PRE_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(KEY_PASS, passwd1);

            editor.apply();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

    }


    private void init() {
        imageView_back = findViewById(R.id.id_ivBack);
        imageView_done = findViewById(R.id.id_ivDone);
        editText_passwd1 = findViewById(R.id.id_editT_passwd1);
        editText_passwd2 = findViewById(R.id.id_editT_passwd2);
        textInputLayout_1 = findViewById(R.id.id_textIPLayout_1);
        textInputLayout_2 = findViewById(R.id.id_textIPLayout_2);

//      To String

//      SETCOLOR

        imageView_back.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
        imageView_done.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);

    }
}