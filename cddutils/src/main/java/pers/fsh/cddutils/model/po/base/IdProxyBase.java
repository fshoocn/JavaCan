package pers.fsh.cddutils.model.po.base;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author fanshuhua
 * @date 2025/2/10 19:46
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class IdProxyBase {
    @XmlAttribute(name = "idref")
    private String idref;
}
