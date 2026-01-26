create table users_role (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    CONSTRAINT pk_user_role PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_users_role_users
        FOREIGN KEY (user_id) REFERENCES USERS(id) ON DELETE CASCADE,
    CONSTRAINT fk_users_role_role
        FOREIGN KEY (role_id) REFERENCES ROLE(id) ON DELETE CASCADE
)