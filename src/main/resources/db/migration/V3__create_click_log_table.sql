CREATE TABLE click_log (
    id UUID PRIMARY KEY,
    ip_address VARCHAR(255) NOT NULL,
    user_agent VARCHAR(255) NOT NULL,
    url_id UUID NOT NULL REFERENCES url(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_ip_address ON click_log(ip_address);
CREATE INDEX idx_user_agent ON click_log(user_agent);
CREATE INDEX idx_url_id ON click_log(url_id);
