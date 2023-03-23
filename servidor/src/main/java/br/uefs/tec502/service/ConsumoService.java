package br.uefs.tec502.service;

import br.uefs.tec502.AtendimentoThread;
import br.uefs.tec502.dto.MedidorDTO;
import br.uefs.tec502.enums.HttpMethod;
import br.uefs.tec502.exception.HTTPException;
import br.uefs.tec502.http.HttpRequest;
import br.uefs.tec502.model.BoletoCliente;
import br.uefs.tec502.model.ConsumoCliente;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ConsumoService {
    public synchronized static ConsumoCliente getConsumoAtual(HttpRequest httpRequest) throws HTTPException, Error {
        if(!httpRequest.getMethod().equals(HttpMethod.GET.getDescricao()))
            throw new HTTPException();
        if(!httpRequest.getParams().containsKey("contrato") || !AtendimentoThread.getMedidores().containsKey(httpRequest.getParams().get("contrato")))
            throw new Error("'contrato' not found!");
        List<MedidorDTO> medidorDTOS = AtendimentoThread.getMedidores().get(httpRequest.getParams().get("contrato"));
        int ultimaMedicao = !medidorDTOS.isEmpty()? medidorDTOS.get(medidorDTOS.size()-1).getValorMedicao() : 0;
        List<MedidorDTO> alertas = medidorDTOS.stream().filter(MedidorDTO::isAlerta).collect(Collectors.toList());
        List<String> mensagem = new ArrayList<>();
        alertas.forEach(alerta -> {
            String aux = String.format("ALERTA: Houve um aumento significativo no seu consumo de energia na medição do momento: %s", alerta.getDataHoraMedicao());
            mensagem.add(aux);
        });
        return new ConsumoCliente(ultimaMedicao, mensagem);
    }

    public synchronized static BoletoCliente getBoleto(HttpRequest httpRequest) throws HTTPException, Error {
        if(!httpRequest.getMethod().equals(HttpMethod.GET.getDescricao()))
            throw new HTTPException();
        Objects.requireNonNull(httpRequest.getParams());
        if(!httpRequest.getParams().containsKey("contrato") || !AtendimentoThread.getMedidores().containsKey(httpRequest.getParams().get("contrato")))
            throw new Error("contrato Not Found");
        List<MedidorDTO> medidorDTOS = AtendimentoThread.getMedidores().get(httpRequest.getParams().get("contrato"));
        Integer consumoTotal = !medidorDTOS.isEmpty()? medidorDTOS.get(medidorDTOS.size()-1).getValorMedicao() : 0;
        return new BoletoCliente(httpRequest.getParams().get("contrato"), consumoTotal);
    }
}
