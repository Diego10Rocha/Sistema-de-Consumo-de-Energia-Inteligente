package br.uefs.tec502;

import br.uefs.tec502.dto.MedidorDTO;
import br.uefs.tec502.dto.ResponseAPI;
import br.uefs.tec502.enums.HttpStatus;
import br.uefs.tec502.exception.HTTPException;
import br.uefs.tec502.http.HttpRequest;
import br.uefs.tec502.http.HttpResponse;
import br.uefs.tec502.model.BoletoCliente;
import br.uefs.tec502.model.ConsumoCliente;
import br.uefs.tec502.service.ConsumoService;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.Socket;
import java.util.*;


public class AtendimentoThread extends Thread {

    private static UUID uuid;
    private static Map<String, List<MedidorDTO>> medidores = new HashMap<>();

    private Socket cliente;

    public AtendimentoThread(Socket socket) {
        this.cliente = socket;
    }

    @Override
    public void run() {
        uuid = UUID.randomUUID();
        //System.out.println(uuid);

        try {
            HttpRequest httpRequest = HttpRequest.getRequest(cliente);
            ResponseAPI responseAPI = redirectToRoute(httpRequest);

            switch (responseAPI.getStatus()) {
                case OK:
                    cliente.getOutputStream().write(HttpResponse.OK(responseAPI.getPayload()).getBytes("UTF-8"));
                    break;
                case CREATED:
                    cliente.getOutputStream().write(HttpResponse.Created().getBytes("UTF-8"));
                    break;
                case BAD_REQUEST:
                    cliente.getOutputStream().write(HttpResponse.BadRequest(responseAPI.getPayload()).getBytes("UTF-8"));
                    break;
                case NOT_FOUND:
                    cliente.getOutputStream().write(HttpResponse.NotFound(responseAPI.getPayload()).getBytes("UTF-8"));
                    break;
                case NOT_ALLOWED:
                    cliente.getOutputStream().write(HttpResponse.NotAllowed().getBytes("UTF-8"));
                    break;
                case INTERNAL_SERVER_ERROR:
                    cliente.getOutputStream().write(HttpResponse.InternalServerError(responseAPI.getPayload()).getBytes("UTF-8"));
                    break;
                default:
                    cliente.getOutputStream().write(HttpResponse.OK(uuid.toString()).getBytes("UTF-8"));
                    break;
            }
            cliente.close();
        } catch (IOException ignored) {

        }
    }

    /**
     * Metodo para redirecionar cada requisição para a sua devida rota
     * @param httpRequest
     * @return
     */
    private synchronized static ResponseAPI redirectToRoute(HttpRequest httpRequest) {
        try {
            String payload = "";
            Gson gson = new Gson();
            switch (httpRequest.getEndpoint()) {
                case "/consumo/acompanhar-consumo":
                    ConsumoCliente consumoCliente = ConsumoService.getConsumoAtual(httpRequest);
                    payload = gson.toJson(consumoCliente);
                    return new ResponseAPI(payload, HttpStatus.OK);
                case "/consumo/obter-boleto":
                    BoletoCliente boletoCliente = ConsumoService.getBoleto(httpRequest);
                    payload = gson.toJson(boletoCliente);
                    return new ResponseAPI(payload, HttpStatus.OK);
                case "/uuid":
                    payload = uuid.toString();
                    medidores.put(payload, new ArrayList<>());
                    return new ResponseAPI(payload, HttpStatus.OK);
                case "/medicao":
                    String body = httpRequest.getBody();
                    MedidorDTO medidorDTO = gson.fromJson(body, MedidorDTO.class);
                    if (medidorDTO.getCodigoContrato() == null || medidorDTO.getCodigoContrato().trim().isEmpty()) {
                        medidorDTO.setCodigoContrato(uuid.toString());
                        medidores.put(medidorDTO.getCodigoContrato(), new ArrayList<>());
                    }

                    List<MedidorDTO> medidicoes = medidores.get(medidorDTO.getCodigoContrato());
                    if(medidicoes.size() > 1) {
                        int somaConsumo = medidicoes.get(medidicoes.size()-1).getValorMedicao();
                        int ultimaVariacao = somaConsumo - medidicoes.get(medidicoes.size()-2).getValorMedicao();
                        double mediaConsumo = somaConsumo / medidicoes.size();
                        if((medidorDTO.getValorMedicao() - somaConsumo) > mediaConsumo + ultimaVariacao)
                            medidorDTO.setAlerta(true);
                    }
                    medidicoes.add(medidorDTO);
                    payload = medidorDTO.getCodigoContrato();
                    return new ResponseAPI(payload, HttpStatus.OK);
                case "/teste":
                    return new ResponseAPI("{\"message\":\"Servidor socket funcionando!\"}", HttpStatus.OK);
                default:
                    return new ResponseAPI();
            }
        } catch (Error ex) {
            return new ResponseAPI(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (HTTPException ex) {
            return new ResponseAPI(ex.getMessage(), HttpStatus.NOT_ALLOWED);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseAPI(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Metodo get de encapsulamento dos dados dos medidores de cada usuário
     * @return
     */
    public static Map<String, List<MedidorDTO>> getMedidores() {
        return medidores;
    }


}
