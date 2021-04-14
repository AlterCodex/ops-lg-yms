CREATE TABLE some_object (
        task_id TEXT NOT NULL,
        status TEXT,
        barcode TEXT,
        order_id TEXT NOT NULL,
        order_index INTEGER,
        close_date DATE,
        warehouse TEXT NOT NULL,
        wave_label TEXT NOT NULL,
        wave_type TEXT NOT NULL,
        sku TEXT NOT NULL,
        product_name TEXT,
        unit TEXT,
        quantity DECIMAL,
        zone_id TEXT,
        route NUMERIC,
        stop NUMERIC,
	    etd TIMESTAMP,
        priority NUMERIC,
        location TEXT,
        customer_id TEXT,
        customer_name TEXT,
        source TEXT NOT NULL,
	    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT some_object_pk PRIMARY KEY (task_id, warehouse)
);

CREATE INDEX warehouse_index ON some_object(warehouse, task_id);
CREATE INDEX tasks_index ON some_object(warehouse, close_date, wave_type);
