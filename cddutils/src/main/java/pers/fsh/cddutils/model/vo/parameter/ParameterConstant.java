package pers.fsh.cddutils.model.vo.parameter;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import pers.fsh.base.utils.Bits;
import pers.fsh.cddutils.model.po.base.Name;
import pers.fsh.cddutils.model.po.ecudoc.datatypes.ValueType;

/**
 * @author fanshuhua
 * @date 2025/7/28 17:10
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public final class ParameterConstant implements ParameterInterface, ValueInterface<ParameterConstant> {
    private Name name;
    private String qualifiers;
    private String id;
    @JSONField(serializeFeatures = JSONWriter.Feature.WriteNonStringValueAsString)
    private Bits bits = new Bits();
    @JSONField(serialize = false)
    private ValueType cValueType = new ValueType().setBl(8).setBo(21).setEnc("uns").setSig("0").setDf("hex")
            .setQty("atom").setSz("no").setMinsz(1).setMaxsz(1); // 初始化一个默认的ValueType对象
    @JSONField(serialize = false)
    private ValueType pValueType = new ValueType().setBl(8).setBo(21).setEnc("uns").setSig("0").setDf("hex")
            .setQty("atom").setSz("no").setMinsz(1).setMaxsz(1); // 初始化一个默认的ValueType对象

    public ParameterConstant(Name name, String qualifiers) {
        this.name = name;
        this.qualifiers = qualifiers;
    }

    public ParameterConstant(Name name, String qualifiers, ValueType cValueType) {
        this.name = name;
        this.qualifiers = qualifiers;
        this.cValueType = cValueType;
    }

    public ParameterConstant setMinsz(long minsz) {
        this.cValueType.setMinsz(minsz);
        return this;
    }

    public ParameterConstant setMaxsz(long maxsz) {
        this.cValueType.setMaxsz(maxsz);
        return this;
    }

    @Override
    public byte[] getValue() {
        return bits.toHexBytes();
    }

    public ParameterConstant setValue(long value) {
        this.bits.setValue(value);
        return this;
    }

    @Override
    public long getMinSize() {
        return cValueType.getMinsz();
    }

    @Override
    public long getMaxSize() {
        return cValueType.getMaxsz();
    }

    public long getBitLength() {
        return cValueType.getBl();
    }

    public ParameterConstant setBitLength(long bitLength) {
        this.cValueType.setBl(bitLength);
        bits.setLength(bitLength);
        return this;
    }

    @Override
    public long getMinBitLength() {
        return getBitLength() * cValueType.getMinsz();
    }

    @Override
    public long getMaxBitLength() {
        return getBitLength() * cValueType.getMaxsz();
    }
}
