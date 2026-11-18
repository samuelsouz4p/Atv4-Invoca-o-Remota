
import java.net.*;
import java.io.*;

public class Servidor {

    private static final int PORTA = 5000;

    // Primitiva getRequest()
    public static Protocolo.Requisicao getRequest(
            DatagramSocket socket,
            DatagramPacket pacote) throws IOException {

        socket.receive(pacote);

        byte[] dados = new byte[pacote.getLength()];

        System.arraycopy(
                pacote.getData(),
                pacote.getOffset(),
                dados,
                0,
                pacote.getLength()
        );

        return Protocolo.desempacotarRequisicao(dados);
    }

    // Primitiva sendReply()
    public static void sendReply(
            DatagramSocket socket,
            Protocolo.Resposta resposta,
            InetAddress enderecoCliente,
            int portaCliente) throws IOException {

        byte[] dados = Protocolo.empacotarResposta(resposta);

        DatagramPacket pacote = new DatagramPacket(
                dados,
                dados.length,
                enderecoCliente,
                portaCliente
        );

        socket.send(pacote);
    }

    public static void main(String[] args) {

        try (DatagramSocket socket = new DatagramSocket(PORTA)) {

            System.out.println("Servidor iniciado!");
            System.out.println("Aguardando requisicoes na porta " + PORTA);

            while (true) {

                byte[] buffer = new byte[1024];

                DatagramPacket pacote = new DatagramPacket(
                        buffer,
                        buffer.length
                );

                // Recebe a solicitação
                Protocolo.Requisicao requisicao =
                        getRequest(socket, pacote);

                System.out.println("\nRequisicao recebida!");
                System.out.println("ID: " + requisicao.id);
                System.out.println("Operacao: " + requisicao.operacao);

                Protocolo.Resposta resposta =
                        new Protocolo.Resposta();

                resposta.id = requisicao.id;
                resposta.sucesso = true;
                resposta.resultado = 0;
                resposta.erro = "";

                // Identifica e executa a operação
                if (requisicao.operacao == Protocolo.SOMAR) {

                    resposta.resultado =
                            requisicao.argumento1 +
                            requisicao.argumento2;

                    System.out.println(
                            "Executando: " +
                            requisicao.argumento1 + " + " +
                            requisicao.argumento2
                    );

                } else {

                    resposta.sucesso = false;
                    resposta.erro = "Operacao desconhecida";
                }

                // Envia a resposta ao cliente
                sendReply(
                        socket,
                        resposta,
                        pacote.getAddress(),
                        pacote.getPort()
                );

                System.out.println("Resposta enviada!");
            }

        } catch (Exception e) {
            System.out.println("Erro no servidor: " + e.getMessage());
        }
    }
}
