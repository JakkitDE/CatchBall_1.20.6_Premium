package de.tomstahlberg.fangball.utils.nbt;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class NBTSerialization {
    public static byte[] serializeNBT(CompoundTag compoundTag) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        NbtIo.write(compoundTag, dataOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}