ALTER TABLE url ADD COLUMN user_id UUID REFERENCES users(id);

CREATE INDEX idx_url_user_id ON url(user_id);
