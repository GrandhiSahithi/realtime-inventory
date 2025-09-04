CREATE KEYSPACE IF NOT EXISTS rtinv
  WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

CREATE TABLE IF NOT EXISTS rtinv.inventory_by_store (
  store_id text,
  product_id text,
  quantity int,
  updated_at timestamp,
  PRIMARY KEY ((store_id), product_id)
);

CREATE TABLE IF NOT EXISTS rtinv.price_by_region (
  region_id text,
  product_id text,
  price decimal,
  currency text,
  updated_at timestamp,
  PRIMARY KEY ((region_id), product_id)
);
