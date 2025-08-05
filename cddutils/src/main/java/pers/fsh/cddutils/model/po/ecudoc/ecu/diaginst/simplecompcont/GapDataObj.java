package pers.fsh.cddutils.model.po.ecudoc.ecu.diaginst.simplecompcont;

import lombok.Data;
import lombok.ToString;
import pers.fsh.cddutils.model.po.base.Name;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author fanshuhua
 * @date 2025/4/18 17:10
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@ToString(callSuper = true)
public class GapDataObj {
    @XmlAttribute(name = "oid")
    private String oid;
    @XmlElement(name = "NAME")
    private Name name;
    @XmlElement(name = "QUAL")
    private String qual;
    @XmlElement(name = "bl")
    private long bl;
}
