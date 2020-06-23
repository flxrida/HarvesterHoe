package me.paper.harvesterhoe.harvester;

import me.paper.harvesterhoe.HarvesterHoe;
import me.paper.harvesterhoe.utils.Color;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Harvester implements Color {

    private Player player;

    private FileConfiguration config = HarvesterHoe.getInstance().getConfig();

    public Harvester(Player player) {
        this.player = player;
    }

    public ItemStack getHarvester(int gemHunter, int fortune, int spawnerHunter, int autoSell) {
        ItemStack harvester = new ItemStack(Material.getMaterial(config.getString("harvester_item.material")));
        ItemMeta harvesterMeta = harvester.getItemMeta();
        harvesterMeta.setDisplayName(format(config.getString("harvester_item.name")));
        ArrayList<String> lore = new ArrayList<>();
        for (String harvesterLore : config.getStringList("harvester_item.lore")) {
            lore.add(format(harvesterLore).replaceAll("%gem_hunter%", gemHunter+"")
            .replaceAll("%fortune%", fortune+"").replaceAll("%spawner_hunter%", spawnerHunter+"")
            .replaceAll("%auto_sell%", autoSell+""));
        }
        harvesterMeta.setLore(lore);
        harvesterMeta.spigot().setUnbreakable(true);
        harvester.setItemMeta(harvesterMeta);
        return harvester;
    }

}
