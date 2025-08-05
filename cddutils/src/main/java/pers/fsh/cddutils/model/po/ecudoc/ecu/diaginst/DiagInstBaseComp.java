package pers.fsh.cddutils.model.po.ecudoc.ecu.diaginst;

import lombok.Data;
import pers.fsh.cddutils.model.po.base.ProxyInterface;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author fanshuhua
 * @date 2025/2/12 10:25
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public abstract class DiagInstBaseComp implements ProxyInterface<String> {
    @XmlAttribute(name = "oid")
    private String oid;

}
