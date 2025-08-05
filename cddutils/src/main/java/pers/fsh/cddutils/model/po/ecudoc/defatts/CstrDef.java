package pers.fsh.cddutils.model.po.ecudoc.defatts;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author fanshuhua
 * @date 2025/2/8 16:18
 */
@EqualsAndHashCode(callSuper = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CSTRDEF")
@Data
@ToString(callSuper = true)
public class CstrDef extends DefattsBaseComp {
    @XmlElement(name = "COMMONSTRING")
    private String commonString;
}
