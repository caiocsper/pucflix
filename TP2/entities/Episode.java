package entities;

import aeds3.EntidadeArquivoCompleto;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.time.LocalDate;

public class Episode implements EntidadeArquivoCompleto {

    private int id;
    private int idShow;
    private String name;
    private byte season;
    private byte length;
    private LocalDate releaseDate;

    public Episode() throws Exception {
        this(-1, -1, "", (byte) 0, (byte) 0, LocalDate.now());
    }

    public Episode(int idShow, String name, byte season, byte length, LocalDate releaseDate) throws Exception {
        this(-1, idShow, name, season, length, releaseDate);
    }

    public Episode(int id, int idShow, String name, byte season, byte length, LocalDate releaseDate) throws Exception {
        this.id = id;
        this.idShow = idShow;
        this.name = name;
        this.season = season;
        this.length = length;
        this.releaseDate = releaseDate;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void setID(int id) {
        this.id = id;
    }

    public int getShowID() {
        return idShow;
    }

    public void setShowID(int idShow) {
        this.idShow = idShow;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public byte getSeason() {
        return season;
    }

    public void setSeason(byte season) {
        this.season = season;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getLength() {
        return length;
    }

    public void setLength(byte length) {
        this.length = length;
    }

    @Override
    public byte[] toByteArray() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(id);
        dos.writeInt(idShow);
        dos.writeUTF(name);
        dos.writeByte(season);
        dos.writeByte(length);
        dos.writeInt((int) releaseDate.toEpochDay());

        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] vb) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(vb);
        DataInputStream dis = new DataInputStream(bais);

        id = dis.readInt();
        idShow = dis.readInt();
        name = dis.readUTF();
        season = dis.readByte();
        length = dis.readByte();
        releaseDate = LocalDate.ofEpochDay(dis.readInt());
    }
}
