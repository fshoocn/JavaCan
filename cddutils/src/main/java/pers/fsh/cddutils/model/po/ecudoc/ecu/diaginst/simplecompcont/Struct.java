package pers.fsh.cddutils.model.po.ecudoc.ecu.diaginst.simplecompcont;

import lombok.Data;
import lombok.ToString;
import pers.fsh.cddutils.model.po.base.Name;
import pers.fsh.cddutils.model.po.base.ProxyInterface;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author fanshuhua
 * @date 2025/4/18 16:51
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@ToString(callSuper = true)
@XmlRootElement(name = "STRUCT")
public class Struct implements ProxyInterface<String> {

    @XmlAttribute(name = "oid")
    private String oid;
    @XmlElement(name = "NAME")
    private Name name;
    @XmlElement(name = "QUAL")
    private String qual;
    @XmlAttribute(name = "spec")
    private String spec;
    @XmlAttribute(name = "dtref")
    private String dtref;
    @XmlElements({
            @XmlElement(name = "DATAOBJ", type = DataObj.class),
            @XmlElement(name = "GAPDATAOBJ", type = GapDataObj.class)
    })
    private List<Object> objList;

    @Override
    public String getRef() {
        return dtref;
    }
}
