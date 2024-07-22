insert into runninghi.tbl_post (post_no, create_date, update_date, difficulty, distance ,kcal, mean_pace, time, gpx_url, location_name, post_content, report_cnt, role, status, member_no, main_data, geometry)
values
    (1, '2024-06-06 16:36:12', '2024-06-06 16:36:59', 0, 12.3, 250, 300, 4500, null, '부산', '테스트', 0, 0, true, 2, '200Kcal',  ST_GeomFromText('POINT(126.543 36.9876)', 4326, 'axis-order=long-lat')),
    (2, '2024-06-06 16:36:34', '2024-06-06 16:36:59', 1, 8.7,  180, 420, 3000, null, '경기도', '테스트2', 0, 1, true, 870, '3시간 26분',  ST_GeomFromText('POINT(129.876 35.4321)', 4326, 'axis-order=long-lat')),
    (3, '2024-06-06 16:36:59', '2024-06-06 16:36:59', 2,30, 180, 420,  3000, null, '대구', '테스트', 0, 1, true, 870, '5.8 KM',  ST_GeomFromText('POINT(129.876 35.4321)', 4326, 'axis-order=long-lat')),
    (4, '2023-08-16 07:39:19', null, 3, 54, 324, 300, 1675, null, '경남', '1 번째 게시글입니다.', 8, 0, true, 870, '200Kcal',  ST_GeomFromText('POINT(128.895 33.1083)', 4326, 'axis-order=long-lat')),
    (5, '2023-05-29 14:52:49', null, 0, 56.3, 633, 240,   6838, null, '광주', '2 번째 게시글입니다.', 8, 0, true, 870, '3시간 26분',  ST_GeomFromText('POINT(126.023 34.3751)', 4326, 'axis-order=long-lat')),
    (6, '2024-01-15 19:25:33', null, 4, 42.8, 180, 300,  7407, null, '전북', '3 번째 게시글입니다.', 4, 0, true, 870, '5.8 KM',  ST_GeomFromText('POINT(128.011 34.1147)', 4326, 'axis-order=long-lat')),
    (7, '2024-05-17 11:47:54', null, 3, 73.5, 716, 420, 5410, null, '제주', '4 번째 게시글입니다.', 6, 0, true, 870, '200Kcal',  ST_GeomFromText('POINT(126.815 37.5256)', 4326, 'axis-order=long-lat')),
    (8, '2023-12-01 13:16:06', null, 4, 12.1611, 300, 300, 6000,  null, '울산', '5 번째 게시글입니다.', 0, 0, true, 870, '3시간 26분',  ST_GeomFromText('POINT(125.557 34.9739)', 4326, 'axis-order=long-lat')),
    (9, '2024-03-29 13:16:06', null, 3, 18.79, 409, 240, 1293, null, '서울', '6 번째 게시글입니다.', 0, 0, true, 870, '5.8 KM',  ST_GeomFromText('POINT(128.802 34.6162)', 4326, 'axis-order=long-lat')),
    (10, '2023-07-13 13:16:06', null, 0, 16.18, 766, 180,   4071, null, '충청남도', '7 번째 게시글입니다.', 0, 0, true, 870, '200Kcal',  ST_GeomFromText('POINT(129.856 34.4566)', 4326, 'axis-order=long-lat')),
    (11, '2024-05-17 13:16:06', null, 1, 15.81, 728, 600, 6840, null, '경상북도', '8 번째 게시글입니다.', 0, 0, true, 870, '3시간 26분',  ST_GeomFromText('POINT(129.602 37.1931)', 4326, 'axis-order=long-lat')),
    (12, '2023-11-07 13:16:06', null, 3, 15.76, 148, 360, 3948, null, '강원도', '9 번째 게시글입니다.', 0, 0, true, 870, '5.8 KM',  ST_GeomFromText('POINT(128.378 34.3535)', 4326, 'axis-order=long-lat')),
    (13, '2023-12-22 13:16:06', null, 3, 15.36, 745, 240,  4196, null, '전라남도', '10 번째 게시글입니다.', 0, 0, true, 870, '200Kcal',  ST_GeomFromText('POINT(125.287 37.7886)', 4326, 'axis-order=long-lat')),
    (14, '2023-11-22 13:16:06', null, 0, 4.88,  201, 180, 7244, null, '경상남도', '11 번째 게시글입니다.', 0, 0, true, 870, '3시간 26분',  ST_GeomFromText('POINT(128.184 37.0123)', 4326, 'axis-order=long-lat')),
    (15, '2024-02-15 13:16:06', null, 0, 15.18,  891, 240, 2414, null, '충청남도', '12 번째 게시글입니다.', 0, 0, true, 870, '5.8 KM',  ST_GeomFromText('POINT(126.632 35.5335)', 4326, 'axis-order=long-lat')),
    (16, '2023-06-25 13:16:06', null, 4, 6.91, 888, 360,  7075, null, '전라남도', '13 번째 게시글입니다.', 0, 0, true, 870, '200Kcal',  ST_GeomFromText('POINT(126.83 35.1246)', 4326, 'axis-order=long-lat')),
    (17, '2024-05-10 13:16:06', null, 1, 8.24,  324, 540, 1571, null, '대전', '14 번째 게시글입니다.', 0, 0, true, 870, '3시간 26분',  ST_GeomFromText('POINT(129.633 33.1568)', 4326, 'axis-order=long-lat')),
    (18, '2023-11-27 13:16:06', null, 2, 6.92, 161, 300,  8365, null, '대구', '15 번째 게시글입니다.', 0, 0, true, 870, '5.8 KM',  ST_GeomFromText('POINT(124.328 37.674)', 4326, 'axis-order=long-lat')),
    (19, '2023-07-26 13:16:06', null, 2, 18.64, 169, 600,  3138, null, '광주', '16 번째 게시글입니다.', 0, 0, true, 870, '200Kcal',  ST_GeomFromText('POINT(128.708 34.747)', 4326, 'axis-order=long-lat')),
    (20, '2023-12-24 13:16:06', null, 1, 2.06,  614, 300,   3807, null, '세종', '17 번째 게시글입니다.', 0, 0, true, 870, '3시간 26분',  ST_GeomFromText('POINT(129.963 37.6143)', 4326, 'axis-order=long-lat')),
    (21, '2023-11-20 13:16:06', null, 0, 8.18, 899, 420,  1970, null, '부산', '18 번째 게시글입니다.', 0, 0, true, 870, '5.8 KM',  ST_GeomFromText('POINT(124.681 36.254)', 4326, 'axis-order=long-lat')),
    (22, '2024-03-27 13:16:06', null, 4, 11.05,  281, 420,  6333, null, '서울', '19 번째 게시글입니다.', 0, 0, true, 482, '200Kcal',  ST_GeomFromText('POINT(125.748 34.9374)', 4326, 'axis-order=long-lat')),
    (23, '2024-04-26 13:16:06', null, 0, 5.34,  425, 480,  5474, null, '강원도', '20 번째 게시글입니다.', 0, 0, true, 482, '3시간 26분',  ST_GeomFromText('POINT(126.994 37.4341)', 4326, 'axis-order=long-lat')),
    (24, '2023-12-07 13:16:06', null, 0, 4.72,  504, 420,  8876, null, '경기도', '21 번째 게시글입니다.', 0, 0, true, 482, '5.8 KM',  ST_GeomFromText('POINT(124.818 36.2935)', 4326, 'axis-order=long-lat')),
    (25, '2023-08-20 13:16:06', null, 3, 8.46,  693, 300,   3222, null, '울산', '22 번째 게시글입니다.', 0, 0, true, 482, '200Kcal',  ST_GeomFromText('POINT(127.998 33.0312)', 4326, 'axis-order=long-lat')),
    (26, '2024-03-06 13:16:06', null, 0, 1.17,  320, 540,  6461, null, '충청북도', '23 번째 게시글입니다.', 0, 0, true, 482, '3시간 26분',  ST_GeomFromText('POINT(125.569 34.8728)', 4326, 'axis-order=long-lat')),
    (27, '2023-07-07 13:16:06', null, 2, 19.26,  368, 240,   2040, null, '경기도', '24 번째 게시글입니다.', 0, 0, true, 482, '5.8 KM',  ST_GeomFromText('POINT(128.595 35.8082)', 4326, 'axis-order=long-lat')),
    (28, '2024-01-26 13:16:06', null, 0, 4.04,  868, 360,   9405, null, '강원도', '25 번째 게시글입니다.', 0, 0, true, 482, '200Kcal',  ST_GeomFromText('POINT(127.438 36.8504)', 4326, 'axis-order=long-lat')),
    (29, '2024-04-05 13:16:07', null, 3, 19.55,  771, 540,   9407, null, '강원도', '26 번째 게시글입니다.', 0, 0, true, 482, '3시간 26분',  ST_GeomFromText('POINT(124.829 35.8427)', 4326, 'axis-order=long-lat')),
    (30, '2024-02-04 14:23:39', null, 1, 6.8,  395, 180,   6120, null, '대전', '27 번째 게시글입니다.', 10, 0, true, 482, '5.8 KM',  ST_GeomFromText('POINT(125.153 34.5964)', 4326, 'axis-order=long-lat')),
    (31, '2023-07-20 16:39:04', null, 0, 58.4, 122, 360,   6430, null, '광주', '28 번째 게시글입니다.', 0, 0, true, 1255, '200Kcal',  ST_GeomFromText('POINT(130.54 33.539)', 4326, 'axis-order=long-lat')),
    (32, '2023-08-08 19:34:50', null, 3, 74.9,  220, 420,   2533, null, '인천', '28 번째 게시글입니다.', 7, 0, true, 1255, '3시간 26분',  ST_GeomFromText('POINT(127.623 34.9016)', 4326, 'axis-order=long-lat')),
    (33, '2023-08-27 05:42:32', null, 2, 32.4,  946, 540,   5189, null, '대전', '30 번째 게시글입니다.', 3, 0, true, 1255, '5.8 KM',  ST_GeomFromText('POINT(128.349 36.6726)', 4326, 'axis-order=long-lat')),
    (34, '2023-12-21 06:10:41', null, 0, 65.7,  898, 540,   4488, null, '세종', '31 번째 게시글입니다.', 8, 0, true, 1255, '200Kcal',  ST_GeomFromText('POINT(126.333 34.3086)', 4326, 'axis-order=long-lat')),
    (35, '2023-06-11 23:12:29', null, 3, 5.6,  737, 420,   4163, null, '충북', '32 번째 게시글입니다.', 8, 0, true, 1255, '3시간 26분',  ST_GeomFromText('POINT(127.386 36.1882)', 4326, 'axis-order=long-lat')),
    (36, '2024-04-27 17:15:52', null, 0, 30.3,  107, 540,  3630, null, '경북', '33 번째 게시글입니다.', 3, 0, true, 1255, '5.8 KM',  ST_GeomFromText('POINT(125.807 35.1851)', 4326, 'axis-order=long-lat')),
    (37, '2023-07-15 20:14:06', null, 1, 15.8, 415, 480,   9888, null, '경남', '34 번째 게시글입니다.', 9, 0, true, 1255, '200Kcal',  ST_GeomFromText('POINT(127.405 36.758)', 4326, 'axis-order=long-lat')),
    (38, '2024-03-18 22:15:50', null, 3, 12.2,  136, 540,   1726, null, '경기', '35 번째 게시글입니다.', 4, 0, true, 1255, '3시간 26분',  ST_GeomFromText('POINT(125.744 33.2692)', 4326, 'axis-order=long-lat')),
    (39, '2023-09-30 14:21:26', null, 0, 44,  788, 540,   5580, null, '경기', '36 번째 게시글입니다.', 8, 0, true, 1255, '5.8 KM',  ST_GeomFromText('POINT(125.863 35.7978)', 4326, 'axis-order=long-lat')),
    (40, '2023-09-06 22:12:57', null, 3, 24.6,  877, 360,   3391, null, '경기', '37 번째 게시글입니다.', 7, 0, true, 1255, '200Kcal',  ST_GeomFromText('POINT(126.736 37.5993)', 4326, 'axis-order=long-lat')),
    (41, '2024-05-10 15:16:11', null, 4, 7.8,  388, 300,   5895, null, '전남', '38 번째 게시글입니다.', 4, 0, true, 1255, '3시간 26분',  ST_GeomFromText('POINT(128.982 35.8541)', 4326, 'axis-order=long-lat')),
    (42, '2023-07-15 16:14:45', null, 3, 95, 375, 300,  1544, null, '경기', '39 번째 게시글입니다.', 2, 0, true, 1255, '5.8 KM',  ST_GeomFromText('POINT(127.826 33.788)', 4326, 'axis-order=long-lat')),
    (43, '2023-06-15 05:26:29', null, 0, 24,  849, 360,   4158, null, '대전', '40 번째 게시글입니다.', 6, 0, true, 3107, '200Kcal',  ST_GeomFromText('POINT(124.901 36.9883)', 4326, 'axis-order=long-lat')),
    (44, '2024-03-13 01:50:55', null, 4, 53,  888, 300,   2700, null, '전남', '41 번째 게시글입니다.', 8, 0, true, 3107, '3시간 26분',  ST_GeomFromText('POINT(130.643 33.5191)', 4326, 'axis-order=long-lat')),
    (45, '2023-11-27 23:22:04', null, 1, 5.3,  243, 240,   2917, null, '광주', '42 번째 게시글입니다.', 4, 0, true, 3107, '5.8 KM',  ST_GeomFromText('POINT(125.277 36.1072)', 4326, 'axis-order=long-lat')),
    (46, '2023-12-22 02:18:55', null, 3, 25.4,  642, 420,   7212, null, '제주', '43 번째 게시글입니다.', 1, 0, true, 3107, '200Kcal',  ST_GeomFromText('POINT(128.883 34.3007)', 4326, 'axis-order=long-lat')),
    (47, '2024-04-21 09:55:56', null, 0, 20.4, 220, 180,   4812, null, '서울', '44 번째 게시글입니다.', 2, 0, true, 3107, '3시간 26분',  ST_GeomFromText('POINT(124.601 33.186)', 4326, 'axis-order=long-lat')),
    (48, '2024-02-15 10:17:22', null, 3, 13.6, 517, 360,  2821, null, '서울', '45 번째 게시글입니다.', 9, 0, true, 3107, '5.8 KM',  ST_GeomFromText('POINT(130.84 33.002)', 4326, 'axis-order=long-lat')),
    (49, '2024-04-15 09:37:53', null, 1, 96.8,  796, 600,   3875, null, '충남', '46 번째 게시글입니다.', 3, 0, true, 3107, '200Kcal',  ST_GeomFromText('POINT(125.373 37.5485)', 4326, 'axis-order=long-lat')),
    (50, '2023-10-18 18:43:36', null, 0, 90.5,  596, 480,   2347, null, '충남', '47 번째 게시글입니다.', 6, 0, true, 3107, '3시간 26분',  ST_GeomFromText('POINT(125.314 33.5128)', 4326, 'axis-order=long-lat')),
    (51, '2024-01-17 20:12:01', null, 2, 22.8,  249, 600,   7311, null, '전북', '48 번째 게시글입니다.', 7, 0, true, 3107, '5.8 KM',  ST_GeomFromText('POINT(126.773 35.8745)', 4326, 'axis-order=long-lat'));

-- 거리 테스트 시 사용
# SELECT COUNT(*)
# FROM tbl_post
# WHERE status = true
#   AND ST_Distance_Sphere(tbl_post.geometry, ST_GeomFromText('POINT(127.543 36.9876)', 4326, 'axis-order=long-lat')) <= 100000;
#
# SELECT post_no, ST_AsText(geometry), ST_Distance_Sphere(tbl_post.geometry,
#                                                    (SELECT geometry FROM tbl_post WHERE post_no = 1)) AS distance
# FROM tbl_post
# WHERE status = true;

