package pers.fsh.cddutils.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import pers.fsh.cddutils.model.po.base.Name;

import java.util.List;

/**
 * @author fanshuhua
 * @date 2025/7/19 16:23
 */
@Data
@Accessors(chain = true)
public class DiagInstVo {
    private List<DiagVo> diagVoList; // 诊断服务列表
    private Name name; // ECU名称

}
