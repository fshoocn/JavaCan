package pers.fsh.cddutils.model.vo.parameter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import pers.fsh.cddutils.model.po.base.Name;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fanshuhua
 * @date 2025/7/29 11:08
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Log4j2
public sealed class ParameterProxy implements ParameterInterface
        permits ParameterIdent, ParameterDid, ParameterStruct, ParameterDtc {
    private Name name;
    private String qualifiers;
    private String id;
    private boolean serial = true;  // 串行或并行，
    private List<ParameterInterface> parameterList = new ArrayList<>();
    private long minBitLength = 0; // 最小位长度
    private long maxBitLength = 0; // 最大位长度
    private long minSize = 1; // 最小字节数
    private long maxSize = 1; // 最大字节数

    public ParameterProxy(Name name, String qualifiers) {
        this.name = name;
        this.qualifiers = qualifiers;
    }

    public ParameterProxy addParameter(ParameterInterface parameter) {
        if (parameter != null) {
            this.parameterList.add(parameter);
        } else {
            log.error("Parameter is null");
        }
        return this;
    }

    public long getMinBitLength() {
        if (serial) {
            long sum = parameterList.stream()
                    .mapToLong(ParameterInterface::getMinBitLength)
                    .sum();
            return Math.max(sum, minBitLength);
        } else {
            long min = parameterList.stream()
                    .mapToLong(ParameterInterface::getMinBitLength)
                    .min()
                    .orElse(0);
            return Math.max(min, minBitLength);
        }
    }

    public long getMaxBitLength() {
        if (serial) {
            // 串行时，最大位长度为所有参数的位长度之和
            long sum = parameterList.stream()
                    .mapToLong(ParameterInterface::getMaxBitLength)
                    .sum();
            return Math.max(sum, maxBitLength);
        } else {
            // 并行时，最大位长度为所有参数的最大位长度中的最大值
            long max = parameterList.stream()
                    .mapToLong(ParameterInterface::getMaxBitLength)
                    .max()
                    .orElse(0);
            return Math.max(max, maxBitLength);
        }
    }

    public ParameterProxy setMinSize(long minSize) {
        this.minSize = minSize;
        if (this.maxSize < minSize) {
            this.maxSize = minSize; // 确保最大值不小于最小值
        }
        return this;
    }
}
