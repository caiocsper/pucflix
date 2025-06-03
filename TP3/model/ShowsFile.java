package model;

import aeds3.ArvoreBMais;
import aeds3.ParNomeId;
import entities.Show;
import java.util.ArrayList;
import java.util.List;

public class ShowsFile extends aeds3.Arquivo<Show> {
    private final ArvoreBMais<ParNomeId> indexName;
    private final InvertedIndex invertedIndex;

    public ShowsFile() throws Exception {
        super("shows", Show.class.getConstructor());
        this.indexName = new ArvoreBMais<>(ParNomeId.class.getConstructor(), 5, "./dados/" + nomeEntidade + "/indexNames.db");
        this.invertedIndex = new InvertedIndex("./dados/" + nomeEntidade + "/invertedIndexDict.db", "./dados/" + nomeEntidade + "/invertedIndexBlocks.db");
        this.invertedIndex.setTotalEntities(getLastId());
    }

    @Override
    public int create(Show show) throws Exception {
        int id = super.create(show);
        indexName.create(new ParNomeId(show.getName(), id));
        invertedIndex.index(id, show.getName());
        invertedIndex.setTotalEntities(getLastId());
        return id;
    }

    public Show[] readName(String name) throws Exception {
        if (name.length() == 0) {
            return null;
        }
        var scores = invertedIndex.search(name);
        if (scores.isEmpty()) return null;
        List<Integer> sortedIds = new ArrayList<>(scores.keySet());
        sortedIds.sort((a, b) -> Float.compare(scores.get(b), scores.get(a)));
        List<Show> result = new ArrayList<>();
        for (int id : sortedIds) {
            Show show = read(id);
            if (show != null) result.add(show);
        }
        Show[] resultArr = new Show[result.size()];
        for (int i = 0; i < result.size(); i++) {
            resultArr[i] = result.get(i);
        }
        return resultArr;
    }

    public Show[] readAll() throws Exception {
        int lastId = getLastId();
        List<Show> shows = new ArrayList<>();
        for (int currentId = 1; currentId <= lastId; currentId++) {
            Show show = read(currentId);
            if (show != null)
                shows.add(show);
        }
        Show[] showsArr = new Show[shows.size()];
        for (int i = 0; i < shows.size(); i++) {
            showsArr[i] = shows.get(i);
        }
        return showsArr;
    }

    @Override
    public boolean delete(int id) throws Exception {
        Show show = read(id);
        if (show == null || !super.delete(id))
            return false;
        invertedIndex.remove(id, show.getName());
        invertedIndex.setTotalEntities(getLastId());
        return indexName.delete(new ParNomeId(show.getName(), id));
    }

    @Override
    public boolean update(Show newShow) throws Exception {
        Show show = read(newShow.getID());
        if (show == null || !super.update(newShow))
            return false;
        if (!show.getName().equals(newShow.getName())) {
            indexName.delete(new ParNomeId(show.getName(), show.getID()));
            indexName.create(new ParNomeId(newShow.getName(), newShow.getID()));
            invertedIndex.update(newShow.getID(), show.getName(), newShow.getName());
        }
        return true;
    }
}
