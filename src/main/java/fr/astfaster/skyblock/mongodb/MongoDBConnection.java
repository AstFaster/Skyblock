package fr.astfaster.skyblock.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

public class MongoDBConnection {

    private MongoClientSettings mongoClientSettings;
    private MongoClient mongoClient;

    private final String mongoUrl;

    public MongoDBConnection(String mongoUrl, boolean setup) {
        this.mongoUrl = mongoUrl;
        if (setup) this.setup();
    }

    public void setup() {
        final ConnectionString connectionString = new ConnectionString(this.mongoUrl);
        final CodecRegistry pojoCodecProvider = CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build());
        final CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecProvider);

        this.mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();

        this.mongoClient = MongoClients.create(this.mongoClientSettings);
    }

    public MongoClient getMongoClient() {
        return this.mongoClient;
    }

    public MongoClientSettings getMongoClientSettings() {
        return this.mongoClientSettings;
    }

}
