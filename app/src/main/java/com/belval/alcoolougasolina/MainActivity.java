package com.belval.alcoolougasolina;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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

    private PrefsUtil prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LoginMsg.newInstance().show(getSupportFragmentManager(), "login");

        setContentView(R.layout.activity_main);

        this.prefs = new PrefsUtil(MainActivity.this);

        carregaComponentes();

        this.btnCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                erroEntrada = false;
                double precoAlcool = getDouble(editPrecoAlcool);

                double precoGasolina = getDouble(editPrecoGasolina);

                if (!erroEntrada) {
                    // faz o calculo
                    //if (precoAlcool < 0.7 * precoGasolina) {
                    //if (precoAlcool < 70/100 * precoGasolina) {
                    if (ehMaisVantajosoAlcoolQueGasolina(precoAlcool, precoGasolina)) {
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

    private void criarPropriedade() {
        SharedPreferences sharedPreferences =
                getSharedPreferences("PREF_FILE", MainActivity.this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("primeira_vez", "1");
        editor.apply();

        String primeiraVez = sharedPreferences.getString("primeira_vez", "");
    }

    private boolean ehMaisVantajosoAlcoolQueGasolina(
            double precoAlcool, double precoGasolina) {
        //Recupera do arquivo de preferencias o valor da precisão
        double precisao = prefs.getDouble("precisao", 0.01);
        //Recupera do arquivo de preferencias o valor da proporcao
        int proporcao = prefs.getInt("proporcaoAlcoolGasolina", 70);
        //double diferenca = 100 * precoAlcool - proporcao * precoGasolina;
        double diferenca = precoAlcool - proporcao/100.0 * precoGasolina;

        if (Math.abs(diferenca) < precisao) {
            //Se a diferenca for menor que a precisao, entao, considera que sao
            //valores iguais e, na igualdade, o alcool NAO eh mais vantajoso
            return false;
        }

        if (diferenca < 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Recupera do arquivo de preferencias o valor da cor em hexadecimal
        String corDeFundo = prefs.getStr("corDeFundo", "FFFFFF");
        //Recupera o Layput de MainActivity pelo id
        ConstraintLayout layout = findViewById(R.id.telaMain);
        //Converte a String como valor hexa para o objeto Color correspondente
        //e atribui à cor de fundo
        layout.setBackgroundColor(Color.parseColor("#" + corDeFundo));
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

    private static final int MENU_CONF = 1;
    private static final int MENU_SAIR = 2;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(
                Menu.NONE, //groupId -> Identifica que este item de menu faz parte deste grupo
                MENU_CONF, //itemId -> Usamos este id para identificar que este menu foi clicado
                Menu.NONE,  // order -> identifica a ordem em que o menu aparece
                "Configurações"); //title -> Texto do menu, pode ser o id de uma string

        menu.add(
                Menu.NONE,
                MENU_SAIR, //itemId -> Menu sair
                Menu.NONE,
                "Sair"); //title -> Texto do menu, pode ser o id de uma string

        return true; //Se retornar true o menu eh apresentado, se retornar false o menu não eh mostrado
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case MENU_CONF:
                abrirConfiguracoes();
                return true;
            case MENU_SAIR:
                closeActivity();
            default:
                return false;
        }
    }

    private void closeActivity() {
        //Encerra a aplicação
        MainActivity.this.finishAndRemoveTask();
        System.exit(0);
    }

    /**
     * <p>Abre a tela de configurações</p>
     */
    private void abrirConfiguracoes() {
        Intent it = new Intent(MainActivity.this, ConfiguracaoActivity.class);
        startActivity(it);
    }

    public static class LoginMsg extends DialogFragment {
        public static LoginMsg newInstance() {
            return new LoginMsg();
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            //Na pratica define que a caixa de dialogo sera modal
            setCancelable(false);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            //obtem o inflater
            LayoutInflater inflater = requireActivity().getLayoutInflater();

            //"Infla" a view dialog_senha
            View view = inflater.inflate(R.layout.dialog_senha, null);
            //recupera os campos da view para usar
            //eles precisam ser marcados como "final" para serem usados
            //dentro das classes anomimas criadas abaixo
            final EditText loginUser = view.findViewById(R.id.loginUser);
            final EditText loginPass = view.findViewById(R.id.loginPass);
            final TextView errorMsg = view.findViewById(R.id.errorMsg);

            //Infla o layout e passa para o nbuilder
            builder.setView(view)
                    //Nao adiciona o botao entrar aqui
                    .setPositiveButton("Entrar",null)
                    .setNegativeButton("Sair", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Encerra a aplicação
                            ((MainActivity)getActivity()).closeActivity();
                        }
                    });
            final AlertDialog alert = builder.create();
            //Para que a caixa de dialogo nao feche ao clicar no positive button
            //se faz necessario substituir o listener do botão depois
            //que a tela eh apresentada
            alert.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    Button btn = ((AlertDialog)dialog)
                            .getButton(AlertDialog.BUTTON_POSITIVE);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //verifica usuario e senha
                            if (loginUser.getText().toString().equals("ze") &&
                                    loginPass.getText().toString().equals("123456")) {
                                //dismiss fecha a caixa de dialogo
                                alert.dismiss();
                            } else {
                                errorMsg.setText("Usuário ou senha inválido");
                            }
                        }
                    });
                }
            });
            //Qualquer tecla pressionada limpa a mensagem de erro
            alert.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog,
                                     int keyCode, KeyEvent event) {
                    errorMsg.setText("");
                    return false;
                }
            });
            return alert;
        }
    }
}
