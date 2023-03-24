# MI Concorrência e Conectividade: Consumo de Energia Inteligente
O objetivo deste projeto foi a implementação de um sistema cliente-servidor utilizando Socket para implementar a comunicação entre servidor e cliente sobre uma arquitetura de rede baseada no padrão da internet.

## Introdução

Atualmente as concessionárias que gerenciam o fornecimento de energia elétrica dependem de um funcionário para realizar as medições de consumo em kwh e imprimir a fatura do consumo de cada cliente, visto que para realizar esta tarefa o funcionário tem que se deslocar de casa em casa de cada cliente para fazer a leitura do valor consumido no medidor, gerar a fatura e entregar ao cliente. Outro problema é que quando um serviço de abastecimento é interrompido em determinada residência ou região a concessionária não têm como detectar essas falhas.
    
Pensando nisso, foi proposto aos alunos do curso de Engenharia de Computação da Universidade Estadual de Feira de Santana(UEFS) que desenvolvessem um sistema inteligente com infraestrutura de IoT para oferecer um serviço eficiente de energia.

Este relatório busca descrever de forma sucinta e clara um sistema de comunicação baseado na arquitetura em rede utilizando a linguagem de programação Java para solucionar a proposta apresentada anteriormente.

O sistema foi desenvolvido utilizando sockets para criação de aplicações cliente-servidor com a comunicação utilizando o padrão de APIs Rest. Os medidores foram criados para enviar os valores de medição para o servidor, além de prover uma interface para gerenciamento que possibilita aumentar a taxa de consumo, diminuir a taxa de consumo, e definir um valor de taxa de consumo. Já o servidor foi o responsável para armazenar os valores de medição dos medidores e disponibilizar na internet para o usuário final os serviços de: acompanhar o seu consumo de energia enviando um alerta caso o mesmo tenha aumentado muito o seu consumo dentro do intervalo entre uma medição e outra; gerar boleto para pagamento da fatura. Ao final do desenvolvimento da solução do problema proposto, foi utilizado containers docker para virtualização da aplicação e execução nos ambientes de teste.


## Fundamentação Teórica
- Um servidor pode ser definido como um sistema computacional que provê serviços para serem consumidos por usuários ou até mesmo  outros sistemas. No desenvolvimento do problema, o servidor ficou responsável por centralizar o armazenamento dos dados de medição dos medidores e através da criação de APIs Rest para que possam ser consumidos por usuários ou outros sistemas, desde que tenham o código de contrato de cada usuário.
- Um cliente pode ser visto como um sistema computacional que consome dados de servidores. Estes que solicitam dados dos servidores que ao chegar uma requisição ela é tratada e uma resposta é devolvida ao cliente.
Um socket é uma abstração da camada de rede que permite a comunicação entre diferentes aplicações que precisam se comunicar via rede. Utilizando socket é possível criar servidores e fazer com que clientes possam requisitar serviços desses servidores. Ele foi utilizado para criar o servidor na solução do problema proposto implementado e para que o medidor pudesse enviar a ele os dados de medição.
- O termo API vem do inglês Application Programming Interface, em portugues Interface de Programação de Aplicativos. Uma API pode ser vista como um protocolo de comunicação utilizado na integração de aplicações, visando padronizar e definir o que é exigido por um servidor a partir de uma requisição de um cliente e a resposta dessa requisição. Em outras palavras, uma api é uma ponte entre o que um servidor fornece e espera de uma requisição e o que um cliente espera de resposta dessa requisição ao servidor e desta forma é realizada a troca de dados.
- No tópico anterior foi apresentado o conceito de API, já REST significa Representation State Transfer ou Transferência de Estado Representacional. Tendo em vista isso, uma API REST nada mais é do que um conjunto de restrições de arquitetura predefinidas para que as requisições que utilizam o protocolo HTTP atendam a estas normas e estejam todas padronizadas. 
As APIs desenvolvidas no problema podem ser testadas através dos softwares Postman ou Insomnia, que são softwares desenvolvidos justamente com o intuito de testar APIs Rest.
- Containers são ambientes isolados que podem ser instalados em computadores e serem utilizados para seus devidos fins de forma que não é necessário instalar o serviço dentro do computador como uma aplicação, então caso um container dê problema, como ele é um ambiente isolado ele pode ser deletado e uma outra instância deste container pode ser reinstalado.
- Docker é um software open source que permite a criação de container e armazenamento dos mesmo em repositórios na internet, mas também que faz a virtualização em nível de sistema operacional de suas aplicações em seus contêineres nos computadores facilitando a instalação e remoção sem afetar a máquina física caso seja necessário.


## Metodologia
Para solucionar o problema proposto, primeiro foram iniciados os estudos para esclarecer alguns conceitos presentes no problema como IoT, como implementar uma aplicação utilizando uma comunicação de rede baseada no padrão da internet e como criar uma imagem de uma aplicação e executá-la dentro de um container docker.

A linguagem de programação escolhida para a solução do problema foi Java, visto que nenhum framework pode ser utilizado no desenvolvimento, a solução foi desenvolvida através de soquetes.

Foi criado um servidor soquete para centralizar as informações, no qual foram implementados APIs Rest para tratar as requisições recebidas pelos usuários, utilizando como padrão para tráfego de dados o  formato JSON(Javascript Object Notation).
	
Foi criado também um cliente soquete para simular as medições de energia e enviar esses dados para que o servidor armazene e processe, para que os mesmos possam ser disponibilizados através de APIs para cada usuário.

