package pers.fsh.cddutils.model.po.ecudoc.datatypes;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author fanshuhua
 * @date 2025/2/8 16:08
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@Accessors(chain = true)
public class ValueType {
    @XmlAttribute(name = "bl")
    private long bl;  // Bits length ，位长度

    @JSONField(serialize = false)
    @XmlAttribute(name = "bo")
    private long bo; // Byte Order 字节顺序（high/low）

    @JSONField(serialize = false)
    @XmlAttribute(name = "enc")
    private String enc; // Encoding 编码

    @JSONField(serialize = false)
    @XmlAttribute(name = "sig")
    private String sig; // Significance 显著性

    @JSONField(serialize = false)
    @XmlAttribute(name = "df")
    private String df;  // Display format 显示格式

    @JSONField(serialize = false)
    @XmlAttribute(name = "qty")
    private String qty;  // Quantity ,分为atom和field两种类型，atom表示单个值（字符、整数等），field表示同一大小的值的数组（整数数组、字符字符串等）。

    @JSONField(serialize = false)
    @XmlAttribute(name = "sz")
    private String sz; // String Termination 字符串终止符？ 不太确定

    @XmlAttribute(name = "minsz")
    private long minsz; // Minimum Size 最小值

    @XmlAttribute(name = "maxsz")
    private long maxsz; // Maximum Size 最大值

    @JSONField(serialize = false)
    @XmlElement(name = "UNIT")
    private String unit; // Unit 单位
}
