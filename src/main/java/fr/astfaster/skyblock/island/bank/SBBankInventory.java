package fr.astfaster.skyblock.island.bank;

import fr.astfaster.skyblock.Skyblock;
import fr.astfaster.skyblock.island.SBIsland;
import fr.astfaster.skyblock.island.SBIslandManager;
import fr.astfaster.skyblock.player.SBPlayer;
import fr.astfaster.skyblock.util.SerializerUtils;
import fr.astfaster.skyblock.util.inventory.SBInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SBBankInventory extends SBInventory {

    private final SBIsland island;
    private final Skyblock skyblock;

    public SBBankInventory(Skyblock skyblock, SBIsland island, Player owner, String name, int size) {
        super(owner, name, size);
        this.skyblock = skyblock;
        this.island = island;

        this.setCancelClickEvent(false);
    }

    @Override
    protected void onOpen(InventoryOpenEvent event) {
        this.skyblock.getIslandManager().getIslandsBankOpen().put(this.island.getUuid(), this.getOwner());
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {
        final SBIslandManager islandManager = this.skyblock.getIslandManager();
        final String bank = SerializerUtils.inventoryToString(event.getInventory());
        final SBPlayer player = this.skyblock.getPlayerManager().getPlayerFromRedis(event.getPlayer().getUniqueId());

        if (player.getIsland().equals(this.island.getUuid())) {
            this.island.setBank(bank);

            islandManager.sendIslandToRedis(this.island);
            islandManager.getIslandsBankOpen().remove(this.island.getUuid());
        }
    }

    public static SBBankInventory buildFromInventory(Skyblock skyblock, SBIsland island, Inventory inventory) {
        final SBBankInventory bank = new SBBankInventory(skyblock, island, null, inventory.getName(), island.getBankUpgrade().getSlot());

        for (int i = 0; i < inventory.getSize(); i++) {
            final ItemStack itemStack = inventory.getItem(i);

            if (itemStack != null) {
                bank.setItem(i, itemStack);
            }
        }

        return bank;
    }

}
