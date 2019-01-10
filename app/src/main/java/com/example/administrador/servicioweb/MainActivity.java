package com.example.administrador.servicioweb;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    Button btn_validar;
    TextView texto;
    String apellido;
    User persona;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("¿Esta conectado Internet?");
        dialogo1.setCancelable(false);
        dialogo1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                cancelar();
            }
        });
        dialogo1.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                aceptar();
            }
        });
        dialogo1.show();
    }

    private void aceptar() {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        EjemploAsyncTask ejemplo= new EjemploAsyncTask(this);
        ejemplo.execute();
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            texto = (TextView)findViewById(R.id.texto);
            btn_validar = (Button)findViewById(R.id.btn_validar);
        } else {
            Toast.makeText(getApplicationContext(), "No, no esta conectado", Toast.LENGTH_LONG).show();
            btn_validar.setEnabled(false);
        }

    }
    private void cancelar() {
        Toast.makeText(getApplicationContext(), "Lo sentimos, conectate a Internet y vuelve a intentarlo", Toast.LENGTH_LONG).show();
        finish();
    }


    private class EjemploAsyncTask extends AsyncTask<String, String, String> {

        public EjemploAsyncTask(MainActivity mainActivity) {
        }


        @Override
        protected String doInBackground(String... params) {

            DatabaseReference dePersona = mDatabase.child("persona");

            dePersona.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    persona = dataSnapshot.getValue(User.class);
                    apellido = persona.getApellido();
                    validacion();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    private void validacion() {
        btn_validar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                texto.setText(apellido);
            }
        });
    }
}


