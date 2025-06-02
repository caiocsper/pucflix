package model;

import aeds3.Arquivo;
import aeds3.ArvoreBMais;
import aeds3.ParIdId;
import entities.Actor;
import entities.ActorShow;
import entities.Show;
import java.util.ArrayList;
import java.util.List;

public class ActorsShowsFile extends Arquivo<ActorShow> {

    private final ArvoreBMais<ParIdId> indexActor;
    private final ArvoreBMais<ParIdId> indexShow;
    private final ActorsFile actorsFile;
    private final ShowsFile showsFile;

    public ActorsShowsFile() throws Exception {
        super("actorsShows", ActorShow.class.getConstructor());
        this.indexActor = new ArvoreBMais<>(ParIdId.class.getConstructor(), 5, "dados/" + this.nomeEntidade + "/actorShows.db");
        this.indexShow = new ArvoreBMais<>(ParIdId.class.getConstructor(), 5, "dados/" + this.nomeEntidade + "/showActors.db");
        this.showsFile = new ShowsFile();
        this.actorsFile = new ActorsFile();
    }

    @Override
    public int create(ActorShow actorShow) throws Exception {
        int id = super.create(actorShow);
        this.indexActor.create(new ParIdId(actorShow.getActorId(), actorShow.getShowId()));
        this.indexShow.create(new ParIdId(actorShow.getShowId(), actorShow.getActorId()));

        return id;
    }

    public ActorShow[] readName(String name) throws Exception {
        throw new UnsupportedOperationException("Erro inesperado.");
    }


    public Actor[] readShowActors(int showId) throws Exception {
        ArrayList<ParIdId> piis = this.indexShow.read(new ParIdId(showId, -1));

        if (piis.isEmpty())
            return null;

        Actor[] showActors = new Actor[piis.size()];
        int i = 0;

        for (ParIdId pii : piis)
            showActors[i++] = this.actorsFile.read(pii.getId());

        return showActors;
    }

    public Show[] readActorShows(int actorId) throws Exception {
        ArrayList<ParIdId> piis = this.indexActor.read(new ParIdId(actorId, -1));

        if (piis.isEmpty())
            return null;

        Show[] actorShows = new Show[piis.size()];
        int i = 0;

        for (ParIdId pii : piis)
            actorShows[i++] = this.showsFile.read(pii.getId());

        return actorShows;
    }

    @Override
    public boolean delete(int id) throws Exception {
        ActorShow actorShow = this.read(id);

        if (actorShow == null || !super.delete(id))
            return false;
        
        return this.indexActor.delete(new ParIdId(actorShow.getActorId(), actorShow.getShowId()))
                && this.indexShow.delete(new ParIdId(actorShow.getShowId(), actorShow.getActorId()));
    }

    public ActorShow[] readAll() throws Exception {
        int lastId = this.getLastId();
        List<ActorShow> actorShow = new ArrayList<>();
        
        for (int currentId = 1; currentId <= lastId; currentId++) {
            ActorShow hasActorShow = this.read(currentId);

            if (hasActorShow != null)
                actorShow.add(hasActorShow);
        }
        
        ActorShow[] actorShowArr = new ActorShow[actorShow.size()];
        return actorShow.toArray(actorShowArr);
    }

    public boolean isEmpty(int actorId) throws Exception {
        return this.indexActor.read(new ParIdId(actorId, -1)).isEmpty();
    }
}
