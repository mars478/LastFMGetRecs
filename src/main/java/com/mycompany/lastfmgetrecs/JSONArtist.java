package com.mycompany.lastfmgetrecs;

import org.codehaus.jackson.annotate.JsonIgnore;

public class JSONArtist {

    String name;

    @JsonIgnore
    String mbid;

    @JsonIgnore
    String match;
    String url;

    @JsonIgnore
    String[] image;

    @JsonIgnore
    String streamable;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String[] getImage() {
        return image;
    }

    public void setImage(String[] image) {
        this.image = image;
    }

    public String getStreamable() {
        return streamable;
    }

    public void setStreamable(String streamable) {
        this.streamable = streamable;
    }

}
