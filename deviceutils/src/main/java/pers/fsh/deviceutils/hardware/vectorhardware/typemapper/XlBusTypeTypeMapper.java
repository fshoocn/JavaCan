package pers.fsh.deviceutils.hardware.vectorhardware.typemapper;

import com.sun.jna.FromNativeContext;
import com.sun.jna.ToNativeContext;
import com.sun.jna.TypeConverter;

/**
 * @author fanshuhua
 * @date 2025/5/13 19:48
 */
public class XlBusTypeTypeMapper implements TypeConverter {
    @Override
    public Object fromNative(Object value, FromNativeContext context) {
        if (value instanceof Number) {
            return XlBusType.values()[((Number) value).intValue()];
        }
        return null;
    }

    @Override
    public Object toNative(Object value, ToNativeContext context) {
        if (value instanceof XlBusType type) {
            return type.getValue();
        }
        return value;
    }

    @Override
    public Class<?> nativeType() {
        return Integer.class;
    }
}
