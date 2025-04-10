package controller;

import entities.Show;
import model.ShowsFile;

public class ShowsController implements IController<Show> {

    ShowsFile showsFile;

    public ShowsController() throws Exception {
        showsFile = new ShowsFile();
    }

    @Override
    public Show[] findByName(String name) throws Exception {
        return showsFile.readName(name);
    }

    @Override
    public Show[] findAll() throws Exception {
        return showsFile.readAll();
    }

    @Override
    public int create(Show show) throws Exception {
        return showsFile.create(show);
    }

    @Override
    public boolean update(Show show) throws Exception {
        return showsFile.update(show);
    }

    @Override
    public boolean delete(int id) throws Exception {
        return showsFile.delete(id);
    }
}
