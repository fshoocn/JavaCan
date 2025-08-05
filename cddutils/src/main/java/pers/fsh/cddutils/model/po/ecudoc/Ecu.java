package pers.fsh.cddutils.model.po.ecudoc;

import lombok.Data;
import pers.fsh.cddutils.model.po.base.Name;
import pers.fsh.cddutils.model.po.ecudoc.ecu.EcuInfo;
import pers.fsh.cddutils.model.po.ecudoc.ecu.Var;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author fanshuhua
 * @date 2025/2/10 19:59
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Ecu {
    @XmlAttribute(name = "id")
    private String id;
    @XmlAttribute(name = "oid")
    private String oid;
    @XmlAttribute(name = "maxCombinedServiceIds")
    private Integer maxCombinedServiceIds;
    @XmlElement(name = "NAME")
    private Name name;
    @XmlElement(name = "QUAL")
    private String qual;

    @XmlElements({
            @XmlElement(name = "UNS", type = EcuInfo.class),
            @XmlElement(name = "ENUM", type = EcuInfo.class)
    })
    private List<EcuInfo> ecuInfoList;

    @XmlElement(name = "VAR")
    private Var var;
}
