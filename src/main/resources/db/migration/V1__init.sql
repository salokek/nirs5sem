--CREATE SCHEMA IF NOT EXISTS autosalon;

CREATE TABLE IF NOT EXISTS employees (
                           employee_id SERIAL PRIMARY KEY,
                           full_name VARCHAR(100) NOT NULL,
                           phone_number VARCHAR(20) NOT NULL,
                           birth_date DATE NOT NULL,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица клиентов
CREATE TABLE IF NOT EXISTS clients (
                         client_id SERIAL PRIMARY KEY,
                         full_name VARCHAR(100) NOT NULL,
                         phone_number VARCHAR(20) NOT NULL,
                         birth_date DATE NOT NULL,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица каталога товаров (автомобилей)
CREATE TABLE IF NOT EXISTS catalog (
                         product_id SERIAL PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         category VARCHAR(50) NOT NULL,
                         price DECIMAL(15,2) NOT NULL,
                         description TEXT,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица поставок
CREATE TABLE IF NOT EXISTS deliveries (
                            delivery_id SERIAL PRIMARY KEY,
                            delivery_date DATE NOT NULL,
                            product_id INTEGER NOT NULL,
                            quantity INTEGER NOT NULL DEFAULT 0,
                            FOREIGN KEY (product_id) REFERENCES catalog(product_id) ON DELETE CASCADE,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица склада (связь между поставками и товарами)
CREATE TABLE IF NOT EXISTS warehouse (
                           record_id SERIAL PRIMARY KEY,
                           product_id INTEGER NOT NULL,
                           purchase_cost DECIMAL(15,2) NOT NULL,
                           quantity INTEGER NOT NULL DEFAULT 0,
                           delivery_id INTEGER NOT NULL,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           FOREIGN KEY (product_id) REFERENCES catalog(product_id) ON DELETE CASCADE,
                           FOREIGN KEY (delivery_id) REFERENCES deliveries(delivery_id) ON DELETE CASCADE
);

-- Таблица заказов
CREATE TABLE IF NOT EXISTS orders (
                        order_id SERIAL PRIMARY KEY,
                        transaction_date DATE NOT NULL,
                        client_id INTEGER NOT NULL,
                        product_id INTEGER NOT NULL,
                        employee_id INTEGER NOT NULL,
                        payment_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
                        quantity INTEGER NOT NULL DEFAULT 1,
                        total_amount DECIMAL(15,2) NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (client_id) REFERENCES clients(client_id) ON DELETE CASCADE,
                        FOREIGN KEY (product_id) REFERENCES catalog(product_id) ON DELETE CASCADE,
                        FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE
);