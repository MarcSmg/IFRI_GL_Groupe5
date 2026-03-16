package utilities;

public enum DbTable {
    USER("users"),
    ACT("administrative_act"),
    DEMAND("demand");

    private final String tableName;
    DbTable(String tableName) {
        this.tableName = tableName;
    }

    public String getName() { return tableName; }
}
