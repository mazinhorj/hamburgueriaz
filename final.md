
---

# Projeto HamburgueriaZ - Código Fonte Consolidado

Este documento contém a versão final e otimizada de todos os ficheiros do projeto para a entrega acadêmica (**UNOPAR/Anhanguera - 2026.1**).

## 1. AndroidManifest.xml

**Caminho:** `app/src/main/AndroidManifest.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <queries>
        <intent>
            <action android:name="android.intent.action.SENDTO" />
            <data android:scheme="mailto" />
        </intent>
    </queries>

    <application
        android:allowBackup="true"
        android:icon="@drawable/ic_hamburguer_icon"
        android:label="@string/app_name"
        android:roundIcon="@drawable/ic_hamburguer_icon"
        android:supportsRtl="true"
        android:theme="@style/Theme.HamburgueriaZ">

        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

    </application>

</manifest>

```
## 2. MainActivity.java

**Caminho:** `app/src/main/java/com/example/hamburgueriaz/MainActivity.java`

```java
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

public class MainActivity extends AppCompatActivity {

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        tvQuantidade = findViewById(R.id.tv_quantidade);
        tvResumo = findViewById(R.id.tv_resumo);
        editNome = findViewById(R.id.edit_nome);
        cbBacon = findViewById(R.id.cb_bacon);
        cbQueijo = findViewById(R.id.cb_queijo);
        cbOnion = findViewById(R.id.cb_onion);

        atualizarUI();
    }
    
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
            Toast.makeText(this, getString(R.string.aviso_minimo), Toast.LENGTH_SHORT).show();
        }
    }

    private void atualizarUI() {
        tvQuantidade.setText(String.format(Locale.getDefault(), "%d", quantidade));
        float precoTotal = (float) calcularPrecoTotal();
        tvResumo.setText(getString(R.string.formato_preco, precoTotal));
    }

    private int calcularPrecoTotal() {
        int precoUnitario = PRECO_BASE;
        if (cbBacon.isChecked()) precoUnitario += PRECO_BACON;
        if (cbQueijo.isChecked()) precoUnitario += PRECO_QUEIJO;
        if (cbOnion.isChecked()) precoUnitario += PRECO_ONION;

        return quantidade * precoUnitario;
    }

    public void enviarPedido(View view) {
        String nome = editNome.getText().toString().trim();

        if (nome.isEmpty()) {
            Toast.makeText(this, getString(R.string.aviso_nome_vazio), Toast.LENGTH_SHORT).show();
            return;
        }

        if (quantidade == 0) {
            Toast.makeText(this, getString(R.string.aviso_pedido_vazio), Toast.LENGTH_SHORT).show();
            return;
        }

        String resumo = getString(R.string.resumo_template,
                nome,
                cbBacon.isChecked() ? getString(R.string.sim) : getString(R.string.nao),
                cbQueijo.isChecked() ? getString(R.string.sim) : getString(R.string.nao),
                cbOnion.isChecked() ? getString(R.string.sim) : getString(R.string.nao),
                quantidade,
                (float) calcularPrecoTotal());

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); 
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"pedidos@hamburgueriaz.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.assunto_email, nome));
        intent.putExtra(Intent.EXTRA_TEXT, resumo);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, getString(R.string.erro_email), Toast.LENGTH_LONG).show();
        }
    }
}
```

## 3. ic_hamburguer_icon.xml

**Caminho:** `app/src/main/res/drawable/ic_hamburguer_icon.xml`

