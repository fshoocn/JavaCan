package pers.fsh.cddutils.model.po.ecudoc.protocolservices;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author fanshuhua
 * @date 2025/7/31 15:23
 */
@EqualsAndHashCode(callSuper = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "EOSITERCOMP")
@Data
@ToString(callSuper = true)
public class EosIterComp extends ProtocolServiceBaseComp {
    @XmlAttribute(name = "minNumOfItems")
    private long minNumOfItems;
    @XmlElements({
            @XmlElement(name = "CONSTCOMP", type = ConstComp.class),
            @XmlElement(name = "STATICCOMP", type = StaticComp.class),
            @XmlElement(name = "SIMPLEPROXYCOMP", type = SimpleProxyComp.class),
            @XmlElement(name = "GROUPOFDTCPROXYCOMP", type = GroupOfDtcProxyComp.class),
            @XmlElement(name = "STATUSDTCPROXYCOMP", type = SimpleProxyComp.class),
            @XmlElement(name = "EOSITERCOMP", type = EosIterComp.class),
            @XmlElement(name = "DOMAINDATAPROXYCOMP", type = DomainDataProxyComp.class)
    })
    private List<ProtocolServiceBaseComp> childrenList;
}
