package pers.fsh.cddutils.model.po.ecudoc.ecu.diaginst.simplecompcont;

import lombok.Data;
import lombok.ToString;
import pers.fsh.cddutils.model.po.base.Name;
import pers.fsh.cddutils.model.po.ecudoc.recorddtpool.Recorddt;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author fanshuhua
 * @date 2025/7/22 19:17
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@ToString(callSuper = true)
public class RecordDataObj {
    @XmlAttribute(name = "oid")
    private String oid;
    @XmlAttribute(name = "rtSpec")
    private String rtSpec;
    @XmlElement(name = "NAME")
    private Name name;
    @XmlElement(name = "QUAL")
    private String qual;

    @XmlElement(name = "RECORDDT")
    private List<Recorddt> recorddtPool;
}
