DROP TABLE IF EXISTS playlist_category CASCADE;
DROP TABLE IF EXISTS playlist_category_mapping CASCADE;
DROP TABLE IF EXISTS playlist_likes CASCADE;
DROP TABLE IF EXISTS content_likes CASCADE;
DROP TABLE IF EXISTS refresh_tokens CASCADE;
DROP TABLE IF EXISTS watch_comments CASCADE;
DROP TABLE IF EXISTS watch_session_participants CASCADE;
DROP TABLE IF EXISTS playlists_contents CASCADE;
DROP TABLE IF EXISTS notifications CASCADE;
DROP TABLE IF EXISTS follows CASCADE;
DROP TABLE IF EXISTS messages CASCADE;
DROP TABLE IF EXISTS chatrooms_users CASCADE;
DROP TABLE IF EXISTS playlists CASCADE;
DROP TABLE IF EXISTS reviews CASCADE;
DROP TABLE IF EXISTS subscribes CASCADE;
DROP TABLE IF EXISTS watch_sessions CASCADE;
DROP TABLE IF EXISTS chat_rooms CASCADE;
DROP TABLE IF EXISTS contents CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE watch_comments (
    comment_id UUID PRIMARY KEY,
    session_id UUID NOT NULL,
    user_id    UUID NOT NULL,
    content    VARCHAR(500),
    sent_at    TIMESTAMP WITH TIME ZONE,
    message    TEXT
);

CREATE TABLE users (
    user_id                UUID PRIMARY KEY,
    user_name              VARCHAR(25),
    email                  VARCHAR(50),
    password               VARCHAR(100),
    role                   VARCHAR(25),
    user_source            VARCHAR(25),
    is_locked              BOOLEAN,
    is_using_temp_password BOOLEAN,
    create_at              TIMESTAMP WITH TIME ZONE,
    update_at              TIMESTAMP WITH TIME ZONE
);

CREATE TABLE watch_session_participants (
    participant_id UUID PRIMARY KEY,
    session_id     UUID NOT NULL,
    user_id        UUID NOT NULL,
    joined_at      TIMESTAMP WITH TIME ZONE,
    left_at        TIMESTAMP WITH TIME ZONE
);

CREATE TABLE playlists_contents (
    playlist_content_id BIGSERIAL PRIMARY KEY,
    playlist_id         UUID NOT NULL,
    content_id          UUID NOT NULL
);

CREATE TABLE notifications (
    notification_id UUID,
    user_id         UUID,
    type            VARCHAR(50),
    content         VARCHAR(50),
    related_url     VARCHAR(1000),
    is_read         BOOLEAN,
    created_at      TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (notification_id, user_id)
);

CREATE TABLE contents (
    content_id         UUID PRIMARY KEY,
    external_id        VARCHAR(100),
    source             VARCHAR(25),
    title              VARCHAR(255),
    summary            TEXT,
    category           VARCHAR(25),
    poster_url         VARCHAR(500),
    genres             JSONB,
    released_at        TIMESTAMP WITH TIME ZONE,
    created_at         TIMESTAMP WITH TIME ZONE,
    updated_at         TIMESTAMP WITH TIME ZONE,
    average_rating     NUMERIC(3, 2),
    total_rating_count BIGINT
);

CREATE TABLE follows (
    follow_id   UUID PRIMARY KEY,
    follower_id UUID NOT NULL,
    followee_id UUID NOT NULL
);

