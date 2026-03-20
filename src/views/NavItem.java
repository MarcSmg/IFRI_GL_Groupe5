package views;

public class NavItem {
    private String label;
    private Runnable action;

    public NavItem(String label, Runnable action) {
        this.label = label;
        this.action = action;
    }

    public String getLabel() {
        return label;
    }

    public Runnable getAction() {
        return action;
    }
}
