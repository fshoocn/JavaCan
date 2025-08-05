package pers.fsh.cddutils.model.po.ecudoc.ecu.diaginst.simplecompcont;

import lombok.Data;
import lombok.ToString;
import pers.fsh.cddutils.model.po.base.Name;
import pers.fsh.cddutils.model.po.base.ProxyInterface;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author fanshuhua
 * @date 2025/3/5 16:27
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@ToString(callSuper = true)
public class SpecDataObj implements ProxyInterface<List<NegresCodeProxy>> {

    @XmlAttribute(name = "oid")
    private String oid;
    @XmlElement(name = "NAME")
    private Name name;
    @XmlElement(name = "QUAL")
    private String qual;
    @XmlElementWrapper(name = "NEGRESCODEPROXIES")
    @XmlElement(name = "NEGRESCODEPROXY")
    private List<NegresCodeProxy> negresCodeProxyList;

    @Override
    public List<NegresCodeProxy> getRef() {
        return negresCodeProxyList;
    }
}
