package pers.fsh.cddutils.model.po.base;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author fanshuhua
 * @date 2025/2/6 19:10
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Name {
    @XmlElement(name = "TUV")
    private List<Tuv> tuvList;  // 多语言名称

    @Override
    public String toString() {
//        默认打印英文名字
        return tuvList.get(0).getValue();
    }
}
