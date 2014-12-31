package com.mycompany.lastfmgetrecs;

import com.mycompany.lastfmgetrecs.engine.LastFMRec;
import net.dontdrinkandroot.lastfm.api.model.Period;

public class Main {

    public static void main(String[] args) {
        try {
            if (args != null && args.length == 1) {
                new LastFMRec().process(Period.OVERALL, args[0]);
            } else if (args != null && args.length > 1) {
                new LastFMRec().process(Period.OVERALL, args);
            } else {
                new LastFMRec().process(Period.THREE_MONTH, "Vnv192");
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

}
