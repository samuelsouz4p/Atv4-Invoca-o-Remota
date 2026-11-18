
import java.io.*;

public class Protocolo {

    // Identificadores das operações
    public static final int SOMAR = 1;

    // Estrutura de uma requisição
    public static class Requisicao {
        public int id;
        public int operacao;
        public int argumento1;
        public int argumento2;

        public Requisicao(int id, int operacao, int argumento1, int argumento2) {
            this.id = id;
            this.operacao = operacao;
            this.argumento1 = argumento1;
            this.argumento2 = argumento2;
        }
    }

    // Estrutura de uma resposta
    public static class Resposta {
        public int id;
        public boolean sucesso;
        public int resultado;
        public String erro;
    }

    // Empacota a requisição em um vetor de bytes
    public static byte[] empacotarRequisicao(Requisicao r)
            throws IOException {

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        DataOutputStream saida = new DataOutputStream(buffer);

        saida.writeInt(r.id);
        saida.writeInt(r.operacao);
        saida.writeInt(r.argumento1);
        saida.writeInt(r.argumento2);

        return buffer.toByteArray();
    }

    // Desempacota a requisição recebida
    public static Requisicao desempacotarRequisicao(byte[] dados)
            throws IOException {

        DataInputStream entrada = new DataInputStream(
                new ByteArrayInputStream(dados));

        int id = entrada.readInt();
        int operacao = entrada.readInt();
        int argumento1 = entrada.readInt();
        int argumento2 = entrada.readInt();

        return new Requisicao(id, operacao, argumento1, argumento2);
    }

    // Empacota a resposta em um vetor de bytes
    public static byte[] empacotarResposta(Resposta r)
            throws IOException {

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        DataOutputStream saida = new DataOutputStream(buffer);

        saida.writeInt(r.id);
        saida.writeBoolean(r.sucesso);
        saida.writeInt(r.resultado);
        saida.writeUTF(r.erro == null ? "" : r.erro);

        return buffer.toByteArray();
    }

    // Desempacota a resposta recebida
    public static Resposta desempacotarResposta(byte[] dados)
            throws IOException {

        DataInputStream entrada = new DataInputStream(
                new ByteArrayInputStream(dados));

        Resposta r = new Resposta();

        r.id = entrada.readInt();
        r.sucesso = entrada.readBoolean();
        r.resultado = entrada.readInt();
        r.erro = entrada.readUTF();

        return r;
    }
}
