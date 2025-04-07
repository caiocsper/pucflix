package model;

import aeds3.*;
import entities.Show;
import java.util.ArrayList;

public class ShowsFile extends Arquivo<Show> {

    private final ArvoreBMais<ParNomeId> indexName;

    public ShowsFile() throws Exception {
        super("shows", Show.class.getConstructor());
        indexName = new ArvoreBMais<>(
                ParNomeId.class.getConstructor(),
                5,
                "./dados/" + this.nomeEntidade + "/indexNames.db");
    }

    @Override
    public int create(Show show) throws Exception {
        int id = super.create(show);
        indexName.create(new ParNomeId(show.getName(), id));
        return id;
    }

    public Show[] readName(String name) throws Exception {
        if (name.length() == 0) {
            return null;
        }
        ArrayList<ParNomeId> pnis = indexName.read(new ParNomeId(name, -1));
        if (!pnis.isEmpty()) {
            Show[] shows = new Show[pnis.size()];
            int i = 0;
            for (ParNomeId pni : pnis) {
                shows[i++] = read(pni.getId());
            }
            return shows;
        } else {
            return null;
        }
    }

    @Override
    public boolean delete(int id) throws Exception {
        Show show = read(id);   // na superclasse
        if (show != null) {
            if (super.delete(id)) {
                return indexName.delete(new ParNomeId(show.getName(), id));
            }
        }
        return false;
    }

    @Override
    public boolean update(Show newShow) throws Exception {
        Show show = read(newShow.getID());    // na superclasse
        if (show != null) {
            if (super.update(newShow)) {
                if (!show.getName().equals(newShow.getName())) {
                    indexName.delete(new ParNomeId(show.getName(), show.getID()));
                    indexName.create(new ParNomeId(newShow.getName(), newShow.getID()));
                }
                return true;
            }
        }
        return false;
    }
}
