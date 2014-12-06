package com.mycompany.lastfmgetrecs.engine;

import com.mycompany.lastfmgetrecs.util.URLParamEncoder;
import com.mycompany.lastfmgetrecs.entity.JSONArtistTrack;
import com.mycompany.lastfmgetrecs.entity.JSONTopTrackList;
import com.mycompany.lastfmgetrecs.entity.JSONTopTrack;
import com.mycompany.lastfmgetrecs.entity.JSONArtistList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.dontdrinkandroot.lastfm.api.LastfmWebServicesException;
import net.dontdrinkandroot.lastfm.api.model.Artist;
import net.dontdrinkandroot.lastfm.api.model.Period;
import net.dontdrinkandroot.lastfm.api.model.User;
import net.dontdrinkandroot.lastfm.api.model.paginatedresult.PaginatedResult;
import net.dontdrinkandroot.lastfm.api.ws.DefaultLastfmWebServices;
import net.dontdrinkandroot.lastfm.api.ws.LastfmWebServices;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LastFMRec {

    final static Logger logger = LoggerFactory.getLogger(LastFMRec.class);

    private final int SIMILAR_ARTISTS = 1;
    private final int TOP_TRACKS = 2;
    private final int USER_ARTIST_TRACKS = 3;

    private int WELL_KNOWN_LIMIT = 5;
    private int ARTIST_TOP_LIMIT = 30;
    private int ARTIST_RECS_LIMIT = 5;

    String apiKey = "1bc96f65cb9bfdcdcfdc4ded472f8c71";
    String apiSecret = "3c864d2a6433b66625da441af3e6151c";

    LastfmWebServices ws = null;
    String userName = null;
    Period period = Period.OVERALL;

    public LastFMRec() {
        try {
            ws = new DefaultLastfmWebServices(apiKey, apiSecret);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LastFMRec initLimits(int wellKnownLimit, int artistTopLimit, int artistRecsLimit) {
        WELL_KNOWN_LIMIT = (wellKnownLimit > 0) ? wellKnownLimit : WELL_KNOWN_LIMIT;
        ARTIST_TOP_LIMIT = (artistTopLimit > 0) ? artistTopLimit : ARTIST_TOP_LIMIT;
        ARTIST_RECS_LIMIT = (artistRecsLimit > 0) ? artistRecsLimit : ARTIST_RECS_LIMIT;
        return this;
    }

    public void test(String username) throws Exception {
        this.userName = username;
        test(getTopArtist(username), null);
    }

    public void test(String... artists) throws Exception {
        test(getArtistsFromArray(artists), null);
    }

    public void test(Period period, String username) throws Exception {
        this.userName = username;
        test(getTopArtist(username), period);
    }

    public void test(Period period, String... artists) throws Exception {
        test(getArtistsFromArray(artists), period);
    }

    private void test(List<Artist> artists, Period newPeriod) {
        period = (newPeriod == null) ? Period.OVERALL : newPeriod;
        try {
            for (JSONTopTrack track : getRecsTracks(getRecsArtists(artists, ARTIST_RECS_LIMIT))) {
                System.out.println("\t" + track.getArtist().getName() + "-" + track.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<JSONTopTrack> getRecsTracks(List<Artist> artists) {
        List<JSONTopTrack> tracks = new ArrayList<>();
        for (Artist art : artists) {
            tracks.addAll(getTopTracks(art));
        }

        return tracks;
    }

    public List<Artist> getArtistsFromArray(String... artists) {
        List<Artist> artList = new ArrayList<>();
        for (String str : artists) {
            artList.add(new Artist(str));
        }
        return artList;
    }

    public List<Artist> getTopArtist(String userName) throws LastfmWebServicesException {
        List<Artist> topArtists = new ArrayList<>();
        PaginatedResult<List<Artist>> result = ws.fetch(User.getTopArtists(userName, period, ARTIST_TOP_LIMIT, null));
        topArtists.addAll(result.getEntries());

        for (Artist art : topArtists) {
            System.out.println("\t" + art.getName());
        }

        return topArtists;
    }

    public List<Artist> getRecsArtists(List<Artist> topArtists, int limit) {
        Map<Artist, Integer> newArtists = new HashMap<>();
        List<Artist> similar = null;
        for (Artist tArt : topArtists) {
            similar = getSimilar(tArt);
            if (similar != null) {
                for (Artist sArt : similar) {
                    newArtists.put(sArt, ((!newArtists.containsKey(sArt)) ? 1 : newArtists.get(sArt) + 1));
                }
            }
        }

        //**** delete top artists from recs ****
        List<Artist> sorted = sortMapByVal(newArtists);
        List<Artist> toRemove = new ArrayList<>();
        for (Artist sortArt : sorted) {
            for (Artist topArt : topArtists) {
                try {
                    if (sortArt.getName().equals(topArt.getName())) {
                        toRemove.add(sortArt);
                    }
                } catch (Exception e) {
                }
            }
        }

        for (Artist remArt : toRemove) {
            if (sorted.contains(remArt)) {
                sorted.remove(remArt);
            }
        }
        //**** delete well-known artists from recs ****
        ArrayList<Artist> out = new ArrayList<>();
        for (Artist curArt : sorted) {
            try {
                if (userName == null) {
                    out.add(curArt);
                } else if (!(getUserArtistTracks(curArt).size() > WELL_KNOWN_LIMIT)) {
                    out.add(curArt);
                }
                if (out.size() == limit) {
                    break;
                }
            } catch (Exception e) {
                System.out.println("wtf");
                e.printStackTrace();
            }
        }

        return out;
    }

    private List<Artist> sortMapByVal(Map<Artist, Integer> map) {
        Object[] pairs = new Object[map.size()];
        int cnt = 0;
        int i = 0;
        for (Artist art : map.keySet()) {
            cnt = map.get(art);
            pairs[i++] = new Object[]{cnt, art};
        }
        // **** pairs[0] = new Object[]{count,artist} ****

        Object[] temp1;
        Object[] temp2;
        Object[] hold;
        for (i = 0; i < pairs.length; i++) {
            for (int j = i; j < pairs.length; j++) {
                temp1 = (Object[]) pairs[i];
                temp2 = (Object[]) pairs[j];
                if ((int) temp1[0] < (int) temp2[0]) {
                    hold = (Object[]) pairs[i];
                    pairs[i] = pairs[j];
                    pairs[j] = hold;
                }
            }
        }
        //**** pairs sorted by count ****

        List<Artist> ret = new ArrayList<>();
        for (i = 0; i < pairs.length; i++) {
            temp1 = (Object[]) pairs[i];
            ret.add((Artist) temp1[1]);
        }
        return ret;
    }

    protected List<JSONTopTrack> getUserArtistTracks(Artist artist) {
        ObjectMapper mapper = new ObjectMapper();

        JSONArtistTrack trck = null;
        try {
            trck = mapper.readValue(getJSON(artist, USER_ARTIST_TRACKS), JSONArtistTrack.class);
        } catch (Exception ex) {
            System.out.println("getUserArtistTracks exception:");
            ex.printStackTrace();
        }

        if (trck == null || trck.getArtisttracks() == null || trck.getArtisttracks().getTrack() == null || trck.getArtisttracks().getTrack().length == 0) {
            return new ArrayList<>();
        }

        //http://ws.audioscrobbler.com/2.0/?method=artist.getsimilar&artist=cher&api_key=1bc96f65cb9bfdcdcfdc4ded472f8c71&format=json
        try {
            return trck.getTracks(artist);//trck.getArtisttracks();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    protected List<JSONTopTrack> getTopTracks(Artist artist) {
        ObjectMapper mapper = new ObjectMapper();

        JSONTopTrackList trck = null;
        try {
            trck = mapper.readValue(getJSON(artist, TOP_TRACKS), JSONTopTrackList.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //http://ws.audioscrobbler.com/2.0/?method=artist.getsimilar&artist=cher&api_key=1bc96f65cb9bfdcdcfdc4ded472f8c71&format=json
        try {
            return trck.getTracks();
        } catch (Exception e) {
            return null;
        }
    }

    protected List<Artist> getSimilar(Artist artist) {
        ObjectMapper mapper = new ObjectMapper();

        JSONArtistList art = null;
        try {
            art = mapper.readValue(getJSON(artist, SIMILAR_ARTISTS), JSONArtistList.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //http://ws.audioscrobbler.com/2.0/?method=artist.getsimilar&artist=cher&api_key=1bc96f65cb9bfdcdcfdc4ded472f8c71&format=json
        try {
            return art.getLastFMArtistList();
        } catch (Exception e) {
            return null;
        }
    }

    private String getJSON(Artist art, int request) throws Exception {

        String url = "http://ws.audioscrobbler.com/2.0/";
        if (request == SIMILAR_ARTISTS) {
            url = url + "?method=artist.getsimilar&artist=" + URLParamEncoder.encode(art.getName()) + "&api_key=" + apiKey + "&format=json&autocorrect=1";
        } else if (request == TOP_TRACKS) {
            url = url + "?method=artist.gettoptracks&artist=" + URLParamEncoder.encode(art.getName()) + "&api_key=" + apiKey + "&format=json&autocorrect=1&limit=5";
        } else if (request == USER_ARTIST_TRACKS && userName != null) {
            http://ws.audioscrobbler.com/2.0/?method=user.getartisttracks&user=rj&artist=metallica&api_key=1bc96f65cb9bfdcdcfdc4ded472f8c71&format=json
            url = url + "?method=user.getartisttracks&user=" + URLParamEncoder.encode(userName) + "&artist=" + URLParamEncoder.encode(art.getName()) + "&api_key=" + apiKey + "&format=json&autocorrect=1&limit=12";
        } else {
            return "";
        }

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setDoOutput(true);

        StringBuilder response;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }
        System.out.println("response:" + response.toString());

        return response.toString();
    }
}