```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="110dp"
    android:height="110dp"
    android:viewportWidth="100"
    android:viewportHeight="100">

    <path
        android:fillColor="#D32F2F"
        android:pathData="M50,50m-48,0a48,48 0,1 1,96 0a48,48 0,1 1,-96 0" />

    <path
        android:fillColor="#D2691E"
        android:pathData="M25,45 Q50,20 75,45 Z" />

    <path
        android:fillColor="#32CD32"
        android:pathData="M22,45 L78,45 Q78,48 75,48 L25,48 Q22,48 22,45 Z" />

    <path
        android:fillColor="#FFD700"
        android:pathData="M25,48 L75,48 L72,52 L28,52 Z" />

    <path
        android:fillColor="#5D2E0D"
        android:pathData="M24,50 L76,50 Q78,58 76,60 L24,60 Q22,58 24,50 Z" />

    <path
        android:fillColor="#D2691E"
        android:pathData="M25,62 L75,62 Q75,70 50,70 Q25,70 25,62 Z" />

    <path
        android:fillColor="#FFFFFF"
        android:pathData="M38,38 H62 V44 L46,58 H62 V64 H38 V58 L54,44 H38 V38 Z" />
</vector>

```


## 4. activity_main.xml

**Caminho:** `app/src/main/res/layout/activity_main.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:background="#FFFFFF">

    <androidx.appcompat.widget.Toolbar
        android:id="@+id/toolbar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:minHeight="?attr/actionBarSize"
        android:background="@color/purple_500"
        android:paddingTop="10dp"
        android:elevation="4dp">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/app_name"
            android:textColor="#FFFFFF"
            android:textSize="20sp"
            android:textStyle="bold"
            android:layout_gravity="center" />

    </androidx.appcompat.widget.Toolbar>

    <ScrollView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:fillViewport="true">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:padding="24dp">

            <ImageView
                android:id="@+id/img_logo"
                android:layout_width="match_parent"
                android:layout_height="220dp"
                android:scaleType="centerCrop"
                android:src="@drawable/banner_hamburguer"
                android:background="#F5F5F5"
                android:contentDescription="@string/desc_logo"
                android:layout_marginBottom="16dp" />

            <TextView
                style="@style/EstiloTexto"
                android:text="@string/titulo_pedido"
                android:textColor="@color/purple_700" />

            <EditText
                android:id="@+id/edit_nome"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:hint="@string/hint_nome"
                android:inputType="textPersonName"
                android:minHeight="48dp" />

            <TextView
                style="@style/EstiloTexto"
                android:text="@string/label_adicionais" />

            <CheckBox android:id="@+id/cb_bacon" android:layout_width="wrap_content" android:layout_height="wrap_content" android:text="@string/opcao_bacon" android:onClick="onCheckboxClicked" />
            <CheckBox android:id="@+id/cb_queijo" android:layout_width="wrap_content" android:layout_height="wrap_content" android:text="@string/opcao_queijo" android:onClick="onCheckboxClicked" />
            <CheckBox android:id="@+id/cb_onion" android:layout_width="wrap_content" android:layout_height="wrap_content" android:text="@string/opcao_onion" android:onClick="onCheckboxClicked" />

            <TextView style="@style/EstiloTexto" android:text="@string/label_quantidade" />

            <LinearLayout
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:orientation="horizontal"
                android:gravity="center_vertical">
                <Button android:layout_width="56dp" android:layout_height="wrap_content" android:text="@string/btn_menos" android:onClick="subtrair" />
                <TextView android:id="@+id/tv_quantidade" android:layout_width="wrap_content" android:layout_height="wrap_content" android:text="@string/num_zero" android:padding="20dp" android:textSize="22sp" android:textStyle="bold" />
                <Button android:layout_width="56dp" android:layout_height="wrap_content" android:text="@string/btn_mais" android:onClick="somar" />
            </LinearLayout>

            <TextView style="@style/EstiloTexto" android:text="@string/resumo_pedido" />
            <TextView android:id="@+id/tv_resumo" android:layout_width="match_parent" android:layout_height="wrap_content" android:text="@string/valor_inicial" android:textSize="28sp" android:textStyle="bold" />

            <Button
                android:id="@+id/btn_fazer_pedido"
                android:layout_width="match_parent"
                android:layout_height="64dp"
                android:layout_marginTop="32dp"
                android:text="@string/botao_enviar"
                android:textColor="#FFFFFF"
                android:onClick="enviarPedido" />

            <TextView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:text="@string/copyright"
                android:textAlignment="center"
                android:textSize="12sp"
                android:textColor="#757575"
                android:layout_marginTop="48dp" />
        </LinearLayout>
    </ScrollView>
</LinearLayout>
```

