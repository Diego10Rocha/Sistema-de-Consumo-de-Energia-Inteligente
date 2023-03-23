package br.uefs.tec502.model;

import java.time.LocalDateTime;
import java.util.List;

public class ConsumoCliente {
    private String dataMedicao;
    private Integer valorConsumido;

    private List<String> mensagem;

    /**
     * Construtor da classe ConsumoCliente
     * @param valorConsumido
     */
    public ConsumoCliente(Integer valorConsumido) {
        this.dataMedicao = LocalDateTime.now().toString();
        this.valorConsumido = valorConsumido;
        this.mensagem = null;
    }

    /**
     * Construtor da classe ConsumoCliente
     * @param valorConsumido
     * @param mensagem
     */
    public ConsumoCliente(Integer valorConsumido, List<String> mensagem) {
        this.dataMedicao = LocalDateTime.now().toString();
        this.valorConsumido = valorConsumido;
        this.mensagem = mensagem;
    }
}
