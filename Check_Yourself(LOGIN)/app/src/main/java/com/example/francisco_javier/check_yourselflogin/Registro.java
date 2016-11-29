package com.example.francisco_javier.check_yourselflogin;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
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

public class Registro extends AppCompatActivity {

    private AutoCompleteTextView mRutView;
    private AutoCompleteTextView mNombreView;
    private AutoCompleteTextView mMailView;
    private AutoCompleteTextView mTelefonoView;
    private AutoCompleteTextView mPassView;
    private AutoCompleteTextView mPass2View;
    private Button btnConectar;


    private View mProgressView;
    private View mLoginFormView;

    private String IP = "10.10.18.246:3306";
    private String baseDatos = "/intro";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRutView = (AutoCompleteTextView)findViewById(R.id.rut);
        mNombreView = (AutoCompleteTextView)findViewById(R.id.nombre);
        mMailView = (AutoCompleteTextView)findViewById(R.id.mail);
        mTelefonoView = (AutoCompleteTextView)findViewById(R.id.telefono);
        mPassView = (AutoCompleteTextView)findViewById(R.id.contrasena);
        mPass2View = (AutoCompleteTextView)findViewById(R.id.contrasena2);
        btnConectar = (Button)findViewById(R.id.done_button);


        btnConectar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String email = mMailView.getText().toString();
                String pass = mPassView.getText().toString();
                String rut = mRutView.getText().toString();
                String nombre = mNombreView.getText().toString();
                String telefono = mTelefonoView.getText().toString();
                String tipo = "1";
                String id = "0";
                new ConexionDB().execute(IP,baseDatos, email, pass, rut, nombre, telefono, tipo, id );
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class ConexionDB extends AsyncTask<String,Void,ResultSet>{


        @Override
        protected ResultSet doInBackground(String... strings) {
        // ip, bd, mail, pass, rut, nombre, telefono , tipo, id
            try {
                Connection conn;
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mysql://"+strings[0]+strings[1], "root", "");

                Statement estado = conn.createStatement();
                System.out.println("Conexion establecida");
                String peticion ="insert into usuarios values (" +strings[8]+","+strings[3]+","+strings[5]+","+strings[6]+","+strings[2]+","+strings[7]+")";
                ResultSet result = estado.executeQuery(peticion);
                return result;
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}

