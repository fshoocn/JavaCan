package pers.fsh.cddutils.model.po.ecudoc;

import lombok.Data;
import pers.fsh.cddutils.model.po.ecudoc.negrescodes.NegResCode;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author fanshuhua
 * @date 2025/2/8 15:43
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "NEGRESCODES")
@Data
public class Negrescodes {
    @XmlAttribute(name = "bl")
    private Integer bl;
    @XmlAttribute(name = "bo")
    private Integer bo;
    @XmlAttribute(name = "stateGroupDefault")
    private Integer stateGroupDefault;

    @XmlElement(name = "NEGRESCODE")
    private List<NegResCode> negResCodeList;
}
