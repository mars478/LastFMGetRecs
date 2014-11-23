package com.mycompany.lastfmgetrecs.entity;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JSONSimilarArtists {

    JSONArtist[] artist = null;

    public JSONArtist[] getArtist() {
        return artist;
    }

    public void setArtist(JSONArtist[] artist) {
        this.artist = artist;
    }


}
