package pers.fsh.cddutils.model.vo.parameter;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import pers.fsh.base.utils.Bits;
import pers.fsh.cddutils.model.po.ecudoc.datatypes.ValueType;
import pers.fsh.cddutils.model.po.ecudoc.datatypes.texttbl.Excl;

import java.util.List;

/**
 * @author fanshuhua
 * @date 2025/7/31 15:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public non-sealed class ParameterIdent extends ParameterProxy implements ValueInterface<ParameterIdent> {
    private long bm;
    @JSONField(serialize = false)
    private ValueType cValueType;
    @JSONField(serialize = false)
    private ValueType pValueType;
    private List<Excl> exclList;  // Exclusion list, optional
    @JSONField(serializeFeatures = JSONWriter.Feature.WriteNonStringValueAsString)
    private Bits bits = new Bits();

    public ParameterIdent(ValueType cValueType,
                          ValueType pValueType) {
        this.cValueType = cValueType;
        this.pValueType = pValueType;
        this.setSerial(false); // TextTable is always parallel
        bits.setLength(getBitLength());
    }

    @Override
    public byte[] getValue() {
        return bits.toHexBytes();
    }

    public ParameterIdent setValue(long value) {
        this.bits.setValue(value);
        return this;
    }

    @Override
    public long getMinSize() {
        return cValueType.getMinsz();
    }

    @Override
    public ParameterProxy setMaxSize(long maxSize) {
        cValueType.setMaxsz(maxSize);
        return this;
    }

    @Override
    public ParameterProxy setMinSize(long minSize) {
        cValueType.setMinsz(minSize);
        return this;
    }

    @Override
    public long getMaxSize() {
        return cValueType.getMaxsz();
    }

    public long getBitLength() {
        return cValueType.getBl();
    }

    @Override
    public long getMinBitLength() {
        return getBitLength() * getMinSize();
    }

    @Override
    public long getMaxBitLength() {
        return getBitLength() * cValueType.getMaxsz();
    }
}
