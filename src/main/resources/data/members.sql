INSERT INTO tbl_member (deactivate_date, alarm_consent, name, role, is_active, total_distance, total_kcal, distance_to_next_level, level, report_cnt, is_blacklisted)
VALUES
    (null, true, 'name', 'USER', false, 1000, 5000, 30, 1, 0, false),
    (null, true, 'name2', 'USER', false, 1000, 5000, 30, 1, 0, false),
    (NOW() - INTERVAL 15 DAY, true, 'nyam', 'USER', true, 1000, 5000, 30, 1, 0, false);