package com.creative.share.apps.wash_squad.models;

import android.content.Context;
import android.text.TextUtils;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.creative.share.apps.wash_squad.BR;
import com.creative.share.apps.wash_squad.R;

import java.io.Serializable;

public class EditProfileModel extends BaseObservable implements Serializable {

    private String name;

    public ObservableField<String> error_name = new ObservableField<>();


    public EditProfileModel() {
        this.name = "";

    }

    public EditProfileModel(String name) {
        setName(name);
        notifyPropertyChanged(BR.name);


    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);


    }







    public boolean isDataValid(Context context)
    {
        //Log.e("llll",name);
        if (!TextUtils.isEmpty(name)
        )
        {
            error_name.set(null);


            return true;
        }else
        {
            if (name.isEmpty())
            {
                error_name.set(context.getString(R.string.field_req));
            }else
            {
                error_name.set(context.getString(R.string.field_req));
            }










            return false;
        }
    }
}
