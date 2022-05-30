package com.creative.share.apps.wash_squad.activities_fragments.activity_sign_in;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_login_option.LoginOptionActivity;
import com.creative.share.apps.wash_squad.databinding.ActivitySignInBinding;
import com.creative.share.apps.wash_squad.language.LanguageHelper;
import com.creative.share.apps.wash_squad.models.ServiceDataModel;
import com.creative.share.apps.wash_squad.models.UserModel;
import com.creative.share.apps.wash_squad.preferences.Preferences;

import java.util.Locale;

import io.paperdb.Paper;

public class SignInActivity extends AppCompatActivity {
    private ActivitySignInBinding binding;
    private int fragment_count = 0;
    private FragmentManager manager;
    private Fragment_Language fragment_language;
    private Fragment_Sign_In fragment_sign_in;
    private Fragment_Sign_Up fragment_sign_up;
    private Fragment_Code_Verification fragment_code_verification;
    private Preferences preferences;
    private Fragment_ForgetPassword fragment_forgetpass;
    private Fragment_Newpass fragment_newpass;
    private String type;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);
        manager = getSupportFragmentManager();
        preferences = Preferences.newInstance();
        getDatafromintent();
        if (savedInstanceState == null) {
            //if (preferences.isLangSelected(this)) {
                if (type!=null&&type.equals("quick")) {
                    displayFragmentSignUp();
                } else {
                    displayFragmentSignIn();
                }
//            } else {
//                displayFragmentLanguage();
//            }
        }
    }

    private void displayFragmentLanguage() {
        fragment_language = Fragment_Language.newInstance();

        manager.beginTransaction().add(R.id.fragment_sign_in_container, fragment_language, "fragment_language").addToBackStack("fragment_language").commit();

    }

    public void displayFragmentForgetpass() {
        fragment_count++;
        fragment_forgetpass = Fragment_ForgetPassword.newInstance();

        manager.beginTransaction().add(R.id.fragment_sign_in_container, fragment_forgetpass, "fragment_forgetpass").addToBackStack("fragment_forgetpass").commit();

    }

    public void displayFragmentCodeVerification(UserModel userModel, int type) {
        fragment_count++;
        fragment_code_verification = Fragment_Code_Verification.newInstance(userModel, type);

        manager.beginTransaction().add(R.id.fragment_sign_in_container, fragment_code_verification, "fragment_code_verification").addToBackStack("fragment_code_verification").commit();

    }

    public void displayFragmentNewpass(UserModel userModel) {
        fragment_count++;
        fragment_newpass = Fragment_Newpass.newInstance(userModel, 1);

        manager.beginTransaction().add(R.id.fragment_sign_in_container, fragment_newpass, "fragment_newpass").addToBackStack("fragment_newpass").commit();

    }

    public void displayFragmentSignIn() {
        fragment_count++;
        fragment_sign_in = Fragment_Sign_In.newInstance();

        manager.beginTransaction().add(R.id.fragment_sign_in_container, fragment_sign_in, "fragment_sign_in").addToBackStack("fragment_sign_in").commit();

    }

    public void displayFragmentSignUp() {
        fragment_count++;

        fragment_sign_up = Fragment_Sign_Up.newInstance();

        manager.beginTransaction().add(R.id.fragment_sign_in_container, fragment_sign_up, "fragment_sign_up").addToBackStack("fragment_sign_up").commit();

    }

    public void refreshActivity(String lang) {
        Paper.init(this);
        Paper.book().write("lang", lang);
        preferences.selectedLanguage(this, lang);
        preferences.saveSelectedLanguage(this);
        LanguageHelper.setNewLocale(this, lang);
        Intent intent = getIntent();
        finish();
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        back();
    }

    public void back() {
        if (fragment_count > 1) {
            fragment_count--;
            super.onBackPressed();
        } else {
            Intent intent=new Intent(SignInActivity.this, LoginOptionActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void getDatafromintent() {
        Intent intent = getIntent();
        if (intent != null && intent.getStringExtra("data") != null) {
            type = intent.getStringExtra("data");


        }
    }


}
