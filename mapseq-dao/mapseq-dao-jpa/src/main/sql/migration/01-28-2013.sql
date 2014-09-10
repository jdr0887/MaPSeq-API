update entity set type = 'Job' where guid in (select a.guid from Job a, entity b where a.guid = b.guid and b.type = 'Processing');
alter table htsf_sample drop column mean_quality_score_passing_filtering;
alter table htsf_sample drop column number_of_reads;
alter table htsf_sample drop column one_mismatch_reads_index;
alter table htsf_sample drop column passed_filtering;
alter table htsf_sample drop column perfect_index_reads;
alter table htsf_sample drop column q30_yeild_passing_filtering;
alter table htsf_sample drop column raw_clusters_per_lane;
alter table htsf_sample drop column yeild;