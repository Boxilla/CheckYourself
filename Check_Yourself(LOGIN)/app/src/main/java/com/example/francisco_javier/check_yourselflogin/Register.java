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

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Register extends AppCompatActivity implements LoaderCallbacks<Cursor> {

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
        if (!TextUtils.isEmpty(pass2) && !isPassword2Valid(pass, pass2)) {
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
        if (TextUtils.isEmpty(telefono)) {
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
            mAuthTask = new UserLoginTask(email, pass, rut, telefono);
     //       mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        String email2 = "'" + email + "'";
        //       if (email2 in basedatos){
        //         return false;
        //   }

        return email.contains("@");
    }

    private boolean isrutValid(String rut) {
        String todos = "0123456789";
        for (int i = 0; i < rut.length(); i++) {
            if (!todos.contains(Character.toString(rut.charAt(i)))) {
                return false;
            }
        }
        return rut.length() == 8;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private boolean isPassword2Valid(String password, String password2) {
        return password.equals(password2);
    }

    private boolean isTelefonoValid(String telefono) {
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
        List<String> emails = new ArrayList();
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
    public class UserLoginTask extends AsyncTask<String, Void, ResultSet> {

        private final String mEmail;
        private final String mPassword;
        private final String mrut;
        private final String mTelefono;

        UserLoginTask(String email, String password, String rut, String telefono) {
            mEmail = email;
            mPassword = password;
            mrut = rut;
            mTelefono = telefono;

        }

        @Override
        protected ResultSet doInBackground(String... strings) {
            // TODO: attempt authentication against a network service.

            try {
                Connection conn;
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mysql://" + strings[0] + strings[1], "root", "");

                Statement estado = conn.createStatement();
                System.out.println("Conexion establecida");
                String peticion = "select * from " + strings[2] + " where nombre='" + strings[3] + "'";
                ResultSet result = estado.executeQuery(peticion);
                return result;

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;

            // TODO: register the new account here.
        }

        @Override
        protected void onPostExecute(ResultSet result) {

            try {
                if (result != null) {
                    if (!result.next()) {
                        Toast toast = Toast.makeText(getApplicationContext(), "No existen resultados con ese nombre", Toast.LENGTH_LONG);
                        toast.show();
                    } else {
           //             tvGenero.setText(result.getString("genero"));
             //           tvValoracion.setText(Float.toString(result.getFloat("valoracion")));
               //         tvPEGI.setText(Integer.toString(result.getInt("PEGI")));
                 //       tvPrecio.setText(Float.toString(result.getFloat("precio")));
                    }
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "La videoconsola no está en la base de datos", Toast.LENGTH_LONG);
                    toast.show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
