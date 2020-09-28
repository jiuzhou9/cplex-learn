package cn.jiuzhou.ido;

import cn.jiuzhou.ido.model.Item;
import ilog.concert.IloException;
import ilog.concert.IloNumVar;
import ilog.concert.IloObjective;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;

/**
 * @Description
 * @Author wangjiuzhou (835540436@qq.com)
 * @Date 2020-09-28 22:56
 */
public class Demo03 {

    public static void main(String[] args) {
        Item itemA = new Item().setName("A").setProductCost(1).setPurchaseCost(1).setDemand(1);
        Item itemB = new Item().setName("B").setProductCost(1).setPurchaseCost(1).setDemand(1);
        Item itemC = new Item().setName("C").setProductCost(1).setPurchaseCost(1).setDemand(1);

        Item[] items = {itemA, itemB, itemC};

        //总产能
        double[] capacaity = {1};

        IloNumVar[] product = new IloNumVar[items.length];
        IloNumVar[] purchase = new IloNumVar[items.length];

        try {
            IloCplex cplex = new IloCplex();
            IloObjective iloObjective = cplex.addMinimize();

            for (int i = 0; i < items.length; i++) {
                IloRange demandIloRange =
                        cplex.addRange(items[i].getDemand(), items[i].getDemand());

                product[i] =
                        cplex.numVar(
                                cplex.column(iloObjective, items[i].getProductCost())
                                        .and(cplex.column(demandIloRange, 1.)),
                                0, Double.MAX_VALUE);

                purchase[i] =
                        cplex.numVar(
                                cplex.column(iloObjective, items[i].getPurchaseCost())
                                        .and(cplex.column(demandIloRange, 1.)),
                                0, Double.MAX_VALUE);
            }

            for (int i = 0; i < items.length; i++) {

            }

        } catch (IloException e) {
            e.printStackTrace();
        }

    }
}
