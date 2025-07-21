-- Вставка данных в таблицу customers
INSERT INTO customers (version, created_at, updated_at, user_id, is_active, email_verified, phone_verified,
                       first_name, last_name, middle_name, email, value, street, city, state, postal_code, country, apartment)
VALUES
    (1, NOW(), NOW(), 1001, true, true, true, 'Иван', 'Петров', 'Александрович', 'ivan.petrov@email.com', '+77771234567',
     'ул. Абая 150', 'Алматы', 'Алматинская область', '050000', 'Казахстан', '45'),
    (1, NOW(), NOW(), 1002, true, true, false, 'Мария', 'Сидорова', 'Викторовна', 'maria.sidorova@email.com', '+77772345678',
     'пр. Достык 200', 'Алматы', 'Алматинская область', '050010', 'Казахстан', NULL),
    (1, NOW(), NOW(), 1003, true, false, true, 'Алексей', 'Козлов', NULL, 'alexey.kozlov@email.com', '+77773456789',
     'ул. Сатпаева 90', 'Нур-Султан', 'Акмолинская область', '010000', 'Казахстан', '12'),
    (1, NOW(), NOW(), 1004, false, true, true, 'Анна', 'Васильева', 'Сергеевна', 'anna.vasilyeva@email.com', '+77774567890',
     'ул. Республики 15', 'Шымкент', 'Туркестанская область', '160000', 'Казахстан', '78'),
    (1, NOW(), NOW(), 1005, true, true, true, 'Дмитрий', 'Николаев', 'Павлович', 'dmitry.nikolaev@email.com', '+77775678901',
     'пр. Назарбаева 120', 'Караганда', 'Карагандинская область', '100000', 'Казахстан', NULL);

-- Вставка данных в таблицу carts
INSERT INTO carts (created_at, updated_at, customer_id, items_count, is_active)
VALUES
    (NOW(), NOW(), 1, 3, true),
    (NOW(), NOW(), 2, 1, true),
    (NOW(), NOW(), 3, 0, false),
    (NOW(), NOW(), 4, 2, true),
    (NOW(), NOW(), 5, 5, true);

-- Вставка данных в таблицу cart_items
INSERT INTO cart_items (created_at, updated_at, cart_id, product_id, unit_price, total_price, value)
VALUES
-- Корзина пользователя 1
(NOW(), NOW(), 1, 101, 25000.00, 50000.00, 2),
(NOW(), NOW(), 1, 102, 15000.00, 15000.00, 1),
(NOW(), NOW(), 1, 103, 8500.00, 8500.00, 1),
-- Корзина пользователя 2
(NOW(), NOW(), 2, 104, 45000.00, 45000.00, 1),
-- Корзина пользователя 4
(NOW(), NOW(), 4, 105, 12000.00, 36000.00, 3),
(NOW(), NOW(), 4, 106, 7500.00, 7500.00, 1),
-- Корзина пользователя 5
(NOW(), NOW(), 5, 107, 3500.00, 14000.00, 4),
(NOW(), NOW(), 5, 108, 28000.00, 28000.00, 1),
(NOW(), NOW(), 5, 109, 9500.00, 19000.00, 2),
(NOW(), NOW(), 5, 110, 6500.00, 19500.00, 3),
(NOW(), NOW(), 5, 111, 15500.00, 15500.00, 1);

-- Вставка данных в таблицу favorites
INSERT INTO favorites (created_at, updated_at, customer_id, is_active)
VALUES
    (NOW(), NOW(), 1, true),
    (NOW(), NOW(), 2, true),
    (NOW(), NOW(), 3, false),
    (NOW(), NOW(), 4, true),
    (NOW(), NOW(), 5, true);

-- Вставка данных в таблицу favorite_products
INSERT INTO favorite_products (favorites_id, product_id)
VALUES
-- Избранное пользователя 1
(1, 101), (1, 103), (1, 107), (1, 112),
-- Избранное пользователя 2
(2, 104), (2, 108), (2, 113),
-- Избранное пользователя 4
(4, 105), (4, 106), (4, 109), (4, 114), (4, 115),
-- Избранное пользователя 5
(5, 102), (5, 110), (5, 111), (5, 116), (5, 117), (5, 118);

-- Вставка данных в таблицу comments
INSERT INTO comments (created_at, updated_at, customer_id, product_id, is_verified, is_hidden, content, value)
VALUES
    (NOW(), NOW(), 1, 101, true, false, 'Отличный товар, быстрая доставка. Рекомендую!', 5),
    (NOW(), NOW(), 2, 104, true, false, 'Качество соответствует цене. Доволен покупкой.', 4),
    (NOW(), NOW(), 1, 102, true, false, 'Неплохо, но есть небольшие недостатки в упаковке.', 3),
    (NOW(), NOW(), 3, 105, false, false, 'Пока тестирую, но первые впечатления положительные.', 4),
    (NOW(), NOW(), 4, 106, true, false, 'Превосходное качество! Буду заказывать еще.', 5),
    (NOW(), NOW(), 5, 107, true, true, 'Товар не соответствует описанию.', 2),
    (NOW(), NOW(), 2, 108, true, false, 'Хорошее соотношение цена-качество.', 4),
    (NOW(), NOW(), 1, 103, true, false, 'Быстрая доставка, товар в отличном состоянии.', 5),
    (NOW(), NOW(), 4, 109, false, false, 'Жду результата использования, пока всё нравится.', 4),
    (NOW(), NOW(), 5, 110, true, false, 'Стандартное качество за свои деньги.', 3);