package com.example.francisco_javier.check_yourselflogin;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.media.AudioRouting;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

public class Register extends AppCompatActivity implements LoaderCallbacks<Cursor> {


    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mRutView;
    private AutoCompleteTextView mNombreView;
    private AutoCompleteTextView mMailView;
    private AutoCompleteTextView mTelefonoView;
    private AutoCompleteTextView mPassView;
    private AutoCompleteTextView mPass2View;

    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Set up the login form.
        mRutView = (AutoCompleteTextView) findViewById(R.id.rut);
        mNombreView = (AutoCompleteTextView) findViewById(R.id.nombre);
        mMailView = (AutoCompleteTextView) findViewById(R.id.mail);
        mTelefonoView = (AutoCompleteTextView) findViewById(R.id.telefono);
        mPassView = (AutoCompleteTextView) findViewById(R.id.contrasena);
        mPass2View = (AutoCompleteTextView) findViewById(R.id.contrasena2);

        Button mDoneButton = (Button) findViewById(R.id.done_button);
        mDoneButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });
        mLoginFormView = findViewById(R.id.loginForm);
        mProgressView = findViewById(R.id.loginProgress);
    }
    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptRegister() {
        if (mAuthTask != null) {
            return;
        }
        // Reset errors.
        mMailView.setError(null);
        mPassView.setError(null);

        // Store values at the time of the login attempt.
        String email = mMailView.getText().toString();
        String pass = mPassView.getText().toString();
        String rut = mRutView.getText().toString();
        String nombre = mNombreView.getText().toString();
        String telefono = mTelefonoView.getText().toString();
        String pass2 = mPass2View.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Passwords valida
        if (!TextUtils.isEmpty(pass) && !isPasswordValid(pass)) {
            mPassView.setError(getString(R.string.error_invalid_password));
            focusView = mPassView;
            cancel = true;
        }
        if (!TextUtils.isEmpty(pass2) && !isPassword2Valid(pass,pass2)){
            mPassView.setError(getString(R.string.error_password2));
            focusView = mPass2View;
            cancel = true;
        }
        // Rut Valido
        if (TextUtils.isEmpty(rut)) {
            mRutView.setError(getString(R.string.error_field_required));
            focusView = mRutView;
            cancel = true;
        } else if (!isrutValid(rut)) {
            mRutView.setError(getString(R.string.error_invalid_rut));
            focusView = mRutView;
            cancel = true;
        }
        // Telefono Valido
        if (TextUtils.isEmpty(telefono)){
            mTelefonoView.setError(getString(R.string.error_field_required));
            focusView = mTelefonoView;
            cancel = true;
        } else if (!isTelefonoValid(telefono)) {
            mTelefonoView.setError(getString(R.string.error_invalid_telefono));
            focusView = mTelefonoView;
            cancel = true;
        }

        // Nombre Valido
        if (TextUtils.isEmpty(nombre)) {
            mNombreView.setError(getString(R.string.error_field_required));
            focusView = mNombreView;
            cancel = true;
        }
        // Email Valido
        if (TextUtils.isEmpty(email)) {
            mMailView.setError(getString(R.string.error_field_required));
            focusView = mMailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mMailView.setError(getString(R.string.error_invalid_email));
            focusView = mMailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt register and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, pass);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {

        return email.contains("@");
    }
    private boolean isrutValid(String rut) {
        String todos = "0123456789";
        for (int i = 0 ; i < rut.length(); i++){
            if (!todos.contains(Character.toString(rut.charAt(i)))){
                return false;
            }
        }
        return rut.length() == 8;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
    private boolean isPassword2Valid(String password, String password2){
        return password.equals(password2);
    }
    private boolean isTelefonoValid(String telefono){
        return telefono.length() == 12;
    }
    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPassView.setError(getString(R.string.error_incorrect_password));
                mPassView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

