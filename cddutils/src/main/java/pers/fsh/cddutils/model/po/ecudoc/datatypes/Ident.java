package pers.fsh.cddutils.model.po.ecudoc.datatypes;

import lombok.Data;
import pers.fsh.cddutils.model.po.base.Name;
import pers.fsh.cddutils.model.po.ecudoc.datatypes.texttbl.Excl;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author fanshuhua
 * @date 2025/2/8 16:07
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "IDENT")
@Data
public class Ident {
    @XmlAttribute(name = "bm")
    private long bm;  // bit mask
    @XmlAttribute(name = "id")
    private String id;
    @XmlAttribute(name = "oid")
    private String oid;
    @XmlElement(name = "NAME")
    private Name name;
    @XmlElement(name = "QUAL")
    private String qual;
    @XmlElement(name = "CVALUETYPE")
    private ValueType cValueType;  // Coded value type
    @XmlElement(name = "PVALUETYPE")
    private ValueType pValueType;  // Physical value type
    @XmlElement(name = "EXCL")
    private List<Excl> exclList;  // Exclusion list, optional
}
