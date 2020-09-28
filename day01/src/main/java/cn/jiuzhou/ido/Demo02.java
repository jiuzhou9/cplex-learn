package cn.jiuzhou.ido;

import ilog.concert.IloException;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

/**
 * @Description
 * @Author wangjiuzhou (835540436@qq.com)
 * @Date 2020-09-26 23:34
 */
public class Demo02 {

    public static void main(String[] args) throws IloException {
        IloCplex cplex = new IloCplex();

        //变量:3个 以及 区间 1-2
        IloNumVar[] x1 = cplex.numVarArray(3, 1, 2);;
        //倍数
        double[] x2 = {1, 2, 1};
        //表达式
        IloNumExpr iloNumExpr = cplex.scalProd(x1, x2);
        System.out.println(iloNumExpr);
        //优化目标
        cplex.addMaximize(iloNumExpr);
        //是否优化成功
        boolean solve = cplex.solve();
        System.out.println("是否求解完：" + solve);
        //变量... 各自的值
//        double value = cplex.getValue(x1);
//        System.out.println("变量1的值：" + value);
//        double value1 = cplex.getValue(x2);
//        System.out.println("变量2的值：" + value1);
        //目标结果
        double objValue = cplex.getObjValue();
        System.out.println("目标表达式结果：" + objValue);

    }
}
