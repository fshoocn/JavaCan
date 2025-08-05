package pers.fsh.cddutils.model.po.ecudoc;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author fanshuhua
 * @date 2025/3/5 17:23
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Author {
    @XmlAttribute(name = "id")
    private String id;

    @XmlAttribute(name = "obs")
    private String obs;

    @XmlElement(name = "LASTNAME")
    private String lastName;

    @XmlElement(name = "SHORTNAME")
    private String shortName;

    @XmlElement(name = "COMPANY")
    private String company;
}
