-- Sample data for company_db database
-- This file is automatically executed by Spring Boot on startup

-- Insert sample companies
INSERT INTO company (name, email, phone) VALUES 
('TechCorp', 'contact@techcorp.io', '555-0101'),
('DataFlow', 'info@dataflow.ai', '555-0102'),
('CloudBiz', 'hello@cloudbiz.com', '555-0103'),
('InnoSoft', 'support@innosoft.com', '555-0104'),
('DevStream', 'team@devstream.com', '555-0105');

-- -- Insert sample price data (range: ¤1.00 - ¤20.00)
-- INSERT INTO price (company_id, time, price) VALUES
-- -- TechCorp prices
-- (1, '09:00:00', 15.50),
-- (1, '12:00:00', 16.25),
-- (1, '15:00:00', 14.75),
--
-- -- DataFlow prices
-- (2, '09:00:00', 8.25),
-- (2, '12:00:00', 9.50),
-- (2, '15:00:00', 7.75),
--
-- -- CloudBiz prices
-- (3, '09:00:00', 19.75),
-- (3, '12:00:00', 20.00),
-- (3, '15:00:00', 18.25),
--
-- -- InnoSoft prices
-- (4, '09:00:00', 5.50),
-- (4, '12:00:00', 6.25),
-- (4, '15:00:00', 4.75),
--
-- -- DevStream prices
-- (5, '09:00:00', 12.25),
-- (5, '12:00:00', 13.50),
-- (5, '15:00:00', 11.75);

-- Insert calculated demand data based on formula: demand = 100 - 5p + 2q
-- where p = own price, q = average competitor price

-- INSERT INTO demand (company_id, time, demand) VALUES
-- -- 09:00:00 demands (recalculated with realistic prices ¤1-¤20)
-- (1, '09:00:00', 45.38),    -- TechCorp: 100 - 5*15.50 + 2*11.44 = 45.38
-- (2, '09:00:00', 85.25),    -- DataFlow: 100 - 5*8.25 + 2*13.25 = 85.25
-- (3, '09:00:00', 22.01),    -- CloudBiz: 100 - 5*19.75 + 2*10.38 = 22.01
-- (4, '09:00:00', 100.38),   -- InnoSoft: 100 - 5*5.50 + 2*13.94 = 100.38
-- (5, '09:00:00', 63.75),    -- DevStream: 100 - 5*12.25 + 2*12.25 = 63.75
--
-- -- 12:00:00 demands
-- (1, '12:00:00', 42.75),    -- TechCorp: 100 - 5*16.25 + 2*12.31 = 42.75
-- (2, '12:00:00', 75.25),    -- DataFlow: 100 - 5*9.50 + 2*13.75 = 75.25
-- (3, '12:00:00', 22.50),    -- CloudBiz: 100 - 5*20.00 + 2*11.25 = 22.50
-- (4, '12:00:00', 98.63),    -- InnoSoft: 100 - 5*6.25 + 2*14.81 = 98.63
-- (5, '12:00:00', 58.00),    -- DevStream: 100 - 5*13.50 + 2*12.75 = 58.00
--
-- -- 15:00:00 demands
-- (1, '15:00:00', 49.19),    -- TechCorp: 100 - 5*14.75 + 2*10.59 = 49.19
-- (2, '15:00:00', 86.63),    -- DataFlow: 100 - 5*7.75 + 2*12.31 = 86.63
-- (3, '15:00:00', 27.69),    -- CloudBiz: 100 - 5*18.25 + 2*8.84 = 27.69
-- (4, '15:00:00', 106.88),   -- InnoSoft: 100 - 5*4.75 + 2*13.13 = 106.88
-- (5, '15:00:00', 65.94);    -- DevStream: 100 - 5*11.75 + 2*11.56 = 65.94

-- Insert calculated profit data based on formula: profit = (price - 2) * demand
-- where price is company's price and demand is calculated demand

-- INSERT INTO profit (company_id, time, profit) VALUES
-- -- 09:00:00 profits
-- (1, '09:00:00', 612.63),   -- TechCorp: (15.50 - 2) * 45.38 = 612.63
-- (2, '09:00:00', 532.06),   -- DataFlow: (8.25 - 2) * 85.25 = 532.06
-- (3, '09:00:00', 390.68),   -- CloudBiz: (19.75 - 2) * 22.01 = 390.68
-- (4, '09:00:00', 351.33),   -- InnoSoft: (5.50 - 2) * 100.38 = 351.33
-- (5, '09:00:00', 653.44),   -- DevStream: (12.25 - 2) * 63.75 = 653.44
--
-- -- 12:00:00 profits
-- (1, '12:00:00', 609.44),   -- TechCorp: (16.25 - 2) * 42.75 = 609.44
-- (2, '12:00:00', 564.38),   -- DataFlow: (9.50 - 2) * 75.25 = 564.38
-- (3, '12:00:00', 405.00),   -- CloudBiz: (20.00 - 2) * 22.50 = 405.00
-- (4, '12:00:00', 418.17),   -- InnoSoft: (6.25 - 2) * 98.63 = 418.17
-- (5, '12:00:00', 667.00),   -- DevStream: (13.50 - 2) * 58.00 = 667.00
--
-- -- 15:00:00 profits
-- (1, '15:00:00', 627.17),   -- TechCorp: (14.75 - 2) * 49.19 = 627.17
-- (2, '15:00:00', 498.12),   -- DataFlow: (7.75 - 2) * 86.63 = 498.12
-- (3, '15:00:00', 450.47),   -- CloudBiz: (18.25 - 2) * 27.69 = 450.47
-- (4, '15:00:00', 293.92),   -- InnoSoft: (4.75 - 2) * 106.88 = 293.92
-- (5, '15:00:00', 643.42);   -- DevStream: (11.75 - 2) * 65.94 = 643.42