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
        this.indexShowEpisode = new ArvoreBMais<>(ParIdId.class.getConstructor(), 5, "dados/" + this.nomeEntidade + "/showEpisodes.db");
        this.indexName = new ArvoreBMais<>(ParNomeId.class.getConstructor(), 5, "dados/" + this.nomeEntidade + "/indexNames.db");
    }

    @Override
    public int create(Episode episode) throws Exception {
        int id = super.create(episode);
        this.indexShowEpisode.create(new ParIdId(episode.getShowID(), id));
        this.indexName.create(new ParNomeId(episode.getName(), id));

        return id;
    }

    public Episode[] readName(String name, int showID) throws Exception {
        if (name.length() == 0)
            return null;

        ArrayList<ParNomeId> pnis = indexName.read(new ParNomeId(name, -1));

        if (pnis.isEmpty())
            return null;

        List<Episode> episodes = new ArrayList<>();

        for (ParNomeId pni : pnis) {
            Episode episode = this.read(pni.getId());

            if (episode.getShowID() == showID)
                episodes.add(episode);
        }

        if (episodes.isEmpty()) 
            return null;

        Episode[] episodesArr = new Episode[episodes.size()];
        return episodes.toArray(episodesArr);
    }

    public Episode[] readAll(int showID) throws Exception {
        ArrayList<ParIdId> piis = this.indexShowEpisode.read(new ParIdId(showID, -1));

        if (piis.isEmpty())
            return null;

        Episode[] episodes = new Episode[piis.size()];
        int i = 0;

        for (ParIdId pii : piis)
            episodes[i++] = this.read(pii.getId());

        return episodes;
    }

    @Override
    public boolean delete(int id) throws Exception {
        Episode episode = this.read(id);

        if (episode == null || !super.delete(id))
            return false;
        
        return this.indexShowEpisode.delete(new ParIdId(episode.getShowID(), id))
                && this.indexName.delete(new ParNomeId(episode.getName(), id));
    }

    @Override
    public boolean update(Episode newEpisode) throws Exception {
        Episode episode = this.read(newEpisode.getID());

        if (episode == null || !super.update(newEpisode))
            return false;

        if (!episode.getName().equals(newEpisode.getName())) {
            this.indexName.delete(new ParNomeId(episode.getName(), episode.getID()));
            this.indexName.create(new ParNomeId(newEpisode.getName(), newEpisode.getID()));
        }

        return true;
    }

    public boolean isEmpty(int showId) throws Exception {
        return this.indexShowEpisode.read(new ParIdId(showId, -1)).isEmpty();
    }
}
