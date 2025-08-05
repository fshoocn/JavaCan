package pers.fsh.cddutils.model.po.ecudoc.ecu;

import lombok.Data;
import pers.fsh.cddutils.model.po.base.Name;
import pers.fsh.cddutils.model.po.ecudoc.ecu.diaginst.DiagInstBaseComp;
import pers.fsh.cddutils.model.po.ecudoc.ecu.diaginst.Service;
import pers.fsh.cddutils.model.po.ecudoc.ecu.diaginst.SimpleCompCont;
import pers.fsh.cddutils.model.po.ecudoc.ecu.diaginst.StaticValue;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author fanshuhua
 * @date 2025/2/12 10:03
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class DiagInst {
    @XmlAttribute(name = "id")
    private String id;
    @XmlAttribute(name = "oid")
    private String oid;
    @XmlAttribute(name = "tmplref")
    private String tmplref;
    // 猜测是Required in all Variants
    @XmlAttribute(name = "req")
    private String req;
    @XmlElement(name = "NAME")
    private Name name;
    @XmlElement(name = "QUAL")
    private String qual;

    @XmlElement(name = "SERVICE")
    private List<Service> serviceList;

    //    STATICVALUE 代表静态值 SIMPLECOMPCONT 代表简单组件
    @XmlElements({
            @XmlElement(name = "STATICVALUE", type = StaticValue.class),
            @XmlElement(name = "SIMPLECOMPCONT", type = SimpleCompCont.class)
    })
    private List<DiagInstBaseComp> compList;
}
