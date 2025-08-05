package pers.fsh.cddutils.model.po.ecudoc.ecu.diaginst.simplecompcont;

import lombok.Data;
import lombok.ToString;
import pers.fsh.cddutils.model.po.base.Name;
import pers.fsh.cddutils.model.po.base.ProxyInterface;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author fanshuhua
 * @date 2025/3/5 16:24
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@ToString(callSuper = true)
public class DidDataRef implements ProxyInterface<String> {

    @XmlAttribute(name = "didRef")
    private String didRef;
    @XmlElement(name = "NAME")
    private Name name;
    @XmlElement(name = "QUAL")
    private String qual;

    @Override
    public String getRef() {
        return didRef;
    }
}
