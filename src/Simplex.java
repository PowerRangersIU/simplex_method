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
            table[i + 1][n_vars + i] = 1; // Slack variables
            table[i + 1][n_vars + n_constraints] = b[i]; // RHS value
        }

        System.out.println("\n2. Initialize:");
        System.out.println("- Form the initial tableau by introducing slack variables to convert inequalities into equalities.");
        printTable(table);

        System.out.println("\n3. Iteratively apply the Simplex method:");

        while (Arrays.stream(table[0]).limit(n_vars).anyMatch(x -> x <- -eps)) {
            int pivot_column = findPivotColumn(table);
            //if solution is optimal: pivot_column == -1

            System.out.println("Pivot column: " + pivot_column);

            double[] ratios = new double[n_constraints];
            Arrays.fill(ratios, Double.POSITIVE_INFINITY);
            for (int i = 0; i < n_constraints; i++) {
                if (table[i + 1][pivot_column] > eps) {
                    ratios[i] = table[i + 1][n_vars + n_constraints] / table[i + 1][pivot_column]; // RHS value / pivot column
                }
            }

            int pivot_row = findPivotRow(table, ratios);
            //if solution is unbounded: pivot_row == -1

            System.out.println("Pivot row: " + pivot_row);

            pivotOperation(table, pivot_row, pivot_column, n_constraints);
            printTable(table);
        }
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

    public static void pivotOperation(double[][] table, int pivot_row, int pivot_column, int n_constraints) {
        double pivot_element = table[pivot_row][pivot_column];

        for (int i = 0; i < table[pivot_row].length; i++) {
            table[pivot_row][i] /= pivot_element;
        }

        for (int i = 0; i < n_constraints + 1; i++) {
            if (i != pivot_row) {
                double multiplier = table[i][pivot_column];
                for (int j = 0; j < table[i].length; j++) {
                    table[i][j] -= multiplier * table[pivot_row][j];
                }
            }
        }
    }

    public static void printTable(double[][] table) {
        for (double[] row : table) {
            System.out.println(Arrays.toString(row));
        }
    }

    public static int findPivotColumn(double[][] table) {
        int pivot_column = -1;  // Initialize as -1 to handle cases when no negative values are found
        for (int i = 0; i < table[0].length - 1; i++) {
            // Find the most negative coefficient in the objective row
            if (table[0][i] < 0 && (pivot_column == -1 || table[0][i] < table[0][pivot_column])) {
                pivot_column = i;
            }
        }
        // Return the index of the entering variable, or -1 if all coefficients are non-negative (optimal solution)
        return pivot_column;
    }

    // Find the pivot row (row with the smallest positive ratio)
    public static int findPivotRow(double[][] table, double[] ratios) {
        int pivot_row = -1;
        double min_ratio = Double.POSITIVE_INFINITY;
        for (int i = 0; i < ratios.length; i++) {
            if (ratios[i] > 0 && ratios[i] < min_ratio) {
                min_ratio = ratios[i];
                pivot_row = i + 1; // Offset by 1 because the first row is the objective row
            }
        }
        return pivot_row;
    }
}
