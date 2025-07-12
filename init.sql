USE dcnds;

CREATE TABLE IF NOT EXISTS TICKET
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    message_uuid VARCHAR(36)   NOT NULL UNIQUE,
    subject      VARCHAR(600),
    message      VARCHAR(6000) NOT NULL,
    sender_email VARCHAR(100),
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS TICKET_FILE
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    message_uuid VARCHAR(36) NOT NULL,
    file_path    VARCHAR(600),
    file_name    VARCHAR(300),
    file_type    VARCHAR(100),
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (message_uuid) REFERENCES TICKET (message_uuid)
);

CREATE TABLE IF NOT EXISTS TICKET_CLASSIFICATION
(
    id               INT AUTO_INCREMENT PRIMARY KEY,
    message_uuid     VARCHAR(36) NOT NULL,
    result           VARCHAR(32) NOT NULL,
    prob_certainty   FLOAT       NOT NULL,
    change_certainty FLOAT       NOT NULL,
    req_certainty    FLOAT       NOT NULL,
    resolved         BOOL        NOT NULL,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (message_uuid) REFERENCES TICKET (message_uuid)
);

INSERT INTO TICKET (message_uuid, subject, message, sender_email)
VALUES (
           '5da1d172-43a7-4af1-bdc7-ad03c7fd6ef8',
           'Something is not quite right',
           'Lorem ipsum muspi meroL',
           'jan@example.com'
       );

INSERT INTO TICKET (message_uuid, subject, message, sender_email)
VALUES (
           'aa07eb65-e09e-463b-b5d4-397d56701054',
           'Angry bird',
           'Lorem ipsum muspi meroL Lorem ipsum muspi meroL Lorem ipsum muspi meroL',
           'IAN_nepo@example.com'
       );

INSERT INTO TICKET (message_uuid, subject, message, sender_email)
VALUES (
           '1cccd172-4ds7-2221-3fg7-ad03c7f12345',
           'Something is not quite right',
           'Pardon my delay, I\'m navigating -> MY HEAD!',
           'pudzian@example.com'
       );

INSERT INTO TICKET_FILE (message_uuid, file_path, file_name, file_type)
VALUES (
           '5da1d172-43a7-4af1-bdc7-ad03c7fd6ef8',
           '/home/dir/',
           'test.eml',
           'EML'
       );