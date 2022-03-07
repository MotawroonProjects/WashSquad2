package com.creative.share.apps.wash_squad.activities_fragments.activity_sign_in;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_home.activity.HomeActivity;
import com.creative.share.apps.wash_squad.databinding.DialogAlertBinding;
import com.creative.share.apps.wash_squad.databinding.FragmentCodeVerificationBinding;
import com.creative.share.apps.wash_squad.models.UserModel;
import com.creative.share.apps.wash_squad.preferences.Preferences;
import com.creative.share.apps.wash_squad.remote.Api;
import com.creative.share.apps.wash_squad.share.Common;
import com.creative.share.apps.wash_squad.tags.Tags;

import java.io.IOException;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Code_Verification extends Fragment {
    private static final String TAG = "DATA";
    private static final String TAG2 = "Type";

    private SignInActivity activity;
    private FragmentCodeVerificationBinding binding;
    private boolean canResend = true;
    private UserModel userModel;
    private int type;
    private CountDownTimer countDownTimer;
    private Preferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_code_verification, container, false);
        View view = binding.getRoot();
        initView();
        return view;
    }

    public static Fragment_Code_Verification newInstance(UserModel userModel, int type) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, userModel);
        bundle.putInt(TAG2, type);
        Fragment_Code_Verification fragment_code_verification = new Fragment_Code_Verification();
        fragment_code_verification.setArguments(bundle);
        return fragment_code_verification;
    }

    private void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            userModel = (UserModel) bundle.getSerializable(TAG);
            type = bundle.getInt(TAG2);
        }

        activity = (SignInActivity) getActivity();
        preferences = Preferences.newInstance();
        Paper.init(activity);
        binding.btnConfirm.setOnClickListener(v -> {

            checkData();

        });

        binding.btnResend.setOnClickListener(v -> {

            Log.e("ddd", "ddd");
            if (canResend) {
                if (type == 2) {
                    reSendSMSCode();
                }

            }
        });


        startCounter();

    }

    private void checkData() {
        String code = binding.edtCode.getText().toString().trim();
        if (!TextUtils.isEmpty(code)) {
            Common.CloseKeyBoard(activity, binding.edtCode);
            if (type == 2) {
                ValidateCode(code);
            } else {
                ValidateCodepass(code);
            }
        } else {
            binding.edtCode.setError(getString(R.string.field_req));
        }
    }

    private void ValidateCode(String code) {
        try {
            ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            Api.getService(Tags.base_url)
                    .confirmCode(userModel.getId(), code)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                preferences.create_update_userData(activity, userModel);
                                preferences.createSession(activity, Tags.session_login);
                                Intent intent = new Intent(activity, HomeActivity.class);
                                startActivity(intent);
                                activity.finish();

                            } else {

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 422) {
                                    Toast.makeText(activity, R.string.failed, Toast.LENGTH_SHORT).show();
                                } else if (response.code() == 500) {
                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();


                                } else if (response.code() == 401) {
                                    Toast.makeText(activity, R.string.inc_code, Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }


                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
        }
    }

    private void ValidateCodepass(String code) {
        try {
            ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            Api.getService(Tags.base_url)
                    .confirmCodepass(userModel.getId(), code)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                CreateAlertDialog(userModel);

                            } else {

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 422) {
                                    Toast.makeText(activity, R.string.failed, Toast.LENGTH_SHORT).show();
                                } else if (response.code() == 500) {
                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();


                                } else if (response.code() == 401) {
                                    Toast.makeText(activity, R.string.inc_code, Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }


                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
        }
    }

    private void CreateAlertDialog(UserModel userModel) {
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .create();

        DialogAlertBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_alert, null, false);

        binding.tvMsg.setText(getString(R.string.suc));

        binding.btnCancel.setOnClickListener(view -> {
            dialog.dismiss();
            activity.displayFragmentNewpass(userModel);


        });
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    private void startCounter() {
        countDownTimer = new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                canResend = false;

                int AllSeconds = (int) (millisUntilFinished / 1000);
                int seconds = AllSeconds % 60;


                binding.btnResend.setText("00:" + seconds);
            }

            @Override
            public void onFinish() {
                if (type == 2) {
                    canResend = true;
                    binding.btnResend.setText(getString(R.string.resend));
                } else {
                    activity.back();
                }
            }
        }.start();
    }

    private void reSendSMSCode() {
        final ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .resendCode(userModel.getId())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        dialog.dismiss();

                        if (response.isSuccessful()) {
                            startCounter();

                        } else {
                            try {
                                Log.e("error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 422) {
                                Common.CreateDialogAlert(activity, getString(R.string.inc_code_verification));
                            } else if (response.code() == 500) {
                                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error", t.getMessage());


                        } catch (Exception e) {
                        }
                    }
                });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
