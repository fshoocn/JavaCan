package pers.fsh.cddutils.model.po.ecudoc;

import lombok.Data;
import pers.fsh.cddutils.model.po.ecudoc.defatts.*;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author fanshuhua
 * @date 2025/2/6 19:46
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "DEFATTS")
@Data
public class Defatts {

    @XmlElementWrapper(name = "ECUATTS")
    @XmlElementRefs({
            @XmlElementRef(name = "ENUMDEF", type = EnumDef.class),
            @XmlElementRef(name = "UNSDEF", type = UnsDef.class),
            @XmlElementRef(name = "CSTRDEF", type = CstrDef.class),
            @XmlElementRef(name = "STRDEF", type = StrDef.class)
    })
    private List<DefattsBaseComp> ecuattsList;


    @XmlElementWrapper(name = "SERVICEATTS")
    @XmlElementRefs({
            @XmlElementRef(name = "ENUMDEF", type = EnumDef.class),
            @XmlElementRef(name = "UNSDEF", type = UnsDef.class),
            @XmlElementRef(name = "CSTRDEF", type = CstrDef.class),
            @XmlElementRef(name = "STRDEF", type = StrDef.class)
    })
    private List<DefattsBaseComp> serviceattsList;
}
