package pers.fsh.cddutils.model.po.ecudoc;

import lombok.Data;
import pers.fsh.cddutils.model.po.ecudoc.datatypes.Ident;
import pers.fsh.cddutils.model.po.ecudoc.datatypes.LinComp;
import pers.fsh.cddutils.model.po.ecudoc.datatypes.TextTbl;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author fanshuhua
 * @date 2025/2/8 16:06
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "DATATYPES")
@Data
public class Datatypes {
    //    @XmlElementWrapper(name = "DATATYPES")
    @XmlElementRefs({
            @XmlElementRef(name = "IDENT", type = Ident.class),
            @XmlElementRef(name = "TEXTTBL", type = TextTbl.class),
            @XmlElementRef(name = "LinComp", type = LinComp.class)
    })
    private List<Ident> Datatypes;
}
