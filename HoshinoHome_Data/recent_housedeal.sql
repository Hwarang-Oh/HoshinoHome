-- Active: 1715070222040@@127.0.0.1@3306@hoshinohome
CREATE TABLE recent_housedeal (
    house_code BIGINT NOT NULL,
    house_type INT NOT NULL,
    deal_type INT NOT NULL,
    deal_code BIGINT NOT NULL,
    deal_amount VARCHAR(20),
    deposit_amount VARCHAR(20),
    monthly_amount VARCHAR(20),
    jibun_address VARCHAR(40),
    road_address VARCHAR(40),
    deal_year INT,
    deal_month INT,
    deal_day INT,
    area VARCHAR(20),
    floor VARCHAR(4),
    PRIMARY KEY (
        house_code,
        house_type,
        deal_type
    )
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO
    recent_housedeal (
        house_code,
        house_type,
        deal_type,
        deal_code,
        deal_amount,
        deposit_amount,
        monthly_amount,
        jibun_address,
        road_address,
        deal_year,
        deal_month,
        deal_day,
        area,
        floor
    )
SELECT hd.house_code, hd.house_type, hd.deal_type, hd.deal_code, hd.deal_amount, hd.deposit_amount, hd.monthly_amount, hd.jibun_address, hd.road_address, hd.deal_year, hd.deal_month, hd.deal_day, hd.area, hd.floor
FROM
    housedeal hd
    INNER JOIN (
        SELECT
            house_code,
            house_type,
            deal_type,
            MAX(
                deal_year * 10000 + deal_month * 100 + deal_day
            ) AS max_date,
            MAX(deal_code) AS max_deal_code
        FROM housedeal
        GROUP BY
            house_code,
            house_type,
            deal_type
    ) latest ON hd.house_code = latest.house_code
    AND hd.house_type = latest.house_type
    AND hd.deal_type = latest.deal_type
    AND (
        hd.deal_year * 10000 + hd.deal_month * 100 + hd.deal_day = latest.max_date
        AND hd.deal_code = latest.max_deal_code
    );