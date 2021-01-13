create TABLE LOOKUP
(
    `ID`                  BIGINT                           NOT NULL AUTO_INCREMENT,
    `NAME`                VARCHAR(200)                     NOT NULL,
    `VALUE`               VARCHAR(200)                     NOT NULL,
    `CATEGORY`            VARCHAR(200)                     NOT NULL,
    `ORDINAL`             BIGINT                           NOT NULL,
    `MODIFIED_BY`         BIGINT                           NOT NULL,
    `MODIFIED_DATE`       timestamp                        NOT NULL DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP,
    `DELETED`             BOOLEAN                          NOT NULL DEFAULT FALSE,
    PRIMARY KEY (`ID`)
);


