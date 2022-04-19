package com.example.appnote_3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class Add_Activity extends AppCompatActivity {
    final String DATABASE_NAME = "appNote.db";
    private ImageView imgV_back, imgV_done, imgV_img_add, img_icon_check;
    private EditText editText_title, editText_content, editText_time;
    private Button button_date, button_time;
    private final int REQUEST_CHOOSE_PHOTO = 123;
    private DatePickerDialog datePickerDialog;
    private int hour, minute;
    private Uri uri = null;

    private Calendar calendar = Calendar.getInstance();
    private final int year = calendar.get(Calendar.YEAR);
    private final int month = calendar.get(Calendar.MONTH);
    private final int day = calendar.get(Calendar.DAY_OF_MONTH);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        init();
        editText_time.setFocusable(false);

        initDataPicker();
        button_date.setText(getTodayDate());

        imgV_img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhoto();
            }
        });

//DONE
        imgV_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert();
            }
        });

//        BTN_CHECK TO show Button
        img_icon_check.setOnClickListener(new View.OnClickListener() {
            boolean flag = false;

            @Override
            public void onClick(View view) {
                if (!flag) {
                    img_icon_check.setImageResource(R.drawable.ic_checkbox_checked);
                    button_date.setVisibility(View.VISIBLE);
                    button_time.setVisibility(View.VISIBLE);
                    flag = true;
                } else {
                    img_icon_check.setImageResource(R.drawable.ic_checkbox_uncheck);
                    button_date.setVisibility(View.GONE);
                    button_time.setVisibility(View.GONE);
                    flag = false;
                }
            }
        });


        imgV_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    //        Insert
    private void insert() {
        String title = editText_title.getText().toString();
        String content = editText_content.getText().toString();
        String time = button_time.getText().toString();
        String day = button_date.getText().toString();

        ContentValues contentValues = new ContentValues();
//        PUT dữ liệu
        contentValues.put("title", title);
        contentValues.put("content", content);
        contentValues.put("day", day);
        contentValues.put("time", time);
        if (uri != null) {
            byte[] img = getByteArrayFromImageView(imgV_img_add);

            contentValues.put("img", img);

//            Log.d("CHECK_IMG" , im)
        } else {
//            Toast.makeText(this, "BBB", Toast.LENGTH_SHORT).show();
            contentValues.put("img", new byte[]{});
        }

        SQLiteDatabase database = Database.initDatabase(this, "appNote.db");
        database.insert("ghichu", null, contentValues);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    //      GetByte
    private byte[] getByteArrayFromImageView(ImageView imageView) {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        ByteArrayOutputStream BoutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, BoutputStream);
        byte[] byteArray = BoutputStream.toByteArray();
        return byteArray;
    }


    private void init() {
        imgV_back = findViewById(R.id.id_ivBack);
        imgV_done = findViewById(R.id.id_ivDone);
        imgV_img_add = findViewById(R.id.id_imgV_anh);
        img_icon_check = findViewById(R.id.id_imgV_uncheck_1);
        editText_title = findViewById(R.id.id_edt_title);
        editText_content = findViewById(R.id.id_edt_content);
        editText_time = findViewById(R.id.id_edit_time);
        button_date = findViewById(R.id.id_btn_chonngay);
        button_time = findViewById(R.id.id_btn_chongio);

//      SET GONE BUTTON
        button_date.setVisibility(View.GONE);
        button_time.setVisibility(View.GONE);

//      SET COLOR
        imgV_back.setColorFilter(Color.parseColor("#686EFE"), PorterDuff.Mode.SRC_IN);
        imgV_done.setColorFilter(Color.parseColor("#686EFE"), PorterDuff.Mode.SRC_IN);
        imgV_img_add.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
        img_icon_check.setColorFilter(Color.parseColor("#686EFE"), PorterDuff.Mode.SRC_IN);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CHOOSE_PHOTO) {
                try {
                    uri = data.getData();
                    Uri imgURI = data.getData();
                    InputStream is = getContentResolver().openInputStream(imgURI);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    imgV_img_add.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        month = month + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
    }


    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectHour, int selectMinute) {
                hour = selectHour;
                minute = selectMinute;
                button_time.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("Chọn giờ");
        timePickerDialog.show();
    }

    private void initDataPicker() {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                button_date.setText(date);
            }
        };

        datePickerDialog = new DatePickerDialog(this, onDateSetListener, year, month, day);

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

    public void openDatePicker(View view) {
        datePickerDialog.setTitle("Chọn ngày");
        datePickerDialog.show();

    }

}