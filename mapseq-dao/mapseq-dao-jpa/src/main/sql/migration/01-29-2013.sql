create temporary table to_delete (job_guid int not null);
insert into to_delete select guid from job where workflow_run_guid is null;
delete from job where guid in (select job_guid from to_delete);
delete from entity_file_data where entity_guid in (select job_guid from to_delete);
delete from entity_entity_attribute where entity_guid in (select job_guid from to_delete);
delete from entity where guid in (select job_guid from to_delete);

