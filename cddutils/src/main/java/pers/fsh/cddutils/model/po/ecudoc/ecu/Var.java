package pers.fsh.cddutils.model.po.ecudoc.ecu;

import lombok.Data;
import pers.fsh.cddutils.model.po.base.Name;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author fanshuhua
 * @date 2025/2/10 20:08
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Var {
    @XmlAttribute(name = "id")
    private String id;
    @XmlAttribute(name = "oid")
    private String oid;
    @XmlAttribute(name = "base")
    private String base;
    @XmlElement(name = "NAME")
    private Name name;
    @XmlElement(name = "QUAL")
    private String qual;

    @XmlElementWrapper(name = "DIDREFS")
    @XmlElement(name = "DIDREF")
    private List<DidRef> didRefList;

    @XmlElement(name = "DIAGCLASS")
    private List<DiagClass> diagClassList;

    @XmlElement(name = "DIAGINST")
    private List<DiagInst> diagInstList;
}
