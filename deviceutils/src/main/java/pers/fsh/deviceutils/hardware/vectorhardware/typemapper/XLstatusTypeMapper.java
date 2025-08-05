package pers.fsh.deviceutils.hardware.vectorhardware.typemapper;

import com.sun.jna.FromNativeContext;
import com.sun.jna.ToNativeContext;
import com.sun.jna.TypeConverter;

/**
 * @author fanshuhua
 * @date 2025/5/12 20:44
 */


public class XLstatusTypeMapper implements TypeConverter {
    @Override
    public Object fromNative(Object value, FromNativeContext fromNativeContext) {
        if (value instanceof Number) {
            return XLstatus.valueOf(((Number) value).intValue());
        }
        return null;
    }

    @Override
    public Object toNative(Object value, ToNativeContext toNativeContext) {
        if (value instanceof XLstatus) {
            return ((XLstatus) value).getCode();
        }
        return value;
    }

    @Override
    public Class<?> nativeType() {
        return Integer.class;
    }
}