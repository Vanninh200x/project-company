package com.example.appnote_3;


import static java.lang.String.*;

import androidx.appcompat.app.AppCompatActivity;


import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import Alarm.AlarmBrodcast;

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
    private boolean flag = false;

    private String timeTonotify;
    private String DayAlert = "";
    private String TimeAlert;
    private char aChar;


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
            textView_update_info.setText("Cập nhật lần cuối: " + updatetime);
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
                                imageView_unCheck_detail.setImageResource(R.drawable.ic_checkbox_uncheck);
                                buttonTime.setVisibility(View.GONE);
                                buttonDay.setVisibility(View.GONE);
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

                                    @Override
                                    public void onClick(View view) {
                                        if (!flag) {
                                            imageView_unCheck_detail.setImageResource(R.drawable.ic_checkbox_checked);
                                            buttonTime.setVisibility(View.VISIBLE);
                                            buttonDay.setVisibility(View.VISIBLE);
                                            flag = true;
                                        } else {
                                            imageView_unCheck_detail.setImageResource(R.drawable.ic_checkbox_uncheck);
                                            buttonDay.setVisibility(View.GONE);
                                            buttonTime.setVisibility(View.GONE);
                                            flag = false;
                                        }
                                    }
                                });

//                                ImgView Done!
                                imageView_done.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        update();
                                        Log.d("CHECK_T", getTodayDate() + " " + getCurrentTime());

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


        buttonDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate();
            }
        });

        buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTime();
            }
        });
    }


    private void setAlarm(String title, String content, String day, String time, byte[] img) {

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);                   //assigining alaram manager object to set alaram

        Intent intent = new Intent(getApplicationContext(), AlarmBrodcast.class);
        intent.putExtra("title", title);                                                       //sending data to alarm class to create channel and notification
        intent.putExtra("content", content);
        intent.putExtra("day", day);
        intent.putExtra("time", time);
        intent.putExtra("img", img);

        Log.e("CHECK_TI", title);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String dateandtime = day + " " + timeTonotify;
//        Log.d("KIEMTRA", dateandtime);
//        Log.e("CHECK_IMG", img + "");
        DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
        try {
            Date date1 = formatter.parse(dateandtime);
//            Log.d("KIEMTRA_1", date1.toString());
            am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);

            Toast.makeText(getApplicationContext(), "Editing Success", Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Intent intentBack = new Intent(getApplicationContext(), MainActivity.class);
        intentBack.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentBack);
    }


    public String FormatTime(int hour, int minute) {                                                //this method converts the time into 12hr farmat and assigns am or pm

        String time;
        time = "";
        String formattedMinute;

        if (minute / 10 == 0) {
            formattedMinute = "0" + minute;
        } else {
            formattedMinute = "" + minute;
        }


        if (hour == 0) {
            time = "12" + ":" + formattedMinute + " AM";
        } else if (hour < 12) {
            time = hour + ":" + formattedMinute + " AM";
        } else if (hour == 12) {
            time = "12" + ":" + formattedMinute + " PM";
        } else {
            int temp = hour - 12;
            time = temp + ":" + formattedMinute + " PM";
        }
        return time;
    }

    private void selectTime() {                                                                     //this method performs the time picker task
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                timeTonotify = i + ":" + i1;                                                        //temp variable to store the time to set alarm
                buttonTime.setText(String.format(Locale.getDefault(), "%02d:%02d", i, i1));
                TimeAlert = i + ":" + i1;
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }

    private void selectDate() {                                                                     //this method performs the date picker task
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                buttonDay.setText(day + " Tháng " + (month + 1) + " " + year);//sets the selected date as test for button
                DayAlert = day + "-" + (month + 1) + "-" + year;
//                Log.d("KIEMTRA", DayAlert);
            }

        }, year, month, day);
        datePickerDialog.show();
    }


    private void update() {
        if (isEmpty()) {
            Toast.makeText(this, "Đề nghị nhập đầy đủ", Toast.LENGTH_SHORT).show();
        } else {

            String title = editText_title.getText().toString();
            String content = editText_content.getText().toString();
            String time = buttonTime.getText().toString();
            String day = buttonDay.getText().toString();
            String updatetime = getTodayDate() + ". " + getCurrentTime();

            ContentValues contentValues = new ContentValues();
//            contentValues.put("id", id);
            contentValues.put("title", title);
            Log.d("CHECK_TITLE", title);
            contentValues.put("content", content);
            contentValues.put("day", day);
            contentValues.put("time", time);
            contentValues.put("updatetime", updatetime);


            if (uri != null || img.length > 0) {
                byte[] img = getByteArrayFromImageView(imageView_img_dd);
                contentValues.put("img", img);
            } else {
                contentValues.put("img", new byte[]{});
            }
            database.update("ghichu", contentValues, "id = ?", new String[]{id + ""});

//            if (uri != null || img.length > 0) {
//                img = getByteArrayFromImageView(imageView_img_dd);
//                Log.e("CHECK_IMG_DETAIL", img +"");
//                contentValues.put("img", img);
//                database.update("ghichu",contentValues, "id= ?", new String[]{id + ""});
//                setAlarm(title, content, DayAlert, time, img);
//            } else {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        RelativeLayout layout = (RelativeLayout) findViewById(R.id.id_img_null_add);
//                        layout.setDrawingCacheEnabled(true);
//                        Bitmap bitmap = Bitmap.createBitmap(layout.getDrawingCache());
//                        layout.setDrawingCacheEnabled(false);
//
//                        //CONVERT BITMAP TO BYTE
//                        ByteArrayOutputStream BoutputStream = new ByteArrayOutputStream();
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, BoutputStream);
//                        byte[] byteArray = BoutputStream.toByteArray();
//                        img = byteArray;
//                        TextView textView_aChar = findViewById(R.id.id_textView_wImg_Null);
//                        aChar = title.charAt(0);
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                contentValues.put("img", new byte[]{});
//                                database.update("ghichu",contentValues, "id= ?", new String[]{id + ""});
//                                textView_aChar.setText(String.valueOf(aChar));
//                                setAlarm(title, content, DayAlert, time, img);
//                            }
//                        });
//                    }
//                }).start();
//            }
//
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
                    Uri imgURI = data.getData();
                    uri = imgURI;
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
        if (editText_title.getText().toString().isEmpty() || editText_content.getText().toString().isEmpty() || flag == false) {
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







    private String getCurrentTime() {
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


}