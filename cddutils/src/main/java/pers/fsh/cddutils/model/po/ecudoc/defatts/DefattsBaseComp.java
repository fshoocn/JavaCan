package pers.fsh.cddutils.model.po.ecudoc.defatts;

import lombok.Data;
import pers.fsh.cddutils.model.po.base.Name;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author fanshuhua
 * @date 2025/2/6 19:49
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public abstract class DefattsBaseComp {
    @XmlAttribute(name = "id")
    private String id;
    @XmlAttribute(name = "oid")
    private String oid;
    // attrcatref 是 Attrcat 的 id
    @XmlAttribute(name = "attrcatref")
    private String attrcatref;
    // usage 一般为sys 或 admin
    @XmlAttribute(name = "usage")
    private String usage;
    // v 代表value
    @XmlAttribute(name = "v")
    private Integer v;
    @XmlElement(name = "NAME")
    private Name name;
    @XmlElement(name = "QUAL")
    private String qual;
}
