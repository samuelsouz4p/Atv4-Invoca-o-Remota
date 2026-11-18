# ProtocoloUDP

Projeto da disciplina de **Sistemas Distribuídos** que demonstra a
comunicação entre cliente e servidor usando UDP em Java. O cliente
solicita uma operação de soma, o servidor executa o cálculo e devolve o
resultado.

## Estrutura

``` text
ProtocoloUDP/
├── Cliente.java    # Envia a requisição e recebe a resposta
├── Servidor.java   # Recebe a requisição, executa a soma e responde
├── Protocolo.java  # Define e empacota/desempacota as mensagens
└── README.md       # Instruções do projeto
```

## Requisitos

-   Java JDK instalado (17 ou superior recomendado).
-   VS Code (opcional), com a extensão **Extension Pack for Java**.

Confira a instalação no terminal:

``` bash
java -version
javac -version
```

## Como executar

Abra o terminal na pasta `ProtocoloUDP` e compile os arquivos:

``` bash
javac Protocolo.java Servidor.java Cliente.java
```

1.  **Terminal 1 --- servidor:** inicie o servidor:

    ``` bash
    java Servidor
    ```

2.  **Terminal 2 --- cliente:** abra outro terminal e execute:

    ``` bash
    java Cliente
    ```

3.  Digite os dois números solicitados pelo cliente. O resultado da soma
    será retornado pelo servidor.

Para testar na mesma máquina, o cliente usa `127.0.0.1`. Para testar em
computadores diferentes, altere esse endereço em `Cliente.java` para o
IP do computador onde o servidor está rodando. Os computadores precisam
ter conectividade entre si e a porta UDP `5000` deve estar liberada no
firewall do servidor.

## Teste rápido

Exemplo: informe `10` e `20` no cliente. A resposta esperada é:

``` text
Resultado da soma: 30
```

O servidor deve exibir que recebeu a requisição e enviou a resposta.
