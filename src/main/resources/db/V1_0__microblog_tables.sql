set schema app;
/* ************************************ USERS ***************************************************/

/* starting sequence from 100 to be able to inculde some predefined users */
CREATE SEQUENCE users_seq START WITH 100 INCREMENT BY 5;

create table users (
  user_name        varchar(64) not null PRIMARY KEY,
  password         varchar(64) not null,
  nickname         varchar(64) not null UNIQUE,
  description      varchar(256),
  photo_url        varchar(128)
);

INSERT INTO users VALUES ('bob','bob','nickname of bob','description of bob','http://photos.net/bob_photo');

/* ************************************ ROLES ****************************************************/

create table user_roles (
  user_name         varchar(64) not null,
  role_name         varchar(15) not null,
  primary key (user_name, role_name)
);

INSERT INTO user_roles VALUES ('bob','CHAT_USER');

ALTER TABLE user_roles ADD CONSTRAINT user_fc FOREIGN KEY (user_name)  REFERENCES users (user_name);

/* ************************************ POSTS ****************************************************/

create table posts (
  id               NUMERIC(22,0) PRIMARY KEY,
  message          varchar(256) NOT NULL,
  creator          varchar(64) not null,
  topics          varchar(256),
  mentions          varchar(256),
  created          TIMESTAMP NOT NULL
);

CREATE SEQUENCE posts_seq START WITH 100 INCREMENT BY 5;

ALTER TABLE posts ADD CONSTRAINT creator_fc FOREIGN KEY (creator)  REFERENCES users (user_name);

/* ************************************ FOLLOW DATA ****************************************************/

CREATE SEQUENCE follow_data_seq START WITH 100 INCREMENT BY 5;

create table follow_data (
  id               NUMERIC(22,0) PRIMARY KEY,
  follower         varchar(64) not null,
  following        varchar(64) not null
);

ALTER TABLE follow_data ADD CONSTRAINT follow_data_uc UNIQUE (follower,following);

ALTER TABLE follow_data ADD CONSTRAINT follower_fc FOREIGN KEY (follower)  REFERENCES users (user_name);
ALTER TABLE follow_data ADD CONSTRAINT following_fc FOREIGN KEY (following)  REFERENCES users (user_name);

