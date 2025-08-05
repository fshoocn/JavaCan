package pers.fsh.cddutils.model.po.ecudoc.defatts;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author fanshuhua
 * @date 2025/2/6 18:28
 */
@EqualsAndHashCode(callSuper = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ENUMDEF")
@Data
@ToString(callSuper = true)
public class EnumDef extends DefattsBaseComp {
    @XmlAttribute(name = "sort")
    private String sort;

    @XmlElement(name = "ETAG")
    private List<Etag> etagList;
}

