package pers.fsh.deviceutils.hardware.vectorhardware.typemapper;

import com.sun.jna.FromNativeContext;
import com.sun.jna.ToNativeContext;
import com.sun.jna.TypeConverter;

/**
 * @author fanshuhua
 * @date 2025/5/13 20:11
 */
public class XlInterfaceVersionTypeMapper implements TypeConverter {
    @Override
    public Object fromNative(Object o, FromNativeContext fromNativeContext) {
        if (o instanceof Number) {
            return XlInterfaceVersion.values()[((Number) o).intValue()];
        }
        return null;
    }

    @Override
    public Object toNative(Object o, ToNativeContext toNativeContext) {
        if (o instanceof XlInterfaceVersion) {
            return ((XlInterfaceVersion) o).getVersion();
        }
        return o;
    }

    @Override
    public Class<?> nativeType() {
        return Integer.class;
    }
}
