package com.belval.alcoolougasolina;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText editPrecoAlcool, editPrecoGasolina;
    private Button btnCalc;
    private TextView txtRes;

    private boolean erroEntrada = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        carregaComponentes();

        this.btnCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                erroEntrada = false;
                double precoAlcool = getDouble(editPrecoAlcool);

                double precoGasolina = getDouble(editPrecoGasolina);

                if (!erroEntrada) {
                    // faz o calculo
                    if (precoAlcool < 0.7 * precoGasolina) {
                        txtRes.setText("Abasteça com Álcool");
                    } else { // precoAlcool >= 0.7 * precoGasolina
                        txtRes.setText("Abasteça com Gasolina");
                    }
                } else {
                    //avisa sobre os problemas de entrada de dados
                    Toast msg = Toast.makeText(MainActivity.this, "Digite corretamente os valores de entrada", Toast.LENGTH_LONG);
                    msg.setGravity(Gravity.CENTER, 0, 0);
                    //msg.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
                    msg.show();
                }

            }
        });
    }

    private double getDouble(EditText edit) {
        String valStr = edit.getText().toString();
        double valor = 0.0;
        try {
            valor = Double.parseDouble(valStr);
        } catch (Exception  e) {
            edit.setError("Informe uma valor apropriado");
            erroEntrada = true;
        }
        return valor;
    }

    private void carregaComponentes() {
        this.editPrecoAlcool = findViewById(R.id.editPrecoAlcool);
        this.editPrecoGasolina = findViewById(R.id.editPrecoGasolina);
        this.btnCalc = findViewById(R.id.btnCalc);
        this.txtRes = findViewById(R.id.txtRes);
    }
}
