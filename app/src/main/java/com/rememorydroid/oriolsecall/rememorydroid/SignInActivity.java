package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends BaseActivity implements
        View.OnClickListener {

    private static final String TAG = "SignInActivity";
    private static final int MY_PERMISSIONS_REQUEST_INTERNET = 3 ;

    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private EditText mEmailField;
    private EditText mPasswordField;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.INTERNET)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.INTERNET},
                        MY_PERMISSIONS_REQUEST_INTERNET);

            }
        }

        // Views
        mStatusTextView = (TextView) findViewById(R.id.status);
        mDetailTextView = (TextView) findViewById(R.id.detail);
        mEmailField = (EditText) findViewById(R.id.field_email);
        mPasswordField = (EditText) findViewById(R.id.field_password);

        // Buttons
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.email_create_account_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.verify_email_button).setOnClickListener(this);

        // Inicialitzem l'autenticació
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null && checkIfEmailVerified()) {
                    // Usuari té sessió activa
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    showToast(user.getEmail().toString(),true);

                    Intent areaAvaluador = new Intent(SignInActivity.this, AreaAvaluadorActivity.class);
                    startActivity(areaAvaluador);

                } else {
                    // User ha acabat la sessió
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                updateUI(user);
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        //Crear usuari
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            showToast(getString(R.string.auth_failed), false);
                            Log.e(TAG,task.getResult().toString());
                        }
                        hideProgressDialog();
                    }
                });
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
             return;
        }
        showProgressDialog();

         mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            showToast(getString(R.string.auth_failed),false);
                        }
                        else{
                            if(checkIfEmailVerified()){
                                Intent areaAvaluador = new Intent(SignInActivity.this, AreaAvaluadorActivity.class);
                                startActivity(areaAvaluador);
                            }
                        }

                        if (!task.isSuccessful()) {
                            mStatusTextView.setText(R.string.auth_failed);
                            Log.d(TAG,"Error en iniciar sessió."+task.getResult().toString());
                        }
                        hideProgressDialog();
                    }
                });
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private void sendEmailVerification() {

        findViewById(R.id.verify_email_button).setEnabled(false);

        // email de verificació
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        findViewById(R.id.verify_email_button).setEnabled(true);

                        if (task.isSuccessful()) {
                            showToast(getString(R.string.VerificationEmailSent, user.getEmail()),true);
                            Log.d(TAG,"Email de verificació enviat a"+user.getEmail());
                            signOut();

                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            showToast(getString(R.string.VerificationEmailError),true);
                        }
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError(getString(R.string.Required));
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError(getString(R.string.Required));
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {

            mStatusTextView.setText(getString(R.string.emailpassword_status_fmt,
                    user.getEmail()));
            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            findViewById(R.id.email_password_buttons).setVisibility(View.GONE);
            findViewById(R.id.email_password_fields).setVisibility(View.GONE);
            findViewById(R.id.signed_in_buttons).setVisibility(View.VISIBLE);

            findViewById(R.id.verify_email_button).setEnabled(!user.isEmailVerified());
        } else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            findViewById(R.id.email_password_buttons).setVisibility(View.VISIBLE);
            findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
            findViewById(R.id.signed_in_buttons).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.email_create_account_button) {
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.email_sign_in_button) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.sign_out_button) {
            signOut();
        } else if (i == R.id.verify_email_button) {
            sendEmailVerification();
        }
    }



    private boolean checkIfEmailVerified()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (!user.isEmailVerified())
        {
            showToast(getString(R.string.EmailNotVerifiedYet),true);
            return false;
        }
        else{
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST_INTERNET: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;

            }
        }
    }
}

