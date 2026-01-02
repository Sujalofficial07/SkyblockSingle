package com.sb.stats.mixin;

import com.sb.api.stats.IPlayerStats;
import com.sb.api.stats.SBStat;
import com.sb.api.utils.SBConstants;
import com.sb.stats.SkyBlockStatsMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class DamageMixin {

    // ✅ FIX: Correct Signature for 1.20.1
    // Method: public float getDamageAgainst(Entity target)
    @Inject(method = "getDamageAgainst", at = @At("HEAD"), cancellable = true)
    private void calculateHypixelDamage(Entity target, CallbackInfoReturnable<Float> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        // 1. Get Stats (API Check)
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
            // Crit Particles
            player.onCrit(target);
        }

        // 5. Return Damage
        cir.setReturnValue((float) finalDamage);
    }
}
