package pers.fsh.cddutils.model.vo.parameter;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import pers.fsh.cddutils.model.po.ecudoc.datatypes.ValueType;
import pers.fsh.cddutils.model.po.ecudoc.datatypes.texttbl.TextMap;

import java.util.List;

/**
 * @author fanshuhua
 * @date 2025/7/29 14:21
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public final class ParameterTextTable extends ParameterIdent {
    @Getter
    @Setter
    private List<TextMap> textMapList;

    public ParameterTextTable(ValueType cValueType,
                              ValueType pValueType,
                              List<TextMap> textMapList) {
        super(cValueType, pValueType);
        this.textMapList = textMapList;
        this.setSerial(false); // TextTable is always parallel
    }
}
