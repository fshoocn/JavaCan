package pers.fsh.cddutils.model.po.ecudoc;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author fanshuhua
 * @date 2025/3/5 17:20
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class QualGenOptionS {
    //        <QUALGENOPTIONS case='ignore' minLen='1' maxLen='128'/>
    @XmlAttribute(name = "case")
    private String caseText;
    @XmlAttribute(name = "minLen")
    private Integer minLen;
    @XmlAttribute(name = "maxLen")
    private Integer maxLen;
}
