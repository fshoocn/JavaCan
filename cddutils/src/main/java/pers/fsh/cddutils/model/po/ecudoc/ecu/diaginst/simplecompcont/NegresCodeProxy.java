package pers.fsh.cddutils.model.po.ecudoc.ecu.diaginst.simplecompcont;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author fanshuhua
 * @date 2025/3/5 16:31
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class NegresCodeProxy {
    @XmlAttribute(name = "idref")
    private String idref;
}
