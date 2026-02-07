create table orders (
  id uuid primary key,
  status varchar(30) not null,
  currency varchar(10) not null,
  created_at timestamptz not null,
  paid_payment_reference varchar(200)
);

create table order_items (
  id uuid primary key,
  order_id uuid not null references orders(id) on delete cascade,
  product_id uuid not null,
  product_name_snapshot varchar(200) not null,
  unit_price_amount numeric(19,2) not null,
  unit_price_currency varchar(10) not null,
  quantity int not null
);

create index idx_order_items_order_id on order_items(order_id);
