package pers.fsh.deviceutils.hardware.vectorhardware;

import com.sun.jna.DefaultTypeMapper;
import pers.fsh.deviceutils.hardware.vectorhardware.typemapper.*;

/**
 * @author fanshuhua
 * @date 2025/5/13 19:59
 */
public class VectorTypeMapper extends DefaultTypeMapper {
    public VectorTypeMapper() {
        addTypeConverter(XlBusType.class, new XlBusTypeTypeMapper());
        addTypeConverter(XLstatus.class, new XLstatusTypeMapper());
        addTypeConverter(XlInterfaceVersion.class, new XlInterfaceVersionTypeMapper());
    }
}
