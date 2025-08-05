package pers.fsh.cddutils.model.po.ecudoc.ecu.diaginst;

import lombok.Data;
import pers.fsh.cddutils.model.po.base.Name;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author fanshuhua
 * @date 2025/2/12 10:16
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Service {
    @XmlAttribute(name = "id")
    private String id;
    @XmlAttribute(name = "oid")
    private String oid;
    @XmlAttribute(name = "tmplref")
    private String tmplref;
    @XmlAttribute(name = "func")
    private Boolean func;
    @XmlAttribute(name = "phys")
    private Boolean phys;
    @XmlAttribute(name = "respOnFunc")
    private Boolean respOnFunc;
    @XmlAttribute(name = "respOnPhys")
    private Boolean respOnPhys;
    @XmlAttribute(name = "req")
    private Boolean req;
    @XmlAttribute(name = "mayBeExec")
    private String mayBeExec;

    @XmlElement(name = "NAME")
    private Name name;
    @XmlElement(name = "QUAL")
    private String qual;
    @XmlElement(name = "SHORTCUTNAME")
    private Name shortcutName;
}
