public class ComplexMatrix {
    private Complex[][] data;
    private int rows, cols;

    public ComplexMatrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        data = new Complex[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = new Complex(0, 0);
            }
        }
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }

    public void set(int r, int c, Complex val) { data[r][c] = val; }
    public Complex get(int r, int c) { return data[r][c]; }

    public ComplexMatrix add(ComplexMatrix other) {
        if (rows != other.rows || cols != other.cols) {
            throw new IllegalArgumentException("размеры не совпадают");
        }
        ComplexMatrix res = new ComplexMatrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                res.data[i][j] = data[i][j].add(other.data[i][j]);
            }
        }
        return res;
    }

    public ComplexMatrix multiply(ComplexMatrix other) {
        if (cols != other.rows) {
            throw new IllegalArgumentException("несовместимые размеры");
        }
        ComplexMatrix res = new ComplexMatrix(rows, other.cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < other.cols; j++) {
                Complex sum = new Complex(0, 0);
                for (int k = 0; k < cols; k++) {
                    sum = sum.add(data[i][k].mul(other.data[k][j]));
                }
                res.data[i][j] = sum;
            }
        }
        return res;
    }

    public ComplexMatrix transpose() {
        ComplexMatrix res = new ComplexMatrix(cols, rows);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                res.data[j][i] = data[i][j];
            }
        }
        return res;
    }

    public Complex determinant() {
        if (rows != cols) {
            throw new IllegalArgumentException("нужна квадратная матрица");
        }
        return calcDet(data, rows);
    }

    private Complex calcDet(Complex[][] mat, int size) {
        if (size == 1) return mat[0][0];
        if (size == 2) {
            return mat[0][0].mul(mat[1][1]).sub(mat[0][1].mul(mat[1][0]));
        }
        
        Complex det = new Complex(0, 0);
        for (int col = 0; col < size; col++) {
            Complex[][] minor = new Complex[size - 1][size - 1];
            for (int i = 1; i < size; i++) {
                int cIdx = 0;
                for (int j = 0; j < size; j++) {
                    if (j == col) continue;
                    minor[i - 1][cIdx] = mat[i][j];
                    cIdx++;
                }
            }
            Complex term = mat[0][col].mul(calcDet(minor, size - 1));
            if (col % 2 != 0) {
                term = term.mul(new Complex(-1, 0)); 
            }
            det = det.add(term);
        }
        return det;
    }

    public ComplexMatrix inverse() {
        if (rows != cols) {
            throw new IllegalArgumentException("нужна квадратная матрица");
        }
        int n = rows;
        Complex[][] aug = new Complex[n][2 * n];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                aug[i][j] = data[i][j];
                aug[i][n + j] = (i == j) ? new Complex(1, 0) : new Complex(0, 0);
            }
        }

        for (int col = 0; col < n; col++) {
            int pivotRow = -1;
            for (int row = col; row < n; row++) {
                if (!aug[row][col].isZero()) {
                    pivotRow = row;
                    break;
                }
            }
            if (pivotRow == -1) {
                throw new ArithmeticException("матрица вырождена");
            }

            Complex[] temp = aug[col];
            aug[col] = aug[pivotRow];
            aug[pivotRow] = temp;

            Complex lead = aug[col][col];
            for (int j = 0; j < 2 * n; j++) {
                aug[col][j] = aug[col][j].div(lead);
            }

            for (int row = 0; row < n; row++) {
                if (row == col) continue;
                Complex factor = aug[row][col];
                for (int j = 0; j < 2 * n; j++) {
                    aug[row][j] = aug[row][j].sub(factor.mul(aug[col][j]));
                }
            }
        }

        ComplexMatrix res = new ComplexMatrix(n, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                res.data[i][j] = aug[i][n + j];
            }
        }
        return res;
    }

    public ComplexMatrix divide(ComplexMatrix other) {
        return multiply(other.inverse());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sb.append(data[i][j]);
                if (j < cols - 1) sb.append("\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}