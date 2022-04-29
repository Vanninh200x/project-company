package com.example.appnote_3;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdapterNote extends BaseAdapter {
    private Activity context;
    private List<oneNote_class> list_oneNote;
//    Khai bao


    public AdapterNote(Activity context, List<oneNote_class> list_oneNote) {
        this.context = context;
        this.list_oneNote = list_oneNote;
    }

    @Override
    public int getCount() {
        return list_oneNote.size();
    }

    @Override
    public Integer getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void filterList(ArrayList<oneNote_class> filterList) {
        list_oneNote  = filterList;
        notifyDataSetChanged();
    }


    public class ViewHolder {
        ImageView imageView_img, imageView_clock;
        TextView textView_title, textView_content, textView_day, textView_time, textView_wImg_Null;
        RelativeLayout rlvNull;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_one_note, null);
            holder.imageView_img = view.findViewById(R.id.id_img_dd);
            holder.textView_title = view.findViewById(R.id.id_textV_title);
            holder.textView_content = view.findViewById(R.id.id_textV_content);
            holder.textView_day = view.findViewById(R.id.id_textV_day);
            holder.textView_time = view.findViewById(R.id.id_textV_time);
            holder.imageView_clock = view.findViewById(R.id.id_imgV_clock);
            holder.rlvNull = view.findViewById(R.id.id_img_null);
            holder.textView_wImg_Null = view.findViewById(R.id.id_textView_wImg_Null);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.imageView_clock.setColorFilter(Color.parseColor("#999999"), PorterDuff.Mode.SRC_IN);
        oneNote_class note = list_oneNote.get(i);
        holder.textView_title.setText(note.getTextV_title());
        holder.textView_content.setText(note.getTextV_content());
        holder.textView_day.setText(note.getTextV_day());
        holder.textView_time.setText(note.getTextV_time());

        if (note.getImg_font().length > 0) {
            holder.rlvNull.setVisibility(View.GONE);
            holder.imageView_img.setVisibility(View.VISIBLE);
            Bitmap bitmap_img = BitmapFactory.decodeByteArray(note.getImg_font(), 0, note.getImg_font().length);
            holder.imageView_img.setImageBitmap(bitmap_img);
        } else {
            holder.imageView_img.setVisibility(View.GONE);
            holder.rlvNull.setVisibility(View.VISIBLE);
            char s = note.getTextV_title().charAt(0);
            holder.textView_wImg_Null.setText(String.valueOf(s));
        }
        return view;
    }

}
