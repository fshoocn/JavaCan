package pers.fsh.cddutils.model.po.ecudoc;

import lombok.Data;
import pers.fsh.cddutils.model.po.base.Name;
import pers.fsh.cddutils.model.po.base.ProxyInterface;
import pers.fsh.cddutils.model.po.ecudoc.ecu.diaginst.simplecompcont.DataObj;
import pers.fsh.cddutils.model.po.ecudoc.ecu.diaginst.simplecompcont.Struct;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author fanshuhua
 * @date 2025/7/25 14:36
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "DID")
@Data
public class Did {
    @XmlAttribute(name = "id")
    private String id;
    @XmlAttribute(name = "oid")
    private String oid;
    @XmlAttribute(name = "n")
    private long n;
    @XmlElement(name = "NAME")
    private Name name;
    @XmlElement(name = "QUAL")
    private String qual;

    @XmlElementWrapper(name = "STRUCTURE")
    @XmlElements({
            @XmlElement(name = "DATAOBJ", type = DataObj.class),
            @XmlElement(name = "STRUCT", type = Struct.class)
    })
    private List<ProxyInterface<String>> structure;
}
