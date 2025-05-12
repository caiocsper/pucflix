package model;

import aeds3.Arquivo;
import aeds3.ArvoreBMais;
import aeds3.ParIdId;
import entities.ActorShow;
import java.util.ArrayList;

public class ActorsShowsFile extends Arquivo<ActorShow> {

    private final ArvoreBMais<ParIdId> indexActor;
    private final ArvoreBMais<ParIdId> indexShow;

    public ActorsShowsFile() throws Exception {
        super("actorsShows", ActorShow.class.getConstructor());
        this.indexActor = new ArvoreBMais<>(ParIdId.class.getConstructor(), 5, "dados/" + this.nomeEntidade + "/actors.db");
        this.indexShow = new ArvoreBMais<>(ParIdId.class.getConstructor(), 5, "dados/" + this.nomeEntidade + "/shows.db");
    }

    @Override
    public int create(ActorShow actorShow) throws Exception {
        int id = super.create(actorShow);
        this.indexActor.create(new ParIdId(actorShow.getActorId(), actorShow.getShowId()));
        this.indexShow.create(new ParIdId(actorShow.getShowId(), actorShow.getActorId()));

        return id;
    }


    public ActorShow[] readActors(int showId) throws Exception {
        ArrayList<ParIdId> piis = this.indexShow.read(new ParIdId(showId, -1));

        if (piis.isEmpty())
            return null;

        ActorShow[] actorsShow = new ActorShow[piis.size()];
        int i = 0;

        for (ParIdId pii : piis)
            actorsShow[i++] = this.read(pii.getId());

        return actorsShow;
    }

    public ActorShow[] readShows(int actorId) throws Exception {
        ArrayList<ParIdId> piis = this.indexActor.read(new ParIdId(actorId, -1));

        if (piis.isEmpty())
            return null;

        ActorShow[] actorShows = new ActorShow[piis.size()];
        int i = 0;

        for (ParIdId pii : piis)
            actorShows[i++] = this.read(pii.getId());

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

    @Override
    public boolean update(ActorShow newEpisode) throws Exception {
        ActorShow actorShow = this.read(newEpisode.getID());

        return !(actorShow == null || !super.update(newEpisode));
    }

    public boolean isEmpty(int id) throws Exception {
        return this.indexActor.read(new ParIdId(id, -1)).isEmpty();
    }
}
