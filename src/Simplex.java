import java.util.Arrays;

public class Simplex {
    public static void main(String[] args) {
        double[] C = {2, 3, 4};
        double[][] A = {
                {1, 1, 0},
                {0, 1, 1},
                {1, 2, 3}
        };
        double[] b = {5, 8, 15};
        double eps = 1e-9;
        simplex(C, A, b, eps);
    }

    public static void simplex(double[] C, double[][] A, double[] b, double eps) {
        // Output information about the function
        System.out.println("Function_name(C, A, b, eps = " + eps + ")");
        System.out.println("\nInput:");
        System.out.println("- C: A vector of coefficients of the objective function");
        System.out.println("- A: A matrix of coefficients of the constraint functions");
        System.out.println("- b: A vector of right-hand side values");
        System.out.println("- eps: Approximation accuracy (optional, default = " + eps + ")\n");

        System.out.println("Steps:");

        // Print the optimization problem:
        printLpp(C, A, b);

        // Initialization of length variables
        int n_vars = C.length;
        int n_constraints = A.length;
        double[][] table = new double[n_constraints + 1][n_vars + n_constraints + 1];

        // Z string initialization
        for (int i = 0; i < n_vars; i++) {
            table[0][i] = -C[i];
        }

        // Filling in the other lines
        for (int i = 0; i < n_constraints; i++) {
            for (int j = 0; j < n_vars; j++) {
                table[i + 1][j] = A[i][j];
            }
            table[i + 1][n_vars + n_constraints] = b[i];
        }

        System.out.println("\n2. Initialize:");
        System.out.println("- Form the initial tableau by introducing slack variables to convert inequalities into equalities.");


        System.out.println("\n3. Iteratively apply the Simplex method:");
    }

    public static void printLpp(double[] C, double[][] A, double[] b) {
        StringBuilder problem = new StringBuilder();
        problem.append("1. Print the optimization problem:\n");
        problem.append("   - max z = ").append(C[0]).append(" * x1");
        for (int i = 1; i < C.length; i++) {
            if (C[i] >= 0) {
                problem.append(" + ").append(C[i]).append(" * x").append(i + 1);
            } else {
                problem.append(" - ").append(-C[i]).append(" * x").append(i + 1);
            }
        }

        problem.append("\n   - subject to the constraints:\n");
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[i].length; j++) {
                problem.append(A[i][j]).append(" * x").append(j + 1);
                if (j < A[i].length - 1) {
                    problem.append(" + ");
                }
            }
            problem.append(" <= ").append(b[i]).append("\n");
        }

        System.out.println(problem);
    }
}
