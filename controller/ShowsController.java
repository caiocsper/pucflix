package controller;

import entities.Show;
import model.EpisodesFile;
import model.ShowsFile;

public class ShowsController {

    ShowsFile showsFile;
    EpisodesFile episodesFile;

    public ShowsController() throws Exception {
        showsFile = new ShowsFile();
        episodesFile = new EpisodesFile();
    }

    public Show[] findByName(String name) throws Exception {
        return showsFile.readName(name);
    }

    public int create(Show show) throws Exception {
        return showsFile.create(show);
    }

    public boolean update(Show show) throws Exception {
        return showsFile.update(show);
    }

    public boolean delete(int id) throws Exception {
        return showsFile.delete(id);
    }
}
