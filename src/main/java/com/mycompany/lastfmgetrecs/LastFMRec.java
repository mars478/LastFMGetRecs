package com.mycompany.lastfmgetrecs;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import net.dontdrinkandroot.lastfm.api.LastfmWebServicesException;
import net.dontdrinkandroot.lastfm.api.model.Artist;
import net.dontdrinkandroot.lastfm.api.model.Period;
import net.dontdrinkandroot.lastfm.api.model.User;
import net.dontdrinkandroot.lastfm.api.model.paginatedresult.PaginatedResult;
import net.dontdrinkandroot.lastfm.api.ws.DefaultLastfmWebServices;
import net.dontdrinkandroot.lastfm.api.ws.LastfmWebServices;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpRequest;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.params.HttpParams;
import org.codehaus.jackson.map.ObjectMapper;

public class LastFMRec {

    String apiKey = "1bc96f65cb9bfdcdcfdc4ded472f8c71";
    String apiSecret = "3c864d2a6433b66625da441af3e6151c";

    LastfmWebServices ws = null;

    public LastFMRec() {
    }

    public void test() {
        try {
            init().getRecsArtists(getTopArtist("mars478"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected LastFMRec init() throws ParserConfigurationException {
        ws = new DefaultLastfmWebServices(apiKey, apiSecret);
        return this;
    }

    public List<Artist> getTopArtist(String userName) throws LastfmWebServicesException {
        List<Artist> topArtists = new ArrayList<>();
        PaginatedResult<List<Artist>> result = ws.fetch(User.getTopArtists(userName, Period.OVERALL, 50, null));
        topArtists.addAll(result.getEntries());

        for (Artist art : topArtists) {
            System.out.println("\t" + art.getName());
        }

        return topArtists;
    }

    public List<Artist> getRecsArtists(List<Artist> topArtists) {
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

        return null;
    }

    protected List<Artist> getSimilar(Artist artist) {
        ObjectMapper mapper = new ObjectMapper();

        JSONArtistList art = null;
        try {
            art = mapper.readValue(getSimilarJSON(artist), JSONArtistList.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //http://ws.audioscrobbler.com/2.0/?method=artist.getsimilar&artist=cher&api_key=1bc96f65cb9bfdcdcfdc4ded472f8c71&format=json
        return null;
    }

    private String getSimilarJSON(Artist art) throws Exception {

        String url = "http://ws.audioscrobbler.com/2.0/";
        url = url + "?method=artist.getsimilar&artist=" + URLParamEncoder.encode(art.getName()) + "&api_key=" + apiKey + "&format=json&autocorrect=1";
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
