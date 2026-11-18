
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Cliente {

    private static final int PORTA_SERVIDOR = 5000;

    private static int contadorRequisicoes = 1;

    // Primitiva doOperation()
    public static Protocolo.Resposta doOperation(
            InetAddress enderecoServidor,
            int portaServidor,
            int operacao,
            int argumento1,
            int argumento2) throws IOException {

        try (DatagramSocket socket = new DatagramSocket()) {

            int id = contadorRequisicoes++;

            // Cria a requisicao
            Protocolo.Requisicao requisicao =
                    new Protocolo.Requisicao(
                            id,
                            operacao,
                            argumento1,
                            argumento2
                    );

            // Empacota os argumentos
            byte[] dados =
                    Protocolo.empacotarRequisicao(requisicao);

            // Envia a requisicao
            DatagramPacket pacote = new DatagramPacket(
                    dados,
                    dados.length,
                    enderecoServidor,
                    portaServidor
            );

            socket.send(pacote);

            System.out.println("\nRequisicao enviada!");
            System.out.println("Aguardando resposta do servidor...");

            // Aguarda a resposta
            byte[] buffer = new byte[1024];

            DatagramPacket respostaPacote = new DatagramPacket(
                    buffer,
                    buffer.length
            );

            socket.setSoTimeout(5000);
            socket.receive(respostaPacote);

            // Verifica se a resposta veio do servidor esperado
            if (!respostaPacote.getAddress().equals(enderecoServidor)
                    || respostaPacote.getPort() != portaServidor) {
                throw new IOException("Resposta de origem inesperada.");
            }

            byte[] respostaDados = new byte[respostaPacote.getLength()];

            System.arraycopy(
                    respostaPacote.getData(),
                    respostaPacote.getOffset(),
                    respostaDados,
                    0,
                    respostaPacote.getLength()
            );

            Protocolo.Resposta resposta =
                    Protocolo.desempacotarResposta(respostaDados);

            if (resposta.id != id) {
                throw new IOException("Identificador de resposta incorreto.");
            }

            return resposta;
        }
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        try {

            // Para testar na mesma maquina, utilizamos localhost
            InetAddress servidor = InetAddress.getByName("127.0.0.1");

            System.out.println("=== CLIENTE UDP ===");

            System.out.print("Digite o primeiro numero: ");
            int n1 = scanner.nextInt();

            System.out.print("Digite o segundo numero: ");
            int n2 = scanner.nextInt();

            // Chama remotamente a operacao de soma
            Protocolo.Resposta resposta = doOperation(
                    servidor,
                    PORTA_SERVIDOR,
                    Protocolo.SOMAR,
                    n1,
                    n2
            );

            if (resposta.sucesso) {

                System.out.println("\nResposta recebida do servidor!");
                System.out.println("Resultado da soma: " +
                        resposta.resultado);

            } else {

                System.out.println("Erro: " + resposta.erro);
            }

        } catch (SocketTimeoutException e) {

            System.out.println("Tempo esgotado! Servidor nao respondeu.");

        } catch (Exception e) {

            System.out.println("Erro no cliente: " + e.getMessage());

        } finally {
            scanner.close();
        }
    }
}
