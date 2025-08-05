package pers.fsh.cddutils.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import pers.fsh.cddutils.model.po.base.Name;
import pers.fsh.cddutils.model.vo.parameter.ParameterInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fanshuhua
 * @date 2025/7/19 15:13
 */
@Data
@Accessors(chain = true)
public class DiagVo {

    private List<ParameterInterface> req = new ArrayList<>(); // 请求ID列表
    private List<ParameterInterface> pos = new ArrayList<>(); // 正应答ID列表
    private List<ParameterInterface> neg = new ArrayList<>(); // 负应答ID列表

    private boolean phys; // 是否支持
    private boolean func; // 是否支持
    private Name name; // 名称
    private String qualifiers; // 限定符
    private String id; // 唯一标识符

    private boolean active = false; // 是否启用

    public DiagVo addReqIds(ParameterInterface reqId) {
        this.req.add(reqId);
        return this;
    }

    public DiagVo addPosIds(ParameterInterface posId) {
        this.pos.add(posId);
        return this;
    }

    public DiagVo addNegIds(ParameterInterface negId) {
        this.neg.add(negId);
        return this;
    }
}
