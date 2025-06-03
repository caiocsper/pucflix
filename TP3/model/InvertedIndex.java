package model;

import aeds3.ElementoLista;
import aeds3.ListaInvertida;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import util.TextUtils;

public class InvertedIndex {
    private final ListaInvertida invertedList;
    private int totalEntities;

    public InvertedIndex(String dictFile, String blocksFile) throws Exception {
        this.invertedList = new ListaInvertida(32, dictFile, blocksFile);
        this.totalEntities = 0;
    }

    public void setTotalEntities(int total) {
        this.totalEntities = total;
    }

    public int getTotalEntities() {
        return this.totalEntities;
    }

    public void index(int id, String text) throws Exception {
        ArrayList<String> terms = TextUtils.tokenize(text);
        Map<String, Integer> frequencies = new HashMap<>();
        for (String term : terms) {
            frequencies.put(term, frequencies.getOrDefault(term, 0) + 1);
        }
        int total = terms.size();
        for (Map.Entry<String, Integer> entry : frequencies.entrySet()) {
            float tf = (float) entry.getValue() / total;
            invertedList.create(entry.getKey(), new ElementoLista(id, tf));
        }
        totalEntities++;
    }

    public void remove(int id, String text) throws Exception {
        ArrayList<String> terms = TextUtils.tokenize(text);
        Set<String> uniqueTerms = new HashSet<>(terms);
        for (String term : uniqueTerms) {
            invertedList.delete(term, id);
        }
        totalEntities = Math.max(0, totalEntities - 1);
    }

    public void update(int id, String oldText, String newText) throws Exception {
        remove(id, oldText);
        index(id, newText);
    }

    public Map<Integer, Float> search(String query) throws Exception {
        ArrayList<String> terms = TextUtils.tokenize(query);
        Map<Integer, Float> scores = new HashMap<>();
        for (String term : terms) {
            ElementoLista[] elements = invertedList.read(term);
            if (elements == null || elements.length == 0) continue;
            int df = elements.length;
            float idf = (float) (Math.log((double) Math.max(1, totalEntities) / (df == 0 ? 1 : df)) + 1);
            for (ElementoLista element : elements) {
                float score = element.getFrequencia() * idf;
                scores.put(element.getId(), scores.getOrDefault(element.getId(), 0f) + score);
            }
        }
        return scores;
    }
}
