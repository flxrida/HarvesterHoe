package me.paper.harvesterhoe;

import me.paper.harvesterhoe.commands.HarvesterHoeCommand;
import me.paper.harvesterhoe.enchants.EnchantManager;
import me.paper.harvesterhoe.enchants.enchantments.AutoSellEnchantment;
import me.paper.harvesterhoe.listeners.HarvestListener;
import me.paper.harvesterhoe.listeners.UpgradeListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class HarvesterHoe extends JavaPlugin {

    private static HarvesterHoe instance;

    private Economy economy;
    private EnchantManager enchantManager;

    public void onEnable() {
        instance = this;
        if (!setupEconomy()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "PickaxeUpgrades requires Vault in order to work properly!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }else {
            enchantManager = new EnchantManager(this);
            enchantManager.load();
            loadConfig();
            loadCommands();
            loadListeners();
            Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "PickaxeUpgrades has been successfully enabled.");
        }
    }

    public void onDisable() {
        enchantManager.unload();
        instance = null;
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "PickaxeUpgrades is shutting down...");
    }

    private void loadConfig() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }

    private void loadCommands() {
        getCommand("HH").setExecutor(new HarvesterHoeCommand());
    }

    private void loadListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new UpgradeListener(), this);
        pluginManager.registerEvents(new HarvestListener(), this);
        pluginManager.registerEvents(enchantManager.autoSell, this);
    }

    private boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    public static HarvesterHoe getInstance() {
        return instance;
    }

    public EnchantManager getEnchantManager() {
        return this.enchantManager;
    }

    public Economy getEconomy() {
        return economy;
    }
}
