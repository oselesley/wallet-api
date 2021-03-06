DROP TABLE if EXISTS monies CASCADE;
DROP TABLE if EXISTS transactions CASCADE;
DROP TABLE if EXISTS users CASCADE;

CREATE TABLE monies (
        id bigint generated BY DEFAULT AS IDENTITY,
        created_at TIMESTAMP,
        update_at TIMESTAMP,
        amount DECIMAL NOT NULL,
        currency VARCHAR(255) NOT NULL,
        user_id bigint, primary key (id)
);
CREATE TABLE transactions (
    id bigint generated BY DEFAULT AS IDENTITY,
    created_at TIMESTAMP,
    update_at TIMESTAMP,
    amount DECIMAL NOT NULL,
    currency VARCHAR(255) NOT NULL,
    transaction_status VARCHAR(255) NOT NULL,
    transaction_type VARCHAR(255) NOT NULL,
    user_id bigint, primary key (id)
    );
CREATE TABLE users (
    id bigint generated BY DEFAULT AS IDENTITY,
    created_at TIMESTAMP,
    update_at TIMESTAMP,
    email VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    main_currency VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    user_role VARCHAR(255) NOT NULL,
    user_name VARCHAR(255) NOT NULL, primary key (id)
    );
ALTER TABLE users ADD CONSTRAINT UK_6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);
ALTER TABLE users ADD CONSTRAINT UK_k8d0f2n7n88w1a16yhua64onx UNIQUE (user_name);
ALTER TABLE monies ADD CONSTRAINT FK6qva4cq48wrf7au0knt90sjte FOREIGN KEY (user_id) REFERENCES users;
ALTER TABLE transactions ADD CONSTRAINT FKqwv7rmvc8va8rep7piikrojds FOREIGN KEY (user_id) REFERENCES users;
