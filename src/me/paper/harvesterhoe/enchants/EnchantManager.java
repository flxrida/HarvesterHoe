package me.paper.harvesterhoe.enchants;

import me.paper.harvesterhoe.HarvesterHoe;
import me.paper.harvesterhoe.enchants.enchantments.AutoSellEnchantment;
import me.paper.harvesterhoe.enchants.enchantments.FortuneEnchantment;
import me.paper.harvesterhoe.enchants.enchantments.GemHunterEnchantment;
import me.paper.harvesterhoe.enchants.enchantments.SpawnerHunterEnchantment;
import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Field;
import java.util.HashMap;

public class EnchantManager {

    private HarvesterHoe harvesterHoe;

    public GemHunterEnchantment gemHunter = new GemHunterEnchantment(200);
    public FortuneEnchantment fortune = new FortuneEnchantment(201);
    public SpawnerHunterEnchantment spawnerHunter = new SpawnerHunterEnchantment(202);
    public AutoSellEnchantment autoSell = new AutoSellEnchantment(203);

    public EnchantManager(HarvesterHoe harvesterHoe) {
        this.harvesterHoe = harvesterHoe;
    }

    public void load() {
        this.loadEnchantment(gemHunter);
        this.loadEnchantment(fortune);
        this.loadEnchantment(spawnerHunter);
        this.loadEnchantment(autoSell);
    }

    public void unload() {
        this.unloadEnchantment(gemHunter);
        this.unloadEnchantment(fortune);
        this.unloadEnchantment(spawnerHunter);
        this.unloadEnchantment(autoSell);
    }

    private void loadEnchantment(Enchantment enchantment) {
        try {
            try {
                Field f = Enchantment.class.getDeclaredField("acceptingNew");
                f.setAccessible(true);
                f.set(null, true);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Enchantment.registerEnchantment(enchantment);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unloadEnchantment(Enchantment enchantment) {
        try {
            Field byIdField = Enchantment.class.getDeclaredField("byId");
            Field byNameField = Enchantment.class.getDeclaredField("byName");

            byIdField.setAccessible(true);
            byNameField.setAccessible(true);

            HashMap<Integer, Enchantment> byId = (HashMap<Integer, Enchantment>) byIdField.get(null);
            HashMap<Integer, Enchantment> byName = (HashMap<Integer, Enchantment>) byNameField.get(null);

            if (byId.containsKey(enchantment.getId())) {
                byId.remove(enchantment.getId());
            }

            if (byName.containsKey(enchantment.getName())) {
                byName.remove(enchantment.getName());
            }
        } catch (Exception ignored) {
        }
    }

}
