package edu.upc.dsa.firefighteradventure.models;

public class Map {

    private int id;
    private String name;
    private String description;
    private String content;

    public Map() {

    }

    public Map(int id, String name, String description, String content) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
