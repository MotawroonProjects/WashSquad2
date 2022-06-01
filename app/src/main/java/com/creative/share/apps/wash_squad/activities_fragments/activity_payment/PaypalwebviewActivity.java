package com.creative.share.apps.wash_squad.activities_fragments.activity_payment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.databinding.ActivityPaypalBinding;
import com.creative.share.apps.wash_squad.interfaces.Listeners;
import com.creative.share.apps.wash_squad.language.LanguageHelper;

import java.util.Locale;

import io.paperdb.Paper;

public class PaypalwebviewActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityPaypalBinding binding;
    private String videoPath = "";
    private String lang;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "ar")));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_paypal);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        initView();
    }


    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        if (getIntent().getStringExtra("url") != null) {
            videoPath = getIntent().getStringExtra("url");
        }
        setUpWebView();

    }


    private void setUpWebView() {
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        binding.webView.getSettings().setBuiltInZoomControls(false);
        binding.webView.loadUrl(videoPath);
        binding.webView.setWebViewClient(new WebViewClient() {
                                             @Override
                                             public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                                 super.onPageStarted(view, url, favicon);
                                             }

                                             @Override
                                             public void onPageFinished(WebView view, String url) {

                                                 if (url.contains("yes") || url.contains("checkout/done")) {
                                                     setResult(RESULT_OK);
                                                     finish();
                                                     Toast.makeText(PaypalwebviewActivity.this, getString(R.string.payment_suc), Toast.LENGTH_SHORT).show();

                                                 }
                                                 else{
                                                    // Toast.makeText(PaypalwebviewActivity.this, getString(R.string.payment_faild), Toast.LENGTH_SHORT).show();

                                                 }
                                             }

                                             @Override
                                             public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                                                 super.onReceivedError(view, request, error);
                                                 binding.webView.setVisibility(View.INVISIBLE);
                                             }

                                             @Override
                                             public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                                                 super.onReceivedHttpError(view, request, errorResponse);
                                                 binding.webView.setVisibility(View.INVISIBLE);
                                             }
                                         }

        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.webView.onResume();
    }

    @Override
    public void back() {
        finish();
    }
}
