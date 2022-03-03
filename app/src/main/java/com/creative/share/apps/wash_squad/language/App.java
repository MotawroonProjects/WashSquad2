package com.creative.share.apps.wash_squad.language;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.creative.share.apps.wash_squad.preferences.Preferences;
import com.creative.share.apps.wash_squad.share.TypefaceUtil;

public class App extends MultiDexApplication {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageHelper.updateResources(base, Preferences.newInstance().getSelectedLanguage(base)));
    }

    @Override
    public void onCreate() {
        super.onCreate();

        TypefaceUtil.setDefaultFont(this, "DEFAULT", "fonts/ar_font.ttf");
        TypefaceUtil.setDefaultFont(this, "MONOSPACE", "fonts/ar_font.ttf");
        TypefaceUtil.setDefaultFont(this, "SERIF", "fonts/ar_font.ttf");
        TypefaceUtil.setDefaultFont(this, "SANS_SERIF", "fonts/ar_font.ttf");



    }
}
