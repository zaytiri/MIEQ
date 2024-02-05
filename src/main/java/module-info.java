module MIEQ.main {
    requires org.json;
    requires java.sql;

    exports personal.zaytiri.makeitexplicitlyqueryable.pairs;
    exports personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.query.builders;
    exports personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.schema;
    exports personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.response;
}