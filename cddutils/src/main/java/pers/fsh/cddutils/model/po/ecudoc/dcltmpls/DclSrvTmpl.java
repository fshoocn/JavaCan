package pers.fsh.cddutils.model.po.ecudoc.dcltmpls;

import lombok.Data;
import pers.fsh.cddutils.model.po.base.Name;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author fanshuhua
 * @date 2025/2/10 17:49
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class DclSrvTmpl {
    @XmlAttribute(name = "id")
    private String id;
    @XmlAttribute(name = "oid")
    private String oid;
    @XmlElement(name = "NAME")
    private Name name;
    @XmlElement(name = "QUAL")
    private String qual;
    @XmlAttribute(name = "tmplref")
    private String tmplref; // 诊断模板链接
    @XmlAttribute(name = "dtref")
    private String dtref; // 数据类型范围链接
    @XmlAttribute(name = "conv")
    private String conv;
}
