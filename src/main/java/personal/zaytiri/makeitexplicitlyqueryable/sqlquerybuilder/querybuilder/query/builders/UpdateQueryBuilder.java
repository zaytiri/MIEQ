package personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.query.builders;

import personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.query.enums.Clause;
import personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.query.GenericClauses;
import personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.query.enums.Operators;
import personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.query.QueryBuilder;
import personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.query.base.IGenericClauses;
import personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.schema.Column;
import personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.schema.Table;

import java.sql.Connection;
import java.util.Map;

public class UpdateQueryBuilder extends QueryBuilder implements IGenericClauses<UpdateQueryBuilder> {

    private final GenericClauses genericClauses;

    public UpdateQueryBuilder(Connection connection) {
        super(connection);
        genericClauses = new GenericClauses(this);
    }

    @Override
    public UpdateQueryBuilder and(int numberOfConditions) {
        genericClauses.and(numberOfConditions);
        return this;
    }

    @Override
    public UpdateQueryBuilder and() {
        genericClauses.and(0);
        return this;
    }

    @Override
    public UpdateQueryBuilder and(Object value) {
        genericClauses.and(query, values, value);
        return this;
    }

    @Override
    public UpdateQueryBuilder between(Object value) {
        genericClauses.between(query, values, value);
        return this;
    }

    @Override
    public UpdateQueryBuilder or(int numberOfConditions) {
        genericClauses.or(numberOfConditions);
        return this;
    }

    @Override
    public UpdateQueryBuilder or() {
        genericClauses.or(0);
        return this;
    }

    @Override
    public UpdateQueryBuilder where(Column leftColumn, Operators operator, Object rightColumn) {
        genericClauses.where(query, values, leftColumn, operator, rightColumn);
        return this;
    }

    @Override
    public UpdateQueryBuilder where(Column leftColumn, Operators operator, Column rightColumn) {
        genericClauses.where(query, leftColumn, operator, rightColumn);
        return this;
    }

    @Override
    public UpdateQueryBuilder where(Column leftColumn, Operators operator) {
        genericClauses.where(query, leftColumn, operator);
        return this;
    }

    @Override
    public UpdateQueryBuilder where(Column leftColumn) {
        genericClauses.where(query, leftColumn);
        return this;
    }

    /**
     * Generates a partial SQL query to set a value to a specific column.
     * It is an alternative to values() method.
     * This method will set only 1 value at a time, and it could be used multiple times. But in this case it's better to use values() method.
     *
     * @param column   to be set a new value.
     * @param newValue to be updated in a column.
     * @returns UpdateQueryBuilder
     */
    public UpdateQueryBuilder set(Column column, Object newValue) {
        if (!tryAppendKeyword(Clause.SET.value)) {
            query.append(", ");
        }

        query.append(column.getName()).append("=? ");
        values.add(newValue);
        return this;
    }

    /**
     * Generates a partial SQL query to update a table's row/rows.
     * This method should be the entry point of the SQL update query.
     * It should be followed up by set() OR values() method.
     *
     * @param table to be updated.
     * @return
     */
    public UpdateQueryBuilder update(Table table) {
        resetQuery();
        tryAppendKeyword(Clause.UPDATE.value);
        query.append(table.getName());
        return this;
    }

    /**
     * Generates a partial SQL query to set multiple values to their respective column.
     * It is an alternative to set() method.
     * It will generate the query iterating through every column-pair defined. If only 1 value is needed, it's better to use set() method.
     *
     * @param sets which are multiple column-value pairs to be updated.
     * @returns UpdateQueryBuilder
     */
    public UpdateQueryBuilder values(Map<Column, Object> sets) {
        if (!tryAppendKeyword(Clause.SET.value)) {
            return this;
        }

        boolean comma = false;
        for (Map.Entry<Column, Object> entry : sets.entrySet()) {
            if (comma) {
                query.append(", ");
            }

            query.append(entry.getKey().getName()).append("=? ");
            values.add(entry.getValue());
            comma = true;
        }

        return this;
    }
}
