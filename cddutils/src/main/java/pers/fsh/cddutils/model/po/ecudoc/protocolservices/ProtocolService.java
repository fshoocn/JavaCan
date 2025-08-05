package pers.fsh.cddutils.model.po.ecudoc.protocolservices;

import lombok.Data;
import pers.fsh.cddutils.model.po.base.IdProxyBase;
import pers.fsh.cddutils.model.po.base.Name;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author fanshuhua
 * @date 2025/2/10 10:19
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class ProtocolService {
    @XmlAttribute(name = "id")
    private String id;
    @XmlAttribute(name = "oid")
    private String oid;
    @XmlAttribute(name = "func")
    private Integer func;
    @XmlAttribute(name = "phys")
    private Integer phys;
    @XmlAttribute(name = "mresp")
    private Integer mresp;
    @XmlAttribute(name = "respOnPhys")
    private Integer respOnPhys;
    @XmlAttribute(name = "respOnFunc")
    private Integer respOnFunc;
    @XmlElement(name = "NAME")
    private Name name;
    @XmlElement(name = "QUAL")
    private String qual;
    @XmlElement(name = "REQ")
    private ReqPosNeg req;
    @XmlElement(name = "POS")
    private ReqPosNeg pos;
    @XmlElement(name = "NEG")
    private ReqPosNeg neg;
    @XmlElementWrapper(name = "NEGRESCODEPROXIES")
    @XmlElement(name = "NEGRESCODEPROXY")
    private List<IdProxyBase> negResCodeProxyList;

}
