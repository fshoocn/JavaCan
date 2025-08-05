package pers.fsh.cddutils.model.vo.parameter;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import pers.fsh.cddutils.model.po.base.Name;
import pers.fsh.cddutils.model.po.ecudoc.datatypes.ValueType;

/**
 * @author fanshuhua
 * @date 2025/7/29 15:59
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public final class ParameterDtc extends ParameterProxy {
    @JSONField(serialize = false)
    private ValueType cValueType = new ValueType().setBl(8).setBo(21).setEnc("uns").setSig("0").setDf("hex")
            .setQty("atom").setSz("no").setMinsz(1).setMaxsz(1); // 初始化一个默认的ValueType对象
    @JSONField(serialize = false)
    private ValueType pValueType = new ValueType().setBl(8).setBo(21).setEnc("uns").setSig("0").setDf("hex")
            .setQty("atom").setSz("no").setMinsz(1).setMaxsz(1); // 初始化一个默认的ValueType对象
    private long bm; // 位掩码

    public ParameterDtc(ValueType cValueType, ValueType pValueType, Name name, String qualifiers) {
        super(name, qualifiers);
    }

    @Override
    public long getMaxSize() {
        return cValueType.getMaxsz();
    }

    @Override
    public long getMinSize() {
        return cValueType.getMinsz();
    }

    @Override
    public long getMinBitLength() {
        return cValueType.getBl() * cValueType.getMaxsz();
    }

    @Override
    public long getMaxBitLength() {
        return cValueType.getBl() * cValueType.getMaxsz();
    }

    @Override
    public ParameterProxy setMaxBitLength(long maxBitLength) {
        return this; // Struct类型的最大位长度通常由ValueType定义，不需要单独设置
    }

    @Override
    public ParameterProxy setMinBitLength(long minBitLength) {
        return this;// Struct类型的最小位长度通常由ValueType定义，不需要单独设置
    }
}
