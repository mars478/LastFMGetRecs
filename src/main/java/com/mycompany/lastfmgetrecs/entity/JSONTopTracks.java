package com.mycompany.lastfmgetrecs.entity;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JSONTopTracks {

    JSONTopTrack[] track;

    public JSONTopTrack[] getTrack() {
        return track;
    }

    public void setTrack(JSONTopTrack[] track) {
        this.track = track;
    }

}
