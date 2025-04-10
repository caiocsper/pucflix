package model;

import aeds3.Arquivo;
import aeds3.ArvoreBMais;
import aeds3.ParIdId;
import aeds3.ParNomeId;
import entities.Episode;
import java.util.ArrayList;
import java.util.List;

public class EpisodesFile extends Arquivo<Episode> {

    private final ArvoreBMais<ParIdId> indexShowEpisode;
    private final ArvoreBMais<ParNomeId> indexName;

    public EpisodesFile() throws Exception {
        super("episodes", Episode.class.getConstructor());
        indexShowEpisode = new ArvoreBMais<>(ParIdId.class.getConstructor(), 5, "dados/" + this.nomeEntidade + "/showEpisodes.db");
        indexName = new ArvoreBMais<>(ParNomeId.class.getConstructor(), 5, "dados/" + this.nomeEntidade + "/indexNames.db");
    }

    @Override
    public int create(Episode episode) throws Exception {
        int id = super.create(episode);
        indexShowEpisode.create(new ParIdId(episode.getShowID(), id));
        indexName.create(new ParNomeId(episode.getName(), id));

        return id;
    }

    public Episode[] readName(String name, int showID) throws Exception {
        if (name.length() == 0) {
            return null;
        }
        ArrayList<ParNomeId> pnis = indexName.read(new ParNomeId(name, -1));
        if (!pnis.isEmpty()) {
            List<Episode> episodes = new ArrayList<>();
            for (ParNomeId pni : pnis) {
                Episode episode = read(pni.getId());
                if (episode.getShowID() == showID) {
                    episodes.add(episode);
                }
            }
            Episode[] episodesArr = new Episode[episodes.size()];
            return episodes.toArray(episodesArr);
        } else {
            return null;
        }
    }

    public Episode[] readAll(int showID) throws Exception {
        ArrayList<ParIdId> piis = indexShowEpisode.read(new ParIdId(showID, -1));

        if (piis.isEmpty())
            return null;

        Episode[] episodes = new Episode[piis.size()];
        int i = 0;

        for (ParIdId pii : piis)
            episodes[i++] = read(pii.getId());

        return episodes;
    }

    @Override
    public boolean delete(int id) throws Exception {
        Episode episode = this.read(id);

        if (episode != null) {
            if (super.delete(id)) {
                return indexShowEpisode.delete(new ParIdId(episode.getShowID(), id))
                        && indexName.delete(new ParNomeId(episode.getName(), id));
            }
        }

        return false;
    }

    @Override
    public boolean update(Episode newEpisode) throws Exception {
        Episode episode = read(newEpisode.getID());    // na superclasse
        if (episode != null) {
            if (super.update(newEpisode)) {
                if (!episode.getName().equals(newEpisode.getName())) {
                    indexName.delete(new ParNomeId(episode.getName(), episode.getID()));
                    indexName.create(new ParNomeId(newEpisode.getName(), newEpisode.getID()));
                }
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty(int showId) throws Exception {
        return indexShowEpisode.read(new ParIdId(showId, -1)).isEmpty();
    }
}
