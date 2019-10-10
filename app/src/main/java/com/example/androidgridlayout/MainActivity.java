package com.example.androidgridlayout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase b;

    public static int idAtualizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        this.b = this.openOrCreateDatabase("telefones", Context.MODE_PRIVATE, null);

        this.b.execSQL("CREATE table if not exists  telefones (" +
                "id_telefone INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome VARCHAR(120), " +
                "telefone varchar(13))");
    }

    public void btAdicionarClick(View v) {
        EditText etNome = findViewById(R.id.etNome);
        EditText etTelefone = findViewById(R.id.etTelefone);

        ContentValues cv = new ContentValues();
        cv.put("nome", etNome.getText().toString());
        cv.put("telefone", etTelefone.getText().toString());

        long rowId = b.insertOrThrow("telefones", null, cv);

        TableLayout tResultado = findViewById(R.id.tResultado);

        adicionarLinhaTabelaResultado(etNome.getText().toString(), etTelefone.getText().toString(), String.valueOf(rowId), tResultado);
    }

    public void btListarDados(View v) {
        TableLayout tResultado = findViewById(R.id.tResultado);

        tResultado.removeAllViews();
        this.adicionarLinhaTabelaResultado("Nome", "Telefone", "ID", tResultado);

        Cursor c = b.rawQuery("SELECT id_telefone, nome, telefone FROM telefones;", null);

        while (c.moveToNext()) {
            this.adicionarLinhaTabelaResultado(c.getString(1), c.getString(2), c.getString(0), tResultado);
        }

    }

    private void adicionarLinhaTabelaResultado(String etNome, String etTelefone, String rowId, TableLayout tResultado) {
        MinhaLinha tr = new MinhaLinha(this);

        TextView tv = new TextView(this);
        tv.setText(rowId);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setText(etNome);
        tr.addView(tv);

        tv = new TextView(this);
        tv.setText(etTelefone);
        tr.addView(tv);

        if (!rowId.equals("ID")) {
            tr.idTelefone = Integer.parseInt(rowId);
            tr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MinhaLinha tr = (MinhaLinha) view;
                    MainActivity.idAtualizar = tr.idTelefone;

                    Cursor c = b.rawQuery("SELECT id_telefone, nome, telefone FROM telefones WHERE id_telefone = " + tr.idTelefone + ";", null);

                    EditText etNome = findViewById(R.id.etNome);
                    EditText etTelefone = findViewById(R.id.etTelefone);

                    if (c.moveToNext()) {
                        etNome.setText(c.getString(1));
                        etTelefone.setText(c.getString(2));
                    }

                }
            });
        }

        tResultado.addView(tr);
    }

    public void btAtualizar(View v){
        EditText etNome = findViewById(R.id.etNome);
        EditText etTelefone = findViewById(R.id.etTelefone);

        String selecao = "id_telefone = ?";

        String[] selecaoArgs = { String.valueOf(MainActivity.idAtualizar) };

        ContentValues cv = new ContentValues();

        cv.put("nome", etNome.getText().toString());
        cv.put("telefone", etTelefone.getText().toString());

        long rowId = b.update("telefones", cv, selecao, selecaoArgs );
    }

}
