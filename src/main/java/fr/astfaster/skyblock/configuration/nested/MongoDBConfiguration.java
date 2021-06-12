package fr.astfaster.skyblock.configuration.nested;

public class MongoDBConfiguration {

    private String url;
    private String database;

    public MongoDBConfiguration(String url, String database) {
        this.url = url;
        this.database = database;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDatabase() {
        return this.database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

}
