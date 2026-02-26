CREATE TABLE users (
  username VARCHAR(50),
  email VARCHAR(100),
  password VARCHAR(64)
);

INSERT INTO users (username, email, password) VALUES
('admin', 'admin@logincaos.local', '827ccb0eea8a706c4c34a16891f84e7b'),
('user', 'user@logincaos.local', '5f4dcc3b5aa765d61d8327deb882cf99');
