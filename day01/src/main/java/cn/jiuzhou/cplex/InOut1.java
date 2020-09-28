package cn.jiuzhou.cplex;

import ilog.concert.IloException;
import ilog.concert.IloNumVar;
import ilog.concert.IloObjective;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;

/**
 * A company has to produce 3 products, using 2 resources.
 * Each resource has a limited capacity.
 * Each product consumes a given number of machines.
 * Each product has a production cost (the inside cost).
 * Both products can also be purchased outside the company at a given
 * cost (the outside cost).
 *
 * Minimize the total cost so that the company exactly meets the
 * demand.
 *
 * @Description
 * @Author wangjiuzhou (835540436@qq.com)
 * @Date 2020-09-26 22:10
 */
public class InOut1 {

    //产品种类：3
    static int _nbProds = 3;
    //资源种类：2
    static int _nbResources = 2;
    //每种产品消耗的机器情况
    static double[][] _consumption = {{0.5, 0.4, 0.3},
            {0.2, 0.4, 0.6}};
    //需求量
    static double[] _demand = {100.0, 200.0, 300.0};
    //产能
    static double[] _capacity = {20.0, 40.0};
    //内部生产消耗成本
    static double[] _insideCost = {0.6, 0.8, 0.3};
    //外部采购消耗成本
    static double[] _outsideCost = {0.8, .09, 0.4};

    //打印结果
    static void displayResults(IloCplex cplex,
                               IloNumVar[] inside,
                               IloNumVar[] outside) throws IloException {
        System.out.println("cost: " + cplex.getObjValue());

        for(int p = 0; p < _nbProds; p++) {
            System.out.println("P" + p);
            System.out.println("inside:  " + cplex.getValue(inside[p]));
            System.out.println("outside: " + cplex.getValue(outside[p]));
        }
    }

    public static void main( String[] args ) {
        try {
            IloCplex cplex = new IloCplex();

            //内部生产集合、外部采购集合
            IloNumVar[]  inside = new IloNumVar[_nbProds];
            IloNumVar[] outside = new IloNumVar[_nbProds];

            //优化目标成本最低
            IloObjective obj = cplex.addMinimize();

            // Must meet demand for each product

            //必须满足每种产品的需求量
            for(int p = 0; p < _nbProds; p++) {
                //需求量范围
                IloRange demRange = cplex.addRange(_demand[p], _demand[p]);
                //obj 代表目标函数， _insideCost[p]线性系数
                inside[p] = cplex.numVar(cplex.column(obj, _insideCost[p]).and(
                        //demRange 代表范围 1代表线性系数
                        cplex.column(demRange, 1.)),
                        // 下限：0 上限：max_value
                        0., Double.MAX_VALUE);

                outside[p] = cplex.numVar(cplex.column(obj, _outsideCost[p]).and(
                        cplex.column(demRange, 1.)),
                        0., Double.MAX_VALUE);
            }

            // Must respect capacity constraint for each resource
            // 必须考虑每种资源的产能上限
            for(int r = 0; r < _nbResources; r++) {
                //consumption₁ * inside₁ + consumption₂ * inside₂ + consumption₃ * inside₃ <= capacity₁
                cplex.addLe(cplex.scalProd(_consumption[r], inside), _capacity[r]);
            }

            cplex.solve();

            if ( !cplex.getStatus().equals(IloCplex.Status.Optimal) ) {
                System.out.println("No optimal solution found");
                return;
            }
            System.out.println("Solution status: " + cplex.getStatus());
            displayResults(cplex, inside, outside);
            System.out.println("----------------------------------------");
            cplex.end();
        }
        catch (IloException exc) {
            System.err.println("Concert exception '" + exc + "' caught");
        }

    }
}
