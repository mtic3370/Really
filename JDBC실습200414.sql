---------------------------------------------
--네트워크 프로그래밍 미니 프로젝트
/*
클라이언트의 대화 내용을 저장할 테이블 생성
저장할 내용 : 시퀀스, 대화명, 대화내용, 현재시간.
(시퀀스가 저장될 컬럼을 primary key를 설정한다.
테이블명 : chating_tb
*/

Create table chating_tb(
g_idx number(10) primary key,
g_name varchar2(30)
);
--상품테이블에 레코드 입력
insert into chating_tb values(1, '넌누구');
insert into chating_tb values(2, '내가내다');
select max(g_idx)+1 from chating_tb;
select 
    to_char(sysdate, 'hh:mi:ss')"현재시간표시"
from dual;