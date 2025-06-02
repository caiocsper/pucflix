package controller;

import entities.Actor;
import entities.ActorShow;
import entities.Show;
import model.ActorsShowsFile;

public class ActorsShowsController implements IController<ActorShow> {

    ActorsShowsFile actorsShowsFile;
    int showID;

    public ActorsShowsController(int showID) throws Exception {
        actorsShowsFile = new ActorsShowsFile();
        this.showID = showID;
    }

    @Override
    public ActorShow[] findByName(String name) throws Exception {
        return actorsShowsFile.readName(name);
    }

    @Override
    public ActorShow[] findAll() throws Exception {
        return actorsShowsFile.readAll();
    }

    @Override
    public int create(ActorShow actorShow) throws Exception {
        return actorsShowsFile.create(actorShow);
    }

    @Override
    public boolean update(ActorShow actorShow) throws Exception {
        return actorsShowsFile.update(actorShow);
    }

    public Actor[] readShowActors() throws Exception {
        return actorsShowsFile.readShowActors(this.showID);
    }

    public Show[] readActorShows(int actorId) throws Exception {
        return actorsShowsFile.readActorShows(actorId);
    }

    @Override
    public boolean delete(int id) throws Exception {
        return actorsShowsFile.delete(id);
    }

    public boolean isEmpty(int showID) throws Exception{
        return actorsShowsFile.isEmpty(showID);
    }
}
