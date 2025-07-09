-- USERS
INSERT INTO users (user_id, user_name, email, password, role, user_source, is_locked,
                   is_using_temp_password, create_at, update_at)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'Alice', 'alice@example.com', 'pass', 'USER', 'LOCAL', FALSE, FALSE, NOW(), NOW()),
    ('22222222-2222-2222-2222-222222222222', 'Bob', 'bob@example.com', 'pass', 'USER', 'LOCAL', FALSE, FALSE, NOW(), NOW()),
    ('33333333-3333-3333-3333-333333333333', 'Carol', 'carol@example.com', 'pass', 'USER', 'LOCAL', FALSE, FALSE, NOW(), NOW()),
    ('44444444-4444-4444-4444-444444444444', 'Dave', 'dave@example.com', 'pass', 'USER', 'LOCAL', FALSE, FALSE, NOW(), NOW()),
    ('55555555-5555-5555-5555-555555555555', 'Eve', 'eve@example.com', 'pass', 'USER', 'LOCAL', FALSE, FALSE, NOW(), NOW());

-- CONTENTS
INSERT INTO contents (
    content_id, external_id, source, title, summary, category,
    poster_url, genres, released_at, created_at, updated_at,
    average_rating, total_rating_count
) VALUES
      ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1', 'ext1', 'TMDB', 'Content 1', 'Summary 1', 'MOVIE', '', '["Drama"]',
       '2025-07-09T00:00:00+00:00', '2025-07-09T00:00:00+00:00', '2025-07-09T00:00:00+00:00', 4.5, 100),

      ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa2', 'ext2', 'TMDB', 'Content 2', 'Summary 2', 'SHOW', '', '["Comedy"]',
       '2025-07-09T01:00:00+00:00', '2025-07-09T01:00:00+00:00', '2025-07-09T01:00:00+00:00', 3.7, 60),

      ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa3', 'ext3', 'TMDB', 'Content 3', 'Summary 3', 'MOVIE', '', '["Action"]',
       '2025-07-09T02:00:00+00:00', '2025-07-09T02:00:00+00:00', '2025-07-09T02:00:00+00:00', 4.8, 200),

      ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa4', 'ext4', 'TMDB', 'Content 4', 'Summary 4', 'SHOW', '', '["Horror"]',
       '2025-07-09T03:00:00+00:00', '2025-07-09T03:00:00+00:00', '2025-07-09T03:00:00+00:00', 2.9, 30),

      ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa5', 'ext5', 'TMDB', 'Content 5', 'Summary 5', 'MOVIE', '', '["Fantasy"]',
       '2025-07-09T04:00:00+00:00', '2025-07-09T04:00:00+00:00', '2025-07-09T04:00:00+00:00', 3.2, 50);

