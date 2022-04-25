package com.example.appnote_3;


import static java.lang.String.*;

import androidx.appcompat.app.AppCompatActivity;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Detail_Activity extends AppCompatActivity {
    final String DATABASE_NAME = "appNote.db";
    private final int REQUEST_CHOOSE_PHOTO = 123;
    private ImageView imageView_back, imageView_more, imageView_img_dd, imageView_clock, imageView_done, imageView_unCheck_detail;
    private EditText editText_title, editText_content, editText_time, editText_day;
    private TextView textView_update_info, textView_title_detail_edit, textView_title_img_null;
    private EditText editText_clock;
    private RelativeLayout rlvClock, rlvChecktime, rlv_img_parent, rlv_img_null;
    private Button buttonTime, buttonDay;
    private DatePickerDialog datePickerDialog;
    private int hour, minute;
    private int id;
    private SQLiteDatabase database;
    private byte[] img;
    private Uri uri = null;
    private static int count=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        database = Database.initDatabase(this, DATABASE_NAME);

        init();
//        initId();
        initDataPicker();

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("dulieu");
        if (bundle != null) {
            String title = bundle.getString("title");
            String content = bundle.getString("content");
            String time = bundle.getString("time");
            String day = bundle.getString("day");
            String updatetime = bundle.getString("updatetime");
            id = Integer.parseInt(bundle.getString("id"));
//            Toast.makeText(this, id + "", Toast.LENGTH_SHORT).show();

//            byte[] img;
            img = bundle.getByteArray("img");
            if (img.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
                imageView_img_dd.setImageBitmap(bitmap);
            } else {
                char s = title.charAt(0);
                textView_title_img_null.setTextSize(25);
                textView_title_img_null.setText(String.valueOf(s));
                imageView_img_dd.setVisibility(View.GONE);
                rlv_img_null.setVisibility(View.VISIBLE);
            }
//            Lỗi không có ảnh ? => ko decode => Length NULL: 14/4 Fix 19/4
            editText_title.setText(title);
            editText_content.setText(content);
            editText_day.setText(day);
            editText_time.setText(time);
            textView_update_info.setText("Cập nhật lần cuối: "+updatetime);
        }


        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imageView_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(Detail_Activity.this, imageView_more);
                popupMenu.getMenuInflater().inflate(R.menu.menu_detail_option, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.id_item_menu_share:
                                Intent intent1 = new Intent(Intent.ACTION_SEND);
                                intent1.setType("text/plain");
                                startActivity(Intent.createChooser(intent1, "Share Using"));
                                break;

                            case R.id.id_item_menu_edit:
                                rlvClock.setVisibility(View.GONE);
                                imageView_more.setVisibility(View.GONE);
                                rlvChecktime.setVisibility(View.VISIBLE);
                                imageView_done.setVisibility(View.VISIBLE);
                                imageView_unCheck_detail.setImageResource(R.drawable.ic_checkbox_checked);
                                buttonTime.setVisibility(View.VISIBLE);
                                buttonDay.setVisibility(View.VISIBLE);
                                buttonTime.setText(editText_time.getText().toString());
                                buttonDay.setText(editText_day.getText().toString());
                                editText_title.setEnabled(true);
                                editText_content.setEnabled(true);
                                editText_clock.setEnabled(true);
                                editText_clock.setFocusable(false);
                                textView_title_detail_edit.setText("Sửa ghi chú");

//                              ImgageDD Click

//                                imageView_img_dd.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                    }
//                                });
//
                                rlv_img_parent.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (img.length != 0) {
                                            choosePhoto();
                                        }
                                        if (img.length == 0) {
                                            choosePhoto();
                                            imageView_img_dd.setVisibility(View.VISIBLE);
                                        }
                                    }
                                });


//                                Button Check
                                imageView_unCheck_detail.setOnClickListener(new View.OnClickListener() {
                                    boolean flag = false;


                                    @Override
                                    public void onClick(View view) {
                                        if (!flag) {
                                            imageView_unCheck_detail.setImageResource(R.drawable.ic_checkbox_checked);
                                            buttonTime.setVisibility(View.VISIBLE);
                                            buttonDay.setVisibility(View.VISIBLE);
                                            flag = true;
                                            count++;
                                        } else {
                                            imageView_unCheck_detail.setImageResource(R.drawable.ic_checkbox_uncheck);
                                            buttonTime.setVisibility(View.GONE);
                                            buttonDay.setVisibility(View.GONE);
                                            flag = false;
                                            count=0;
                                        }
                                    }
                                });

//                                ImgView Done!
                                imageView_done.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        update();
                                        Log.d("CHECK_T", getTodayDate()+" "+ getCurrentTime());
