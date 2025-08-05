package pers.fsh.cddutils.model.vo.parameter;

/**
 * @author fanshuhua
 * @date 2025/7/31 17:27
 */
public interface ValueInterface<T> {
    T setValue(long value);

    byte[] getValue();
}
