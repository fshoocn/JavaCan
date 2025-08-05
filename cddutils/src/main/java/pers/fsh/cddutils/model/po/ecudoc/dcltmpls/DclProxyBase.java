package pers.fsh.cddutils.model.po.ecudoc.dcltmpls;

import lombok.Data;
import pers.fsh.cddutils.model.po.base.Name;
import pers.fsh.cddutils.model.po.base.ProxyInterface;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author fanshuhua
 * @date 2025/2/10 19:12
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public abstract class DclProxyBase implements ProxyInterface<List<String>> {
    @XmlAttribute(name = "id")
    private String id;
    @XmlAttribute(name = "oid")
    private String oid;
    @XmlElement(name = "NAME")
    private Name name;
    @XmlElement(name = "QUAL")
    private String qual;
}
