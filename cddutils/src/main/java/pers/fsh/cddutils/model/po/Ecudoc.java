package pers.fsh.cddutils.model.po;

import lombok.Data;
import pers.fsh.cddutils.model.po.ecudoc.*;
import pers.fsh.cddutils.model.po.ecudoc.recorddtpool.Recorddt;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author fanshuhua
 * @date 2025/2/6 18:27
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Ecudoc {
    @XmlAttribute(name = "oid")
    private String oid;

    @XmlAttribute(name = "doctype")
    private String doctype;

    @XmlAttribute(name = "manufacturer")
    private String manufacturer;

    @XmlAttribute(name = "mid")
    private String mid;

    @XmlAttribute(name = "saveno")
    private String saveno;

    @XmlAttribute(name = "languages")
    private String languages;

    @XmlAttribute(name = "uptodateLanguages")
    private String uptodateLanguages;

    @XmlAttribute(name = "jobfileext")
    private String jobfileext;

    @XmlAttribute(name = "xdtauth")
    private String xdtauth;

    @XmlAttribute(name = "dtNesting")
    private String dtNesting;

    @XmlElement(name = "PROTOCOLSTANDARD")
    private String protocolStandard;

    @XmlElement(name = "SPECOWNER")
    private String specOwner;

    @XmlElement(name = "DTID")
    private String dtid;

    @XmlElement(name = "QUALGENOPTIONS")
    private QualGenOptionS qualGenOptionS;

    @XmlElementWrapper(name = "ATTRCATS")
    @XmlElement(name = "ATTRCAT")
    private List<Attrcat> attrcats;

    @XmlElement(name = "DEFATTS")
    private Defatts defatts;

    @XmlElementWrapper(name = "AUTHORS")
    @XmlElement(name = "AUTHOR")
    private List<Author> authorList;

    @XmlElement(name = "NEGRESCODES")
    private Negrescodes negrescodes;

    @XmlElement(name = "STATEGROUPS")
    private Stategroups stategroups;

    @XmlElement(name = "DATATYPES")
    private Datatypes datatypes;

    @XmlElementWrapper(name = "DIDS")
    @XmlElement(name = "DID")
    private List<Did> Dids;

    @XmlElement(name = "PROTOCOLSERVICES")
    private Protocolservices protocolservices;

    @XmlElement(name = "DCLTMPLS")
    private Dcltmpls dcltmpls;

    @XmlElementWrapper(name = "RECORDDTPOOL")
    @XmlElement(name = "RECORDDT")
    private List<Recorddt> recorddtPool;

    @XmlElement(name = "ECU")
    private Ecu ecu;
}
