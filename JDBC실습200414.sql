---------------------------------------------
--��Ʈ��ũ ���α׷��� �̴� ������Ʈ
/*
Ŭ���̾�Ʈ�� ��ȭ ������ ������ ���̺� ����
������ ���� : ������, ��ȭ��, ��ȭ����, ����ð�.
(�������� ����� �÷��� primary key�� �����Ѵ�.
���̺�� : chating_tb
*/

Create table chating_tb(
g_idx number(10) primary key,
g_name varchar2(30)
);
--��ǰ���̺� ���ڵ� �Է�
insert into chating_tb values(1, '�ʹ���');
insert into chating_tb values(2, '��������');
select max(g_idx)+1 from chating_tb;
select 
    to_char(sysdate, 'hh:mi:ss')"����ð�ǥ��"
from dual;