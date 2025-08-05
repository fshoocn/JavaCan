package pers.fsh.cddutils.model.po.ecudoc.defatts;

import lombok.Data;
import pers.fsh.cddutils.model.po.base.Tuv;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Etag {
    @XmlAttribute(name = "v")
    private Integer value;

    @XmlElement(name = "TUV")
    private List<Tuv> tuvsList;

}
