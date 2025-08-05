package pers.fsh.cddutils.model.po.ecudoc.ecu;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author fanshuhua
 * @date 2025/2/10 20:04
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class EcuInfo {
    @XmlAttribute(name = "oid")
    private String oid;
    @XmlAttribute(name = "attrref")
    private String attrref;
    @XmlAttribute(name = "v")
    private Integer v;
}
