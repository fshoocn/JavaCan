package pers.fsh.deviceutils.hardware.vectorhardware.typemapper;

/**
 * @author fanshuhua
 * @date 2025/5/13 20:08
 */
public enum XlInterfaceVersion {
    XL_INTERFACE_VERSION_V2(2),
    XL_INTERFACE_VERSION_V3(3),
    XL_INTERFACE_VERSION_V4(4);

    private final int version;

    XlInterfaceVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }
}
