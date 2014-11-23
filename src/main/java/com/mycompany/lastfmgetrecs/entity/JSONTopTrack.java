package com.mycompany.lastfmgetrecs.entity;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JSONTopTrack {

    String name;
    JSONArtist artist;
    /*String duration;
     String playcount;
     String listeners;
     String mbid;
     String url;
     String[] streamable;
     String image;*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JSONArtist getArtist() {
        return artist;
    }

    public void setArtist(JSONArtist artist) {
        this.artist = artist;
    }

    public JSONTopTrack putArtist(String artistName) {
        JSONArtist temp = new JSONArtist();
        temp.setName(artistName);
        this.artist = temp;
        return this;
    }

}
