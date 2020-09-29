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

    static Item itemA = new Item().setName("A").setProductCost(1).setPurchaseCost(2).setDemand(3).setCapacityCost(0.1);
    static Item itemA1 = new Item().setName("A 子件").setProductCost(0).setPurchaseCost(1).setDemand(6).setCapacityCost(0);
    static Item itemB = new Item().setName("B").setProductCost(1).setPurchaseCost(1).setDemand(1).setCapacityCost(0.2);
    static Item itemC = new Item().setName("C").setProductCost(1).setPurchaseCost(2).setDemand(1).setCapacityCost(1);

    static Item[] items = {itemA, itemA1, itemB, itemC};


    public static void main(String[] args) {

        //总产能
        double capacaity = 1;

        IloNumVar[] product = new IloNumVar[items.length];
        IloNumVar[] purchase = new IloNumVar[items.length];
        double[] capacaityCost = new double[items.length];

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

                capacaityCost[i] = items[i].getCapacityCost();
            }


            cplex.addLe(cplex.scalProd(capacaityCost, product), capacaity);

            cplex.solve();

            if ( !cplex.getStatus().equals(IloCplex.Status.Optimal) ) {
                System.out.println("No optimal solution found");
                return;
            }
            System.out.println("Solution status: " + cplex.getStatus());
            displayResults(cplex, product, purchase);
            System.out.println("----------------------------------------");
            cplex.end();

        } catch (IloException e) {
            e.printStackTrace();
        }

    }


    //打印结果
    static void displayResults(IloCplex cplex,
                               IloNumVar[] product,
                               IloNumVar[] purchase) throws IloException {
        System.out.println("cost: " + cplex.getObjValue());

        for(int p = 0; p < items.length; p++) {
            System.out.println(items[p].getName());
            System.out.println("product:  " + cplex.getValue(product[p]));
            System.out.println("purchase: " + cplex.getValue(purchase[p]));
        }
    }
}
