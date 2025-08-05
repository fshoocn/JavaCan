package pers.fsh.cddutils.model.po.ecudoc;

import lombok.Data;
import pers.fsh.cddutils.model.po.ecudoc.dcltmpls.Dcltmpl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author fanshuhua
 * @date 2025/2/10 11:42
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "DCLTMPLS")
@Data
public class Dcltmpls {
    @XmlElement(name = "DCLTMPL")
    private List<Dcltmpl> dcltmplList;
}
