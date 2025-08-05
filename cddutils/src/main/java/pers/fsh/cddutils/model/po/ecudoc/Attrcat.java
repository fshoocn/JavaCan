package pers.fsh.cddutils.model.po.ecudoc;

import lombok.Data;
import pers.fsh.cddutils.model.po.base.Name;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author fanshuhua
 * @date 2025/2/6 18:28
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Attrcat {
    @XmlAttribute(name = "id")
    private String id;

    @XmlAttribute(name = "oid")
    private String oid;

    @XmlAttribute(name = "usage")
    private String usage;

    @XmlAttribute(name = "xauth")
    private String xauth;

    @XmlElement(name = "NAME")
    private Name name;

    @XmlElement(name = "QUAL")
    private String qual;

    // Getters and Setters
}
