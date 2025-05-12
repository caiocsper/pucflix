package entities;

import aeds3.EntidadeArquivo;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Show implements EntidadeArquivo {

    private int id;
    private String name;
    private String summary;
    private String streamingOn;
    private short releaseYear;

    public Show() throws Exception {
        this(-1, "", "", "", (short) 0);
    }

    public Show(String name, String summary, String streamingOn, short releaseYear) throws Exception {
        this(-1, name, summary, streamingOn, releaseYear);
    }

    public Show(int id, String name, String summary, String streamingOn, short releaseYear) throws Exception {
        this.id = id;
        this.name = name;
        this.summary = summary;
        this.streamingOn = streamingOn;
        this.releaseYear = releaseYear;
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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getStreamingOn() {
        return streamingOn;
    }

    public void setStreamingOn(String streamingOn) {
        this.streamingOn = streamingOn;
    }

    public short getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(short releaseYear) {
        this.releaseYear = releaseYear;
    }

    @Override
    public byte[] toByteArray() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(id);
        dos.writeUTF(name);
        dos.writeUTF(summary);
        dos.writeUTF(streamingOn);
        dos.writeShort(releaseYear);

        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] vb) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(vb);
        DataInputStream dis = new DataInputStream(bais);

        id = dis.readInt();
        name = dis.readUTF();
        summary = dis.readUTF();
        streamingOn = dis.readUTF();
        releaseYear = dis.readShort();
    }
}
