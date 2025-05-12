package model;

import aeds3.*;
import entities.Actor;
import java.util.ArrayList;
import java.util.List;

public class ActorsFile extends Arquivo<Actor> {

    private final ArvoreBMais<ParNomeId> indexName;

    public ActorsFile() throws Exception {
        super("actors", Actor.class.getConstructor());
        this.indexName = new ArvoreBMais<>(ParNomeId.class.getConstructor(), 5, "./dados/" + this.nomeEntidade + "/indexNames.db");
    }

    @Override
    public int create(Actor actor) throws Exception {
        int id = super.create(actor);
        this.indexName.create(new ParNomeId(actor.getName(), id));

        return id;
    }

    public Actor[] readName(String name) throws Exception {
        if (name.length() == 0) {
            return null;
        }

        ArrayList<ParNomeId> pnis = this.indexName.read(new ParNomeId(name, -1));

        if (pnis.isEmpty())
            return null;

        Actor[] actors = new Actor[pnis.size()];
        int i = 0;

        for (ParNomeId pni : pnis)
            actors[i++] = this.read(pni.getId());

        return actors;
    }

    public Actor[] readAll() throws Exception {
        int lastId = this.getLastId();
        List<Actor> actors = new ArrayList<>();
        
        for (int currentId = 1; currentId <= lastId; currentId++) {
            Actor hasActor = this.read(currentId);

            if (hasActor != null)
                actors.add(hasActor);
        }
        
        Actor[] actorsArr = new Actor[actors.size()];
        return actors.toArray(actorsArr);
    }

    @Override
    public boolean delete(int id) throws Exception {
        Actor actor = this.read(id);

        if (actor == null || !super.delete(id))
            return false;

        return this.indexName.delete(new ParNomeId(actor.getName(), id));
    }

    @Override
    public boolean update(Actor newActor) throws Exception {
        Actor actor = this.read(newActor.getID());

        if (actor == null || !super.update(newActor))
            return false;

        if (!actor.getName().equals(newActor.getName())) {
            this.indexName.delete(new ParNomeId(actor.getName(), actor.getID()));
            this.indexName.create(new ParNomeId(newActor.getName(), newActor.getID()));
        }

        return true;
    }
}
