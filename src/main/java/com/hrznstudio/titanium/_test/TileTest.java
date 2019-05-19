/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium._test;

import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.IItemStackQuery;
import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.block.tile.TilePowered;
import com.hrznstudio.titanium.block.tile.button.PosButton;
import com.hrznstudio.titanium.block.tile.fluid.PosFluidTank;
import com.hrznstudio.titanium.block.tile.inventory.PosInvHandler;
import com.hrznstudio.titanium.block.tile.progress.PosProgressBar;
import com.hrznstudio.titanium.client.gui.addon.EnergyBarGuiAddon;
import com.hrznstudio.titanium.client.gui.addon.StateButtonAddon;
import com.hrznstudio.titanium.client.gui.addon.StateButtonInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;

import java.util.Collections;
import java.util.List;

public class TileTest extends TilePowered implements ITickable {

    @Save
    private PosProgressBar bar;
    @Save
    private PosInvHandler first;
    @Save
    private PosInvHandler second;
    @Save
    private PosFluidTank third;

    private PosButton button;
    @Save
    private int state;

    public TileTest() {
        super(BlockTest.TEST);
        this.addInventory(first = new PosInvHandler("test", -120, 20, 1).setTile(this).setInputFilter((stack, integer) -> IItemStackQuery.ANYTHING.test(stack)));
        this.addInventory(second = new PosInvHandler("test2", 80, 30, 1).setTile(this).setInputFilter((stack, integer) -> IItemStackQuery.ANYTHING.test(stack)));
        this.addGuiAddonFactory(() -> new EnergyBarGuiAddon(4, 10, getEnergyStorage()));
        this.addProgressBar(bar = new PosProgressBar(40, 20, 500).setCanIncrease(tileEntity -> true).setOnFinishWork(() -> System.out.println("WOWOOW")).setBarDirection(PosProgressBar.BarDirection.VERTICAL_UP).setColor(EnumDyeColor.LIME));
        this.addTank(third = new PosFluidTank(8000, 130, 30, "testTank"));
        this.addButton(button = new PosButton(0, 0, 14, 14) {
            @Override
            public List<IFactory<? extends IGuiAddon>> getGuiAddons() {
                return Collections.singletonList(() -> new StateButtonAddon(button, new StateButtonInfo(0, AssetTypes.BUTTON_SIDENESS_DISABLED), new StateButtonInfo(1, AssetTypes.BUTTON_SIDENESS_ENABLED), new StateButtonInfo(2, AssetTypes.BUTTON_SIDENESS_PULL), new StateButtonInfo(3, AssetTypes.BUTTON_SIDENESS_PUSH)) {
                    @Override
                    public int getState() {
                        return state;
                    }
                });
            }
        }.setId(0).setPredicate(nbtTagCompound -> {
            System.out.println(":pepeD:");
            ++state;
            if (state >= 4) state = 0;
            markForUpdate();
        }));
    }

    @Override
    public boolean onActivated(EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!super.onActivated(playerIn, hand, facing, hitX, hitY, hitZ)) {
            openGui(playerIn);
            return true;
        }
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (!world.isRemote) {
            this.getEnergyStorage().receiveEnergy(10, false);
            markForUpdate();
        } else {

        }
    }
}
