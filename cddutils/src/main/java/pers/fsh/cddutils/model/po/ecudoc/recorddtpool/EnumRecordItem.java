package pers.fsh.cddutils.model.po.ecudoc.recorddtpool;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author fanshuhua
 * @date 2025/7/21 14:33
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ENUMRECORDITEM")
@Data
public class EnumRecordItem implements RecordItem {

    @XmlAttribute(name = "idref")
    private String idref;
    @XmlAttribute(name = "v")
    private long v;

    @Override
    public long getV() {
        return v;
    }

    @Override
    public String getRef() {
        return idref;
    }
}
