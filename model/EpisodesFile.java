package model;

import aeds3.Arquivo;
import aeds3.ArvoreBMais;
import aeds3.ParIdId;
import entities.Episode;
import java.util.ArrayList;
import java.util.List;

public class EpisodesFile extends Arquivo<Episode> {

    private final ArvoreBMais<ParIdId> indexShowEpisode;

    public EpisodesFile() throws Exception {
        super("episodes", Episode.class.getConstructor());
        indexShowEpisode = new ArvoreBMais<>(ParIdId.class.getConstructor(), 5, "dados/" + this.nomeEntidade + "/showEpisodes.db");
    }

    @Override
    public int create(Episode episode) throws Exception {
        int id = super.create(episode);
        indexShowEpisode.create(new ParIdId(episode.getShowID(), id));

        return id;
    }

    @Override
    public boolean delete(int id) throws Exception {
        Episode episode = this.read(id);
        if (episode != null) {
            indexShowEpisode.delete(new ParIdId(episode.getShowID(), id));
        }

        return super.delete(id);
    }

    public List<Episode> findEpisodes(int showId) throws Exception {
        List<Episode> episodes = new ArrayList<>();
        int lastID = this.getLastID();

        for (int id = 1; id <= lastID; id++) {
            Episode episode = this.read(id);

            if (episode != null && episode.getShowID() == showId) {
                episodes.add(episode);
            }
        }

        return episodes;
    }
}
