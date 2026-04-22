package com.example.hamburgueriaz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.Locale;

/**
 * MainActivity - Projeto HamburgueriaZ
 * Desenvolvido por: MazinhoBigDaddy(c) - 2026 - Osmar Menezes da Silva
 * Produto de aula prática da disciplina Desenvolvimento Mobile - Engenharia de Software
 * UNOPAR/Anhanguera - 7º período - 2026.1
 */
public class MainActivity extends AppCompatActivity {

    // Constantes de Preço
    private static final int PRECO_BASE = 20;
    private static final int PRECO_BACON = 2;
    private static final int PRECO_QUEIJO = 2;
    private static final int PRECO_ONION = 3;

    private int quantidade = 0;
    private TextView tvQuantidade, tvResumo;
    private EditText editNome;
    private CheckBox cbBacon, cbQueijo, cbOnion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // CONFIGURAÇÃO DA TOOLBAR (Título Centralizado e Ajuste de Notch)
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Esconde o título padrão (alinhado à esquerda) para usar o nosso TextView centralizado do XML
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Inicialização dos componentes da interface
        tvQuantidade = findViewById(R.id.tv_quantidade);
        tvResumo = findViewById(R.id.tv_resumo);
        editNome = findViewById(R.id.edit_nome);
        cbBacon = findViewById(R.id.cb_bacon);
        cbQueijo = findViewById(R.id.cb_queijo);
        cbOnion = findViewById(R.id.cb_onion);

        // Inicializa a UI com os valores padrões
        atualizarUI();
    }

    /**
     * Atualiza o preço automaticamente ao marcar/desmarcar adicionais.
     */
    public void onCheckboxClicked(View view) {
        atualizarUI();
    }

    public void somar(View view) {
        quantidade++;
        atualizarUI();
    }

    public void subtrair(View view) {
        if (quantidade > 0) {
            quantidade--;
            atualizarUI();
        } else {
            // Usa o recurso de string para o aviso de limite mínimo
            Toast.makeText(this, getString(R.string.aviso_minimo), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Atualiza os textos de quantidade e preço total consumindo recursos do strings.xml.
     */
    private void atualizarUI() {
        tvQuantidade.setText(String.format(Locale.getDefault(), "%d", quantidade));
        float precoTotal = (float) calcularPrecoTotal();

        // Usa o template "R$ %.2f" definido no strings.xml
        tvResumo.setText(getString(R.string.formato_preco, precoTotal));
    }

    /**
     * Calcula o valor total com base na quantidade e nos adicionais selecionados.
     */
    private int calcularPrecoTotal() {
        int precoUnitario = PRECO_BASE;
        if (cbBacon.isChecked()) precoUnitario += PRECO_BACON;
        if (cbQueijo.isChecked()) precoUnitario += PRECO_QUEIJO;
        if (cbOnion.isChecked()) precoUnitario += PRECO_ONION;

        return quantidade * precoUnitario;
    }

    /**
     * Valida os dados, gera o resumo formatado e envia via Intent de e-mail.
     */
    public void enviarPedido(View view) {
        String nome = editNome.getText().toString().trim();

        // Validação 1: Nome do cliente
        if (nome.isEmpty()) {
            Toast.makeText(this, getString(R.string.aviso_nome_vazio), Toast.LENGTH_SHORT).show();
            return;
        }

        // Validação 2: Pedido vazio
        if (quantidade == 0) {
            Toast.makeText(this, getString(R.string.aviso_pedido_vazio), Toast.LENGTH_SHORT).show();
            return;
        }

        // Montagem do resumo usando o template dinâmico do strings.xml
        String resumo = getString(R.string.resumo_template,
                nome,
                cbBacon.isChecked() ? getString(R.string.sim) : getString(R.string.nao),
                cbQueijo.isChecked() ? getString(R.string.sim) : getString(R.string.nao),
                cbOnion.isChecked() ? getString(R.string.sim) : getString(R.string.nao),
                quantidade,
                (float) calcularPrecoTotal());

        // Configuração da Intent de E-mail
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));

        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"pedidos@hamburgueriaz.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.assunto_email, nome));
        intent.putExtra(Intent.EXTRA_TEXT, resumo);

        // Verifica se há um app de e-mail disponível (Gmail, Outlook, etc)
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, getString(R.string.erro_email), Toast.LENGTH_LONG).show();
        }
    }
}