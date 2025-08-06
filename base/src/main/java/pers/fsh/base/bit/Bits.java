package pers.fsh.base.bit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.BitSet;

/**
 * @author fanshuhua
 * @date 2025/6/28 15:37
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Bits extends BitSet {
    private ByteOrder byteOrder = ByteOrder.BIG_ENDIAN; // 默认字节序为大端
    @Setter
    private long length;

    public Bits() {
        this(0);
    }

    public Bits(int bitLength) {
        super(bitLength);
        this.length = bitLength;
    }

    /**
     * 合并多个Bits对象
     *
     * @param bitsToMerge 要合并的Bits对象
     * @return 合并后的新Bits对象
     */
    public static Bits merge(Bits... bitsToMerge) {
        Bits result = new Bits();
        if (bitsToMerge.length > 0) {
            result.setByteOrder(bitsToMerge[0].byteOrder);
        }

        int currentLength = 0;
        for (Bits bits : bitsToMerge) {
            for (int i = 0; i < bits.length(); i++) {
                if (bits.get(i)) {
                    result.set(currentLength + i);
                }
            }
            currentLength += (int) bits.getLength();
        }
        result.setLength(currentLength);
        return result;
    }

    public long getLength() {
        return length > 0 ? length : this.length();
    }

    /**
     * 设置数据
     *
     * @param value long类型的数据
     */
    public void setValue(long value) {
        this.clear();
        BitSet bs = BitSet.valueOf(new long[]{value});
        for (int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i + 1)) {
            this.set(i);
        }
    }

    /**
     * 设置数据
     *
     * @param bytes byte数组
     */
    public void setValue(byte[] bytes) {
        this.clear();
        byte[] newBytes = Arrays.copyOf(bytes, bytes.length);
        // BitSet.valueOf(byte[]) 期望小端字节序
        if (byteOrder.equals(ByteOrder.BIG_ENDIAN)) {
            // 如果是外部是大端，则反转以匹配内部的小端期望
            for (int i = 0; i < newBytes.length / 2; i++) {
                byte temp = newBytes[i];
                newBytes[i] = newBytes[newBytes.length - 1 - i];
                newBytes[newBytes.length - 1 - i] = temp;
            }
        }
        BitSet bs = BitSet.valueOf(newBytes);
        for (int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i + 1)) {
            this.set(i);
        }
    }

    public void setByteOrder(ByteOrder byteOrder) {
        if (byteOrder == null || this.byteOrder.equals(byteOrder)) {
            return;
        }

        byte[] bytes = this.toByteArray();
        // 反转字节数组
        for (int i = 0; i < bytes.length / 2; i++) {
            byte temp = bytes[i];
            bytes[i] = bytes[bytes.length - 1 - i];
            bytes[bytes.length - 1 - i] = temp;
        }

        // 从反转后的字节数组创建新的BitSet
        BitSet reversedBitSet = BitSet.valueOf(bytes);

        // 清除当前位并设置新位
        this.clear();
        for (int i = reversedBitSet.nextSetBit(0); i >= 0; i = reversedBitSet.nextSetBit(i + 1)) {
            this.set(i);
        }

        this.byteOrder = byteOrder;
    }

    /**
     * 将Bits转换为byte数组
     *
     * @return byte数组
     */
    public byte[] toHexBytes() {
        byte[] bytes = this.toByteArray();
        long expectedByteLength = (getLength() + 7) / 8;
        if (bytes.length < expectedByteLength) {
            byte[] paddedBytes = new byte[Math.toIntExact(expectedByteLength)];
            System.arraycopy(bytes, 0, paddedBytes, 0, bytes.length);
            bytes = paddedBytes;
        }

        // BitSet.toByteArray() 返回的是小端字节序
        if (byteOrder.equals(ByteOrder.BIG_ENDIAN)) {
            // 如果需要大端，则反转字节数组
            for (int i = 0; i < bytes.length / 2; i++) {
                byte temp = bytes[i];
                bytes[i] = bytes[bytes.length - 1 - i];
                bytes[bytes.length - 1 - i] = temp;
            }
        }
        return bytes;
    }

    /**
     * 将Bits转换为十六进制字符串数组
     *
     * @return 十六进制字符串数组
     */
    public String[] toHexArray() {
        byte[] bytes = toHexBytes();
        String[] hexArray = new String[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            hexArray[i] = String.format("0x%02X", bytes[i]);
        }
        return hexArray;
    }

    /**
     * 反转一个字节中的所有位
     *
     * @param b 要反转的字节
     * @return 位反转后的字节
     */
    private byte reverseBitsInByte(byte b) {
        int result = 0;
        for (int i = 0; i < 8; i++) {
            result <<= 1;
            result |= (b & 1);
            b >>= 1;
        }
        return (byte) result;
    }

    @Override
    public String toString() {
        return Arrays.toString(toHexArray());
    }
}
