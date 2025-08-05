package pers.fsh.cddutils.model.vo.parameter;

import pers.fsh.cddutils.model.po.base.Name;

/**
 * @author fanshuhua
 * @date 2025/7/19 14:17
 */
public sealed interface ParameterInterface permits ParameterConstant, ParameterProxy {

    Name getName();

    ParameterInterface setName(Name name);

    String getQualifiers();

    ParameterInterface setQualifiers(String qualifiers);

    long getMinSize();

    long getMaxSize();

    long getMinBitLength();

    long getMaxBitLength();

    String getId();

    ParameterInterface setId(String id);
}