-- Таблица товаров
CREATE TABLE items (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       img_path VARCHAR(500),
                       price DECIMAL(10, 2) NOT NULL,
                       description TEXT NOT NULL
);

-- Таблица заказов
CREATE TABLE orders (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        order_number VARCHAR(50) UNIQUE NOT NULL,
                        total_amount DECIMAL(10, 2) NOT NULL,
                        order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица элементов заказа
CREATE TABLE order_items (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             order_id BIGINT NOT NULL,
                             item_id BIGINT NOT NULL,
                             quantity INT NOT NULL DEFAULT 1,
                             FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
                             FOREIGN KEY (item_id) REFERENCES items(id)
);

-- Заполнение таблицы товаров (IT-товары)
INSERT INTO items (title, img_path, price, description) VALUES
                                                            (
                                                                'MacBook Pro 16" M2 Pro',
                                                                'https://gearopen.com/wp-content/uploads/2019/11/mbp16touch-space-gallery1-201911-1280x720.jpeg',
                                                                2499.99,
                                                                'Ноутбук Apple MacBook Pro 16 дюймов с чипом M2 Pro, 16 ГБ ОЗУ, 1 ТБ SSD'
                                                            ),
                                                            (
                                                                'MacBook Pro 16" M5',
                                                                'https://gearopen.com/wp-content/uploads/2019/11/mbp16touch-space-gallery1-201911-1280x720.jpeg',
                                                                3499.99,
                                                                'Ноутбук Apple MacBook Pro 16 дюймов с чипом M5, 16 ГБ ОЗУ, 1 ТБ SSD'
                                                            ),
                                                            (
                                                                'Dell XPS 13 RAM 16 GB',
                                                                'https://media.cnn.com/api/v1/images/stellar/prod/220609090511-dell-xps-13-2022-1.jpg?c=16x9&q=h_720,w_1280,c_fill',
                                                                1299.99,
                                                                'Ультрабук Dell XPS 13 с процессором Intel Core i7, 16 ГБ ОЗУ, 512 ГБ SSD'
                                                            ),
                                                            (
                                                                'Dell XPS 13 RAM 32gb',
                                                                'https://media.cnn.com/api/v1/images/stellar/prod/220609090511-dell-xps-13-2022-1.jpg?c=16x9&q=h_720,w_1280,c_fill',
                                                                1599.99,
                                                                'Ультрабук Dell XPS 13 с процессором Intel Core i7, 16 ГБ ОЗУ, 512 ГБ SSD'
                                                            ),
                                                            (
                                                                'Logitech MX Keys',
                                                                'https://avatars.mds.yandex.net/i?id=27bf07bdcfac2f3f94581037eb50647c_l-12598198-images-thumbs&n=13',
                                                                99.99,
                                                                'Беспроводная клавиатура Logitech MX Keys с подсветкой'
                                                            ),
                                                            (
                                                                'Razer DeathAdder V2',
                                                                'https://avatars.mds.yandex.net/i?id=b6fdf8a8939ac629467195a31b105c5c2a33d90f-10230136-images-thumbs&n=13',
                                                                69.99,
                                                                'Игровая мышь Razer DeathAdder V2 с сенсором 20K DPI'
                                                            ),
                                                            (
                                                                'Samsung Odyssey G7 32"',
                                                                'https://avatars.mds.yandex.net/i?id=f1b6bf7689f90262073f87cf9934d2f5_l-5229416-images-thumbs&n=13',
                                                                699.99,
                                                                'Игровой монитор 32" Samsung Odyssey G7, 240 Гц, QLED, изогнутый'
                                                            ),
                                                            (
                                                                'Samsung Odyssey G7 27"',
                                                                'https://avatars.mds.yandex.net/i?id=f1b6bf7689f90262073f87cf9934d2f5_l-5229416-images-thumbs&n=13',
                                                                599.99,
                                                                'Игровой монитор 27" Samsung Odyssey G7, 240 Гц, QLED, изогнутый'
                                                            ),
                                                            (
                                                                'PlayStation 5',
                                                                'https://images.stopgame.ru/news/2023/10/10/wCkGi0WV.jpg',
                                                                499.99,
                                                                'Игровая консоль Sony PlayStation 5 с Ultra HD Blu-ray'
                                                            ),
                                                            (
                                                                'PlayStation 5 Pro',
                                                                'https://images.stopgame.ru/news/2023/10/10/wCkGi0WV.jpg',
                                                                699.99,
                                                                'Игровая консоль Sony PlayStation 5 Pro с Ultra HD Blu-ray'
                                                            ),
                                                            (
                                                                'AirPods Pro',
                                                                'https://cdn1.ozone.ru/s3/multimedia-1-2/7126526270.jpg',
                                                                249.99,
                                                                'Беспроводные наушники Apple AirPods Pro с шумоподавлением'
                                                            ),
                                                            (
                                                                'AirPods',
                                                                'https://cdn1.ozone.ru/s3/multimedia-1-2/7126526270.jpg',
                                                                349.99,
                                                                'Беспроводные наушники Apple AirPods'
                                                            ),
                                                            (
                                                                'iPad Air M1',
                                                                'https://avatars.mds.yandex.net/i?id=d3a5c39b92504815efb4a3616e9fc6e8_l-8312020-images-thumbs&n=13',
                                                                749.99,
                                                                'Планшет Apple iPad Air с чипом M1, 10.9 дюймов, 256 ГБ'
                                                            ),
                                                            (
                                                                'iPad Air M3',
                                                                'https://avatars.mds.yandex.net/i?id=d3a5c39b92504815efb4a3616e9fc6e8_l-8312020-images-thumbs&n=13',
                                                                849.99,
                                                                'Планшет Apple iPad Air с чипом M3, 10.9 дюймов, 256 ГБ'
                                                            ),
                                                            (
                                                                'Keychron K2',
                                                                'https://avatars.mds.yandex.net/i?id=62b8bed197916a2b54b35748e69c1387d6d1850e-5409727-images-thumbs&n=13',
                                                                89.99,
                                                                'Механическая клавиатура Keychron K2 с Bluetooth'
                                                            ),
                                                            (
                                                                'LG UltraGear 27"',
                                                                'https://avatars.mds.yandex.net/i?id=0a2de61037dbe20dc947f7cc32ddd112_l-5222738-images-thumbs&n=13',
                                                                449.99,
                                                                'Игровой монитор LG UltraGear 27" 144 Гц, IPS'
                                                            ),
                                                            (
                                                                'LG UltraGear 32"',
                                                                'https://avatars.mds.yandex.net/i?id=0a2de61037dbe20dc947f7cc32ddd112_l-5222738-images-thumbs&n=13',
                                                                549.99,
                                                                'Игровой монитор LG UltraGear 32" 144 Гц, IPS'
                                                            );

-- Заполнение таблицы заказов
INSERT INTO orders (order_number, total_amount, order_date) VALUES
                                                                ('ORD-2024-001', 2599.98, '2024-01-15 10:30:00'),
                                                                ('ORD-2024-002', 819.98, '2024-01-16 14:45:00'),
                                                                ('ORD-2024-003', 1189.97, '2024-01-17 09:15:00'),
                                                                ('ORD-2024-004', 149.98, '2024-01-18 16:20:00'),
                                                                ('ORD-2024-005', 1249.98, '2024-01-19 11:00:00');

-- Заполнение таблицы элементов заказа
INSERT INTO order_items (order_id, item_id, quantity) VALUES
                                                          (1, 1, 1),  -- MacBook Pro в заказе 1
                                                          (1, 7, 1),  -- AirPods Pro в заказе 1

                                                          (2, 3, 1),  -- Logitech MX Keys в заказе 2
                                                          (2, 4, 1),  -- Razer DeathAdder в заказе 2
                                                          (2, 9, 2),  -- 2x Keychron K2 в заказе 2

                                                          (3, 2, 1),  -- Dell XPS 13 в заказе 3
                                                          (3, 6, 1),  -- PlayStation 5 в заказе 3

                                                          (4, 4, 2),  -- 2x Razer DeathAdder в заказе 4

                                                          (5, 8, 1),  -- iPad Air в заказе 5
                                                          (5, 10, 1); -- LG UltraGear в заказе 5