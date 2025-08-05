package pers.fsh.cddutils.model.po.ecudoc.protocolservices;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import java.util.List;

/**
 * @author fanshuhua
 * @date 2025/2/10 10:49
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class ReqPosNeg {
    @XmlElements({
            @XmlElement(name = "CONSTCOMP", type = ConstComp.class),
            @XmlElement(name = "STATICCOMP", type = StaticComp.class),
            @XmlElement(name = "SIMPLEPROXYCOMP", type = SimpleProxyComp.class),
            @XmlElement(name = "GROUPOFDTCPROXYCOMP", type = GroupOfDtcProxyComp.class),
            @XmlElement(name = "STATUSDTCPROXYCOMP", type = SimpleProxyComp.class),
            @XmlElement(name = "EOSITERCOMP", type = EosIterComp.class),
            @XmlElement(name = "DOMAINDATAPROXYCOMP", type = DomainDataProxyComp.class)
    })
    private List<ProtocolServiceBaseComp> components;
}
