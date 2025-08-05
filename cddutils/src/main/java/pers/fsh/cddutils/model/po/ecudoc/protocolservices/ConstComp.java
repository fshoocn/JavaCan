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
 * @date 2025/2/10 10:56
 */
@EqualsAndHashCode(callSuper = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CONSTCOMP")
@Data
@ToString(callSuper = true)
public class ConstComp extends ProtocolServiceBaseComp {
    @XmlAttribute(name = "spec")
    private String spec;
    @XmlAttribute(name = "bl")
    private Long bl;
    @XmlAttribute(name = "v")
    private long v;
}
