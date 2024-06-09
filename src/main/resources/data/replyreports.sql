INSERT INTO tbl_reply_report (category, content, status, reporter_no, reported_reply_no, reply_content, is_reply_deleted)
VALUES
    ('SPAM', '스팸 댓글입니다.', 'INPROGRESS', 1, 2, (SELECT reply_content FROM tbl_reply WHERE reply_no = 2), false),
    ('ILLEGALITY', '부적절한 내용이 포함된 댓글입니다.', 'INPROGRESS', 2, 1, (SELECT reply_content FROM tbl_reply WHERE reply_no = 1), false),
    ('ILLEGALITY', '부적절한 내용이 포함된 댓글입니다.', 'INPROGRESS', 2, 3, (SELECT reply_content FROM tbl_reply WHERE reply_no = 3), false);
