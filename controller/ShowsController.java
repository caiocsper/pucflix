package controller;

import entities.Show;
import model.ShowsFile;

public class ShowsController {

    ShowsFile showsFile;

    public ShowsController() throws Exception {
        showsFile = new ShowsFile();
    }

    public Show[] findByName(String name) throws Exception {
        return showsFile.readName(name);
    }

    public Show[] findAll() throws Exception {
        return showsFile.readAll();
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
