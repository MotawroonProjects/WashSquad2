package com.creative.share.apps.wash_squad.general_ui_method;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.tags.Tags;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class GeneralMethod {

    @BindingAdapter("error")
    public static void errorValidation(View view, String error) {
        if (view instanceof EditText) {
            EditText ed = (EditText) view;
            ed.setError(error);
        } else if (view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.setError(error);


        }
    }

    @BindingAdapter("image")
    public static void displayImageHome(ImageView imageView, int image_resource)
    {
        imageView.setImageResource(image_resource);
    }


    @BindingAdapter("serviceImage")
    public static void serviceImage(ImageView imageView,String endPoint)
    {
        Log.e("path",Tags.IMAGE_URL+endPoint);
        Picasso.get().load(Uri.parse(Tags.IMAGE_URL+endPoint)).fit().into(imageView);
    }

    @BindingAdapter("profileImage")
    public static void profileImage(CircleImageView imageView, String endPoint)
    {
        Picasso.get().load(Uri.parse(Tags.IMAGE_URL+endPoint)).placeholder(R.drawable.user_profile).fit().into(imageView);
    }

    @BindingAdapter("offerImage")
    public static void offerImage(RoundedImageView imageView, String endPoint)
    {
        Log.e("Dldlldl",Tags.IMAGE_URL+endPoint);
        Picasso.get().load(Uri.parse(Tags.IMAGE_URL+endPoint)).fit().into(imageView);
    }


    @BindingAdapter("couponImage")
    public static void couponImage(RoundedImageView imageView, String endPoint)
    {
        Picasso.get().load(Uri.parse(Tags.IMAGE_URL+endPoint)).fit().into(imageView);
    }



    @BindingAdapter({"date","workTimehoosen","workTime"})
    public static void displayDate (TextView textView,long date,String work_time_choosen,String work_time)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd/MMM",Locale.ENGLISH);
        String m_date = dateFormat.format(new Date(date*1000));
        if(work_time!=null){
        textView.setText(work_time+" "+work_time_choosen+"\n"+String.format("%s",m_date));}
        else {
            textView.setText(String.format("%s",m_date));
        }

    }


    @BindingAdapter({"orderDate"})
    public static void displayOrderDate (TextView textView,long orderDate)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy",Locale.ENGLISH);
        String m_date = dateFormat.format(new Date(orderDate*1000));
        textView.setText(m_date);

    }

    @BindingAdapter("rate")
    public static void rate (SimpleRatingBar simpleRatingBar, double rate)
    {
        SimpleRatingBar.AnimationBuilder builder = simpleRatingBar.getAnimationBuilder()
                .setRatingTarget((float) rate)
                .setDuration(1000)
                .setRepeatCount(0)
                .setInterpolator(new LinearInterpolator());
        builder.start();
    }

    @BindingAdapter({"startTime2","date2"})
    public static void displayTime2(TextView textView, long start_time,long date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String sTime = dateFormat.format(new Date(start_time * 1000));
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyy", Locale.getDefault());
        String d = dateFormat1.format(new Date(date * 1000));
        //   SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);

        //  String eTime = dateFormat2.format(new Date(Long.parseLong(end_time)*1000));

        textView.setText(d+" "+sTime);
    }
    @BindingAdapter({"startTime","date"})
    public static void displayTime(TextView textView, long start_time,long date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
        String sTime = dateFormat.format(new Date(start_time * 1000));
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyy", Locale.ENGLISH);
        String d = dateFormat1.format(new Date(date * 1000));
        //   SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);

        //  String eTime = dateFormat2.format(new Date(Long.parseLong(end_time)*1000));

        textView.setText(d+"\n"+sTime);
    }

    @BindingAdapter({"startTime","timechose","type"})
    public static void displayTime(TextView textView, long start_time,String timechose,String type) {

        if (start_time == 0) {
            textView.setText(timechose + " " + type);

        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
            String sTime = dateFormat.format(new Date(start_time * 1000));

            //   SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);

            //  String eTime = dateFormat2.format(new Date(Long.parseLong(end_time)*1000));

            textView.setText(sTime);
        }
    }
    @BindingAdapter("order_status")
    public static void orderStatus(TextView textView, String status) {
        if (status.equals("new")) {
            textView.setText(textView.getContext().getString(R.string.next1));
        } else if (status.equals("wait")) {
            textView.setText(textView.getContext().getString(R.string.delayed));

        } else if (status.equals("done")) {
            textView.setText(textView.getContext().getString(R.string.service_done));

        }
    }
}