//                                        ContentValues contentValues = new ContentValues();
//                                        String InfoUpdate = (String) contentValues.get("updateTime");
//                                        Log.d("CHECK_T_T", InfoUpdate);

                                    }
                                });

                                break;
                            case R.id.id_item_menu_delete:
                                delete();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }


    private void update() {
        if (isEmpty()){
            Toast.makeText(this, "Đề nghị nhập đầy đủ", Toast.LENGTH_SHORT).show();
        }else{
            String title = editText_title.getText().toString();
            String content = editText_content.getText().toString();
            String time = buttonTime.getText().toString();
            String day = buttonDay.getText().toString();
            String updatetime = getTodayDate() + ". " + getCurrentTime();

            ContentValues contentValues = new ContentValues();
            //contentValues.put("id", id);
            contentValues.put("title", title);
            contentValues.put("content", content);
            contentValues.put("day", day);
            contentValues.put("time", time);
            contentValues.put("updatetime", updatetime);
            if (uri != null){
                byte[] img = getByteArrayFromImageView(imageView_img_dd);
                contentValues.put("img", img);
            }else{
                contentValues.put("img", new byte[]{});
            }


            database.update("ghichu", contentValues, "id = ?", new String[]{id + ""});

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void delete() {
        database.delete("ghichu", "id = ?", new String[]{id + ""});
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    private byte[] getByteArrayFromImageView(ImageView imageView) {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        ByteArrayOutputStream BoutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, BoutputStream);
        byte[] byteArray = BoutputStream.toByteArray();
        return byteArray;
    }

    private void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
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
                    imageView_img_dd.setImageBitmap(bitmap);
                    rlv_img_null.setVisibility(View.GONE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


//    private void initId() {
//
////        Intent intent = getIntent();
////        id = intent.getIntExtra("id",-1);
//        Cursor cursor = database.rawQuery("SELECT * FROM ghichu WHERE id = ?", new String[]{id + ""});
//        cursor.moveToFirst();
//
//    }


    private boolean isEmpty() {
        if (editText_title.getText().toString().isEmpty() || editText_content.getText().toString().isEmpty() || count == 0) {
            return true;
        }
        return false;
    }


    private void init() {
        imageView_back = findViewById(R.id.id_ivBack);
        imageView_more = findViewById(R.id.id_ivMore);
        imageView_clock = findViewById(R.id.id_imgV_clock);
        imageView_img_dd = findViewById(R.id.id_img_detail);
        imageView_done = findViewById(R.id.id_ivDone);
        imageView_unCheck_detail = findViewById(R.id.id_imgV_uncheck_1);

        rlv_img_parent = findViewById(R.id.id_rlv_img);
        rlv_img_null = findViewById(R.id.id_include_imgNull_detail);

        buttonTime = findViewById(R.id.id_btn_chongio);
        buttonDay = findViewById(R.id.id_btn_chonngay);

        editText_title = findViewById(R.id.id_title_detail_ac);
        editText_content = findViewById(R.id.id_detail_content);
        editText_time = findViewById(R.id.id_detail_time);
        editText_day = findViewById(R.id.id_detail_day);

        textView_update_info = findViewById(R.id.id_detail_update_info);
        textView_title_detail_edit = findViewById(R.id.id_tvTitle_AC_detail);
        textView_title_img_null = findViewById(R.id.id_textView_wImg_Null);

        rlvClock = findViewById(R.id.rlv_Clock);
        rlvChecktime = findViewById(R.id.rlv_CheckTime);

        editText_clock = findViewById(R.id.id_edit_time);
//        Set Color

        imageView_clock.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
        imageView_back.setColorFilter(Color.parseColor("#686EFE"), PorterDuff.Mode.SRC_IN);
        imageView_more.setColorFilter(Color.parseColor("#686EFE"), PorterDuff.Mode.SRC_IN);
        imageView_unCheck_detail.setColorFilter(Color.parseColor("#686EFE"), PorterDuff.Mode.SRC_IN);
        imageView_done.setColorFilter(Color.parseColor("#686EFE"), PorterDuff.Mode.SRC_IN);
    }




    public void openDatePicker(View view) {
        datePickerDialog.setTitle("Chọn ngày");
//        datePickerDialog.getDatePicker();
        datePickerDialog.show();
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


    private void initDataPicker() {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(year, month, day);
                buttonDay.setText(date);
            }
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(this, onDateSetListener, year, month, day);
    }

    private String makeDateString(int year, int month, int day) {
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
        return "Tháng 1";
    }


    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectHour, int selectMinute) {
                hour = selectHour;
                minute = selectMinute;
                buttonTime.setText(format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("Chọn giờ");
        timePickerDialog.show();
    }
}