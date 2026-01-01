// Path: skyblock-stats/src/main/java/com/sb/stats/mixin/DamageMixin.java
package com.sb.stats.mixin;

import com.sb.api.stats.IPlayerStats; // From API mod
import com.sb.stats.registry.StatsComponents; // Registry
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class DamageMixin {

    @Inject(method = "getAttackDamage", at = @At("RETURN"), cancellable = true)
    private void calculateHypixelDamage(float target, float cooldown, CallbackInfoReturnable<Float> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        
        // 1. Get Player Base Stats (Profile)
        IPlayerStats stats = player.getComponent(StatsComponents.PLAYER_STATS);
        double strength = stats.getStrength();
        double critDmg = stats.getCritDamage();
        
        // 2. Get Held Item Stats
        ItemStack heldItem = player.getMainHandStack();
        double weaponDamage = 0;
        
        if (heldItem.hasNbt() && heldItem.getNbt().contains("SkyBlockData")) {
            NbtCompound sbData = heldItem.getNbt().getCompound("SkyBlockData");
            weaponDamage = sbData.getInt("DAMAGE");
            strength += sbData.getInt("STRENGTH"); // Add weapon strength
        }

        // 3. Hypixel Formula
        // Damage = (5 + WeaponDamage) * (1 + Strength/100) * (1 + CritDamage/100 [if crit])
        
        double finalDamage = (5 + weaponDamage) * (1 + (strength / 100.0));
        
        // (Crit logic yahan add hoga)
        
        cir.setReturnValue((float) finalDamage);
    }
}
