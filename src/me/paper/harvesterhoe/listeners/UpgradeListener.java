package me.paper.harvesterhoe.listeners;

import me.paper.harvesterhoe.HarvesterHoe;
import me.paper.harvesterhoe.enchants.EnchantManager;
import me.paper.harvesterhoe.harvester.Harvester;
import me.paper.harvesterhoe.inventory.UpgradeInventory;
import me.paper.harvesterhoe.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class UpgradeListener implements Listener, Color {

    private FileConfiguration config = HarvesterHoe.getInstance().getConfig();

    @EventHandler
    public void onUpgrade(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() != null && event.getAction().toString().contains("RIGHT")) {
            if (player.isSneaking()) {
                if (player.getItemInHand() != null) {
                    ItemStack itemStack = player.getItemInHand();
                    if (itemStack.getType().toString().contains("HOE")) {
                        if (format(itemStack.getItemMeta().getDisplayName()).equalsIgnoreCase(format(config.getString("harvester_item.name")))) {
                            UpgradeInventory upgradeInventory = new UpgradeInventory(player);
                            upgradeInventory.exec();
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onUpgradeClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if (event.getInventory() != null && event.getInventory().getName() != null) {
                Inventory inventory = event.getInventory();
                String name = format(inventory.getName());
                if (name.equalsIgnoreCase(format(config.getString("inventory.upgrade_inventory.name")))) {
                    event.setCancelled(true);
                    if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName()) {
                        EnchantManager enchantManager = HarvesterHoe.getInstance().getEnchantManager();
                        ItemStack itemStack = event.getCurrentItem();
                        String itemName = format(itemStack.getItemMeta().getDisplayName());

                        double balance = HarvesterHoe.getInstance().getEconomy().getBalance(Bukkit.getOfflinePlayer(player.getUniqueId()));
                        ItemStack harvester = player.getItemInHand();
                        int gemHunterL = harvester.getEnchantmentLevel(enchantManager.gemHunter);
                        int fortuneL = harvester.getEnchantmentLevel(enchantManager.fortune);
                        int spawnerHunterL = harvester.getEnchantmentLevel(enchantManager.spawnerHunter);
                        int autoSellL = harvester.getEnchantmentLevel(enchantManager.autoSell);

                        if (itemName.equalsIgnoreCase(format(config.getString("inventory.upgrade_inventory.items.gem_hunter.name")))) {
                            double cost = config.getDouble("enchants.gem_hunter.price_multiplier") * config.getInt("enchants.gem_hunter.base_price")*(gemHunterL+1);
                            if (gemHunterL < 100) {
                                if (balance >= cost) {
                                    Harvester harvesterItem = new Harvester(player);
                                    ItemStack newHarvester = harvesterItem.getHarvester(gemHunterL+1,fortuneL,spawnerHunterL,autoSellL);
                                    newHarvester.addUnsafeEnchantment(enchantManager.gemHunter,gemHunterL+1);
                                    newHarvester.addUnsafeEnchantment(enchantManager.fortune,fortuneL);
                                    newHarvester.addUnsafeEnchantment(enchantManager.spawnerHunter,spawnerHunterL);
                                    newHarvester.addUnsafeEnchantment(enchantManager.autoSell,autoSellL);
                                    player.setItemInHand(newHarvester);
                                    HarvesterHoe.getInstance().getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), cost);
                                    player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1,1);
                                    UpgradeInventory upgradeInventory = new UpgradeInventory(player);
                                    upgradeInventory.exec();
                                }else {
                                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1,1);
                                    player.sendMessage(format(config.getString("messages.insufficient_funds")));
                                    return;
                                }
                            }
                        }else if (itemName.equalsIgnoreCase(format(config.getString("inventory.upgrade_inventory.items.fortune.name")))) {
                            double cost = config.getDouble("enchants.fortune.price_multiplier") * config.getInt("enchants.fortune.base_price")*(fortuneL+1);
                            if (fortuneL < 100) {
                                if (balance >= cost) {
                                    Harvester harvesterItem = new Harvester(player);
                                    ItemStack newHarvester = harvesterItem.getHarvester(gemHunterL,fortuneL+1,spawnerHunterL,autoSellL);
                                    newHarvester.addUnsafeEnchantment(enchantManager.fortune,fortuneL+1);
                                    newHarvester.addUnsafeEnchantment(enchantManager.spawnerHunter,spawnerHunterL);
                                    newHarvester.addUnsafeEnchantment(enchantManager.autoSell,autoSellL);
                                    newHarvester.addUnsafeEnchantment(enchantManager.gemHunter,gemHunterL);
                                    player.setItemInHand(newHarvester);
                                    HarvesterHoe.getInstance().getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), cost);
                                    player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1,1);
                                    UpgradeInventory upgradeInventory = new UpgradeInventory(player);
                                    upgradeInventory.exec();
                                }else {
                                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1,1);
                                    player.sendMessage(format(config.getString("messages.insufficient_funds")));
                                    return;
                                }
                            }
                        }else if (itemName.equalsIgnoreCase(format(config.getString("inventory.upgrade_inventory.items.spawner_hunter.name")))) {
                            double cost = config.getDouble("enchants.spawner_hunter.price_multiplier") * config.getInt("enchants.spawner_hunter.base_price")*(spawnerHunterL+1);
                            if (spawnerHunterL < 100) {
                                if (balance >= cost) {
                                    Harvester harvesterItem = new Harvester(player);
                                    ItemStack newHarvester = harvesterItem.getHarvester(gemHunterL,fortuneL,spawnerHunterL+1,autoSellL);
                                    newHarvester.addUnsafeEnchantment(enchantManager.spawnerHunter,spawnerHunterL+1);
                                    newHarvester.addUnsafeEnchantment(enchantManager.autoSell,autoSellL);
                                    newHarvester.addUnsafeEnchantment(enchantManager.gemHunter,gemHunterL);
                                    newHarvester.addUnsafeEnchantment(enchantManager.fortune,fortuneL);
                                    player.setItemInHand(newHarvester);
                                    HarvesterHoe.getInstance().getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), cost);
                                    player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1,1);
                                    UpgradeInventory upgradeInventory = new UpgradeInventory(player);
                                    upgradeInventory.exec();
                                }else {
                                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1,1);
                                    player.sendMessage(format(config.getString("messages.insufficient_funds")));
                                    return;
                                }
                            }
                        }else if (itemName.equalsIgnoreCase(format(config.getString("inventory.upgrade_inventory.items.auto_sell.name")))) {
                            double cost = config.getInt("enchants.auto_sell.base_price");
                            if (autoSellL < 1) {
                                if (balance >= cost) {
                                    Harvester harvesterItem = new Harvester(player);
                                    ItemStack newHarvester = harvesterItem.getHarvester(gemHunterL,fortuneL,spawnerHunterL,autoSellL+1);
                                    newHarvester.addUnsafeEnchantment(enchantManager.autoSell,autoSellL+1);
                                    newHarvester.addUnsafeEnchantment(enchantManager.spawnerHunter,spawnerHunterL);
                                    newHarvester.addUnsafeEnchantment(enchantManager.gemHunter,gemHunterL);
                                    newHarvester.addUnsafeEnchantment(enchantManager.fortune,fortuneL);
                                    player.setItemInHand(newHarvester);
                                    HarvesterHoe.getInstance().getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), cost);
                                    player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1,1);
                                    UpgradeInventory upgradeInventory = new UpgradeInventory(player);
                                    upgradeInventory.exec();
                                }else {
                                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1,1);
                                    player.sendMessage(format(config.getString("messages.insufficient_funds")));
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