CREATE TABLE subscribes (
    subscribe_id  BIGSERIAL PRIMARY KEY,
    user_id       UUID NOT NULL,
    playlist_id   UUID NOT NULL,
    subscribed_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE chatrooms_users (
    chat_room_id UUID NOT NULL,
    user_id      UUID NOT NULL,
    PRIMARY KEY (chat_room_id, user_id)
);

CREATE TABLE chat_rooms (
    chat_room_id UUID PRIMARY KEY
);

CREATE TABLE watch_sessions (
    session_id UUID PRIMARY KEY,
    content_id UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE,
    is_active  BOOLEAN
);

CREATE TABLE messages (
    message_id   UUID,
    chat_room_id UUID,
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id      UUID                     NOT NULL,
    content      VARCHAR(200),
    PRIMARY KEY (message_id, chat_room_id)
);

CREATE TABLE reviews (
    review_id  UUID PRIMARY KEY,
    content_id UUID NOT NULL,
    user_id    UUID NOT NULL,
    rating     INTEGER,
    comment    VARCHAR(1000),
    created_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE playlists (
    playlist_id UUID PRIMARY KEY,
    user_id     UUID NOT NULL,
    title       VARCHAR(255),
    description VARCHAR(1000),
    is_public   BOOLEAN,
    created_at  TIMESTAMP WITH TIME ZONE,
    updated_at  TIMESTAMP WITH TIME ZONE
);

-- WatchComment (N) -> WatchSession (1)
ALTER TABLE watch_comments
    ADD CONSTRAINT fk_watch_comment_session
        FOREIGN KEY (session_id)
            REFERENCES watch_sessions (session_id)
            ON DELETE CASCADE;

-- WatchComment (N) -> User (1)
ALTER TABLE watch_comments
    ADD CONSTRAINT fk_watch_comment_user
        FOREIGN KEY (user_id)
            REFERENCES users (user_id)
            ON DELETE CASCADE;

-- WatchSessionParticipant (N) -> WatchSession (1)
ALTER TABLE watch_session_participants
    ADD CONSTRAINT fk_watch_session_participant_session
        FOREIGN KEY (session_id)
            REFERENCES watch_sessions (session_id)
            ON DELETE CASCADE;

-- WatchSessionParticipant (N) -> User (1)
ALTER TABLE watch_session_participants
    ADD CONSTRAINT fk_watch_session_participant_user
        FOREIGN KEY (user_id)
            REFERENCES users (user_id)
            ON DELETE CASCADE;

-- PlaylistContent (N) -> Playlist (1)
ALTER TABLE playlists_contents
    ADD CONSTRAINT fk_playlist_content_playlist
        FOREIGN KEY (playlist_id)
            REFERENCES playlists (playlist_id)
            ON DELETE CASCADE;

-- PlaylistContent (N) -> Content (1)
ALTER TABLE playlists_contents
    ADD CONSTRAINT fk_playlist_content_content
        FOREIGN KEY (content_id)
            REFERENCES contents (content_id)
            ON DELETE CASCADE;

ALTER TABLE playlists_contents
    ADD CONSTRAINT uk_playlist_content_unique
        UNIQUE (playlist_id, content_id);

-- Notification (N) -> User (1)
ALTER TABLE notifications
    ADD CONSTRAINT fk_notification_user
        FOREIGN KEY (user_id)
            REFERENCES users (user_id)
            ON DELETE CASCADE;

-- Follow (N) -> User (1) [Follower]
ALTER TABLE follows
    ADD CONSTRAINT fk_follow_follower
        FOREIGN KEY (follower_id)
            REFERENCES users (user_id)
            ON DELETE CASCADE;

-- Follow (N) -> User (1) [Followee]
ALTER TABLE follows
    ADD CONSTRAINT fk_follow_followee
        FOREIGN KEY (followee_id)
            REFERENCES users (user_id)
            ON DELETE CASCADE;

-- Subscribe (N) -> User (1)
ALTER TABLE subscribes
    ADD CONSTRAINT fk_subscribe_user
        FOREIGN KEY (user_id)
            REFERENCES users (user_id)
            ON DELETE CASCADE;

-- ChatroomUser (N) -> ChatRoom (1)
ALTER TABLE chatrooms_users
    ADD CONSTRAINT fk_chatroom_user_chatroom
        FOREIGN KEY (chat_room_id)
            REFERENCES chat_rooms (chat_room_id)
            ON DELETE CASCADE;

-- ChatroomUser (N) -> User (1)
ALTER TABLE chatrooms_users
    ADD CONSTRAINT fk_chatroom_user_user
        FOREIGN KEY (user_id)
            REFERENCES users (user_id)
            ON DELETE CASCADE;

-- WatchSession (N) -> Content (1)
ALTER TABLE watch_sessions
    ADD CONSTRAINT fk_watch_session_content
        FOREIGN KEY (content_id)
            REFERENCES contents (content_id)
            ON DELETE CASCADE;

-- Message (N) -> ChatRoom (1)
ALTER TABLE messages
    ADD CONSTRAINT fk_message_chatroom
        FOREIGN KEY (chat_room_id)
            REFERENCES chat_rooms (chat_room_id)
            ON DELETE CASCADE;

-- Message (N) -> User (1)
ALTER TABLE messages
    ADD CONSTRAINT fk_message_user
        FOREIGN KEY (user_id)
            REFERENCES users (user_id)
            ON DELETE SET NULL;

-- Review (N) -> Content (1)
ALTER TABLE reviews
    ADD CONSTRAINT fk_review_content
        FOREIGN KEY (content_id)
            REFERENCES contents (content_id)
            ON DELETE CASCADE;

-- Review (N) -> User (1)
ALTER TABLE reviews
    ADD CONSTRAINT fk_review_user
        FOREIGN KEY (user_id)
            REFERENCES users (user_id)
            ON DELETE SET NULL;

-- Playlist (N) -> User (1)
ALTER TABLE playlists
    ADD CONSTRAINT fk_playlist_user
        FOREIGN KEY (user_id)
            REFERENCES users (user_id)
            ON DELETE CASCADE;

-- Subscribe (N) -> Playlist (1)
ALTER TABLE subscribes
    ADD CONSTRAINT fk_subscribe_playlist
        FOREIGN KEY (playlist_id)
            REFERENCES playlists (playlist_id)
            ON DELETE CASCADE;

CREATE TABLE refresh_tokens (
    token_id    UUID PRIMARY KEY,
    user_id     UUID      NOT NULL UNIQUE,
    token       TEXT      NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
            REFERENCES users (user_id)
            ON DELETE CASCADE
);

CREATE INDEX idx_follower_followee ON follows (follower_id, followee_id);

CREATE TABLE playlist_likes (
    playlist_like_id BIGSERIAL PRIMARY KEY,
    user_id          UUID        NOT NULL,
    playlist_id      UUID        NOT NULL,
    liked_at         TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uc_playlist_like_user_playlist UNIQUE (user_id, playlist_id),
    CONSTRAINT fk_playlist_like_user
        FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    CONSTRAINT fk_playlist_like_playlist
        FOREIGN KEY (playlist_id) REFERENCES playlists (playlist_id) ON DELETE CASCADE
);

CREATE INDEX idx_playlist_like_playlist ON playlist_likes (playlist_id);
CREATE INDEX idx_playlist_like_user ON playlist_likes (user_id);

CREATE TABLE content_likes (
    content_like_id BIGSERIAL PRIMARY KEY,
    user_id         UUID        NOT NULL,
    content_id      UUID        NOT NULL,
    liked_at        TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_user_content UNIQUE (user_id, content_id),
    CONSTRAINT fk_content_likes_user FOREIGN KEY (user_id) REFERENCES users (user_id),
    CONSTRAINT fk_content_likes_content FOREIGN KEY (content_id) REFERENCES contents (content_id)
);

CREATE INDEX idx_content_likes_user_id ON content_likes (user_id);
CREATE INDEX idx_content_likes_content_id ON content_likes (content_id);



CREATE TABLE playlist_category (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE playlist_category_mapping (
    id BIGSERIAL PRIMARY KEY,
    playlist_id UUID NOT NULL,
    category_id BIGINT NOT NULL,

    CONSTRAINT fk_playlist
        FOREIGN KEY (playlist_id) REFERENCES playlists (playlist_id) ON DELETE CASCADE,

    CONSTRAINT fk_category
        FOREIGN KEY (category_id) REFERENCES playlist_category (id) ON DELETE CASCADE,

    CONSTRAINT uq_playlist_category UNIQUE (playlist_id, category_id) -- 중복 방지
);
