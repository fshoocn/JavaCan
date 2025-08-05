package pers.fsh.cddutils.model.po.ecudoc.defatts;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import pers.fsh.cddutils.model.po.base.Tuv;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author fanshuhua
 * @date 2025/2/8 16:21
 */
@EqualsAndHashCode(callSuper = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "STRDEF")
@Data
@ToString(callSuper = true)
public class StrDef extends DefattsBaseComp {
    @XmlElementWrapper(name = "STRING")
    @XmlElement(name = "TUV")
    private List<Tuv> stringList;
}
