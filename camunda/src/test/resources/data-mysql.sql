

USE camunda;



insert into BUSINESS_DEFINE(ID, NAME,PROCESS,DESCRIPTION_TEMPLATE) VALUES ('FIRE_CHECK','建设工程消防验收','construct-fire-check','项目名称为${name!"-"}, 面积${area!"-"}');

insert into BUSINESS_FILE_DEFINE(BUSINESS, _ORDER,NAME,DESCRIPTION, TYPE) VALUES ('FIRE_CHECK',1,'测试要件','测试','ATT');
insert into BUSINESS_FILE_DEFINE(BUSINESS, _ORDER,NAME,DESCRIPTION, TYPE) VALUES ('FIRE_CHECK',2,'测试要件1','测试','ATT');
insert into BUSINESS_FILE_DEFINE(BUSINESS, _ORDER,NAME,DESCRIPTION, TYPE) VALUES ('FIRE_CHECK',3,'测试要件2','测试','ATT');
insert into BUSINESS_FILE_DEFINE(BUSINESS, _ORDER,NAME,DESCRIPTION, TYPE) VALUES ('FIRE_CHECK',4,'测试要件3','测试','ATT');
insert into BUSINESS_FILE_DEFINE(BUSINESS, _ORDER,NAME,DESCRIPTION, TYPE) VALUES ('FIRE_CHECK',5,'测试要件4','测试','ATT');
insert into BUSINESS_FILE_DEFINE(BUSINESS, _ORDER,NAME,DESCRIPTION, TYPE) VALUES ('FIRE_CHECK',6,'测试要件5','测试','ATT');


