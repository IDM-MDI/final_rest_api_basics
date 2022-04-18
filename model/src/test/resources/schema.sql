-- -----------------------------------------------------
-- Table Gift_Certificates_Test.tags
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS tag
(
    id              BIGINT GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL,
    name            VARCHAR(42) NOT NULL,
    deleted         TINYINT        NULL,
    PRIMARY KEY (id)
);

-- -----------------------------------------------------
-- Table Gift_Certificates_Test.gift_certificates
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS gift_certificate
(
    id               BIGINT GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL,
    name             VARCHAR(42)            NOT NULL,
    description      VARCHAR(300),
    price            DECIMAL(8, 2) NOT NULL,
    duration         SMALLINT      NOT NULL,
    create_date      DATE            NOT NULL,
    last_update_date DATE            NOT NULL,
    deleted          TINYINT        NULL,
    PRIMARY KEY (id)
);

-- -----------------------------------------------------
-- Table Gift_Certificates_Test.gift_certificates_tags
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS gift_tag
(
    id              BIGINT GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL,
    gift_id         INTEGER,
    tag_id          INTEGER,
    deleted         TINYINT        NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (gift_id) REFERENCES gift_certificate (id),
    FOREIGN KEY (tag_id) REFERENCES tag (id)
);