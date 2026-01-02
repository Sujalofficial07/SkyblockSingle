package com.sb.stats.mixin;

import com.sb.api.stats.SBStat;
import com.sb.stats.SkyBlockStatsMod;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class DamageMixin {

    @Inject(method = "getAttackDamage", at = @At("HEAD"), cancellable = true)
    private void calculateHypixelDamage(float target, float cooldown, CallbackInfoReturnable<Float> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        // 1. Get Stats
        double strength = SkyBlockStatsMod.PLAYER_STATS.get(player).getBaseStat(SBStat.STRENGTH);
        double critChance = SkyBlockStatsMod.PLAYER_STATS.get(player).getBaseStat(SBStat.CRIT_CHANCE);
        double critDmg = SkyBlockStatsMod.PLAYER_STATS.get(player).getBaseStat(SBStat.CRIT_DAMAGE);
        
        // 2. Weapon Damage (Abhi ke liye 0, baad mein Item Mod se layenge)
        double weaponDamage = 0; // TODO: Get from Held Item NBT
        
        // 3. Base Calculation
        double baseDamage = (5 + weaponDamage) * (1 + (strength / 100.0));
        
        // 4. Crit Logic
        boolean isCrit = Math.random() * 100 < critChance;
        if (isCrit) {
            baseDamage *= (1 + (critDmg / 100.0));
            // Optional: Add particle effect or sound here
        }

        // Vanilla logic bypass karke apna damage return karo
        cir.setReturnValue((float) baseDamage);
    }
}
