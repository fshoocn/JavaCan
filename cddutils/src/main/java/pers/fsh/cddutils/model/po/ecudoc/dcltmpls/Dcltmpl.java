package pers.fsh.cddutils.model.po.ecudoc.dcltmpls;

import lombok.Data;
import pers.fsh.cddutils.model.po.base.Name;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author fanshuhua
 * @date 2025/2/10 11:44
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Dcltmpl {
    @XmlAttribute(name = "id")
    private String id;
    @XmlAttribute(name = "oid")
    private String oid;
    @XmlAttribute(name = "cls")
    private String cls;
    @XmlAttribute(name = "single")
    private Integer single;
    @XmlElement(name = "NAME")
    private Name name;
    @XmlElement(name = "QUAL")
    private String qual;
    @XmlElementRefs(
            {
                    @XmlElementRef(name = "SHSTATIC", type = ShStatic.class),
                    @XmlElementRef(name = "SHPROXY", type = ShProxy.class)
            }
    )
    private List<DclProxyBase> proxyList;

    @XmlElement(name = "DCLSRVTMPL")
    private List<DclSrvTmpl> dclSrvTmplList;
}
