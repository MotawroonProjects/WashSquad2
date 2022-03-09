package com.creative.share.apps.wash_squad.models;

import android.content.Context;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.creative.share.apps.wash_squad.BR;
import com.creative.share.apps.wash_squad.R;

public class Password2Model extends BaseObservable {


    private String password;
    private String confirm_password;

    public ObservableField<String> error_password = new ObservableField<>();
    public ObservableField<String> error_confirm_password = new ObservableField<>();


    public Password2Model() {

        this.password = "";
        this.confirm_password = "";
    }

    public Password2Model(String password) {

        this.password = password;


    }


    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);

    }

    @Bindable
    public String getConfirm_password() {
        return confirm_password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
        notifyPropertyChanged(BR.confirm_password);
    }

    public boolean isDataValid(Context context) {
        if (
                password.length() >= 6
                        && confirm_password.equals(password)
        ) {

            error_password.set(null);
            error_confirm_password.set(null);

            return true;
        } else {


            if (password.isEmpty()) {
                error_password.set(context.getString(R.string.field_req));
            } else if (password.length() < 6) {
                error_password.set(context.getString(R.string.pass_short));
            } else {
                error_password.set(null);

            }
            if (confirm_password.isEmpty()) {
                error_confirm_password.set(context.getString(R.string.field_req));
            } else if (!password.equals(confirm_password)) {
                error_confirm_password.set(context.getResources().getString(R.string.must_equal));
            } else {
                error_confirm_password.set(null);

            }
            return false;
        }
    }


}
