import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int n;
    private final boolean[][] grid;

    private int openSites = 0;
    private final WeightedQuickUnionUF quickUnionTop;
    private final WeightedQuickUnionUF quickUnionTopAndBottom;

    private final int virtualTop;
    private final int virtualBottom;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be greater than 0");
        }

        this.n = n;

        grid = new boolean[n][n];

        int sites = n * n;

        quickUnionTop = new WeightedQuickUnionUF(sites + 1);
        quickUnionTopAndBottom = new WeightedQuickUnionUF(sites + 2);

        virtualTop = sites;
        virtualBottom = sites + 1;

        connectTopToVirtualTop();
    }

    private void connectTopToVirtualTop() {
        for (int col = 0; col < n; col++) {
            int index = xyTo1D(1, col + 1);
            quickUnionTop.union(virtualTop, index);
            quickUnionTopAndBottom.union(virtualTop, index);
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        checkBounds(row, col);

        if (isOpen(row, col)) {
            return;
        }

        grid[row - 1][col - 1] = true;
        openSites++;

        int index = xyTo1D(row, col);

        if (row == n) {
            quickUnionTopAndBottom.union(virtualBottom, index);
        }

        // up
        if (row > 1 && isOpen(row - 1, col)) {
            int adjacent = index - n;
            quickUnionTop.union(adjacent, index);
            quickUnionTopAndBottom.union(adjacent, index);
        }

        // right
        if (col < n && isOpen(row, col + 1)) {
            int adjacent = index + 1;
            quickUnionTop.union(adjacent, index);
            quickUnionTopAndBottom.union(adjacent, index);
        }

        // down
        if (row < n && isOpen(row + 1, col)) {
            int adjacent = index + n;
            quickUnionTop.union(adjacent, index);
            quickUnionTopAndBottom.union(adjacent, index);
        }

        // left
        if (col > 1 && isOpen(row, col - 1)) {
            int adjacent = index - 1;
            quickUnionTop.union(adjacent, index);
            quickUnionTopAndBottom.union(adjacent, index);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkBounds(row, col);
        return grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        checkBounds(row, col);

        if (!isOpen(row, col)) {
            return false;
        }

        int index = xyTo1D(row, col);
        return quickUnionTop.find(index) == quickUnionTop.find(virtualTop);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return quickUnionTopAndBottom.find(virtualTop) == quickUnionTopAndBottom.find(virtualBottom);
    }

    private void checkBounds(int row, int col) {
        if (row <= 0 || row > n || col <= 0 || col > n) {
            throw new IllegalArgumentException();
        }
    }

    private int xyTo1D(int row, int col) {
        return n * (row - 1) + col - 1;
    }
}
