-- Create initial tables.

ALTER TABLE public.yard ADD is_exclusive boolean default false;
ALTER TABLE public.vehicle_type ADD size int default 0;

