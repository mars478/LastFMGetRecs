package com.mycompany.lastfmgetrecs.entity;

import net.dontdrinkandroot.lastfm.api.model.Artist;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JSONArtist {

    String name;
    String url;

   

    public Artist toArtist() {
        return new Artist(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
