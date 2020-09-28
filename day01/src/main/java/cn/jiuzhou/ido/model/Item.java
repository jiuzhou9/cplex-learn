package cn.jiuzhou.ido.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description
 * @Author wangjiuzhou (835540436@qq.com)
 * @Date 2020-09-28 22:50
 */
@Data
@Accessors(chain = true)
public class Item {
    private String name;

    private double productCost;

    private double purchaseCost;

    private double demand;

    private double capacityCost;
}
