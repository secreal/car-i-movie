package com.secreal.cari_movie.models;

/**
 * Created by secreal on 5/25/2017.
 * saya ulong :)
 */

public class Trailer {
    private String key;
    private String site;
    private String name;

    public Trailer(String key, String site, String name) {
        this.key = key;
        this.site = site;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
