package br.com.douglimar.gasoretanol;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.NumberFormat;
import java.util.Locale;

import static android.graphics.Color.YELLOW;

public class GasOrEtanolActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    private static final Locale ptBR = new Locale("pt", "BR");

    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(ptBR);

    private double billAmount = 0.0;
    private TextView tvDisplayEtanolValue;
    private TextView tvDisplayGasValue;
    private TextView tvEtanol;
    private TextView tvGas;
    private TextView tvResult;
    private ImageView imgResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_or_etanol);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Create a AdView
        // Load Advertisement Banner
        AdView mAdView = findViewById(R.id.adViewGasOrEtanol);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        int maxLength = 3;

        tvResult = findViewById(R.id.tvResult);
        imgResult = findViewById(R.id.imgResult);

        // Etanol Value

        tvEtanol = findViewById(R.id.tvEtanol);

        tvDisplayEtanolValue = findViewById(R.id.tvDisplayEtanolValue);
        final EditText edtDisplayEtanolValue = findViewById(R.id.edtDisplayEtanolValue);

        // Define the Mask to TextView to get Currency Format
        edtDisplayEtanolValue.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
        edtDisplayEtanolValue.addTextChangedListener(editTextTextWatcher);

        edtDisplayEtanolValue.requestFocus();

        // Gas Value
        tvGas = findViewById(R.id.tvGasolina);
        tvDisplayGasValue = findViewById(R.id.tvDisplayGasValue);
        final EditText edtDisplayGasValue = findViewById(R.id.edtDisplayGasValue);
        // Define the Mask to TextView to get Currency Format
        edtDisplayGasValue.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
        edtDisplayGasValue.addTextChangedListener(editTextTextWatcher2);

        final Button btnCalcular = findViewById(R.id.btnCalcular);

        edtDisplayEtanolValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvEtanol.setTextColor(getResources().getColor(R.color.colorLightBlue));
                tvGas.setTextColor(getResources().getColor(R.color.colorBlack));
                tvDisplayEtanolValue.setBackgroundColor(getResources().getColor(R.color.colorLightBlue));
                tvDisplayGasValue.setBackgroundColor(getResources().getColor(R.color.lightGray));
            }
        });

        edtDisplayGasValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvGas.setTextColor(getResources().getColor(R.color.colorLightBlue));
                tvEtanol.setTextColor(getResources().getColor(R.color.colorBlack));
                tvDisplayEtanolValue.setBackgroundColor(getResources().getColor(R.color.lightGray));
                tvDisplayGasValue.setBackgroundColor(getResources().getColor(R.color.colorLightBlue));
            }
        });

        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Firebase Statistics
                //********************************************
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Gas or Etanol Button");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                //********************************************


                tvGas.setTextColor(getResources().getColor(R.color.colorBlack));
                tvEtanol.setTextColor(getResources().getColor(R.color.colorBlack));
                tvDisplayGasValue.setBackgroundColor(getResources().getColor(R.color.lightGray));
                tvDisplayEtanolValue.setBackgroundColor(getResources().getColor(R.color.lightGray));

                btnCalcular.requestFocus();

                hideKeyboardFrom(getApplicationContext(), v);

                if ( (edtDisplayEtanolValue.getText().toString().isEmpty())||
                        (edtDisplayGasValue.getText().toString().isEmpty()) ) {

                    Toast.makeText(getApplicationContext(), "Informe o Valor do ETANOL e da GASOLINA antes de continuar.", Toast.LENGTH_SHORT).show();
                }
                else  {

                    Double dEtanol = Double.parseDouble(edtDisplayEtanolValue.getText().toString()) / 100;
                    Double dGasoline = Double.parseDouble(edtDisplayGasValue.getText().toString()) / 100;

                    tvResult.setText(calculateGas(dEtanol, dGasoline));




                }

            }
        });

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    private final TextWatcher editTextTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {


        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            try {

                billAmount = Double.parseDouble(s.toString()) / 100.0;

            } catch (NumberFormatException e ) {

                billAmount = 0.0;
            }

            tvDisplayEtanolValue.setText(currencyFormat.format(billAmount));

        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    };

    private final TextWatcher editTextTextWatcher2 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            try {

                billAmount = Double.parseDouble(s.toString()) / 100.0;

            } catch (NumberFormatException e ) {

                billAmount = 0.0;
            }

            tvDisplayGasValue.setText(currencyFormat.format(billAmount));

        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    };

    @SuppressLint("DefaultLocale")
    private String calculateGas(Double vEtanol, Double vGas) {

        Double result = (vEtanol*100) / vGas;

        if (result>70.0) {

            imgResult.setImageResource(R.drawable.gasoline);
            tvResult.setBackgroundColor(YELLOW);
            return "ABASTEÇA COM GASOLINA \n\n O Etanol representa " + String.format("%.2f",result) + "% do valor da Gasolina. Por esse motivo é recomendado abastecer com GASOLINA.";
        }
        else {
            imgResult.setImageResource(R.drawable.etanol);
            tvResult.setBackgroundColor(getResources().getColor(R.color.colorLightBlue));
            return "ABASTEÇA COM ETANOL \n\n O Etanol representa " + String.format("%.2f",result) + "% do valor da Gasolina. Por esse motivo é recomendado abastecer com ETANOL.";
        }

    }

    private static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
