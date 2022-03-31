package com.creative.share.apps.wash_squad.activities_fragments.activity_print;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.adapters.PdfGenerator;
import com.creative.share.apps.wash_squad.adapters.PdfGeneratorListener;
import com.creative.share.apps.wash_squad.databinding.ActivityPaypalBinding;
import com.creative.share.apps.wash_squad.databinding.ActivityWebviewBinding;
import com.creative.share.apps.wash_squad.interfaces.Listeners;
import com.creative.share.apps.wash_squad.language.LanguageHelper;
import com.creative.share.apps.wash_squad.models.FailureResponse;
import com.creative.share.apps.wash_squad.models.SuccessResponse;
import com.creative.share.apps.wash_squad.services.ServiceDownload;

import java.io.File;
import java.util.Locale;

import io.paperdb.Paper;

public class PrintActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityWebviewBinding binding;
    private String videoPath = "";
    private String lang;
    private final String write_perm = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final int write_req = 100;
    private boolean isPermissionGranted = false;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final int write_REQ = 1;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "ar")));

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_webview);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        initView();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        if (getIntent().getStringExtra("url") != null) {
            videoPath = getIntent().getStringExtra("url");
        }
        setUpWebView();

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setUpWebView() {
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        binding.webView.getSettings().setBuiltInZoomControls(false);
        binding.webView.setDrawingCacheEnabled(true);
        binding.webView.enableSlowWholeDocumentDraw();
        binding.webView.performClick();
        binding.webView.loadUrl(videoPath);

        binding.webView.setWebViewClient(new WebViewClient() {
                                             @Override
                                             public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                                 super.onPageStarted(view, url, favicon);
                                             }

                                             @Override
                                             public void onPageFinished(WebView view, String url) {
                                                 if(url.contains("dowanloadinvoice")){
                                                     String name = "wash_squad"+System.currentTimeMillis();
                                                     Intent intent = new Intent(PrintActivity.this, ServiceDownload.class);
                                                     intent.putExtra("file_url", url);
                                                     intent.putExtra("file_name", name);
                                                     startService(intent);
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
        binding.btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkWritePermission();
            }
        });
    }

    private void checkWritePermission() {

        if (ContextCompat.checkSelfPermission(this, write_permission) == PackageManager.PERMISSION_GRANTED
        ) {
            startService();

        } else {
            ActivityCompat.requestPermissions(this, new String[]{write_permission}, write_REQ);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == write_REQ) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                startService();
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void printpdf() {

        try {
            String file;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
                file = getExternalFilesDir(Environment.DIRECTORY_DCIM) + "/";
            } else {
                file = Environment.getExternalStorageDirectory().toString() + "/";
            }

            PdfGenerator.getBuilder()
                    .setContext(this)
                    .fromViewSource()
                    .fromView(binding.getRoot().findViewById(R.id.webView))
                    /* "fromLayoutXML()" takes array of layout resources.
                     * You can also invoke "fromLayoutXMLList()" method here which takes list of layout resources instead of array. */
                    //.setPageSize(PdfGenerator.PageSize.A4)
                    /* It takes default page size like A4,A5. You can also set custom page size in pixel
                     * by calling ".setCustomPageSize(int widthInPX, int heightInPX)" here. */
                    .setFileName("FirstPdf")
                    .setFolderName(file)
                    /* It is folder name. If you set the folder name like this pattern (FolderA/FolderB/FolderC), then
                     * FolderA creates first.Then FolderB inside FolderB and also FolderC inside the FolderB and finally
                     * the pdf file named "Test-PDF.pdf" will be store inside the FolderB. */
                    .openPDFafterGeneration(true)
                    /* It true then the generated pdf will be shown after generated. */
                    .build(new PdfGeneratorListener() {
                        @Override
                        public void onFailure(FailureResponse failureResponse) {
                            super.onFailure(failureResponse);
                            Log.d(TAG, "onFailure: " + failureResponse.getErrorMessage());
                            /* If pdf is not generated by an error then you will findout the reason behind it
                             * from this FailureResponse. */
                            //Toast.makeText(MainActivity.this, "Failure : "+failureResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                            // Toast.makeText(getContext(), "" + failureResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void showLog(String log) {
                            super.showLog(log);
                            Log.d(TAG, "log: " + log);
                            /*It shows logs of events inside the pdf generation process*/
                        }

                        @Override
                        public void onStartPDFGeneration() {

                        }

                        @Override
                        public void onFinishPDFGeneration() {

                        }

                        @Override
                        public void onSuccess(SuccessResponse response) {
                            super.onSuccess(response);
                            /* If PDF is generated successfully then you will find SuccessResponse
                             * which holds the PdfDocument,File and path (where generated pdf is stored)*/
                            //Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            // Log.d(TAG, "Success: " + response.getPath());
                            String file1 = response.getPath();
                            Uri path = Uri.fromFile(new File(file1));
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(path, "application/pdf");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    });

        } catch (Exception e) {
            android.util.Log.e("sssssss", e.getMessage());
        }
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
    private void startService(){
        checkWritePermission();
    }
    @Override
    public void back() {
        finish();
    }
}
