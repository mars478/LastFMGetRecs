package com.mycompany.lastfmgetrecs.entity;

import java.util.Arrays;
import java.util.List;

public class JSONTopTrackList {

    JSONTopTracks toptracks;

    public JSONTopTracks getToptracks() {
        return toptracks;
    }

    public void setToptracks(JSONTopTracks toptracks) {
        this.toptracks = toptracks;
    }

    public List<JSONTopTrack> getTracks() {
        return Arrays.asList(toptracks.track);
    }

}
