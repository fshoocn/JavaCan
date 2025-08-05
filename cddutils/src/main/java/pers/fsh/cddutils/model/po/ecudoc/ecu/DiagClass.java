package pers.fsh.cddutils.model.po.ecudoc.ecu;

import lombok.Data;
import pers.fsh.cddutils.model.po.base.Name;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author fanshuhua
 * @date 2025/3/5 16:43
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class DiagClass {
    @XmlAttribute(name = "id")
    private String id;
    @XmlAttribute(name = "oid")
    private String oid;
    @XmlAttribute(name = "tmplref")
    private String tmplref;
    @XmlElement(name = "NAME")
    private Name name;
    @XmlElement(name = "QUAL")
    private String qual;

    @XmlElement(name = "DIAGINST")
    private List<DiagInst> diagInstList;
}
