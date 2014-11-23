package com.mycompany.lastfmgetrecs.entity;

import java.util.ArrayList;
import java.util.List;
import net.dontdrinkandroot.lastfm.api.model.Artist;

public class JSONArtistList {

    JSONSimilarArtists similarartists = null;

    public JSONSimilarArtists getSimilarartists() {
        return similarartists;
    }

    public void setSimilarartists(JSONSimilarArtists similarartists) {
        this.similarartists = similarartists;
    }

    public List<Artist> getLastFMArtistList() {
        try {
            List<Artist> list = new ArrayList<>();
            for (JSONArtist json : similarartists.getArtist()) {
                list.add(json.toArtist());
            }
            return list;
        } catch (Exception e) {
            return null;
        }
    }

}
