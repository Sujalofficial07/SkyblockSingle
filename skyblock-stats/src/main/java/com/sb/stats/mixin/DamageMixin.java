package com.sb.stats.mixin;

import com.sb.api.stats.IPlayerStats;
import com.sb.api.stats.SBStat;
import com.sb.api.utils.SBConstants; // Ensure you have this class in API mod
import com.sb.stats.SkyBlockStatsMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class DamageMixin {

    /**
     * Hypixel SkyBlock Damage Formula Implementation
     * Intercepts the vanilla damage calculation.
     */
    @Inject(method = "getAttackDamage", at = @At("HEAD"), cancellable = true)
    private void calculateHypixelDamage(Entity target, float cooldown, CallbackInfoReturnable<Float> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        // --- STEP 1: Fetch Base Player Stats (From Profile/Armor) ---
        // Hum Cardinal Components API use kar rahe hain data lene ke liye
        IPlayerStats stats = SkyBlockStatsMod.PLAYER_STATS.get(player);
        
        double baseStrength = stats.getBaseStat(SBStat.STRENGTH);
        double critChance = stats.getBaseStat(SBStat.CRIT_CHANCE);
        double critDamagePct = stats.getBaseStat(SBStat.CRIT_DAMAGE);
        
        // --- STEP 2: Fetch Weapon Stats (From Item NBT) ---
        double weaponDamage = 0;
        double weaponStrength = 0;

        ItemStack heldItem = player.getMainHandStack();
        
        // Check agar item ke paas SkyBlock Data hai
        if (heldItem.hasNbt() && heldItem.getNbt().contains(SBConstants.NBT_ROOT)) {
            NbtCompound sbData = heldItem.getNbt().getCompound(SBConstants.NBT_ROOT);
            
            weaponDamage = sbData.getInt("DAMAGE");
            weaponStrength = sbData.getInt("STRENGTH");
            
            // Future: Add "Bonus Attack Speed" or "Crit Damage" from weapon here
        }

        // --- STEP 3: Combine Stats ---
        double totalStrength = baseStrength + weaponStrength;

        // --- STEP 4: The Hypixel Formula ---
        // Damage = (5 + WeaponDamage) * (1 + Strength/100)
        double finalDamage = (5 + weaponDamage) * (1 + (totalStrength / 100.0));

        // --- STEP 5: Crit Calculation ---
        // Crit Chance is percentage (e.g., 30 means 30%)
        boolean isCrit = Math.random() * 100 < critChance;
        
        if (isCrit) {
            // Apply Crit Multiplier: Damage * (1 + CritDamage/100)
            finalDamage *= (1 + (critDamagePct / 100.0));
            
            // Visual Effect for Crit (Optional - Vanilla particles)
            // Player will see the 'Crit' particles automatically if damage > vanilla, 
            // but we can force magic crit particles here if we want.
            player.onCrit(target); 
        }

        // --- STEP 6: Apply Vanilla Cooldown (Optional) ---
        // Agar tum 1.9+ PvP chahte ho toh cooldown multiply karo.
        // Agar 1.8 style spam clicking chahte ho, toh niche wali line hata do.
        // finalDamage *= cooldown; 

        // Return result as Float (Vanilla expects float)
        cir.setReturnValue((float) finalDamage);
    }
}