Vale ressaltar que os padrões da API Rest foram seguidos, no qual nas APIs foi utilizado JSON para a transição dos dados, o método HTTP GET e os códigos de resposta como: 200 que significa ok, 400 que é um bad request e ocorre quando o usuário passa alguma informação errada, 405 not allowed que ocorre quando alguém tenta acessar uma rota através de um método HTTP diferente do esperado, 500 internal server erro que é quando ocorre um erro inesperado no servidor, 404 not found que ocorre quando o servidor não consegue encontrar um recurso requisitado pelo usuário ou quando o usuário faz uma requisição em uma rota inexistente e os demais métodos mas que não foram necessários no desenvolvimento do problema.

## Resultados

A solução do problema contou com um servidor, um cliente para enviar os dados da simulação de um medidor e uma interface para que cada usuário pudesse ter acesso às informações de sua conta.

### Servidor:
O servidor recebe de cada medidor as informações de medição de cada medidor e salva na estrutura de dados HashMap do Java sendo a chave o código de contrato de cada medidor e como valor temos uma lista com os dados de medições como valores e as datas respectivas, além disso o servidor provê APIs Rest com as seguintes rotas para consumo dos dados de medições:
- A rota “/consumo/acompanhar-consumo” utilizando o método HTTP GET fornece os dados atualizados do consumo de um determinado usuário. Para ter acesso o usuário via postman ou pela Interface do Usuário deve informar qual o seu código de contrato e assim a API responde para ele o valor da última medição a data e hora da última medição, além de apresentar um alerta caso tenha ocorrido um aumento muito grande no consumo de energia.
- A partir da rota “/consumo/obter-boleto” os usuários podem informar um código de contrato e gerar o seu boleto com informações de consumo, mês de referência e validade.
- Com a rota “/consumo/historico” utilizando o método GET os usuários podem informar seu código de contrato e visualizar o histórico de medições de seu consumo.
- A rota “/uuid” é utilizada quando um novo medidor se conecta ao servidor, então ele chama essa rota com o método GET e a partir daí é gerado um código de contrato para o devido usuário associado a este medidor, e este código é devolvido ao medidor.
- A rota “/medicao” com o método GET é utilizada pelo medidor após se cadastrar no servidor, para enviar os dados da simulação de medição do medidor de um determinado usuário.
- A rota “/teste” é utilizada apenas para verificar o status do servidor, ela retorna o status funcionando caso o servidor esteja OK e para esta rota qualquer método HTTP funciona da mesma maneira.

### Medidor:

O código do medidor primeiramente quando é executado faz uma requisição ao servidor para a rota “/uuid” para que seja gerado um código de contrado para ele e ele seja registrado no sistema e este código é retornado pela API e armazenado no medidor. 

Logo em seguida é criada uma thread para aumentar o valor consumido pelos usuário baseado numa taxa de consumo que é definida através de uma interface de gerenciamento do medidor. 

Há também uma thread para enviar os dados de medição para o servidor armazenar, nesta thread são feitas requisições para a rota “/medicao” enviando no payload um objeto JSON com o valor da medição a data da medição e o código de contrato do medidor para que o servidor identifique qual o medidor que está enviando dados de medição.

Após estas threads serem criadas, tendo em vista que dentro delas têm um “while(true)” elas executarão eternamente, então na thread de execução principal do programa é apresentado um menu para gerenciamento do medidor com as seguintes opções:

- Definir taxa de consumo.
- Aumentar a taxa de consumo.
- Diminuir a taxa de consumo.
- Visualizar qual a taxa de consumo.
- Visualizar o código de contrato do medidor/usuário.
- Visualizar o valor de medição atual.

### Interface do Usuário: 
A interface do usuário foi criada com o intuito de testar alguns endpoints do servidor, então ela se conecta ao servidor como um cliente soquete e nela é possível enviar requisições para as seguintes rotas: “/consumo/acompanhar-consumo”, “/consumo/obter-boleto” e “/teste” para testar se o servidor está funcionando.

### Docker:
Foi criado uma imagem docker para cada aplicação apresentada acima e estas imagens foram disponibilizadas no repositório online do docker hub. Para poder ter estas aplicações executando em um computador basta abrir no terminal a pasta raiz do sistema desejado e executar o seguinte comando:

```
$ docker compose up
```
Entretanto, vale ressaltar que apenas o servidor é inicializado junto ao container, para executar o medidor e a interface do usuário é necessário executar o arquivo com a extensão .jar do respectivo projeto com o seguinte comando.

Para executar o medidor basta executar o seu arquivo jar no terminal utilizando o seguinte comando
```
$ java -jar medidor-1.0-SNAPSHOT-jar-with-dependencies.jar
```
ou caso queira executar a interface do usuário basta executar o seguinte comando no console do container da imagem:
```
$ java -jar monitor-usuario-1.0-SNAPSHOT-jar-with-dependencies.jar
```
## Conclusão

Os sistemas desenvolvidos atendem o problema proposto, entretanto possui algumas ressalvas como a utilização do servidor tcp para o envio dos dados de medições do medidor, como para o medidor ao enviar os dados de medição ele não têm interesse em nenhum retorno na requisição e como a perda de pacotes não têm influência negativa no funcionamento do sistema a melhor solução seria utilizar um servidor UDP para tratar destes casos. Já como ponto positivo é importante ressaltar que os padrões de APIs Rest estão sendo seguidos como o formato da transferência de dados que foi o JSON e os códigos de retorno das APIs, inclusive do protocolo http com seus métodos de requisição. Como melhoria fica também a questão da interface de usuário que no presente momento foi desenvolvido de forma a ser utilizado através de um console de execução e futuramente poderia ser desenvolvido uma página ou sistema web para facilitar a vida do usuário final. poderia também fazer integração com um gateway de pagamentos para ao gerar o boleto o usuário poderá já pagar diretamente na plataforma. Além dos pontos de melhoria citados, não é possível deixar passar a questão da segurança dos dados para que siga as leis da LGPD.
