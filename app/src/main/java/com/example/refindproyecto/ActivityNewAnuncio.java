package com.example.refindproyecto;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.refindproyecto.Procedimientos.ProcedimientoPreferencias;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import org.json.JSONException;
import java.math.BigDecimal;
import Cliente.RefindCliente;
import POJOS.Anuncio;
import POJOS.Categoria;
import POJOS.Usuario;
//PAYPAL

//Fin paypal


public class ActivityNewAnuncio extends AppCompatActivity {


    EditText nombreAnuncio, telefonoAnuncio, descripcionAnuncio;
    Button aceptar, cancelar;
    Spinner categoriaAnuncio;
    Anuncio anuncio = null;
    ProcedimientoPreferencias pF = null;

    //PAYPAL Inicio
    private static final int MODO_PAYPAL = 1;
    private static final String TAG = "paypentExample";
    private static final String PAYPAL_KEY = "ASa17w2986TBZyGPfvxaTF5mv94shIuGGCyZzIH_LMYIGD0wR2nc8TcPmK98R0qA7plR7WNEFXORCqN8";
    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUES_CODE_FUTURE_PAYMENT = 2;
    private static final String CONFIG_ENVIROMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    private static PayPalConfiguration config;
    PayPalPayment thingsTobuy;


    //PAYPAL fin


    //Falta la imagen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_anuncio);
        aceptar = findViewById(R.id.aceptarNewAnuncio);
        cancelar = findViewById(R.id.cancelarrNewAnuncio);
        Categoria categoria = new Categoria();
        categoria.setCategoriaId(Integer.valueOf(getIntent().getIntExtra("categoriaIdAnuncio", 0)));
        configPaypal();
        pF = new ProcedimientoPreferencias(this.getApplicationContext());
        aceptar.setOnClickListener(v -> {

            Usuario usuario = new Usuario();

            if(pF.obtenerIdentificador() == 0){
                Intent i = new Intent(getApplicationContext(), ActivityLogin.class);
                startActivity(i);
            }else{
                usuario.setUsuarioId(pF.obtenerIdentificador());
            }

            nombreAnuncio = findViewById(R.id.nombreNewAnuncio);
            telefonoAnuncio = findViewById(R.id.telefonoNewAnuncio);
            descripcionAnuncio = findViewById(R.id.descripNewAnuncio);
            anuncio = new Anuncio(null, nombreAnuncio.getText().toString(), descripcionAnuncio.getText().toString(), categoria, usuario, telefonoAnuncio.getText().toString(), "asd");
            //TODO con el objeto anuncio comprobar que no existe otro igual en la misma categoria

            //todo compruebo que la imagen es nueva
            //Creo anuncio

            //Cambiar MODO_PAYPAL a 1 para que se haga el paypal
            if(MODO_PAYPAL == 1){
                makePayment();
            }

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    RefindCliente refindCliente = new RefindCliente("10.0.2.2", 30500);

                    anuncio = refindCliente.crearAnuncio(anuncio);
                }
            });
            thread.start();
            //TODO volver a la pantalla de lista anuncios
            //Subo imagen
        });
    }



    //TODO cambiar mensajes
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (requestCode == Activity.RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        System.out.println(confirmation.toJSONObject().toString(4));
                        System.out.println(confirmation.getPayment().toJSONObject().toString(4));
                        Toast.makeText(this, "tODO ok", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "payment has been cancelled", Toast.LENGTH_LONG).show();
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }else if (requestCode == REQUES_CODE_FUTURE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization authorization = data.getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
                if (authorization != null) {
                    try {
                        Log.i("FuturePaymentExample", authorization.toJSONObject().toString(4));
                        String autherization_code = authorization.getAuthorizationCode();
                        Log.d("FuturePaymentExample", autherization_code);
                        Log.e("FuturePaymentExample", "error 96" + autherization_code);
                    } catch (Exception e) {
                        Toast.makeText(this, "fallo", Toast.LENGTH_LONG).show();
                        Log.e("FuturePaymentExample", "an extremely unlikely failure ocurred ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "payment has been cancelled", Toast.LENGTH_LONG).show();
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void configPaypal(){
        config = new PayPalConfiguration()
                .environment(CONFIG_ENVIROMENT)
                .clientId(PAYPAL_KEY)
                .merchantName("Paypal login")
                .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
                .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));
    }
    public void makePayment(){
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
        thingsTobuy = new PayPalPayment(new BigDecimal(String.valueOf("10.45")),"USD", "Payment", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent payment = new Intent(this, PaymentActivity.class);
        payment.putExtra(PaymentActivity.EXTRA_PAYMENT, thingsTobuy);
        payment.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startActivityForResult(payment, REQUEST_CODE_PAYMENT);
    }

    /**
     * sb-cvrju6972122@personal.example.com
     *
     *  Cuentas sandbox
     *  https://developer.paypal.com/developer/accounts/
     *
     *
     *  alvarito2@gmail.com correo
     *  123456789 pw
     *
     *  La cuenta de PayPal no esta verificada por lo que no funcionaria con esta
     */
}