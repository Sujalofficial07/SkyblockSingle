package com.sb.stats.mixin;

import com.sb.api.stats.IPlayerStats;
import com.sb.api.stats.SBStat;
import com.sb.api.utils.SBConstants;
import com.sb.stats.SkyBlockStatsMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public class DamageMixin {

    // ✅ FIX: We redirect the "getAttributeValue" call inside the "attack" method.
    // Jab game puchega "Player ka damage kitna hai?", hum apna Hypixel damage return karenge.
    @Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/entity/attribute/EntityAttribute;)D"))
    private double overrideAttackDamage(PlayerEntity player, EntityAttribute attribute) {
        
        // Agar game Damage maang raha hai, toh hum apna logic lagayenge
        if (attribute == EntityAttributes.GENERIC_ATTACK_DAMAGE) {
            return calculateHypixelDamage(player);
        }

        // Kisi aur attribute ke liye (Speed, Health), vanilla logic chalne do
        return player.getAttributeValue(attribute);
    }

    // Custom Helper Method to keep code clean
    private double calculateHypixelDamage(PlayerEntity player) {
        // 1. Get Stats
        IPlayerStats stats = SkyBlockStatsMod.PLAYER_STATS.get(player);
        double baseStrength = stats.getBaseStat(SBStat.STRENGTH);
        double critChance = stats.getBaseStat(SBStat.CRIT_CHANCE);
        double critDamagePct = stats.getBaseStat(SBStat.CRIT_DAMAGE);
        
        // 2. Weapon Stats
        double weaponDamage = 0;
        double weaponStrength = 0;

        ItemStack heldItem = player.getMainHandStack();
        if (heldItem.hasNbt() && heldItem.getNbt().contains(SBConstants.NBT_ROOT)) {
            NbtCompound sbData = heldItem.getNbt().getCompound(SBConstants.NBT_ROOT);
            weaponDamage = sbData.getInt("DAMAGE");
            weaponStrength = sbData.getInt("STRENGTH");
        }

        // 3. Hypixel Formula
        double totalStrength = baseStrength + weaponStrength;
        double finalDamage = (5 + weaponDamage) * (1 + (totalStrength / 100.0));

        // 4. Crit Logic
        boolean isCrit = Math.random() * 100 < critChance;
        if (isCrit) {
            finalDamage *= (1 + (critDamagePct / 100.0));
            
            // ✅ FIX: Correct method name for 1.20.1
            player.addCritParticles(player.getAttacking()); 
        }

        return finalDamage;
    }
}
