package pers.fsh.cddutils.model.po.ecudoc;

import lombok.Data;
import pers.fsh.cddutils.model.po.ecudoc.protocolservices.ProtocolService;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author fanshuhua
 * @date 2025/2/10 10:18
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Protocolservices {
    @XmlElement(name = "PROTOCOLSERVICE")
    private List<ProtocolService> protocolServiceList;
}
