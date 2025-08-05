package pers.fsh.cddutils.model.po.ecudoc.ecu.diaginst.simplecompcont;

import lombok.Data;
import lombok.ToString;
import pers.fsh.cddutils.model.po.base.Name;
import pers.fsh.cddutils.model.po.base.ProxyInterface;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author fanshuhua
 * @date 2025/4/18 16:52
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@ToString(callSuper = true)
public class DataObj implements ProxyInterface<String> {

    /*
    *
    * <DATAOBJ id='_000002B09E4C1930' oid='24BD9712E05B4db881E3FA905D1F04C4' spec='no' dtref='_000002B09E186B00'>
                                <NAME>
                                    <TUV xml:lang='en-US'>Address and Length Format Identifier</TUV>
                                    <TUV xml:lang='zh-HANS' uptodate='0'></TUV>
                                </NAME>
                                <QUAL>ALFID</QUAL>
                            </DATAOBJ>
                            <DATAOBJ oid='68B0741341F5432cBCE9F5A5FBB9586D' spec='no' dtref='_000002B0F9B6AE50' dataObjectRef='_000002B09E4C1930'>
                                <NAME>
                                    <TUV xml:lang='en-US'>Address and Size</TUV>
                                    <TUV xml:lang='zh-HANS' uptodate='0'></TUV>
                                </NAME>
                                <QUAL>MA_MS_</QUAL>
                            </DATAOBJ>
                            <DATAOBJ oid='77ff3e36-17a4-4757-9b43-6269566a7076' spec='no' v='200' dtref='_000002B0FC309C40'>
                                <NAME>
                                    <TUV xml:lang='en-US'>P2Ex</TUV>
                                    <TUV xml:lang='zh-HANS' uptodate='0'></TUV>
                                </NAME>
                                <QUAL>P2Ex</QUAL>
                            </DATAOBJ>
    * */
    @XmlAttribute(name = "id")
    private String id;
    @XmlAttribute(name = "oid")
    private String oid;
    @XmlElement(name = "NAME")
    private Name name;
    @XmlElement(name = "QUAL")
    private String qual;
    @XmlAttribute(name = "v")
    private long v;
    @XmlAttribute(name = "spec")
    private String spec;
    @XmlAttribute(name = "dtref")
    private String dtref;
    @XmlAttribute(name = "dataObjectRef")
    private String dataObjectRef;

    @Override
    public String getRef() {
        return dtref;
    }
}
