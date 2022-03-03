package com.creative.share.apps.wash_squad.activities_fragments.activity_splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_home.activity.HomeActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_intro.Intro_Activity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_sign_in.SignInActivity;
import com.creative.share.apps.wash_squad.language.LanguageHelper;
import com.creative.share.apps.wash_squad.preferences.Preferences;
import com.creative.share.apps.wash_squad.tags.Tags;

import java.util.Locale;

import io.paperdb.Paper;

public class SplashActivity extends AppCompatActivity {
    private Animation animation;
    private Preferences preferences;
    private ConstraintLayout cons;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        preferences = Preferences.newInstance();
        animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.lanuch);
        cons = findViewById(R.id.cons);
        cons.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                String session = preferences.getSession(SplashActivity.this);
                if (session.equals(Tags.session_login)) {
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    if (preferences.getisfirsttime(SplashActivity.this) == true) {
                        Intent i = new Intent(SplashActivity.this, Intro_Activity.class);
                        startActivity(i);

                    } else {
                        Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
                        startActivity(intent);
                    }
                    finish();
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
