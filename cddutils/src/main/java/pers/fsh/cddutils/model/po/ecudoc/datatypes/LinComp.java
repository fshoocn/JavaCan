package pers.fsh.cddutils.model.po.ecudoc.datatypes;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import pers.fsh.cddutils.model.po.ecudoc.datatypes.lincomp.Comp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author fanshuhua
 * @date 2025/3/5 17:34
 */
@EqualsAndHashCode(callSuper = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "LINCOMP")
@Data
@ToString(callSuper = true)
public class LinComp extends Ident {
    @XmlElement(name = "COMP")
    private Comp comp;
}
