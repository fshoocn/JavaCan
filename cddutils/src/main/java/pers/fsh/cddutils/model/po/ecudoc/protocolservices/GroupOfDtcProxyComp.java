package pers.fsh.cddutils.model.po.ecudoc.protocolservices;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import pers.fsh.cddutils.model.po.base.ProxyInterface;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author fanshuhua
 * @date 2025/7/29 19:02
 */
@EqualsAndHashCode(callSuper = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "GROUPOFDTCPROXYCOMP")
@Data
@ToString(callSuper = true)
public class GroupOfDtcProxyComp extends ProtocolServiceBaseComp implements ProxyInterface<String> {
    @XmlAttribute(name = "minbl")
    private Long minbl;
    @XmlAttribute(name = "maxbl")
    private Long maxbl;
    @XmlAttribute(name = "dest")
    private String dest;
    @XmlAttribute(name = "dtref")
    private String dtref;


    @Override
    public String getRef() {
        return dtref;
    }
}
