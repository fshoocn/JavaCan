package pers.fsh.cddutils.model.po.ecudoc.recorddtpool;

import lombok.Data;
import pers.fsh.cddutils.model.po.base.Name;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author fanshuhua
 * @date 2025/7/21 14:26
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "RECORD")
@Data
public class Record {
    @XmlAttribute(name = "id")
    private String id;
    @XmlElement(name = "TEXT")
    private Name name;
    @XmlAttribute(name = "oid")
    private String oid;
    @XmlAttribute(name = "v")
    private long v;


    @XmlElements({
            @XmlElement(name = "UNSRECORDITEM", type = UnsRecordItem.class),
            @XmlElement(name = "ENUMRECORDITEM", type = EnumRecordItem.class)
    })
    private List<RecordItem> recordItemList;
}
