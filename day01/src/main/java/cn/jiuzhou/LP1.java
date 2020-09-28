package cn.jiuzhou;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

/**
 * @Description
 * max  X₁ + 2X₂ + 3X₃
 * s.t.
 *      -X₁ + X₂ + X₃ <= 20
 *      X₁ - 3X₂ + X₃ <= 30
 *      0 <= X₁ <= 40
 *
 * 相关文档说明：
 * public IloNumVar[] numVarArray(int n,
 *                       double lb,
 *                       double ub)
 *                         throws IloException
 * Creates an array of numeric variables, all with the same upper and lower bound.
 * See IloModeler.numVarArray(int, double, double)
 * Specified by:
 * numVarArray in interface IloModeler
 * Throws:
 * IloException
 * Parameters:
 * n - Length of the new array; that is, number of new numeric variables.
 * lb - The lower bound of each new variable.
 * ub - The upper bound of each new variable.
 * Returns:
 * The array new modeling variables.
 *
 *
 * public IloObjective addMaximize(IloNumExpr expr)
 *                          throws IloException
 * Creates and returns an objective to maximize the expression and adds it to the invoking model.
 * See IloModeler.addMaximize(IloNumExpr)
 * Do not use this method in a callback to modify the model currently being optimized.
 *
 * Specified by:
 * addMaximize in interface IloModeler
 * Throws:
 * IloException
 * Parameters:
 * expr - Expression to maximize.
 * Returns:
 * An IloObjective object representing the objective to maximize expr.
 *
 *
 * public IloLinearNumExpr scalProd(IloNumVar[] vars,
 *                         double[] vals)
 *                           throws IloException
 * Creates and returns a linear expression representing the scalar product of the provided variables with the provided values.
 * See IloModeler.scalProd(IloNumVar[], double[])
 * Specified by:
 * scalProd in interface IloModeler
 * Throws:
 * IloException
 * Parameters:
 * vars - The variables involved in the new scalar product expression.
 * vals - The values involved in the new scalar product expression.
 * Returns:
 * The new linear expression.
 *
 *
 * @Author wangjiuzhou (835540436@qq.com)
 * @Date 2020-09-26 21:32
 */
public class LP1 {

    public static void main(String[] args) {
        try {
            //模型
            IloCplex cplex = new IloCplex();

            //下限
            double[] lb = {0.0, 0.0, 0.0};
            //上限
            double[] ub = {40.0, Double.MAX_VALUE, Double.MAX_VALUE};
            //变量个数：3 变量下限：lb 变量上限：ub
            IloNumVar[] x = cplex.numVarArray(3, lb, ub);

            //变量倍数
            double[] objvals = {1.0, 2.0, 3.0};
            //目标函数表达式
            IloLinearNumExpr iloNumExpr = cplex.scalProd(x, objvals);
            System.out.println(iloNumExpr);
            //优化目标
            cplex.addMaximize(iloNumExpr);

            //s.t. 两个约束
            double[] coeff1 = {-1.0, 1.0, 1.0};
            double[] coeff2 = {1.0, -3.0, 1.0};

            cplex.addLe(cplex.scalProd(x, coeff1), 20.0);
            cplex.addLe(cplex.scalProd(x, coeff2), 30.0);

            //开始求解
            if (cplex.solve()) {
                cplex.output().println("Solution status = " + cplex.getStatus());
                cplex.output().println("Solution value = " + cplex.getObjValue());
                //打印优化结果
                double[] val = cplex.getValues(x);
                for (int j = 0; j < val.length; j++) {
                    cplex.output().println("x" + (j+1) + "  = " + val[j]);
                }
            }
            cplex.end();

        } catch (IloException e) {
            System.err.println("Concert exception caught: " + e);
        }

    }
}
