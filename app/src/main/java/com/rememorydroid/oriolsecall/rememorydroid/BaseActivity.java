package com.rememorydroid.oriolsecall.rememorydroid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Oriol on 24/02/2017.
 */

public class BaseActivity extends AppCompatActivity {


    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void showToast(String message, boolean llarga){

        int duracio;

        if(llarga){
            duracio = Toast.LENGTH_LONG;
        }
        else duracio = Toast.LENGTH_SHORT;

        Toast.makeText(this, message,
                duracio).show();
    }

    public void showToastError(){
        Toast.makeText(this, "Error!",
                Toast.LENGTH_LONG).show();
    }


    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

}

