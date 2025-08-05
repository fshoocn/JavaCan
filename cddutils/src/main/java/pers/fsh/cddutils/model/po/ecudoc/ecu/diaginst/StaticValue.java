package pers.fsh.cddutils.model.po.ecudoc.ecu.diaginst;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author fanshuhua
 * @date 2025/2/13 20:29
 */
@EqualsAndHashCode(callSuper = true)
@XmlAccessorType(XmlAccessType.FIELD)
@ToString(callSuper = true)
@XmlRootElement(name = "STATICVALUE")
@Data
public class StaticValue extends DiagInstBaseComp {
    @XmlAttribute(name = "shstaticref")
    private String shstaticref;
    @XmlAttribute(name = "v")
    private Long v;

    @Override
    public String getRef() {
        return shstaticref;
    }
}
