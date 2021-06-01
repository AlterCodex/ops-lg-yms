-- TESTS: some object 1 --
INSERT INTO some_object (
  task_id, status, order_id, order_index,
  close_date, warehouse, wave_label,
  wave_type, sku, product_name, unit,
  quantity, zone_id, route, stop, etd,
  priority, location, customer_id,
  customer_name, source, created_at,
  updated_at
) VALUES (
    'TSK0041377', NULL, '362088', 51, '2020-05-22',
    'PA', 'OLA0000432', 'OLA1', 'BOG-FRU1-CAT8-335:454:1158:1159',
    'Arroz Florhuila 12.5kg Paquete 25 unidades x 500g Paquete',
    'UNID', 300.3, NULL, 6, 21, '2020-05-22 12:18:08',
    1234567, 'MEH00321', 'supercalle60asur',
    'super éxito', 'WMS', '2020-06-05 19:18:56',
    '2020-06-09 16:45:10'
);

-- TESTS: some object 2 --
INSERT INTO some_object (
  task_id, status, order_id, order_index,
  close_date, warehouse, wave_label,
  wave_type, sku, product_name, unit,
  quantity, zone_id, route, stop, etd,
  priority, location, customer_id,
  customer_name, source, created_at,
  updated_at
) VALUES (
    'TSK0041378', NULL, '362088', NULL,
    NULL, 'PA', 'OLA0000432', 'OLA1',
    'BOG-FRU1-CAT8-335:454:1158:1159',
    NULL, NULL, 300.3, NULL, NULL, NULL,
    NULL, 1234567, 'MEH00321', NULL, NULL,
    'WMS', '2020-06-09 16:47:03', '2020-06-09 17:36:13'
);

-- TESTS: some object 3 --
INSERT INTO some_object (
  task_id, status, order_id, order_index,
  close_date, warehouse, wave_label,
  wave_type, sku, product_name, unit,
  quantity, zone_id, route, stop, etd,
  priority, location, customer_id,
  customer_name, source, created_at,
  updated_at
) VALUES (
    'TSK0041379', NULL, '1219000536',
    NULL, NULL, 'PA', 'OLA0000432', 'OLA2',
    'BOG-FRU1-CAT8-335:454:1158:1159',
    NULL, NULL, 300.3, NULL, NULL, NULL,
    NULL, 1234567, 'MEH00321', NULL, NULL,
    'WMS', '2020-06-09 17:58:17', '2020-06-09 17:58:17'
);