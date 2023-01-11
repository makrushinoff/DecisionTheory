package lab1;

public class NumberPair {
    private int q1;
    private int q2;
    private boolean paretoDominated;
    private boolean slaterDominated;

    public NumberPair(int q1, int q2, boolean paretoDominated, boolean slaterDominated) {
        this.q1 = q1;
        this.q2 = q2;
        this.paretoDominated = paretoDominated;
        this.slaterDominated = slaterDominated;
    }

    public int getQ1() {
        return q1;
    }

    public void setQ1(int q1) {
        this.q1 = q1;
    }

    public int getQ2() {
        return q2;
    }

    public void setQ2(int q2) {
        this.q2 = q2;
    }

    public boolean isParetoDominated() {
        return paretoDominated;
    }

    public void setParetoDominated(boolean paretoDominated) {
        this.paretoDominated = paretoDominated;
    }

    public boolean isSlaterDominated() {
        return slaterDominated;
    }

    public void setSlaterDominated(boolean slaterDominated) {
        this.slaterDominated = slaterDominated;
    }

    @Override
    public String toString() {
        return "NumberPair{" + "q1=" + q1 + ", q2=" + q2 + ", paretoDominated=" + paretoDominated + ", slaterDominated=" + slaterDominated + "}\n";
    }
}
