package model;

import aeds3.*;
import entities.Show;
import java.util.ArrayList;
import java.util.List;

public class ShowsFile extends Arquivo<Show> {

    private final ArvoreBMais<ParNomeId> indexName;

    public ShowsFile() throws Exception {
        super("shows", Show.class.getConstructor());
        this.indexName = new ArvoreBMais<>(ParNomeId.class.getConstructor(), 5, "./dados/" + this.nomeEntidade + "/indexNames.db");
    }

    @Override
    public int create(Show show) throws Exception {
        int id = super.create(show);
        this.indexName.create(new ParNomeId(show.getName(), id));

        return id;
    }

    public Show[] readName(String name) throws Exception {
        if (name.length() == 0) {
            return null;
        }

        ArrayList<ParNomeId> pnis = this.indexName.read(new ParNomeId(name, -1));

        if (pnis.isEmpty())
            return null;

        Show[] shows = new Show[pnis.size()];
        int i = 0;

        for (ParNomeId pni : pnis)
            shows[i++] = this.read(pni.getId());

        return shows;
    }

    public Show[] readAll() throws Exception {
        int lastId = this.getLastId();
        List<Show> shows = new ArrayList<>();
        
        for (int currentId = 1; currentId <= lastId; currentId++) {
            Show hasShow = this.read(currentId);

            if (hasShow != null)
                shows.add(hasShow);
        }
        
        Show[] showsArr = new Show[shows.size()];
        return shows.toArray(showsArr);
    }

    @Override
    public boolean delete(int id) throws Exception {
        Show show = this.read(id);

        if (show == null || !super.delete(id))
            return false;

        return this.indexName.delete(new ParNomeId(show.getName(), id));
    }

    @Override
    public boolean update(Show newShow) throws Exception {
        Show show = this.read(newShow.getID());

        if (show == null || !super.update(newShow))
            return false;

        if (!show.getName().equals(newShow.getName())) {
            this.indexName.delete(new ParNomeId(show.getName(), show.getID()));
            this.indexName.create(new ParNomeId(newShow.getName(), newShow.getID()));
        }

        return true;
    }
}
