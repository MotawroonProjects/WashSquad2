package com.creative.share.apps.wash_squad.activities_fragments.calendar_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.databinding.ActivityCalendarBinding;
import com.creative.share.apps.wash_squad.interfaces.Listeners;
import com.creative.share.apps.wash_squad.language.LanguageHelper;

import java.util.Calendar;
import java.util.Locale;

import io.paperdb.Paper;

public class CalendarActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityCalendarBinding binding;
    private String lang;
    private  Calendar calendar = Calendar.getInstance();
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_calendar);
        initView();

    }


    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.calendar.setMinDate(calendar.getTimeInMillis());
        binding.calendar.setOnDateChangeListener((calendarView, i, i1, i2) -> {
            Log.e("date",i+"/"+(i1+1)+"/"+i2);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR,i);
            calendar.set(Calendar.MONTH,i1);
            calendar.set(Calendar.DAY_OF_MONTH,i2);

            Intent intent = getIntent();
            if (intent!=null)
            {
                intent.putExtra("date",calendar.getTimeInMillis());
                setResult(RESULT_OK,intent);

            }
            finish();

        });


    }

    @Override
    public void back() {
        finish();
    }

}
