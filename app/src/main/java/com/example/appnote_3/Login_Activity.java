package com.example.appnote_3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.nio.file.Files;

public class Login_Activity extends AppCompatActivity {
    private EditText editText_passswd_loginAC;
    private TextInputLayout textInputLayout;
    private Button button_login_loginAC;
    private TextView title;

    private SharedPreferences sharedPreferences;
    private static final String SHARE_PRE_NAME = "mypref";
    private static final String KEY_PASS = "passwd";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        button_login_loginAC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               check_Passs();
            }
        });

    }

    private void check_Passs(){
        String passReal = sharedPreferences.getString(KEY_PASS, null);
        String passInput = editText_passswd_loginAC.getText().toString();

        if (passInput.equals(passReal)){
            Intent intent = new Intent(Login_Activity.this, MainActivity.class);
            startActivity(intent);
        }else{
            textInputLayout.setErrorEnabled(false);
            textInputLayout.setError("Sai mật khẩu");
            Toast.makeText(this, "Sai mật khẩu", Toast.LENGTH_SHORT).show();
        }
    }

    private void init(){
        title = findViewById(R.id.id_textV_title);
        editText_passswd_loginAC = findViewById(R.id.id_editT_passwd);
        button_login_loginAC = findViewById(R.id.id_button_login);
        textInputLayout = findViewById(R.id.id_textIPLayout_loginAC);
        sharedPreferences = getSharedPreferences(SHARE_PRE_NAME, MODE_PRIVATE);
    }
}