/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.nbthandler.data;


import com.hrznstudio.titanium.nbthandler.INBTHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class ItemStackHandlerNBTHandler implements INBTHandler<ItemStackHandler> {
    @Override
    public boolean isClassValid(Class<?> aClass) {
        return ItemStackHandler.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(@Nonnull NBTTagCompound compound, @Nonnull String name, @Nonnull ItemStackHandler object) {
        compound.put(name, object.serializeNBT());
        return true;
    }

    @Override
    public ItemStackHandler readFromNBT(@Nonnull NBTTagCompound compound, @Nonnull String name, ItemStackHandler current) {
        if (compound.contains(name)) {
            if (current == null) current = new ItemStackHandler();
            current.deserializeNBT(compound.getCompound(name));
            return current;
        }
        return null;
    }
}
