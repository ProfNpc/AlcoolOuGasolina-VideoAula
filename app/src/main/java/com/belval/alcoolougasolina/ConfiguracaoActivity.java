package com.belval.alcoolougasolina;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import androidx.preference.SeekBarPreference;
import androidx.preference.SwitchPreferenceCompat;

public class ConfiguracaoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_configuracao);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.configuracoes, new ConfiguracoesFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //Configura o botão "up"
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class ConfiguracoesFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            //Configuracao da tela de preferencias usando xml
            //setPreferencesFromResource(R.xml.preferences, rootKey);

            //Obtem PreferenceManager e dele obtem o contexto
            //context eh uma referência para a Activity que hospeda o fragmento
            Context context = getPreferenceManager().getContext();

            //Cria a tela de preferencias
            PreferenceScreen screen =
                    getPreferenceManager().createPreferenceScreen(context);
            //Atribui ao Fragmento a tela de preferencias
            setPreferenceScreen(screen);
            //A partir daqui as preferências serão adicionadas à screen
            addSobrePref(context, screen);
            addPrecisaoPref(context, screen);
            addProporcaoPref(context, screen);
            addNomeUsuarioPref(context, screen);
            addVersaoPref(context, screen);
            addCorFundoPref(context, screen);
            addCompartilhamentoPref(context, screen);
        }

        private void addSobrePref(Context context, PreferenceScreen screen) {
            //Instancia uma preferencia passando context(ConfiguracaoActivity)
            Preference sobrePref = new Preference(context);
            //Define a chave, que precisa ser unica, usada para identificar a preferencia dentro
            //do arquivo
            sobrePref.setKey("sobre");
            //Define o título, aquilo que vemos da tela
            sobrePref.setTitle("Sobre o App");
            //Define um valor padrão para a preferência
            sobrePref.setDefaultValue("App desenvolvido para as Aulas de PRAP3");

            //Recupera o objeto SharedPreference a partir do PreferenceManager
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            //Recupera o valor da propriedade que está no arquivo
            String textoSobre =
                    prefs.getString("sobre", "App desenvolvido para as Aulas de PRAP3");

            //Cria uma Intent que chama SobreActivity
            Intent sobreIt = new Intent(context, SobreActivity.class);
            //Adiciona à Intent o texto sobre o aplicativo
            sobreIt.putExtra("textoSobre", textoSobre);
            //Se quisermos podemos passar outros tipos simples como int, float
            //sobreIt.putExtra("valorInt", 10);

            //Esta linha configura que esta Intent será chamada quando esta preferência for
            //clicada
            sobrePref.setIntent(sobreIt);
            //coloca a preference dentro da tela
            screen.addPreference(sobrePref);
        }
        private void addPrecisaoPref(
                Context context, PreferenceScreen screen) {
            //Instancia uma Preferencia de texto editavel para setar
            // o valor da precisao
            EditTextPreference precisaoPref =
                    new EditTextPreference(context);
            //Define a chave dessa preferencia dentro do arquivo
            precisaoPref.setKey("precisao");
            //Define o titulo apresentado para a preferecia
            precisaoPref.setTitle("Precisão");
            //Define o titulo do tela de edição
            precisaoPref.setDialogTitle("Informe a precisão");

            //Configura o Listener
            precisaoPref.setOnBindEditTextListener(
                    new EditTextPreference.OnBindEditTextListener() {
                        @Override
                        public void onBindEditText(@NonNull EditText editText) {
                            //Define que o tipo do text será numérico e decimal
                            editText.setRawInputType(
                                InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        }
                    });
            //Define o comportamento para o resumo(summary) da propriedade
            //Se não tiver nenhum valor definido apresenta "Not set"
            precisaoPref.setSummaryProvider(
                    EditTextPreference.SimpleSummaryProvider.getInstance());
            //Define um valor padrão para a precisao
            precisaoPref.setDefaultValue("0.00001");

            screen.addPreference(precisaoPref);
        }
        /**
         * <p>
         * Proporcao da energia do alcool em relação à gasolina. Gira em torno <br>
         * de 70% e nesse caso essa preference guardaria o número inteiro 70 <br>
         * para representar o 70%
         * </p>
         * @param context
         * @param screen
         */
        private void addProporcaoPref(Context context, PreferenceScreen screen) {
            SeekBarPreference proporcaoPref = new SeekBarPreference(context);
            proporcaoPref.setKey("proporcaoAlcoolGasolina");
            proporcaoPref.setTitle("Proporção Álcool/Gasolina");
            //Define o valor máximo possível
            proporcaoPref.setMax(100);
            //Define o valor mínimo possível
            proporcaoPref.setMin(1);
            //Define o valor mínimo de incremento ou decremento
            proporcaoPref.setSeekBarIncrement(1);

            //Recupera o objeto SharedPreference a partir do PreferenceManager
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            //Obtem o valor da propriedade "proporcaoAlcoolGasolina"
            int valorInt = prefs.getInt("proporcaoAlcoolGasolina", 70);
            //Atribui o valor lido ao resumo(summary) colocando '%' no final
            proporcaoPref.setSummary(String.format("%d%%", valorInt));
            //Define a posição do ponteiro do SeekBar quando não há nenhum valor definido
            //no arquivo de preferencias
            proporcaoPref.setDefaultValue(valorInt);

            //Define um Listener para capturar a mudanção do valor e atualizar o
            // resumo(summary)
            proporcaoPref.setOnPreferenceChangeListener(
                    new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    int valorInt = Integer.parseInt(newValue.toString());
                    preference.setSummary(String.format("%d%%", valorInt));
                    return true;
                }
            });
            //Define que o Listener acima será chamado a cada mínima alteração do valor
            //caso o valor fosse false o Listener seria chamado apenas quando o usuário
            //terminasse e mover SeekBar.
            proporcaoPref.setUpdatesContinuously(true);

            screen.addPreference(proporcaoPref);
        }
        private void addNomeUsuarioPref(Context context, PreferenceScreen screen) {
            EditTextPreference nomeUsuarioPref = new EditTextPreference(context);
            nomeUsuarioPref.setKey("nomeUsuario");
            nomeUsuarioPref.setTitle("Nome que identifica o usuário");
            nomeUsuarioPref.setDialogTitle("Informe o nome do usuário");

            //O Provider não define um valor para a propriedade dentro do arquivo, ele apenas
            //Ele apenas define o que apresentar como resumo(summary) baseado no conteudo
            // armazenado
            nomeUsuarioPref.setSummaryProvider(
                    new Preference.SummaryProvider<EditTextPreference>() {
                @Override
                public CharSequence provideSummary(EditTextPreference preference) {
                    String text = preference.getText();
                    //Verifica se o texto não é vazio
                    if (TextUtils.isEmpty(text)){
                        //Se for vazio retorna "Não Definido"
                        return "Não Definido";
                    }
                    //Se não for vazio, chega aqui e retorna o valor da propriedade
                    return text;
                }
            });

            screen.addPreference(nomeUsuarioPref);
        }
        private void addVersaoPref(
                Context context, PreferenceScreen screen) {
            Preference versaoPref = new Preference(context);
            versaoPref.setKey("versao");
            versaoPref.setTitle("Versão do App");
            versaoPref.setSummary("0.0.1-beta");
            //Define se o usuario pode interagir com a Preferencia "versao"
            versaoPref.setSelectable(false);

            screen.addPreference(versaoPref);
        }
        private void addCorFundoPref(Context context, PreferenceScreen screen) {
            ListPreference corDeFundoPref = new ListPreference(context);
            corDeFundoPref.setKey("corDeFundo");
            corDeFundoPref.setTitle("Cor de Fundo");
            corDeFundoPref.setSummary("Define a cor de fudo da tela principal");
            corDeFundoPref.setDialogTitle("Escolha uma cor");
            //Definimos esse valor para que quando não houver valor no arquivo o componente
            //ListPreference apresente "Branco" no resumo(summary)
            corDeFundoPref.setDefaultValue("FFFFFF");

            CharSequence[] listaNomeCor =
                    new CharSequence[]{"Branco", "Vermelho", "Verde" , "Azul"};

            CharSequence[] listaValHexa =
                    new CharSequence[]{"FFFFFF", "FF0000"  , "00FF00", "0000FF"};

            //Define a lista com os nomes das cores que os usuários veem
            corDeFundoPref.setEntries(listaNomeCor);
            //Define a lista dos valores hexadecimais correspondente as cores
            corDeFundoPref.setEntryValues(listaValHexa);

            //Define SimpleSummaryProvider como provedor de resumo.
            corDeFundoPref.setSummaryProvider(
                    ListPreference.SimpleSummaryProvider.getInstance());

            screen.addPreference(corDeFundoPref);
        }
        private void addCompartilhamentoPref(Context context, PreferenceScreen screen) {
            SwitchPreferenceCompat compartilhamentoPref = new SwitchPreferenceCompat(context);
            compartilhamentoPref.setKey("compartilhar");
            compartilhamentoPref.setTitle("Compartilhamento");
            compartilhamentoPref.setSummary(
                    "Habilita o compartilhamento do preço dos combustíveis");
            compartilhamentoPref.setOnPreferenceChangeListener(
                    new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean valor = Boolean.parseBoolean(newValue.toString());
                    if (!valor) {
                        //Busca a propriedade "compartilharLocalizacao"
                        SwitchPreferenceCompat compartilharLocalizacao =
                                findPreference("compartilharLocalizacao");
                        //Marca como false a propriedade "compartilharLocalizacao"
                        // caso ela esteja como "true"
                        compartilharLocalizacao.setChecked(false);
                    }
                    return true;
                }
            });
            screen.addPreference(compartilhamentoPref);
            //adiciona categoria "opções de compartilhamento"
            addCompCategory(context, screen);
        }
        private void addCompCategory(Context context, PreferenceScreen screen) {
            //Cria a categoria compartilhamento com uma chave(key) e titulo
            PreferenceCategory compartilhamentoCategory = new PreferenceCategory(context);
            compartilhamentoCategory.setKey("categoriaCompartilhamento");
            compartilhamentoCategory.setTitle("Opções de Compartilhamento");
            //Adiciona a categoria à tela de preferencias
            screen.addPreference(compartilhamentoCategory);

            //Define que a Categoria "Compartilhamento" só é habilitada quando
            //a propriedade "compartilhar" está com o valor "true"
            compartilhamentoCategory.setDependency("compartilhar");

            //Cria a propriedade que indica se o local deve ser compartilhado
            SwitchPreferenceCompat compartilharLocalPref = new SwitchPreferenceCompat(context);
            compartilharLocalPref.setKey("compartilharLocalizacao");
            compartilharLocalPref.setTitle("Compartilhar Localização");
            compartilharLocalPref.setSummary("Compartilhar juntamente com valores a localização");
            //Observe que compartilharLocalPref não precisa ser adicionada a screen diretamente
            //basta ser adiciona à categoria pois esta já está dentro de screen
            compartilhamentoCategory.addPreference(compartilharLocalPref);
        }
    }
}