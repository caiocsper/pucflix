package entities;

import aeds3.EntidadeArquivoCompleto;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Actor implements EntidadeArquivoCompleto {

    private int id;
    private String name;

    public Actor() throws Exception {
        this(-1, "");
    }

    public Actor(String name) throws Exception {
        this(-1, name);
    }

    public Actor(int id, String name) throws Exception {
        this.id = id;
        this.name = name;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void setID(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public byte[] toByteArray() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(id);
        dos.writeUTF(name);

        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] vb) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(vb);
        DataInputStream dis = new DataInputStream(bais);

        id = dis.readInt();
        name = dis.readUTF();
    }
}
