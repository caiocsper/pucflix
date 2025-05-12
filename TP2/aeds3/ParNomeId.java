package aeds3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class ParNomeId implements RegistroArvoreBMais<ParNomeId> {

    private String nome;
    private int id;
    private final short TAMANHO = 34;
    private final short TAMANHO_TITULO = 30;

    public ParNomeId() throws Exception {
        this("", -1);
    }

    public ParNomeId(String n) throws Exception {
        this(n, -1);
    }

    public ParNomeId(String t, int i) throws Exception {

        if (!t.isEmpty()) {

            // Converte o título para um vetor de bytes
            byte[] vb = t.getBytes(StandardCharsets.UTF_8);

            // Garante que o vetor de bytes tenha no máximo TAMANHO_TITULO bytes
            if (vb.length > TAMANHO_TITULO) {

                // Cria um vetor do tamanho máximo
                byte[] vb2 = new byte[TAMANHO_TITULO];
                System.arraycopy(vb, 0, vb2, 0, vb2.length);

                // Verifica se os últimos bytes estão fora do intervalo de 0 a 127 (o que indicaria que o último caractere é um caractere acentuado)
                int n = TAMANHO_TITULO - 1;
                while (n > 0 && (vb2[n] < 0 || vb2[n] > 127)) {
                    n--;
                }

                // Cria um novo array de bytes removendo o último byte
                byte[] vb3 = new byte[n + 1];
                System.arraycopy(vb2, 0, vb3, 0, vb3.length);
                vb2 = vb3;

                // Cria uma nova string para o nome a partir desse vetor de no máximo TAMANHO_TITULO bytes
                t = new String(vb2);
            }
        }
        this.nome = t;
        this.id = i;
    }

    public String getNome() {
        return nome;
    }

    public int getId() {
        return id;
    }

    @Override
    public ParNomeId clone() {
        try {
            return new ParNomeId(this.nome, this.id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public short size() {
        return this.TAMANHO;
    }

    @Override
    public int compareTo(ParNomeId a) {
        String str1 = transforma(this.nome);
        String str2 = transforma(a.nome);

        // reduz o tamanho da segunda string, se necessário (para facilitar as buscas)
        if (str2.length() > str1.length()) {
            str2 = str2.substring(0, str1.length());
        }
        if (str1.compareTo(str2) == 0) {
            if (this.id == -1) {
                return 0;
            } else {
                return this.id - a.id;
            }
        } else {
            return str1.compareTo(str2);
        }
    }

    @Override
    public String toString() {
        return this.nome + ";" + String.format("%-3d", this.id);
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        byte[] vb = new byte[TAMANHO_TITULO];
        byte[] vbTitulo = this.nome.getBytes();
        int i = 0;
        while (i < vbTitulo.length) {
            vb[i] = vbTitulo[i];
            i++;
        }
        while (i < TAMANHO_TITULO) {
            vb[i] = ' ';
            i++;
        }
        dos.write(vb);
        dos.writeInt(this.id);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        byte[] vb = new byte[TAMANHO_TITULO];
        dis.read(vb);
        this.nome = (new String(vb)).trim();
        this.id = dis.readInt();
    }

    public static String transforma(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("").toLowerCase();
    }

}
