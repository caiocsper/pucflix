package controller;

import entities.Episode;
import model.EpisodesFile;

public class EpisodesController implements IController<Episode> {

    EpisodesFile episodesFile;
    int showID;

    public EpisodesController(int showID) throws Exception {
        episodesFile = new EpisodesFile();
        this.showID = showID;
    }

    @Override
    public Episode[] findByName(String name) throws Exception {
        return episodesFile.readName(name, this.showID);
    }

    @Override
    public Episode[] findAll() throws Exception {
        return episodesFile.readAll(this.showID);
    }

    @Override
    public int create(Episode episode) throws Exception {
        return episodesFile.create(episode);
    }

    @Override
    public boolean update(Episode episode) throws Exception {
        return episodesFile.update(episode);
    }

    @Override
    public boolean delete(int id) throws Exception {
        return episodesFile.delete(id);
    }

    public boolean isEmpty(int showID) throws Exception{
        return episodesFile.isEmpty(showID);
    }
}
