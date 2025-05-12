package controller;

import entities.Actor;
import model.ActorsFile;

public class ActorsController implements IController<Actor> {

    ActorsFile actorsFile;

    public ActorsController() throws Exception {
        actorsFile = new ActorsFile();
    }

    @Override
    public Actor[] findByName(String name) throws Exception {
        return actorsFile.readName(name);
    }

    @Override
    public Actor[] findAll() throws Exception {
        return actorsFile.readAll();
    }

    @Override
    public int create(Actor show) throws Exception {
        return actorsFile.create(show);
    }

    @Override
    public boolean update(Actor show) throws Exception {
        return actorsFile.update(show);
    }

    @Override
    public boolean delete(int id) throws Exception {
        return actorsFile.delete(id);
    }
}
