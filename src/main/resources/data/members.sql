INSERT INTO tbl_member (deactivate_date, alarm_consent, name, role, is_active, total_distance, total_kcal, distance_to_next_level, level)
VALUES
    ('2024-06-07 10:00:00', true, 'name', 'USER', false, 1000, 5000, 30, 1),
    (NOW() - INTERVAL 15 DAY, true, 'nyam', 'USER', true, 1000, 5000, 30, 1);