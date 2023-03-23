package br.uefs.tec502;

import br.uefs.tec502.dto.MedidorDTO;
import br.uefs.tec502.enums.HttpMethod;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
    private static String codigoContrato;

    private static Integer valorConsumido = 0;

    private static Integer taxaConsumo = 0;

    private static final String IP = "172.16.103.3";
    private static Socket cliente;
    public static void main(String[] args) throws IOException {

        Scanner input = new Scanner(System.in);

        //texto do menu apresentado no gerenciamento do medidor
        String menu = "================================\n" +
                "\tEscolha uma opcao\t\n" +
                "================================\n" +
                "1- Definir valor de consumo momentaneo\n" +
                "2- Aumentar valor do consumo momentaneo\n" +
                "3- Diminuir valor do consumo momentaneo\n" +
                "4- Visualizar taxa de consumo\n" +
                "5- Visualizar codigo de contrato\n" +
                "6- Visualizar valor consumido\n";
        try {
            abrirConexaoComServidor();
        } catch (IOException ignored) {

        }
        //cadastro do medidor
        String request = "GET /uuid HTTP/1.1 \r\nHost: 172.16.103.3:1017\r\n";
        cliente.getOutputStream().write(request.getBytes("UTF-8"));

        //pegando retorno da requisição
        InputStreamReader isr = new InputStreamReader(cliente.getInputStream());
        BufferedReader reader = new BufferedReader(isr);

        while (!reader.ready());
        String line = reader.readLine();
        //se a requisição retornou status 200 o codigo de contrato é salvo
        if(line.contains("OK")){
            line = reader.readLine();
            codigoContrato = reader.readLine();
        }
        if(line == null)
            System.out.println("Erro ao registrar medidor!!!!");
        System.out.println(codigoContrato);
        try {
            cliente.close();
        } catch (IOException ignored) {

        }

        //thread para fazer a atualização do valor de consumo do usuario, baseado na taxa de consumo
        new Thread(() -> {
            while(true) {
                valorConsumido += taxaConsumo;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
            }
        }).start();

        //thread para enviar os valores obtidos nas medições
        new Thread(() -> {
            while(true) {
                try {
                    abrirConexaoComServidor();
                    MedidorDTO medidorDTO = new MedidorDTO(codigoContrato, valorConsumido);
                    String payload = new Gson().toJson(medidorDTO);
                    String requestPattern = Request.makeRequest(HttpMethod.GET.getDescricao(), "/medicao", payload);
                    cliente.getOutputStream().write(requestPattern.getBytes(StandardCharsets.UTF_8));
                    Thread.sleep(10000);
                } catch (IOException | InterruptedException e) {

                } finally {
                    try {
                        cliente.close();
                    } catch (IOException ignored) {
                    }
                }
            }
        }).start();

        //menu iterativo para gerenciamento do medidor
        while (true) {

            int opcao;
            System.out.println(menu);
            opcao = input.nextInt();

            if(opcao == 1) {
                System.out.println("Defina uma taxa de consumo");
                taxaConsumo = input.nextInt();
                if(taxaConsumo < 0) taxaConsumo = 0;
            } else if(opcao == 2) {
                int aumento;
                System.out.println("Defina um valor a ser acrescido na taxa de consumo");
                aumento = input.nextInt();
                taxaConsumo += aumento;
            } else if(opcao == 3) {
                int diminuir;
                System.out.println("Defina um valor a ser decrescido na taxa de consumo");
                diminuir = input.nextInt();
                taxaConsumo -= diminuir;
                if (taxaConsumo < 0) taxaConsumo = 0;
            } else if(opcao == 4) {
                System.out.println("Taxa de consumo: " + taxaConsumo);
            } else if(opcao == 5) {
                System.out.println("Codigo de contrato do medidor: " + codigoContrato);
            } else if(opcao == 6) {
                System.out.println("Valor consumido no total: " + valorConsumido);
            } else {
                System.out.println("Opção inválida");
            }
        }

    }

    /**
     * metodo que cria uma conexão com um servidor
     * @throws IOException
     */
    private static void abrirConexaoComServidor() throws IOException {
        cliente = new Socket("192.168.0.102",8017);
    }
}