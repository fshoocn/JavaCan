package pers.fsh.cddutils.model.po.ecudoc.ecu.diaginst;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import pers.fsh.cddutils.model.po.ecudoc.ecu.diaginst.simplecompcont.*;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author fanshuhua
 * @date 2025/2/21 16:34
 */
@Data
@EqualsAndHashCode(callSuper = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "SIMPLECOMPCONT")
@ToString(callSuper = true)
public class SimpleCompCont extends DiagInstBaseComp {
    @XmlAttribute(name = "shproxyref")
    private String shproxyref;

    @XmlElements({
            @XmlElement(name = "DIDDATAREF", type = DidDataRef.class),
            @XmlElement(name = "SPECDATAOBJ", type = SpecDataObj.class),
            @XmlElement(name = "STRUCT", type = Struct.class),
            @XmlElement(name = "DATAOBJ", type = DataObj.class),
            @XmlElement(name = "RECORDDATAOBJ", type = RecordDataObj.class),
    })
    private List<Object> objList;

    @Override
    public String getRef() {
        return shproxyref;
    }
}
