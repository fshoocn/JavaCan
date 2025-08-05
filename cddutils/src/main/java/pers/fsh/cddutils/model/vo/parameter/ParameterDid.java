package pers.fsh.cddutils.model.vo.parameter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fanshuhua
 * @date 2025/7/29 15:16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public final class ParameterDid extends ParameterProxy {
    private long did; // DID值

}
