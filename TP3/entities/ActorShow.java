package entities;

import aeds3.EntidadeArquivoCompleto;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class ActorShow implements EntidadeArquivoCompleto {

    private int id;
    private int actorId;
    private int showId;

    public ActorShow() throws Exception {
        this(-1, -1, -1);
    }

    public ActorShow(int actorId, int showId) throws Exception {
        this(-1, actorId, showId);
    }

    public ActorShow(int id, int actorId, int showId) throws Exception {
        this.id = id;
        this.actorId = actorId;
        this.showId = showId;
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
        throw new UnsupportedOperationException("Erro inesperado");
    }

    @Override
    public void setName(String name) {
        throw new UnsupportedOperationException("Erro inesperado");
    }

    public int getActorId() {
        return actorId;
    }

    public void setActorId(int id) {
        this.actorId = id;
    }

    public int getShowId() {
        return showId;
    }

    public void setShowId(int id) {
        this.showId = id;
    }

    @Override
    public byte[] toByteArray() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(id);
        dos.writeInt(actorId);
        dos.writeInt(showId);

        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] vb) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(vb);
        DataInputStream dis = new DataInputStream(bais);

        id = dis.readInt();
        actorId = dis.readInt();
        showId = dis.readInt();
    }
}