## 5. themes.xml

**Caminho:** `app/src/main/res/values/themes.xml`

```xml
<resources xmlns:tools="http://schemas.android.com/tools">
    <style name="Theme.HamburgueriaZ" parent="Theme.MaterialComponents.Light.NoActionBar">
        <item name="colorPrimary">@color/purple_500</item>
        <item name="colorPrimaryVariant">@color/purple_700</item>
        <item name="colorOnPrimary">@color/white</item>
        <item name="android:windowFullscreen">true</item>
        <item name="android:windowContentOverlay">@null</item>
        <item name="android:statusBarColor">?attr/colorPrimaryVariant</item>
    </style>

    <style name="EstiloTexto">
        <item name="android:layout_width">wrap_content</item>
        <item name="android:layout_height">wrap_content</item>
        <item name="android:gravity">center_vertical</item>
        <item name="android:textAllCaps">true</item>
        <item name="android:textSize">15sp</item>
        <item name="android:paddingTop">16dp</item>
        <item name="android:paddingBottom">16dp</item>
        <item name="android:textColor">#212121</item>
        <item name="android:textStyle">bold</item>
    </style>
</resources>
```

## 6. colors.xml

**Caminho:** `app/src/main/res/values/colors.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="purple_200">#FFBB86FC</color>
    <color name="purple_500">#FF6200EE</color>
    <color name="purple_700">#FF3700B3</color>
    <color name="teal_200">#FF03DAC5</color>
    <color name="teal_700">#FF018786</color>
    <color name="black">#FF000000</color>
    <color name="white">#FFFFFFFF</color>

    <color name="cor_primaria">#D32F2F</color>
    <color name="cor_secundaria">#FFA000</color>
</resources>

```



## 7. strings.xml

**Caminho:** `app/src/main/res/values/strings.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">HamburgueriaZ</string>
    <string name="titulo_pedido">Faça seu pedido!</string>
    <string name="hint_nome">Introduza o nome do cliente</string>
    <string name="label_adicionais">Adicionais</string>
    <string name="opcao_bacon">Bacon (R$ 2,00)</string>
    <string name="opcao_queijo">Queijo (R$ 2,00)</string>
    <string name="opcao_onion">Onion Rings (R$ 3,00)</string>
    <string name="label_quantidade">Quantidade</string>
    <string name="resumo_pedido">Resumo do Pedido</string>
    <string name="botao_enviar">FINALIZAR PEDIDO</string>
    <string name="desc_logo">Banner da HamburgueriaZ</string>
    <string name="btn_mais">+</string>
    <string name="btn_menos">-</string>
    <string name="num_zero">0</string>
    <string name="valor_inicial">R$ 0,00</string>
    <string name="formato_preco">R$ %.2f</string>
    <string name="aviso_minimo">A quantidade mínima é zero</string>
    <string name="aviso_nome_vazio">Por favor, introduza o nome do cliente</string>
    <string name="aviso_pedido_vazio">Seu pedido está vazio! Adicione itens.</string>
    <string name="erro_email">Nenhuma aplicação de e-mail encontrada</string>
    <string name="sim">Sim</string>
    <string name="nao">Não</string>
    <string name="assunto_email">Pedido de %s (HamburgueriaZ)</string>
    <string name="resumo_template">Pedido: HamburgueriaZ\nCliente: %1$s\n\nAdicionais:\n- Bacon? %2$s\n- Queijo? %3$s\n- Onion Rings? %4$s\n\nQuantidade: %5$d\nTotal: R$ %6$.2f</string>
    <string name="copyright">MazinhoBigDaddy© - 2026 - Osmar Menezes da Silva</string>
</resources>
```








