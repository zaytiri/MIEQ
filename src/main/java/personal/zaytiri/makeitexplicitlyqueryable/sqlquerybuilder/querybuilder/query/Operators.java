package personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.query;

public enum Operators {
    EQUALS("="),
    NOT_EQUAL("!="),
    GREATER_THAN(">"),
    LESS_THAN("<"),
    GREATER_OR_EQUAL_THAN(">="),
    LESS_OR_EQUAL_THAN("<="),
    IN("IN"),
    NOT_IN("NOT IN"),
    LIKE("LIKE"),
    IS_NULL("IS NULL"),
    IS_NOT_NULL("IS NOT NULL"),
    NULL("NULL"),
    NOT_NULL("NOT NULL"),
    BETWEEN("BETWEEN"),
    NOT_BETWEEN("NOT BETWEEN");

    public final String value;

    Operators(String value) {
        this.value = value;
    }

    public static Operators get(String text) {
        for (Operators b : Operators.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
        throw new IllegalArgumentException("No constant with value " + text + " found.");
    }
}