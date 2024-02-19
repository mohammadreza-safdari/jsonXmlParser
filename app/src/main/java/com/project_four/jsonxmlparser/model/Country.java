package com.project_four.jsonxmlparser.model;

public class Country {

    private String name;
    private String code;
    private int rank;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoed() {
        return code;
    }

    public void setCoed(String coed) {
        this.code = coed;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return name + "\n" +
                code + "\n" +
                rank;
    }
}
