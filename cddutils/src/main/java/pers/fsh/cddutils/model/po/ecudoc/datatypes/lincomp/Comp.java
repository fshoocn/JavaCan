package pers.fsh.cddutils.model.po.ecudoc.datatypes.lincomp;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author fanshuhua
 * @date 2025/3/5 17:35
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Comp {
    @XmlAttribute(name = "s")
    private String s;
    @XmlAttribute(name = "e")
    private String e;
    @XmlAttribute(name = "f")
    private String f;
    @XmlAttribute(name = "o")
    private String o;
}
