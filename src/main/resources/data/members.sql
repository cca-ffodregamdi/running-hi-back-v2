INSERT INTO tbl_member (deactivate_date, alarm_consent, name, role, is_active, total_distance, total_kcal, distance_to_next_level, level, report_cnt, is_blacklisted, geometry)
VALUES
    (null, true, 'name', 'USER', false, 1000, 5000, 30, 1, 0, false,ST_GeomFromText('POINT(129.602 37.1931)', 4326, 'axis-order=long-lat')),
    (null, true, 'name2', 'USER', false, 1000, 5000, 30, 1, 0, false,ST_GeomFromText('POINT(129.602 37.1931)', 4326, 'axis-order=long-lat')),
    (NOW() - INTERVAL 15 DAY, true, 'nyam', 'USER', true, 1000, 5000, 30, 1, 0, false,ST_GeomFromText('POINT(129.602 37.1931)', 4326, 'axis-order=long-lat'));

UPDATE tbl_member
SET geometry = ST_GeomFromText('POINT(129.602 37.1931)', 4326, 'axis-order=long-lat')
WHERE member_no = 4;
