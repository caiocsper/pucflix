package model;

import aeds3.ArvoreBMais;
import aeds3.ParNomeId;
import entities.Actor;
import java.util.ArrayList;
import java.util.List;

public class ActorsFile extends aeds3.Arquivo<Actor> {
    private final ArvoreBMais<ParNomeId> indexName;
    private final InvertedIndex invertedIndex;

    public ActorsFile() throws Exception {
        super("actors", Actor.class.getConstructor());
        this.indexName = new ArvoreBMais<>(ParNomeId.class.getConstructor(), 5, "./dados/" + nomeEntidade + "/indexNames.db");
        this.invertedIndex = new InvertedIndex("./dados/" + nomeEntidade + "/invIndexDict.db", "./dados/" + nomeEntidade + "/invIndexBlocks.db");
        this.invertedIndex.setTotalEntities(getLastId());
    }

    @Override
    public int create(Actor actor) throws Exception {
        int id = super.create(actor);
        indexName.create(new ParNomeId(actor.getName(), id));
        invertedIndex.index(id, actor.getName());
        invertedIndex.setTotalEntities(getLastId());
        return id;
    }

    public Actor[] readName(String name) throws Exception {
        if (name.length() == 0) {
            return null;
        }
        var scores = invertedIndex.search(name);
        if (scores.isEmpty()) return null;
        List<Integer> sortedIds = new ArrayList<>(scores.keySet());
        sortedIds.sort((a, b) -> Float.compare(scores.get(b), scores.get(a)));
        List<Actor> result = new ArrayList<>();
        for (int id : sortedIds) {
            Actor actor = read(id);
            if (actor != null) result.add(actor);
        }
        Actor[] resultArr = new Actor[result.size()];
        for (int i = 0; i < result.size(); i++) {
            resultArr[i] = result.get(i);
        }
        return resultArr;
    }

    public Actor[] readAll() throws Exception {
        int lastId = getLastId();
        List<Actor> actors = new ArrayList<>();
        for (int currentId = 1; currentId <= lastId; currentId++) {
            Actor actor = read(currentId);
            if (actor != null)
                actors.add(actor);
        }
        Actor[] actorsArr = new Actor[actors.size()];
        for (int i = 0; i < actors.size(); i++) {
            actorsArr[i] = actors.get(i);
        }
        return actorsArr;
    }

    @Override
    public boolean delete(int id) throws Exception {
        Actor actor = read(id);
        if (actor == null || !super.delete(id))
            return false;
        invertedIndex.remove(id, actor.getName());
        invertedIndex.setTotalEntities(getLastId());
        return indexName.delete(new ParNomeId(actor.getName(), id));
    }

    @Override
    public boolean update(Actor newActor) throws Exception {
        Actor actor = read(newActor.getID());
        if (actor == null || !super.update(newActor))
            return false;
        if (!actor.getName().equals(newActor.getName())) {
            indexName.delete(new ParNomeId(actor.getName(), actor.getID()));
            indexName.create(new ParNomeId(newActor.getName(), newActor.getID()));
            invertedIndex.update(newActor.getID(), actor.getName(), newActor.getName());
        }
        return true;
    }
}
