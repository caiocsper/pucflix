package entidades;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import aeds3.EntidadeArquivo;

public class Serie implements EntidadeArquivo {

    private int id;
    private String nome;
    private String sinopse;
    private String streaming;
    private int anoLancamento;

    public Serie() throws Exception {
        this(-1, "", "", "", 0);
    }

    public Serie(String nome, String sinopse, String streaming, int anoLancamento) throws Exception {
        this(-1, nome, sinopse, streaming, anoLancamento);
    }

    public Serie(int id, String nome, String sinopse, String streaming, int anoLancamento) throws Exception {
        this.id = id;
        this.nome = nome;
        this.sinopse = sinopse;
        this.streaming = streaming;
        this.anoLancamento = anoLancamento;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public String getStreaming() {
        return streaming;
    }

    public void setStreaming(String streaming) {
        this.streaming = streaming;
    }

    public int getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(int anoLancamento) {
        this.anoLancamento = anoLancamento;
    }

    public byte[] toByteArray() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(id);
        dos.writeUTF(nome);
        dos.writeUTF(sinopse);
        dos.writeUTF(streaming);
        dos.writeInt(anoLancamento);

        return baos.toByteArray();
    }

    public void fromByteArray(byte[] vb) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(vb);
        DataInputStream dis = new DataInputStream(bais);

        id = dis.readInt();
        nome = dis.readUTF();
        sinopse = dis.readUTF();
        streaming = dis.readUTF();
        anoLancamento = dis.readInt();
    }
}