package pers.fsh.cddutils.model.po.ecudoc.negrescodes;

import lombok.Data;
import pers.fsh.cddutils.model.po.base.Name;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author fanshuhua
 * @date 2025/2/8 15:49
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class NegResCode {
    @XmlAttribute(name = "id")
    private String id;
    @XmlAttribute(name = "oid")
    private String oid;
    @XmlAttribute(name = "v")
    private long v;

    @XmlElement(name = "NAME")
    private Name name;

    @XmlElement(name = "QUAL")
    private String qual;
}
