package br.uefs.tec502;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket servidor = new ServerSocket(1017);
        System.out.println("Porta 1017 aberta!");

        while (true) {
            Socket cliente = servidor.accept();
            new AtendimentoThread(cliente).start();

        }
    }
}