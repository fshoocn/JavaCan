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
 * @date 2025/2/10 10:59
 * 这个类只是占位使用，实际不包含任何数据
 */
@EqualsAndHashCode(callSuper = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "SIMPLEPROXYCOMP")
@Data
@ToString(callSuper = true)
public class SimpleProxyComp extends ProtocolServiceBaseComp {
    @XmlAttribute(name = "minbl")
    private Long minbl;
    @XmlAttribute(name = "maxbl")
    private Long maxbl;
    @XmlAttribute(name = "dest")
    private String dest;
}
