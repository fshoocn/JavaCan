package pers.fsh.cddutils.model.po.ecudoc.protocolservices;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * 否定应答码代理
 * 用于引用NegResCode对象
 *
 * @author fanshuhua
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class NegResCodeProxy {

    @XmlAttribute(name = "idref")
    private String idref;
} 