-- REVIEWS
INSERT INTO reviews (review_id, content_id, user_id, rating, comment, created_at)
VALUES
    ('00000000-0000-0000-0000-000000000001', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1', '11111111-1111-1111-1111-111111111111', 5, 'Loved it', '2025-07-09T00:00:00+00:00'),
    ('00000000-0000-0000-0000-000000000002', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1', '22222222-2222-2222-2222-222222222222', 4, 'Nice',     '2025-07-09T00:10:00+00:00'),
    ('00000000-0000-0000-0000-000000000003', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa2', '33333333-3333-3333-3333-333333333333', 3, 'Okay',     '2025-07-09T00:20:00+00:00'),
    ('00000000-0000-0000-0000-000000000004', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa3', '44444444-4444-4444-4444-444444444444', 5, 'Awesome',  '2025-07-09T00:30:00+00:00'),
    ('00000000-0000-0000-0000-000000000005', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa4', '55555555-5555-5555-5555-555555555555', 2, 'Not great','2025-07-09T00:40:00+00:00');

-- CONTENT LIKES
INSERT INTO content_likes (user_id, content_id, liked_at)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1', NOW()),
    ('22222222-2222-2222-2222-222222222222', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1', NOW()),
    ('33333333-3333-3333-3333-333333333333', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa2', NOW()),
    ('44444444-4444-4444-4444-444444444444', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa3', NOW()),
    ('55555555-5555-5555-5555-555555555555', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa5', NOW());

-- PLAYLISTS
INSERT INTO playlists (playlist_id, user_id, title, description, is_public, created_at, updated_at)
VALUES
    ('66666666-6666-6666-6666-666666666661', '11111111-1111-1111-1111-111111111111', 'My Playlist 1', 'desc', TRUE, NOW(), NOW()),
    ('66666666-6666-6666-6666-666666666662', '22222222-2222-2222-2222-222222222222', 'My Playlist 2', 'desc', TRUE, NOW(), NOW()),
    ('66666666-6666-6666-6666-666666666663', '33333333-3333-3333-3333-333333333333', 'My Playlist 3', 'desc', FALSE, NOW(), NOW()),
    ('66666666-6666-6666-6666-666666666664', '44444444-4444-4444-4444-444444444444', 'My Playlist 4', 'desc', TRUE, NOW(), NOW()),
    ('66666666-6666-6666-6666-666666666665', '55555555-5555-5555-5555-555555555555', 'My Playlist 5', 'desc', FALSE, NOW(), NOW());

-- PLAYLISTS_CONTENTS
INSERT INTO playlists_contents (playlist_id, content_id)
VALUES
    ('66666666-6666-6666-6666-666666666661', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1'),
    ('66666666-6666-6666-6666-666666666661', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa2'),
    ('66666666-6666-6666-6666-666666666662', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa3'),
    ('66666666-6666-6666-6666-666666666663', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1'),
    ('66666666-6666-6666-6666-666666666664', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa5');

-- PLAYLIST LIKES
INSERT INTO playlist_likes (user_id, playlist_id)
VALUES
    ('11111111-1111-1111-1111-111111111111', '66666666-6666-6666-6666-666666666661'),
    ('22222222-2222-2222-2222-222222222222', '66666666-6666-6666-6666-666666666662'),
    ('33333333-3333-3333-3333-333333333333', '66666666-6666-6666-6666-666666666663'),
    ('44444444-4444-4444-4444-444444444444', '66666666-6666-6666-6666-666666666664'),
    ('55555555-5555-5555-5555-555555555555', '66666666-6666-6666-6666-666666666665');

-- SUBSCRIBES
INSERT INTO subscribes (subscribe_id, user_id, playlist_id, subscribed_at)
VALUES
    ('77777777-7777-7777-7777-777777777771', '11111111-1111-1111-1111-111111111111', '66666666-6666-6666-6666-666666666661', NOW()),
    ('77777777-7777-7777-7777-777777777772', '22222222-2222-2222-2222-222222222222', '66666666-6666-6666-6666-666666666662', NOW()),
    ('77777777-7777-7777-7777-777777777773', '33333333-3333-3333-3333-333333333333', '66666666-6666-6666-6666-666666666663', NOW()),
    ('77777777-7777-7777-7777-777777777774', '44444444-4444-4444-4444-444444444444', '66666666-6666-6666-6666-666666666664', NOW()),
    ('77777777-7777-7777-7777-777777777775', '55555555-5555-5555-5555-555555555555', '66666666-6666-6666-6666-666666666665', NOW());

-- FOLLOWS
INSERT INTO follows (follow_id, follower_id, followee_id)
VALUES
    ('88888888-8888-8888-8888-888888888881', '11111111-1111-1111-1111-111111111111', '22222222-2222-2222-2222-222222222222'),
    ('88888888-8888-8888-8888-888888888882', '22222222-2222-2222-2222-222222222222', '33333333-3333-3333-3333-333333333333'),
    ('88888888-8888-8888-8888-888888888883', '33333333-3333-3333-3333-333333333333', '44444444-4444-4444-4444-444444444444'),
    ('88888888-8888-8888-8888-888888888884', '44444444-4444-4444-4444-444444444444', '55555555-5555-5555-5555-555555555555'),
    ('88888888-8888-8888-8888-888888888885', '55555555-5555-5555-5555-555555555555', '11111111-1111-1111-1111-111111111111');
