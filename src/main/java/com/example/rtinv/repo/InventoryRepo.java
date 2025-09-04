package com.example.rtinv.repo;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.example.rtinv.model.InventoryRecord;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public class InventoryRepo {

    private final CqlSession session;
    private final PreparedStatement getStmt;

    public InventoryRepo(CqlSession session) {
        this.session = session;
        this.getStmt = session.prepare(
                "SELECT store_id, product_id, quantity, updated_at " +
                "FROM rtinv.inventory_by_store WHERE store_id = ? AND product_id = ?"
        );
    }

    public InventoryRecord get(String storeId, String productId) {
        BoundStatement bs = getStmt.bind(storeId, productId);
        Row row = session.execute(bs).one();
        if (row == null) return null;
        return new InventoryRecord(
                row.getString("store_id"),
                row.getString("product_id"),
                row.getInt("quantity"),
                row.getInstant("updated_at")
        );
    }

    /** Upsert helper for consumers/tests. */
    public void upsert(String storeId, String productId, int qty, Instant updatedAt) {
        session.execute(
            SimpleStatement.newInstance(
                "INSERT INTO rtinv.inventory_by_store (store_id, product_id, quantity, updated_at) VALUES (?,?,?,?)",
                storeId, productId, qty, updatedAt
            )
        );
    }
}
