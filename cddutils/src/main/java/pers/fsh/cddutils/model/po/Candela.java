package pers.fsh.cddutils.model.po;

import lombok.Data;

import javax.xml.bind.annotation.*;

/**
 * @author fanshuhua
 * @date 2025/2/6 18:25
 */
@XmlRootElement(name = "CANDELA")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Candela {
    @XmlElement(name = "ECUDOC")
    private Ecudoc ecudoc;

    @XmlAttribute(name = "dtdvers")
    private String dtdvers;
}
