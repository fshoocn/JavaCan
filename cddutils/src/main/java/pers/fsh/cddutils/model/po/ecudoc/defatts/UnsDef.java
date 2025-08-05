package pers.fsh.cddutils.model.po.ecudoc.defatts;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author fanshuhua
 * @date 2025/2/6 18:29
 */
@EqualsAndHashCode(callSuper = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "UNSDEF")
@Data
@ToString(callSuper = true)
public class UnsDef extends DefattsBaseComp {
    // df 的值一般为 dec 或 hex 代表十进制或十六进制
    @XmlAttribute(name = "df")
    private String df;
}
