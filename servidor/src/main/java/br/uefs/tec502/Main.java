package br.uefs.tec502;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket servidor = new ServerSocket(8017);
        System.out.println("Porta 1017 aberta!");

        while (true) {
            Socket cliente = servidor.accept();
            //Cria uma nova thread para cada requisição que chega no servidor
            new AtendimentoThread(cliente).start();

        }
    }
}