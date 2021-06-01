-- Create initial tables.

CREATE TABLE IF NOT EXISTS public.yard (
          Id serial not null,
          Color varchar,
          Warehouse varchar,
          constraint "PK_Yard" primary key(ID)
);