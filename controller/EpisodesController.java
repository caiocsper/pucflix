package controller;

import entities.Episode;
import model.EpisodesFile;

public class EpisodesController {

    EpisodesFile episodesFile;

    public EpisodesController() throws Exception {
        episodesFile = new EpisodesFile();
    }

    public Episode[] findByName(String name, int showID) throws Exception {
        return episodesFile.readName(name, showID);
    }

    public Episode[] findAll( int showID) throws Exception {
        return episodesFile.readAll(showID);
    }

    public int create(Episode episode) throws Exception {
        return episodesFile.create(episode);
    }

    public boolean update(Episode episode) throws Exception {
        return episodesFile.update(episode);
    }

    public boolean delete(int id) throws Exception {
        return episodesFile.delete(id);
    }

    public boolean isEmpty(int showID) throws Exception{
        return episodesFile.isEmpty(showID);
    }
}
