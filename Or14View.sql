/********************
���ϸ� : Or14View.sql
View(��)
���� : View�� ���̺�κ��� ������ ������ ���̺�� ���������δ� ��������
    �ʰ� �������� �����ϴ� ���̺��̴�.
*********************/

/*
���� ����
����]
    create [or replace] view ���̸� [(�÷�1, �÷�2,........]
    as
    select * from ���̺�� where ����1 and ����2 ... ����N
        -> select���� join���� �����ϴ�. 
*/

/*
�ó�����] hr������ ������̺��� �������̵� ST_CLERK�� ����� ������ 
    ��ȸ�Ҽ� �ִ� View�� �����Ͻÿ�.
    ����׸� : ������̵�, �̸�, �������̵�, �Ի���, �μ����̵�
*/
--step1 : �ó����� ���Ǵ�� select�غ���
select 
    employee_id, first_name, job_id, hire_date, department_id
from employees
where job_id=upper('st_clerk');

--stpe2 : �� ����
create view view_employees (emp_id, fname, j_id, h_date, deptid)
as
    select 
        employee_id, first_name, job_id, hire_date, department_id
    from employees
    where job_id=upper('st_clerk');

--step3 : �� ȣ���ϱ�
select * from view_employees;
select emp_id, fname, j_id from view_employees;

--step4 : �����ͻ������� View Ȯ���ϱ�
select * from user_views where view_name=upper('view_employees');



/*
���� ����
����]
    create or replace view ���̸� [(�÷�1, �÷�2,........]
    as
    select * from ���̺�� where ����1 and ����2 ... ����N
        -> select���� join���� �����ϴ�. 
    
    - �ش�䰡 �����Ѵٸ� �����ȴ�. �� ������ �並 ����� �ϰԵȴ�. 
    - ������ ������ �䰡 ���ٸ� ���Ӱ� �����ȴ�. �� ó�� �����Ҷ�����
    or replace�� �ᵵ �������. 
    - �� ������ �̸��� �䰡 �����Ѵٸ� �����ؾ� �Ѵ�. 

�ó�����]������ ������ view_employees�並 �Ʒ� ���ǿ� �°� �����Ͻÿ�.
�������̵� ST_MAN�� ����� �����ȣ, �̸�, �̸���, �Ŵ��� ���̵� ��ȸ�Ҽ� �ֵ��� �����Ͻÿ�.
���� �÷����� e_id, name, email, m_id�� ����. ���̸��� first_name�� last_name�� ����� ���·� ���.
*/
--1�ܰ�
SELECT
    EMPLOYEE_id, concat(first_name||'',last_name), email, manager_id
FROM EMPLOYEES
where job_id='ST_MAN';

--2�ܰ�
create or replace view view_employees(e_id, name, email, m_id) as 
 select
 EMPLOYEE_id, concat(first_name||'',last_name), email, manager_id
 FROM EMPLOYEES
 where job_id='ST_MAN';
 
 --3�ܰ�
 select *FROM view_employees;
 
/*
�����ȣ, �̸�, ������ ����Ͽ� ����ϴ� �並 ����Ͻÿ�.
�÷��� �̸��� emp_id, I_name, annual_sal�� �����Ͻÿ�.
��������->(�޿�+(�޿�*���ʽ���))*12
���̸� : v_emp_salary
��, ������ ���ڸ����� �ĸ��� ���ԵǾ�� �Ѵ�.
*/
select 
 employee_id, first_name, 
to_char(salary+(salary*nvl(commission_pct, 0))*12, '999,999,000')"����"
from employees;

create or replace view v_emp_salary as 
 select 
  employee_id, first_name, 
  to_char(salary+(salary*nvl(commission_pct, 0))*12, '999,999,000')"����"
  from employees;
SELECT
    *
FROM v_emp_salary;

--------------------------------------------------------------------------------
-----------------�� �� �� �� --------------------------------------------------
--------------------------------------------------------------------------------


/*
-������ ���� View ����
��������] ������̺�� �μ����̺��� �����Ͽ� ���� ���ǿ� �´� �並 �����Ͻÿ�.
����׸� : �����ȣ, ��ü�̸�, �μ���ȣ, �μ���, �Ի�����, ������
���Ǹ�Ī : v_emp_join
�����÷� : empid, fullname, deptid, deptname, hdate, locname
�÷��� ������� : 
	fullname => first_name+last_name 
	locname => XXX���� YYY (ex : Texas���� Southlake)
	hdate => 0000��00��00��
*/

create or replace view v_emp_join (empid, fullname, deptid, deptname, hdate, locname)
as 
    select
        emp.employee_id, concat(concat(first_name,' '),last_name),
        emp.department_id, department_name, to_char(hire_date,'yyyy"��"mm"��"dd"��"'), state_province||' ���� '||city
    from employees emp 
        inner join departments dep
            on emp.department_id=dep.department_id
        inner join locations loc
            on dep.location_id=loc.location_id
    where 1=1;
select * from v_emp_join;
