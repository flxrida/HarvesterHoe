package me.paper.harvesterhoe.enchants.enchantments;

import me.paper.harvesterhoe.HarvesterHoe;
import me.paper.harvesterhoe.customevents.SugarcaneSellEvent;
import me.paper.harvesterhoe.enchants.EnchantManager;
import me.paper.harvesterhoe.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class AutoSellEnchantment extends Enchantment implements Listener, Color {

    private FileConfiguration config = HarvesterHoe.getInstance().getConfig();

    public AutoSellEnchantment(int id) {
        super(id);
    }

    @EventHandler
    public void onAutoSell(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() != null && event.getAction().toString().contains("RIGHT")) {
            if (player.getItemInHand() != null) {
                ItemStack itemStack = player.getItemInHand();
                if (itemStack.getType() != null && itemStack.getType().toString().contains("HOE")) {
                    EnchantManager enchantManager = HarvesterHoe.getInstance().getEnchantManager();
                    if (itemStack.containsEnchantment(enchantManager.autoSell)) {
                        if (itemStack.getEnchantmentLevel(enchantManager.autoSell) == 1) {
                            int sugarcane = calculateSugarcane(player);
                            int price = config.getInt("enchants.auto_sell.price");
                            int payment = sugarcane * price;
                            if (sugarcane != 0) {
                                SugarcaneSellEvent sugarcaneSellEvent = new SugarcaneSellEvent(player, payment, sugarcane);
                                Bukkit.getPluginManager().callEvent(sugarcaneSellEvent);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onSell(SugarcaneSellEvent event) {
        Player player = event.getPlayer();

        for (ItemStack itemstack : player.getInventory().getContents()) {
            if (itemstack != null && itemstack.getType().equals(Material.SUGAR_CANE)) {
                player.getInventory().remove(itemstack);
            }
        }

        HarvesterHoe.getInstance().getEconomy().depositPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), event.getSellPrice());
        player.sendMessage(format(config.getString("messages.sold_sugarcane").replaceAll("%amount%",
                event.getAmount() + "").replaceAll("%money%", event.getSellPrice() + "")));
        return;
    }

    private int calculateSugarcane(Player player) {
        int amount = 0;
        Inventory inventory = player.getInventory();
        for (ItemStack items : inventory.getContents()) {
            if (items != null) {
                if (items.getType().equals(Material.SUGAR_CANE)) {
                    amount += items.getAmount();
                }
            }
        }
        return amount;
    }

    @Override
    public int getId() {
        return 203;
    }

    @Override
    public String getName() {
        return "Auto Sell";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.TOOL;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return true;
    }

}
