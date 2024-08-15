INSERT INTO tbl_member (member_no, name, nickname, alarm_consent, distance_to_next_level, is_active, is_blacklisted, level, report_cnt, total_distance, total_kcal, create_date, profile_url, geometry)
VALUES
    (1, '유저1', '러너 1', true, 10, true, false, 0, 0, 0, 0, '2024-08-15 16:21:28', null, ST_GeomFromText('POINT(129.602 37.1931)', 4326, 'axis-order=long-lat')),
    (2, '유저2', '러너 2', true, 10, true, false, 0, 0, 0, 0, '2024-08-15 16:21:30', null, ST_GeomFromText('POINT(129.602 37.1931)', 4326, 'axis-order=long-lat')),
    (3, '유저3', '러너 3', true, 10, true, false, 0, 0, 0, 0, '2024-08-15 16:21:31', null, ST_GeomFromText('POINT(129.602 37.1931)', 4326, 'axis-order=long-lat')),
    (4, '유저4', '러너 4', true, 10, true, false, 0, 0, 0, 0, '2024-08-15 16:21:34', null, ST_GeomFromText('POINT(129.602 37.1931)', 4326, 'axis-order=long-lat')),
    (5, '유저5', '러너 5', true, 10, true, false, 0, 0, 0, 0, '2024-08-15 16:21:36', null, ST_GeomFromText('POINT(129.602 37.1931)', 4326, 'axis-order=long-lat'));


UPDATE tbl_member
SET geometry = ST_GeomFromText('POINT(127.518 37.263)', 4326, 'axis-order=long-lat')
WHERE member_no =11246;


