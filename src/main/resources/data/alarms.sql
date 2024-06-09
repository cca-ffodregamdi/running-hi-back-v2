INSERT INTO runninghi.tbl_alarm (member_no, title, content, is_read, create_date, read_date)
VALUES
    (1, '테스트 알림 1', '테스트 알림 내용 1', false, NOW() - INTERVAL 1 DAY, NULL),
    (1, '테스트 알림 2', '테스트 알림 내용 2', true, NOW() - INTERVAL 2 DAY, NOW() - INTERVAL 1 DAY),
    (2, '테스트 알림 3', '테스트 알림 내용 3 : 남아있는 테스트 알림입니다.', true, NOW() - INTERVAL 2 DAY, NOW() - INTERVAL 1 DAY);
