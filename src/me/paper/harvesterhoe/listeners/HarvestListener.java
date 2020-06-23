package me.paper.harvesterhoe.listeners;

import me.paper.harvesterhoe.HarvesterHoe;
import me.paper.harvesterhoe.enchants.EnchantManager;
import me.paper.harvesterhoe.inventory.UpgradeInventory;
import me.paper.harvesterhoe.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class HarvestListener implements Listener, Color {

    private FileConfiguration config = HarvesterHoe.getInstance().getConfig();

    @EventHandler
    public void onHarvest(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand() != null) {
            ItemStack itemStack = player.getItemInHand();
            if (itemStack.getType().toString().contains("HOE")) {
                if (format(itemStack.getItemMeta().getDisplayName()).equalsIgnoreCase(format(config.getString("harvester_item.name")))) {
                    if (event.getBlock() != null && event.getBlock().getType() != null) {
                        if (event.getBlock().getType().equals(Material.SUGAR_CANE_BLOCK)) {
                            EnchantManager enchantManager = HarvesterHoe.getInstance().getEnchantManager();
                            event.setCancelled(true);
                            List<Block> sugarcane = getSugarcane(event.getBlock());
                            giveSugarcaneAccordingly(player,sugarcane);
                            return;
                        }
                    }
                }
            }
        }
    }

    private void giveSugarcaneAccordingly(Player player, List<Block> sugarcane) {
        ItemStack harvester = player.getItemInHand();
        EnchantManager enchantManager = HarvesterHoe.getInstance().getEnchantManager();

        int gemHunterL = harvester.getEnchantmentLevel(enchantManager.gemHunter);
        int fortuneL = harvester.getEnchantmentLevel(enchantManager.fortune);
        int spawnerHunterL = harvester.getEnchantmentLevel(enchantManager.spawnerHunter);

        int original = sugarcane.size();
        player.sendMessage("original: " + original);

        if (fortuneL > 0) {
            original = (int) Math.round(config.getDouble("enchants.fortune.multiplier")*fortuneL*original);
            player.sendMessage("fortune original: " + original);
        }
        if (gemHunterL > 0) {
            player.sendMessage("you should have gotten " + Math.round(config.getDouble("enchants.gem_hunter.multiplier")*gemHunterL*original) + " gems");
        }
        if (spawnerHunterL > 0) {
            int realChance = (int) Math.round(config.getDouble("enchants.spawner_hunter.multiplier")*spawnerHunterL);
            Random random = new Random();
            int randomChance = random.nextInt(100) + 1;
            player.sendMessage("you had a chance of " + randomChance + ", but the real chance is " + realChance);
            if (randomChance <= realChance) {
                if (player.getInventory().firstEmpty() != -1) {
                    ArrayList<EntityType> entities = new ArrayList<>(Arrays.asList(EntityType.values()));
                    EntityType randomEntity = entities.get(new Random().nextInt(entities.size()));
                    String entityName = randomEntity.getName();
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ss give " + player.getName() + " " + entityName + " 1");
                }
            }
        }
        if (canHoldItems(player, sugarcane.size())) {
            ListIterator<Block> iterator = sugarcane.listIterator(sugarcane.size());
            while (iterator.hasPrevious()) {
                Block previous = iterator.previous();
                previous.setType(Material.AIR);
            }
            player.getInventory().addItem(new ItemStack(Material.SUGAR_CANE, original));
        } else {
            player.sendMessage(format(config.getString("messages.full_inventory")));
            return;
        }
    }

    private List<Block> getSugarcane(Block block) {
        List<Block> sugarcane = new ArrayList<>();
        sugarcane.add(block);
        Location possibleLocation = block.getLocation().clone().add(0.0,1.0,0.0);
        while (possibleLocation.getBlock().getType() == Material.SUGAR_CANE_BLOCK) {
            sugarcane.add(possibleLocation.getBlock());
            possibleLocation.add(0.0,1.0,0.0);
        }
        return sugarcane;
    }

    private boolean canHoldItems(Player player, int amount) {
        for (ItemStack itemstacks : player.getInventory().getContents()) {
            if (itemstacks == null) {
                return true;
            }else {
                if (itemstacks.getType().equals(Material.SUGAR_CANE)) {
                    if (itemstacks.getAmount() + amount <= 64) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
