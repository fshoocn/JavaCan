package pers.fsh.cddutils.model.po.ecudoc.protocolservices;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author fanshuhua
 * @date 2025/7/31 15:47
 */
@EqualsAndHashCode(callSuper = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "DOMAINDATAPROXYCOMP")
@Data
@ToString(callSuper = true)
public class DomainDataProxyComp extends ProtocolServiceBaseComp {
    @XmlAttribute(name = "dest")
    private String dest;
}
