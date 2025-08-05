package pers.fsh.cddutils.model.po.base;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

/**
 * @author fanshuhua
 * @date 2025/2/6 18:28
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Tuv {
    @XmlAttribute(name = "lang", namespace = "http://www.w3.org/XML/1998/namespace")
    private String lang;

    @XmlAttribute(name = "uptodate")
    private Integer uptodate; // 可能是Integer或Boolean，根据实际需求调整

    @XmlValue
    private String value;

}
