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
    private final InvertedIndex invertedIndex;

    public EpisodesFile() throws Exception {
        super("episodes", Episode.class.getConstructor());
        this.indexShowEpisode = new ArvoreBMais<>(ParIdId.class.getConstructor(), 5, "dados/" + nomeEntidade + "/showEpisodes.db");
        this.indexName = new ArvoreBMais<>(ParNomeId.class.getConstructor(), 5, "dados/" + nomeEntidade + "/indexNames.db");
        this.invertedIndex = new InvertedIndex("./dados/" + nomeEntidade + "/invIndexDict.db", "./dados/" + nomeEntidade + "/invIndexBlocks.db");
        this.invertedIndex.setTotalEntities(getLastId());
    }

    @Override
    public int create(Episode episode) throws Exception {
        int id = super.create(episode);
        indexShowEpisode.create(new ParIdId(episode.getShowID(), id));
        indexName.create(new ParNomeId(episode.getName(), id));
        invertedIndex.index(id, episode.getName());
        invertedIndex.setTotalEntities(getLastId());
        return id;
    }

    public Episode[] readName(String name, int showID) throws Exception {
        if (name.length() == 0)
            return null;
        var scores = invertedIndex.search(name);
        if (scores.isEmpty()) return null;
        List<Integer> sortedIds = new ArrayList<>(scores.keySet());
        sortedIds.sort((a, b) -> Float.compare(scores.get(b), scores.get(a)));
        List<Episode> result = new ArrayList<>();
        for (int id : sortedIds) {
            Episode episode = read(id);
            if (episode != null && episode.getShowID() == showID) result.add(episode);
        }
        Episode[] resultArr = new Episode[result.size()];
        for (int i = 0; i < result.size(); i++) {
            resultArr[i] = result.get(i);
        }
        return result.isEmpty() ? null : resultArr;
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
        Episode episode = read(id);
        if (episode == null || !super.delete(id))
            return false;
        invertedIndex.remove(id, episode.getName());
        invertedIndex.setTotalEntities(getLastId());
        return indexShowEpisode.delete(new ParIdId(episode.getShowID(), id))
                && indexName.delete(new ParNomeId(episode.getName(), id));
    }

    @Override
    public boolean update(Episode newEpisode) throws Exception {
        Episode episode = read(newEpisode.getID());
        if (episode == null || !super.update(newEpisode))
            return false;
        if (!episode.getName().equals(newEpisode.getName())) {
            indexName.delete(new ParNomeId(episode.getName(), episode.getID()));
            indexName.create(new ParNomeId(newEpisode.getName(), newEpisode.getID()));
            invertedIndex.update(newEpisode.getID(), episode.getName(), newEpisode.getName());
        }
        return true;
    }

    public boolean isEmpty(int showId) throws Exception {
        return indexShowEpisode.read(new ParIdId(showId, -1)).isEmpty();
    }
}
