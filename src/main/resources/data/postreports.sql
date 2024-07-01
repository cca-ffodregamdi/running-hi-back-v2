INSERT INTO tbl_post_report (category, content, status, reporter_no, reported_post_no, post_content, runninghi.tbl_post_report.is_post_deleted)
VALUES
    ('SPAM', '스팸 게시글입니다.', 'INPROGRESS', 2, 1, (SELECT post_content FROM tbl_post WHERE post_no = 1),false),
    ('ILLEGAL', '부적절한 내용이 포함된 게시글입니다.', 'INPROGRESS', 1, 2, (SELECT post_content FROM tbl_post WHERE post_no = 2),false),
    ('ILLEGAL', '부적절한 내용이 포함된 게시글입니다.', 'INPROGRESS', 2, 2, (SELECT post_content FROM tbl_post WHERE post_no = 2),false);

