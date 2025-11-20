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
                                                            ('MacBook Pro 16"', 'https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/mbp16-spacegray-select-202301?wid=904&hei=840&fmt=jpeg&qlt=90&.v=1671304673202', 2499.99, 'Ноутбук Apple MacBook Pro 16 дюймов с чипом M2 Pro, 16 ГБ ОЗУ, 1 ТБ SSD'),
                                                            ('Dell XPS 13', 'https://i.dell.com/is/image/DellContent/content/dam/ss2/product-images/dell-client-products/notebooks/xps-notebooks/xps-13-9320/media-gallery/notebook-xps-13-9320-nt-blue-gallery-1.psd?fmt=pjpg&pscan=auto&scl=1&wid=3333&hei=3333&qlt=100%2C0&resMode=sharp2&size=3333%2C3333', 1299.99, 'Ультрабук Dell XPS 13 с процессором Intel Core i7, 16 ГБ ОЗУ, 512 ГБ SSD'),
                                                            ('Logitech MX Keys', 'https://resource.logitech.com/w_692,c_lpad,ar_4:3,q_auto,f_auto,dpr_1.0/d_transparent.gif/content/dam/logitech/en/products/keyboards/mx-keys-s/gallery/mx-keys-s-keyboard-top-view-graphite.png?v=1', 99.99, 'Беспроводная клавиатура Logitech MX Keys с подсветкой'),
                                                            ('Razer DeathAdder V2', 'https://assets2.razerzone.com/images/og-image/razer-deathadder-v2-OGimage-1200x630.jpg', 69.99, 'Игровая мышь Razer DeathAdder V2 с сенсором 20K DPI'),
                                                            ('Samsung Odyssey G7', 'https://images.samsung.com/is/image/samsung/p6pim/ru/lc32g75tqsixci/gallery/ru-odyssey-g7-lc32g75tqsixci-532072789?$650_519_PNG$', 699.99, 'Игровой монитор 32" Samsung Odyssey G7, 240 Гц, QLED, изогнутый'),
                                                            ('PlayStation 5', 'https://gmedia.playstation.com/is/image/SIEPDC/ps5-product-thumbnail-01-en-14sep21?$1600px$', 499.99, 'Игровая консоль Sony PlayStation 5 с Ultra HD Blu-ray'),
                                                            ('AirPods Pro', 'https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/MTJV3?wid=1144&hei=1144&fmt=jpeg&qlt=90&.v=1694014871985', 249.99, 'Беспроводные наушники Apple AirPods Pro с шумоподавлением'),
                                                            ('iPad Air', 'https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/ipad-air-select-wifi-blue-202203?wid=940&hei=1112&fmt=png-alpha&.v=1645066731336', 749.99, 'Планшет Apple iPad Air с чипом M1, 10.9 дюймов, 256 ГБ'),
                                                            ('Keychron K2', 'https://cdn.shopify.com/s/files/1/0059/0630/8897/products/keychron-k2-wireless-mechanical-keyboard-for-mac-windows-version-2-red-switch-white-backlight-1060x_1200x1200.jpg?v=1652083789', 89.99, 'Механическая клавиатура Keychron K2 с Bluetooth'),
                                                            ('LG UltraGear', 'https://www.lg.com/ru/images/monitors/md07569916/gallery/27GN800-B_350_D1.jpg', 449.99, 'Игровой монитор LG UltraGear 27" 144 Гц, IPS');

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