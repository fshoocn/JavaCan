package pers.fsh.cddutils.model.po.ecudoc.datatypes;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import pers.fsh.cddutils.model.po.ecudoc.datatypes.texttbl.TextMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author fanshuhua
 * @date 2025/2/8 16:27
 */
@EqualsAndHashCode(callSuper = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "TEXTTBL")
@Data
@ToString(callSuper = true)
public class TextTbl extends Ident {
    @XmlElement(name = "TEXTMAP")
    private List<TextMap> textMapList;
}
