-- Fill values to "Tag" table
INSERT INTO tag (name)
VALUES ('tag1'),
('tag2'),
('tag3'),
('tag4'),
('tag5');

-- Fill values to "Gift_Certificates" table
INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date)
VALUES
('gift1', 'description1', 10, 1, '2020-08-29', '2020-08-29'),
('gift2', 'description2', 20, 2, '2018-08-29', '2018-08-29'),
('gift3', 'description3', 30, 3, '2019-08-29', '2019-08-29');

-- Mapping Gift_Certificate with Tag
INSERT INTO gift_tag (gift_id, tag_id)
VALUES
(1, 2),
(2, 2);