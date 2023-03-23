package br.uefs.tec502;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    private static String codigoContrato;
    private static final String IP = "172.16.103.3";
    private static Socket cliente;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        String menu = "================================\n" +
                "\tEscolha uma opcao\t\n" +
                "================================\n" +
                "1- Acompanhar Consumo\n" +
                "2- Gerar Boleto\n" +
                "3- Testar conexão\n" +
                "4- Sair\n";

        while (true){
            System.out.println("Digite o código de contrato");
            codigoContrato = input.next();

            while (codigoContrato.trim().isEmpty()){
                System.out.println("Código de contrato inválido!\ndigite um código de contrato válido");
                codigoContrato = input.next();
            }
            int opcao;
            do{
                System.out.println(menu);
                opcao = input.nextInt();

                if(opcao == 1) {
                    try {
                        abrirConexaoComServidor();
                        String request = "GET /consumo/acompanhar-consumo?contrato="+ codigoContrato + " HTTP/1.1 \r\nHost: localhost:8081\r\n";
                        cliente.getOutputStream().write(request.getBytes("UTF-8"));

                        HttpRequest httpRequest = HttpRequest.getRequest(cliente);
                        System.out.println(httpRequest.getBody());
                        cliente.close();
                    } catch (IOException ignored){}
                } else if(opcao == 2) {
                    try {
                        abrirConexaoComServidor();
                        String request = "GET /consumo/obter-boleto?contrato="+ codigoContrato + " HTTP/1.1 \r\nHost: localhost:8081\r\n";
                        cliente.getOutputStream().write(request.getBytes("UTF-8"));

                        HttpRequest httpRequest = HttpRequest.getRequest(cliente);
                        System.out.println(httpRequest.getBody());
                        cliente.close();
                    } catch (IOException ignored){}
                } else if(opcao == 3) {
                    try {
                        abrirConexaoComServidor();
                        String request = "GET /teste HTTP/1.1 \r\nHost: localhost:8081\r\n";
                        cliente.getOutputStream().write(request.getBytes("UTF-8"));

                        HttpRequest httpRequest = HttpRequest.getRequest(cliente);
                        System.out.println(httpRequest.getBody());
                        cliente.close();
                    } catch (IOException ignored){}
                } else if(opcao != 4) {
                    System.out.println("Opção inválida");
                }
            } while (opcao != 4);
            codigoContrato = "";
        }

    }

    private static void abrirConexaoComServidor() throws IOException {
        cliente = new Socket(IP,1017);
    }
}