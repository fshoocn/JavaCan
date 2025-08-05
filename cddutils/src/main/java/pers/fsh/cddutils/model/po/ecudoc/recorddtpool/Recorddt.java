package pers.fsh.cddutils.model.po.ecudoc.recorddtpool;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import pers.fsh.cddutils.model.po.ecudoc.datatypes.Ident;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author fanshuhua
 * @date 2025/7/21 14:20
 */

@EqualsAndHashCode(callSuper = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "RECORDDT")
@Data
@ToString(callSuper = true)
public class Recorddt extends Ident {
    @XmlAttribute(name = "rtSpec")
    private String rtSpec;
    @XmlElement(name = "RECORD")
    private List<Record> records;
}
