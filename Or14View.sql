/********************
파일명 : Or14View.sql
View(뷰)
설명 : View는 테이블로부터 생성된 가상의 테이블로 물리적으로는 존재하지
    않고 논리적으로 존재하는 테이블이다.
*********************/

/*
뷰의 생성
형식]
    create [or replace] view 뷰이름 [(컬럼1, 컬럼2,........]
    as
    select * from 테이블명 where 조건1 and 조건2 ... 조건N
        -> select절은 join문도 가능하다. 
*/

/*
시나리오] hr계정의 사원테이블에서 직무아이디가 ST_CLERK인 사원의 정보를 
    조회할수 있는 View를 생성하시오.
    출력항목 : 사원아이디, 이름, 직무아이디, 입사일, 부서아이디
*/
--step1 : 시나리오 조건대로 select해보기
select 
    employee_id, first_name, job_id, hire_date, department_id
from employees
where job_id=upper('st_clerk');

--stpe2 : 뷰 생성
create view view_employees (emp_id, fname, j_id, h_date, deptid)
as
    select 
        employee_id, first_name, job_id, hire_date, department_id
    from employees
    where job_id=upper('st_clerk');

--step3 : 뷰 호출하기
select * from view_employees;
select emp_id, fname, j_id from view_employees;

--step4 : 데이터사전에서 View 확인하기
select * from user_views where view_name=upper('view_employees');



/*
뷰의 수정
형식]
    create or replace view 뷰이름 [(컬럼1, 컬럼2,........]
    as
    select * from 테이블명 where 조건1 and 조건2 ... 조건N
        -> select절은 join문도 가능하다. 
    
    - 해당뷰가 존재한다면 수정된다. 즉 기존의 뷰를 덮어쓰기 하게된다. 
    - 기존에 생성된 뷰가 없다면 새롭게 생성된다. 즉 처음 생성할때부터
    or replace를 써도 상관없다. 
    - 단 동일한 이름의 뷰가 존재한다면 조심해야 한다. 

시나리오]위에서 생성한 view_employees뷰를 아래 조건에 맞게 수정하시오.
직무아이디가 ST_MAN인 사원의 사원번호, 이름, 이메일, 매니져 아이디를 조회할수 있도록 수정하시오.
뷰의 컬럼명은 e_id, name, email, m_id로 지정. 단이름은 first_name과 last_name이 연결된 형태로 출력.
*/
--1단계
SELECT
    EMPLOYEE_id, concat(first_name||'',last_name), email, manager_id
FROM EMPLOYEES
where job_id='ST_MAN';

--2단계
create or replace view view_employees(e_id, name, email, m_id) as 
 select
 EMPLOYEE_id, concat(first_name||'',last_name), email, manager_id
 FROM EMPLOYEES
 where job_id='ST_MAN';
 
 --3단계
 select *FROM view_employees;
 
/*
사원번호, 이름, 연봉을 계산하여 출력하는 뷰를 출력하시오.
컬럼의 이름은 emp_id, I_name, annual_sal로 지정하시오.
연봉계산식->(급여+(급여*보너스율))*12
뷰이름 : v_emp_salary
단, 연봉은 세자리마다 컴마가 삽입되어야 한다.
*/
select 
 employee_id, first_name, 
to_char(salary+(salary*nvl(commission_pct, 0))*12, '999,999,000')"연봉"
from employees;

create or replace view v_emp_salary as 
 select 
  employee_id, first_name, 
  to_char(salary+(salary*nvl(commission_pct, 0))*12, '999,999,000')"연봉"
  from employees;
SELECT
    *
FROM v_emp_salary;

--------------------------------------------------------------------------------
-----------------연 습 문 제 --------------------------------------------------
--------------------------------------------------------------------------------


/*
-조인을 통한 View 생성
연습문제] 사원테이블과 부서테이블을 조인하여 다음 조건에 맞는 뷰를 생성하시오.
출력항목 : 사원번호, 전체이름, 부서번호, 부서명, 입사일자, 지역명
뷰의명칭 : v_emp_join
뷰의컬럼 : empid, fullname, deptid, deptname, hdate, locname
컬럼의 출력형태 : 
	fullname => first_name+last_name 
	locname => XXX주의 YYY (ex : Texas주의 Southlake)
	hdate => 0000년00월00일
*/

create or replace view v_emp_join (empid, fullname, deptid, deptname, hdate, locname)
as 
    select
        emp.employee_id, concat(concat(first_name,' '),last_name),
        emp.department_id, department_name, to_char(hire_date,'yyyy"년"mm"월"dd"일"'), state_province||' 주의 '||city
    from employees emp 
        inner join departments dep
            on emp.department_id=dep.department_id
        inner join locations loc
            on dep.location_id=loc.location_id
    where 1=1;
select * from v_emp_join;
