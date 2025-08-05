package pers.fsh.cddutils.model.po.ecudoc.datatypes.texttbl;

import lombok.Data;
import pers.fsh.cddutils.model.po.base.Tuv;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author fanshuhua
 * @date 2025/2/8 16:28
 * <p>
 * 备注：s和e 暂定为LONG类型，八个字节，超过8个字节可能出现问题
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class TextMap {
    @XmlAttribute(name = "s")
    private Long s;
    @XmlAttribute(name = "e")
    private Long e;
    @XmlElementWrapper(name = "TEXT")
    @XmlElement(name = "TUV")
    private List<Tuv> textList;
}
