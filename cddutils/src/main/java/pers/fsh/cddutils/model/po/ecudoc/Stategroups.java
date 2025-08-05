package pers.fsh.cddutils.model.po.ecudoc;

import lombok.Data;
import pers.fsh.cddutils.model.po.ecudoc.stategroups.Stategroup;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author fanshuhua
 * @date 2025/2/8 15:54
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Stategroups {
    @XmlElement(name = "STATEGROUP")
    private List<Stategroup> stategroupList;
}
