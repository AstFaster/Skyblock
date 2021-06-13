package fr.astfaster.skyblock.island;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import fr.astfaster.skyblock.Skyblock;
import fr.astfaster.skyblock.island.bank.SBBankUpgrade;
import fr.astfaster.skyblock.island.member.SBIslandMember;
import fr.astfaster.skyblock.island.member.SBIslandMemberType;
import fr.astfaster.skyblock.util.References;
import org.bson.Document;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;

import java.util.*;

public class SBIslandManager {

    private final Map<String, Player> islandsBankOpen;
    private final List<String> islands;

    private final MongoCollection<SBIsland> islandsCollection;

    private final Skyblock skyblock;

    public SBIslandManager(Skyblock skyblock) {
        this.skyblock = skyblock;
        this.islandsCollection = this.skyblock.getSkyblockDatabase().getCollection("islands", SBIsland.class);
        this.islands = new ArrayList<>();
        this.islandsBankOpen = new HashMap<>();
    }

    /** Manage */

    public void createIsland(SBIsland island) {
        this.islands.add(island.getUuid());

        this.sendIslandToRedis(island);
        this.sendIslandToMongo(island);
    }

    public void loadIslands() {
        final FindIterable<SBIsland> islands = this.islandsCollection.find();

        for (SBIsland island : islands) {
            this.islands.add(island.getUuid());
            this.sendIslandToRedis(island);
        }
    }

    public void saveIslands() {
        for (String islandId : this.islands) {
            final SBIsland island = this.getIslandFromRedis(islandId);

            this.updateIslandInMongo(island);
            this.removeIslandFromRedis(island);
        }

        this.islands.clear();
    }

    /**
     * Mongo
     */

    public void sendIslandToMongo(SBIsland island) {
        this.islandsCollection.insertOne(island);
    }

    public void updateIslandInMongo(SBIsland island) {
        this.islandsCollection.findOneAndReplace(new Document("_id", island.getUuid()), island, new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER));
    }

    public void removeIslandFromMongo(String uuid) {
        this.islandsCollection.deleteOne(new Document("_id", uuid));
    }

    public SBIsland getIslandFromMongo(String uuid) {
        return this.islandsCollection.find(Filters.eq("_id", uuid)).first();
    }

    /**
     * Redis
     */

    public void sendIslandToRedis(SBIsland island) {
        final Jedis jedis = this.skyblock.getRedisConnection().getJedis();
        final String hash = References.NAME.toLowerCase() + ":islands:" + island.getUuid() + ":";

        jedis.hmset(hash, this.getValuesFromIsland(island));
        jedis.hmset(hash + "members:", this.getMembersValuesFromIsland(island));
    }

    public void removeIslandFromRedis(SBIsland island) {
        final Jedis jedis = this.skyblock.getRedisConnection().getJedis();
        final String hash = References.NAME.toLowerCase() + ":islands:" + island.getUuid() + ":";

        for (Map.Entry<String, String> entry : this.getValuesFromIsland(island).entrySet()) jedis.hdel(hash, entry.getKey());
        for (Map.Entry<String, String> entry : this.getMembersValuesFromIsland(island).entrySet()) jedis.hdel(hash + "members:", entry.getKey());
    }

    public SBIsland getIslandFromRedis(String uuid) {
        final Jedis jedis = this.skyblock.getRedisConnection().getJedis();
        final String hash = References.NAME.toLowerCase() + ":islands:" + uuid + ":";

        return this.getIslandFromValue(jedis.hgetAll(hash), jedis.hgetAll(hash + "members:"));
    }

    private Map<String, String> getValuesFromIsland(SBIsland island) {
        final Map<String, String> values = new HashMap<>();
        final Base64.Encoder encoder = Base64.getEncoder();

        values.put("uuid", island.getUuid());
        values.put("name", encoder.encodeToString(island.getName().getBytes()));
        values.put("description", encoder.encodeToString(island.getDescription().getBytes()));
        values.put("money", String.valueOf(island.getMoney()));
        values.put("bank", island.getBank());
        values.put("bank_upgrade", island.getBankUpgrade().name());
        values.put("created_date", String.valueOf(island.getCreatedDate()));

        return values;
    }

    private Map<String, String> getMembersValuesFromIsland(SBIsland island) {
        final Map<String, String> values = new HashMap<>();

        for (SBIslandMember member : island.getMembers()) {
            values.put(member.getUuid(), String.valueOf(member.getMemberType().getId()));
        }

        return values;
    }

    private SBIsland getIslandFromValue(Map<String, String> values, Map<String, String> membersValues) {
        final SBIsland sbIsland = new SBIsland();
        final Base64.Decoder decoder = Base64.getDecoder();

        sbIsland.setUuid(values.get("uuid"));
        sbIsland.setName(new String(decoder.decode(values.get("name"))));
        sbIsland.setDescription(new String(decoder.decode(values.get("description"))));
        sbIsland.setMoney(Double.parseDouble(values.get("money")));
        sbIsland.setBank(values.get("bank"));
        sbIsland.setBankUpgrade(SBBankUpgrade.valueOf(values.get("bank_upgrade")));
        sbIsland.setCreatedDate(Long.parseLong(values.get("created_date")));
        sbIsland.setMembers(this.getMembersFromValues(membersValues));

        return sbIsland;
    }

    private List<SBIslandMember> getMembersFromValues(Map<String, String> values) {
        final List<SBIslandMember> members = new ArrayList<>();

        for (Map.Entry<String, String> entry : values.entrySet()) {
            members.add(new SBIslandMember(entry.getKey(), SBIslandMemberType.getMemberTypeById(Integer.parseInt(entry.getValue()))));
        }

        return members;
    }

    public Map<String, Player> getIslandsBankOpen() {
        return this.islandsBankOpen;
    }
}
