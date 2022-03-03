package com.creative.share.apps.wash_squad.activities_fragments.activity_intro;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_sign_in.SignInActivity;
import com.creative.share.apps.wash_squad.databinding.ActivityIntroBinding;
import com.creative.share.apps.wash_squad.language.LanguageHelper;
import com.creative.share.apps.wash_squad.preferences.Preferences;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.Locale;

import io.paperdb.Paper;

public class Intro_Activity extends AppCompatActivity {
    ActivityIntroBinding binding;
    private Intro_Pager_Adapter intro_pager_adapter;
    Preferences preferences;
    private int count = 0;
    private ArrayList<View> views;
    private TextView[] dots;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro_);
        if (savedInstanceState == null) {
            preferences = Preferences.newInstance();
            intro_pager_adapter = new Intro_Pager_Adapter(this);

            // binding.tab1.setupWithViewPager(binding.viewPager);
            binding.viewPager.setAdapter(intro_pager_adapter);
            addBottomDots(0);
            views = new ArrayList<>();
            binding.btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int current = binding.viewPager.getCurrentItem() + 1;
                    if (current < 3) {
                        // move to next screen
                        binding.viewPager.setCurrentItem(current);
                        if (current == 2) {
                            binding.btnNext.setText(getResources().getString(R.string.Start));
                            binding.btnSkip.setVisibility(View.GONE);
                        }
                    } else {
                        preferences.create_first_time(Intro_Activity.this, false);
                        Intent i = new Intent(Intro_Activity.this, SignInActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            });

            binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    // changing the next button text 'NEXT' / 'GOT IT'
                    addBottomDots(position);
                    if (position == 2) {
                        // last page. make button text to GOT IT
                        binding.btnNext.setText(getString(R.string.Start));
                        binding.btnSkip.setVisibility(View.GONE);
                    } else {
                        // still pages are left
                        binding.btnNext.setText(getString(R.string.next));
                        binding.btnSkip.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            binding.btnSkip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    preferences.create_first_time(Intro_Activity.this, false);
                    Intent i = new Intent(Intro_Activity.this, SignInActivity.class);
                    startActivity(i);
                    finish();
                }
            });

        }
    }

    public class Intro_Pager_Adapter extends PagerAdapter {
        private int[] layouts = new int[]{R.layout.introslider1, R.layout.introslider2, R.layout.introslider3};
        private int images[] = new int[]{R.drawable.slider1, R.drawable.slider2, R.drawable.slider3};
        private LayoutInflater layoutInflater;
        Intro_Activity intro_activity;
        View view = null;

        public Intro_Pager_Adapter(Intro_Activity intro_activity) {
            this.intro_activity = intro_activity;
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            try {
                view = layoutInflater.inflate(layouts[position], container, false);
                ImageView imageView = view.findViewById(R.id.item_image);
                //    Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+intro_activity.getPackageName()+"/drawable/" + getResources().getDrawable(image[position]));
                Picasso.with(intro_activity).load(images[position]).placeholder(R.drawable.slider1).fit().memoryPolicy(MemoryPolicy.NO_CACHE).into(imageView);
                container.addView(view);


            } catch (OutOfMemoryError e) {
                Log.e("Error", e.getCause() + "");
            }

            return view;
        }

        @Override
        public int getCount() {
            return (layouts != null) ? layouts.length : 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return (view == obj);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup collection, int position, Object view) {
            /*View itemView = (View)view;
            collection.removeView(itemView);
            itemView=null;*/
        }
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[3];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        binding.layoutDots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            binding.layoutDots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

}
