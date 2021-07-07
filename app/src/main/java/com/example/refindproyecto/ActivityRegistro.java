package com.example.refindproyecto;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.refindproyecto.Auxiliar.Auxiliar;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import Cliente.RefindCliente;
import POJOS.Usuario;


public class ActivityRegistro extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText correo;
    private EditText nombre;
    private EditText apellido;
    private EditText pass;
    private EditText confirmacionPass;
    //CheckBox checkBox;
    Boolean isChecked=false;
    MaterialCheckBox checkBox;
    //Notificaciones
    private PendingIntent pendingIntent;
    private final static String CHANNEL_ID = "NOTIFICACION";
    private final static int NOTIFICACION_ID = 0;

    Auxiliar comprobarDatos = new Auxiliar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        mAuth = FirebaseAuth.getInstance();

        correo = findViewById(R.id.emailRegistro);
        nombre = findViewById(R.id.nombreRegistro);
        apellido = findViewById(R.id.apellidoRegistro);
        pass = findViewById(R.id.passRegistro);
        confirmacionPass = findViewById(R.id.passComRegistro);
        checkBox = findViewById(R.id.checkBox);

        checkBox.setOnClickListener(v -> isChecked= !isChecked);

    }

    public void registrarUsuario (View view){
        Usuario usuario = new Usuario();

        usuario.setNombre(nombre.getText().toString());
        usuario.setApellido(apellido.getText().toString());
        usuario.setEmail(correo.getText().toString());
        usuario.setBiografia("");
        usuario.setCreador(0);
        usuario.setFoto("0");
        usuario.setPass(pass.getText().toString());

        boolean comprobar = true;
        if(usuario == null || usuario.getEmail().equals("") || usuario.getNombre().equals("") || usuario.getApellido().equals("") || usuario.getPass().equals("") || confirmacionPass.equals("")){
            Snackbar snackbar = Snackbar.make(view, R.string.todosCamposOk, Snackbar.LENGTH_LONG);
            snackbar.setDuration(10000);
            snackbar.setAction("Ok", v -> {

            });
            snackbar.show();
            comprobar = false;
        }
        else if(!isChecked){
            Snackbar snackbar = Snackbar.make(view, R.string.error18, Snackbar.LENGTH_LONG);
            snackbar.setDuration(10000);
            snackbar.setAction("Ok", v -> {
            });
            snackbar.show();
            comprobar = false;
        }
        else if(!usuario.getPass().equals(confirmacionPass.getText().toString())){
            Snackbar snackbar = Snackbar.make(view, R.string.errorPassNoIguales, Snackbar.LENGTH_LONG);
            snackbar.setDuration(10000);
            snackbar.setAction("Ok", v -> {
            });
            snackbar.show();
            comprobar = false;
        }
        else if(pass.length()<9){
            Snackbar snackbar = Snackbar.make(view, R.string.pass9, Snackbar.LENGTH_LONG);
            snackbar.setDuration(10000);
            snackbar.setAction("Ok", v -> {
            });
            snackbar.show();
            comprobar = false;
        }


        if(comprobar){
            mAuth.createUserWithEmailAndPassword(correo.getText().toString().trim(), pass.getText().toString().trim())
                    .addOnCompleteListener(this, task -> {

                        if (task.isSuccessful()) {
                            usuario.setUsuarioFirebase(mAuth.getUid());
                            usuario.setError(crearUsuario(usuario));

                            setPendingIntent();
                            createNotificacionChanel();
                            createNotification();

                            Snackbar snackbar = Snackbar.make(view, usuario.getError(), Snackbar.LENGTH_LONG);
                            snackbar.setDuration(10000);
                            snackbar.show();

                            Intent registro = new Intent(getApplicationContext(), ActivityListaCat.class);
                            startActivity(registro);
                        } else {
                            Snackbar snackbar = Snackbar.make(view, R.string.errorRegistro, Snackbar.LENGTH_LONG);
                            snackbar.setDuration(10000);
                            snackbar.show();
                        }
                    });
        }
    }

    private String crearUsuario(Usuario usuario){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RefindCliente refindCliente = new RefindCliente("10.0.2.2", 30500);
                usuario.setError(refindCliente.crearUsuario(usuario));
            }
        });
        thread.start();
        return usuario.getError();
    }
    private void setPendingIntent(){
        Intent intent = new Intent(this, ActivityPerfil.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ActivityLogin.class);
        stackBuilder.addNextIntent(intent);
        pendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_CANCEL_CURRENT);
    }
    private void createNotificacionChanel(){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            CharSequence name = "Notificacion";
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
    private void createNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_perfilb);
        builder.setContentTitle("Bienvenido a Refind");
        builder.setContentText("Configura tu perfil a tu gusto");
        builder.setColor(Color.BLUE);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setLights(Color.MAGENTA, 1000, 1000);
        builder.setVibrate(new long[]{1000,1000,1000,1000,1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);

        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());
    }

    public void irLogin(View view){
        Intent registro_login = new Intent(this, ActivityLogin.class);
        startActivity(registro_login);
    }
}