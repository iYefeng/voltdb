CREATE TABLE ALL_TYPES
(
  INT_ITEM INTEGER NOT NULL,
  BYTE_ITEM TINYINT NOT NULL,
  SHORT_ITEM SMALLINT NOT NULL,
  LONG_ITEM BIGINT NOT NULL,
  DOUBLE_ITEM FLOAT,
  DECIMAL_ITEM DECIMAL,
  TIME_ITEM TIMESTAMP,
  STRING_ITEM VARCHAR(100) NOT NULL,
  PRIMARY KEY (SHORT_ITEM)
);
