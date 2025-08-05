package pers.fsh.cddutils.model.po.ecudoc.dcltmpls;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import pers.fsh.cddutils.model.po.base.IdProxyBase;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fanshuhua
 * @date 2025/2/10 19:27
 */
@EqualsAndHashCode(callSuper = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "SHPROXY")
@Data
@ToString(callSuper = true)
public class ShProxy extends DclProxyBase {
    @XmlElement(name = "PROXYCOMPREF")
    private List<IdProxyBase> idrefList;

    @Override
    public List<String> getRef() {
        return idrefList.stream().map(IdProxyBase::getIdref).collect(Collectors.toList());
    }
}
