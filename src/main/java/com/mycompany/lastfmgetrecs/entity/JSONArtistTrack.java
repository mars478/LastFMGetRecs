package com.mycompany.lastfmgetrecs.entity;

import java.util.ArrayList;
import java.util.List;
import net.dontdrinkandroot.lastfm.api.model.Artist;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JSONArtistTrack {

    JSONTopTracks artisttracks;

    public JSONTopTracks getArtisttracks() {
        return artisttracks;
    }

    public void setArtisttracks(JSONTopTracks artisttracks) {
        this.artisttracks = artisttracks;
    }

    public List<JSONTopTrack> getTracks(Artist artist) {
        List<JSONTopTrack> list = new ArrayList<>();
        for (JSONTopTrack trck : artisttracks.track) {
            list.add(trck.putArtist(artist.getName()));
        }
        return list;
    }

}
