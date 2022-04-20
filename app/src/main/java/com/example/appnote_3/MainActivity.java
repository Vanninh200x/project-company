package com.example.appnote_3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final String DATABASE_NAME = "appNote.db";
    private SQLiteDatabase database;
    private ImageView imgV_menu, imgV_Plus, imgV_search, imageView_clock;
    private EditText editText_Search;

    private ListView listView;
    private ArrayList<oneNote_class> list;
    private AdapterNote adapter;

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    private int id = -1;
    private static final String SHARE_PRE_NAME = "mypref";
    private static final String KEY_PASS = "passwd";
    private SharedPreferences sharedPreferences;
    private String passwd_App = "";


    private EditText editText_passswd_loginAC;
    private TextInputLayout textInputLayout;
    private Button button_login_loginAC;
    private static int CHECK_OPEN_MAINAC_ONETIME=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = Database.initDatabase(this, DATABASE_NAME);
        sharedPreferences = getSharedPreferences(SHARE_PRE_NAME, MODE_PRIVATE);
        passwd_App = sharedPreferences.getString(KEY_PASS, "");
        if (passwd_App.length() > 0 && CHECK_OPEN_MAINAC_ONETIME == 0 ) {
            setContentView(R.layout.activity_login);
            initLogin();
            button_login_loginAC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String realPass = sharedPreferences.getString(KEY_PASS, "");
                    String passInput = editText_passswd_loginAC.getText().toString().trim();
                    if (passInput.equals(realPass)) {
                        CHECK_OPEN_MAINAC_ONETIME++;
                        Log.d("CHECK_KQ_1", CHECK_OPEN_MAINAC_ONETIME +"");
                        setContentView(R.layout.activity_main);
                        init();
                        read();
                        initClick();
                        Log.d("CHECK_PO", CHECK_OPEN_MAINAC_ONETIME +"");
                    } else {
                        textInputLayout.setErrorEnabled(false);
                        textInputLayout.setError("Sai mật khẩu");
                    }
                }
            });
        } else {
            setContentView(R.layout.activity_main);
            init();
            read();
            initClick();
        }
    }


//    private void check_Passs(){
//        String passReal = sharedPreferences.getString(KEY_PASS, null);
//        String passInput = editText_passswd_loginAC.getText().toString();
//
//        if (passInput.equals(passReal)){
//            Intent intent = new Intent(Login_Activity.this, MainActivity.class);
//            startActivity(intent);
//        }else{
//            textInputLayout.setErrorEnabled(false);
//            textInputLayout.setError("Sai mật khẩu");
//            Toast.makeText(this, "Sai mật khẩu", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void initClick() {
        imgV_Plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Add_Activity.class);
                startActivity(intent);
            }
        });

        imgV_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, Detail_Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", list.get(i).getId() + "");
                bundle.putString("title", list.get(i).getTextV_title());
                bundle.putString("content", list.get(i).getTextV_content());
                bundle.putString("time", list.get(i).getTextV_time());
                bundle.putString("day", list.get(i).getTextV_day());
                bundle.putString("updatetime", list.get(i).getTextV_updateTime());

                Log.d("UPDATE", list.get(i).getTextV_updateTime());


// Lỗi do không bundle được ảnh null.
                if (list.get(i).getImg_font().length > 0) {
                    Bitmap b = BitmapFactory.decodeByteArray(list.get(i).getImg_font(), 0, list.get(i).getImg_font().length);
                    ByteArrayOutputStream bs = new ByteArrayOutputStream();
                    b.compress(Bitmap.CompressFormat.JPEG, 50, bs);
                    bundle.putByteArray("img", bs.toByteArray());
                } else {
                    bundle.putByteArray("img", new byte[]{
                    });
                }

                intent.putExtra("dulieu", bundle);
                startActivity(intent);
            }
        });


        editText_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
    }

    //    FILTER : SEARCH
    private void filter(String text) {
        ArrayList<oneNote_class> filterList = new ArrayList<>();
        for (oneNote_class oneNote : list) {
            if (oneNote.getTextV_title().toLowerCase().contains(text.toLowerCase())) {
                filterList.add(oneNote);
            }
        }
        adapter.filterList(filterList);
    }

    private void initLogin() {
        editText_passswd_loginAC = findViewById(R.id.id_editT_passwd);
        button_login_loginAC = findViewById(R.id.id_button_login);
        textInputLayout = findViewById(R.id.id_textIPLayout_loginAC);
    }

    private void init() {
        imgV_menu = findViewById(R.id.id_ivMẹnu);
        imgV_Plus = findViewById(R.id.id_ivPlus);
        imgV_search = findViewById(R.id.id_imgV_main_search);
        editText_Search = findViewById(R.id.id_edt_search);


        imgV_menu.setColorFilter(Color.parseColor("#686EFE"), PorterDuff.Mode.SRC_IN);
        imgV_Plus.setColorFilter(Color.parseColor("#686EFE"), PorterDuff.Mode.SRC_IN);
        imgV_search.setColorFilter(Color.parseColor("#999999"), PorterDuff.Mode.SRC_IN);

//
//        Init layoutDrawer
        navigationView = findViewById(R.id.id_navigation);
        drawerLayout = findViewById(R.id.id_drawerlayout);
        navigationView.setNavigationItemSelectedListener(this);


        listView = findViewById(R.id.id_list_View);
        list = new ArrayList<>();
        adapter = new AdapterNote(this, list);
        listView.setAdapter(adapter);


    }

    private void read() {
        Cursor cursor = database.rawQuery("SELECT*FROM ghichu", null);
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            id = cursor.getInt(0);
            String title = cursor.getString(1);
            String content = cursor.getString(2);
            String day = cursor.getString(3);
            String time = cursor.getString(4);
            byte[] img = cursor.getBlob(5);
            String updateTime = cursor.getString(6);
            list.add(new oneNote_class(id, title, content, day, time, img, updateTime));
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.id_item_passwd:
                Intent intent = new Intent(MainActivity.this, Editpasswd_Activity.class);
                startActivity(intent);
                break;
            case R.id.id_item_notification:
                Intent intent1 = new Intent(MainActivity.this, EditNotifi_Activity.class);
                startActivity(intent1);
                break;
            case R.id.id_item_deleteAll:
//                SQLiteDatabase database = Database.initDatabase(this, "appNote.db");
//
                database.delete("ghichu", null, null);
                finish();
                startActivity(getIntent());
                Toast.makeText(this, "Xóa tất cả", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }

    private String getCurrentTime(){
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String timeNow = currentTime;
        return timeNow;
    }

    private String getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        month = month + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private String makeDateString(int day, int month, int year) {
        return day + " " + getMonthFormat(month) + " " + year;
    }

    private String getMonthFormat(int month) {
        if (month == 1) {
            return "Tháng 1";
        }
        if (month == 2) {
            return "Tháng 2";
        }
        if (month == 3) {
            return "Tháng 3";
        }
        if (month == 4) {
            return "Tháng 4";
        }
        if (month == 5) {
            return "Tháng 5";
        }
        if (month == 6) {
            return "Tháng 6";
        }
        if (month == 7) {
            return "Tháng 7";
        }
        if (month == 8) {
            return "Tháng 8";
        }
        if (month == 9) {
            return "Tháng 9";
        }
        if (month == 10) {
            return "Tháng 10";
        }
        if (month == 11) {
            return "Tháng 11";
        }
        if (month == 12) {
            return "Tháng 12";
        }
//        Trường hợp này không bao giờ xảy ra.!

        return "Tháng 1";
    }
}