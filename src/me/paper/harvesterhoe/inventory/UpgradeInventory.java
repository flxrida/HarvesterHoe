package me.paper.harvesterhoe.inventory;

import me.paper.harvesterhoe.HarvesterHoe;
import me.paper.harvesterhoe.enchants.EnchantManager;
import me.paper.harvesterhoe.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.NumberFormat;
import java.util.ArrayList;

public class UpgradeInventory implements Color {

    private Player player;

    private FileConfiguration config = HarvesterHoe.getInstance().getConfig();

    public UpgradeInventory(Player player) {
        this.player = player;
    }

    public void exec() {
        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        EnchantManager enchantManager = HarvesterHoe.getInstance().getEnchantManager();
        Inventory inventory = Bukkit.createInventory(null, 27, format(config.getString("inventory.upgrade_inventory.name")));

        ItemStack harvester = player.getItemInHand();
        int gemHunterL = harvester.getEnchantmentLevel(enchantManager.gemHunter);
        int fortuneL = harvester.getEnchantmentLevel(enchantManager.fortune);
        int spawnerHunterL = harvester.getEnchantmentLevel(enchantManager.spawnerHunter);
        int autoSellL = harvester.getEnchantmentLevel(enchantManager.autoSell);

        ItemStack gemHunter = new ItemStack(Material.getMaterial(config.getString("inventory.upgrade_inventory.items.gem_hunter.material")));
        ItemMeta gemHunterM = gemHunter.getItemMeta();
        gemHunterM.setDisplayName(format(config.getString("inventory.upgrade_inventory.items.gem_hunter.name")));
        ArrayList<String> gLore = new ArrayList<>();
        for (String gLore2 : config.getStringList("inventory.upgrade_inventory.items.gem_hunter.lore")) {
            if (gemHunterL < 100) {
                gLore.add(format(gLore2).replaceAll("%current_tier%", gemHunterL + "").replaceAll("%current_bonus%",
                        gemHunterL * config.getDouble("enchants.gem_hunter.multiplier") + "")
                        .replaceAll("%upgrade_fee%", numberFormat.format(config.getDouble("enchants.gem_hunter.price_multiplier") *
                                config.getInt("enchants.gem_hunter.base_price") * (gemHunterL + 1))).replaceAll("%next_bonus%", (gemHunterL + 1) * config.getDouble("enchants.gem_hunter.multiplier") + ""));
            }else {
                gLore.add(format(gLore2).replaceAll("%current_tier%", "MAX").replaceAll("%current_bonus%",
                        gemHunterL * config.getDouble("enchants.gem_hunter.multiplier") + "")
                        .replaceAll("%upgrade_fee%", 0+"").replaceAll("%next_bonus%", 0+""));
            }
        }
        gemHunterM.setLore(gLore);
        gemHunter.setItemMeta(gemHunterM);

        ItemStack fortune = new ItemStack(Material.getMaterial(config.getString("inventory.upgrade_inventory.items.fortune.material")));
        ItemMeta fortuneM = fortune.getItemMeta();
        fortuneM.setDisplayName(format(config.getString("inventory.upgrade_inventory.items.fortune.name")));
        ArrayList<String> fLore = new ArrayList<>();
        for (String fLore2 : config.getStringList("inventory.upgrade_inventory.items.fortune.lore")) {
            if (fortuneL < 100) {
                fLore.add(format(fLore2).replaceAll("%current_tier%", fortuneL + "").replaceAll("%current_bonus%",
                        fortuneL * config.getDouble("enchants.fortune.multiplier") + "")
                        .replaceAll("%upgrade_fee%", numberFormat.format(config.getDouble("enchants.fortune.price_multiplier") *
                                config.getInt("enchants.fortune.base_price") * (fortuneL + 1))).replaceAll("%next_bonus%", (fortuneL + 1) * config.getDouble("enchants.fortune.multiplier") + ""));
            }else {
                fLore.add(format(fLore2).replaceAll("%current_tier%", "MAX").replaceAll("%current_bonus%",
                        fortuneL * config.getDouble("enchants.fortune.multiplier") + "")
                        .replaceAll("%upgrade_fee%", 0+"").replaceAll("%next_bonus%", 0+""));
            }
        }
        fortuneM.setLore(fLore);
        fortune.setItemMeta(fortuneM);

        ItemStack spawnerHunter = new ItemStack(Material.getMaterial(config.getString("inventory.upgrade_inventory.items.spawner_hunter.material")));
        ItemMeta spawnerHunterM = spawnerHunter.getItemMeta();
        spawnerHunterM.setDisplayName(format(config.getString("inventory.upgrade_inventory.items.spawner_hunter.name")));
        ArrayList<String> sLore = new ArrayList<>();
        for (String sLore2 : config.getStringList("inventory.upgrade_inventory.items.spawner_hunter.lore")) {
            if (spawnerHunterL < 100) {
                sLore.add(format(sLore2).replaceAll("%current_tier%", spawnerHunterL + "").replaceAll("%current_bonus%",
                        spawnerHunterL * config.getDouble("enchants.spawner_hunter.multiplier") + "")
                        .replaceAll("%upgrade_fee%", numberFormat.format(config.getDouble("enchants.spawner_hunter.price_multiplier") *
                                config.getInt("enchants.spawner_hunter.base_price") * (spawnerHunterL + 1))).replaceAll("%next_bonus%", (spawnerHunterL + 1) * config.getDouble("enchants.spawner_hunter.multiplier") + ""));
            } else {
                sLore.add(format(sLore2).replaceAll("%current_tier%", "MAX").replaceAll("%current_bonus%",
                        spawnerHunterL * config.getDouble("enchants.spawner_hunter.multiplier") + "")
                        .replaceAll("%upgrade_fee%", 0+"").replaceAll("%next_bonus%", 0+""));
            }
        }
        spawnerHunterM.setLore(sLore);
        spawnerHunter.setItemMeta(spawnerHunterM);

        ItemStack autoSell = new ItemStack(Material.getMaterial(config.getString("inventory.upgrade_inventory.items.auto_sell.material")));
        ItemMeta autoSellM = autoSell.getItemMeta();
        autoSellM.setDisplayName(format(config.getString("inventory.upgrade_inventory.items.auto_sell.name")));
        ArrayList<String> aLore = new ArrayList<>();
        for (String aLore2 : config.getStringList("inventory.upgrade_inventory.items.auto_sell.lore")) {
            if (autoSellL != 1) {
                aLore.add(format(aLore2).replaceAll("%current_tier%", autoSellL + "").replaceAll("%current_bonus%",
                        0+"")
                        .replaceAll("%upgrade_fee%", numberFormat.format(config.getDouble("enchants.auto_sell.base_price"))).replaceAll("%next_bonus%", "MAX"));
            }else {
                aLore.add(format(aLore2).replaceAll("%current_tier%", 1+"").replaceAll("%current_bonus%",
                        "MAX")
                        .replaceAll("%upgrade_fee%", numberFormat.format(0)).replaceAll("%next_bonus%", 0+""));
            }
        }
        autoSellM.setLore(aLore);
        autoSell.setItemMeta(autoSellM);

        ItemStack filler = new ItemStack(Material.getMaterial(config.getString("inventory.upgrade_inventory.items.filler.material")), 1, (byte) config.getInt("inventory.upgrade_inventory.items.filler.data"));
        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.setDisplayName(" ");
        filler.setItemMeta(fillerMeta);

        inventory.setItem(1, gemHunter);
        inventory.setItem(3, fortune);
        inventory.setItem(5, autoSell);
        inventory.setItem(7, spawnerHunter);

        for (int x = 0; x <= 26; x++) {
            if (inventory.getItem(x) == null) {
                inventory.setItem(x, filler);
            }
        }
        this.player.openInventory(inventory);
    }
}
