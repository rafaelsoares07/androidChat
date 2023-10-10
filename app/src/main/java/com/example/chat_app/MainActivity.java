package com.example.chat_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.transports.WebSocket;

public class MainActivity extends AppCompatActivity {

    String roomName = "";
    private Socket mSocket;
    {
        try {
            IO.Options opts = new IO.Options();
            opts.transports = new String[] {WebSocket.NAME};

            mSocket = IO.socket("http://10.1.11.130:3000" ,opts);
            mSocket.connect();
        } catch (URISyntaxException e) {
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView salaCriada = findViewById(R.id.sala_criada);
        TextView mensagens = findViewById(R.id.mensagens);

        Button btnConectarUsuario = findViewById(R.id.buscar_conexao2);
        Button btnMandarMensagem = findViewById(R.id.mandar_mensagem);

        EditText msg = findViewById(R.id.mansagem);

        btnConectarUsuario.setOnClickListener(v -> {mSocket.emit("conectar_usuario_client");});

        mSocket.on("sala_criada", (texto)->{
                runOnUiThread(() -> {
                    salaCriada.setText(texto[0].toString());
                    roomName = (String) texto[0];
                });
        });

        btnMandarMensagem.setOnClickListener(v -> {
        mSocket.emit("mensagem_sala",roomName, msg.getText().toString());
        msg.setText("");
        });

        mSocket.on("caputurar_mensagens", (msga)->{
            runOnUiThread(() -> {
                mensagens.setText(mensagens.getText().toString() + " \n" + msga[0].toString());
            });
        });




    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }

}

