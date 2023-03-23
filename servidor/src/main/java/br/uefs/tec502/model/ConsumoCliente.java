package br.uefs.tec502.model;

import java.time.LocalDateTime;
import java.util.List;

public class ConsumoCliente {
    private String dataMedicao;
    private Integer valorConsumido;

    private List<String> mensagem;

    public ConsumoCliente(Integer valorConsumido) {
        this.dataMedicao = LocalDateTime.now().toString();
        this.valorConsumido = valorConsumido;
        this.mensagem = null;
    }

    public ConsumoCliente(Integer valorConsumido, List<String> mensagem) {
        this.dataMedicao = LocalDateTime.now().toString();
        this.valorConsumido = valorConsumido;
        this.mensagem = mensagem;
    }
}
