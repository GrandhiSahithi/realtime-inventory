package com.example.rtinv.repo;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.example.rtinv.model.PriceRecord;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;

@Repository
public class PriceRepo {

    private final CqlSession session;
    private final PreparedStatement getStmt;

    public PriceRepo(CqlSession session) {
        this.session = session;
        this.getStmt = session.prepare(
                "SELECT region_id, product_id, price, currency, updated_at " +
                "FROM rtinv.price_by_region WHERE region_id = ? AND product_id = ?"
        );
    }

    public PriceRecord get(String regionId, String productId) {
        BoundStatement bs = getStmt.bind(regionId, productId);
        Row row = session.execute(bs).one();
        if (row == null) return null;
        return new PriceRecord(
                row.getString("region_id"),
                row.getString("product_id"),
                row.getBigDecimal("price"),
                row.getString("currency"),
                row.getInstant("updated_at")
        );
    }

    /** Upsert helper for consumers/tests. */
    public void upsert(String regionId, String productId, BigDecimal price, String currency, Instant updatedAt) {
        session.execute(
            SimpleStatement.newInstance(
                "INSERT INTO rtinv.price_by_region (region_id, product_id, price, currency, updated_at) VALUES (?,?,?,?,?)",
                regionId, productId, price, currency, updatedAt
            )
        );
    }
}
