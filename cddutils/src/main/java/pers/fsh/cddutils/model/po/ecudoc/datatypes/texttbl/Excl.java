package pers.fsh.cddutils.model.po.ecudoc.datatypes.texttbl;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author fanshuhua
 * @date 2025/3/5 17:30
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Excl {
    @XmlAttribute(name = "s")
    private String s;
    @XmlAttribute(name = "e")
    private String e;
    //    invalidText
    @XmlAttribute(name = "inv")
    private String inv;
}
