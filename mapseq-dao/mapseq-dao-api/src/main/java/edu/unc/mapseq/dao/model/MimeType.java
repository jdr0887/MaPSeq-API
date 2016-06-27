package edu.unc.mapseq.dao.model;

public enum MimeType {

    APPLICATION_GZIP("application/x-gzip"),

    APPLICATION_BAM("application/bam"),

    APPLICATION_BAM_INDEX("application/bam-index"),

    APPLICATION_BIGWIG("application/bigWig"),

    APPLICATION_R("application/R"),

    APPLICATION_SRF("application/srf"),

    APPLICATION_XLS("application/xls"),

    APPLICATION_POSTSCRIPT("application/postscript"),

    APPLICATION_PDF("application/pdf"),

    APPLICATION_ZIP("application/zip"),

    BEDGRAPH("bedGraph"),

    BIGWIG("bigWig"),

    BWA_BAM("bwa-bam"),

    BWA_SAI("bwa-sai"),

    CHEMICAL_SEQ_NA_FASTA("chemical/seq-na-fasta"),

    CHEMICAL_SEQ_NA_FASTQ("chemical/seq-na-fastq"),

    COMPOSITE_EXON_COVERAGE("composite_exon_coverage"),

    FASTQ("fastq"),

    BOWTIE_HITS("bowtie/hits"),

    GENE_QUANTIFICATION_TXT("gene.quantification.txt"),

    NCRNA_COUNTS_RPKM("ncRNA.counts_rpkm"),

    PNG_BCTREND("png/BCtrend"),

    PNG_BOXPLOT("png/boxplot"),

    IMAGE_PNG("image/png"),

    TEXT_GRAPHVIZ("text/x-graphviz"),

    TIFF("image/tiff"),

    PNG_COVERAGE_X_TRANSCRIPT_PLOT("png/coverageXTranscript plot"),

    PNG_GENE_CONVERAGE_PLOT("png/geneCoverage plot"),

    PNG_NT_DISTRIBUTION("png/ntDistribution"),

    SJCTS("sjcts"),

    TEXT_ALIGN_STAT("text/alignStat"),

    TEXT_BC_FLAG("text/BCflag"),

    TEXT_BC_TREND("text/BCtrend"),

    TEXT_BED("text/bed"),

    TEXT_COVERAGE_X_TRANSCRIPT("text/coverageXTranscript"),

    TEXT_EXON_QUANT("text/exon-quant"),

    TEXT_GENE_COVERAGE("text/geneCoverage"),

    TEXT_GENE_QUANT("text/gene-quant"),

    TEXT_JUNCTION_QUANT("text/junction-quant"),

    TEXT_KEY_VALUE("text/key-value"),

    TEXT_MAF("text/maf"),

    TEXT_CSV("text/csv"),

    TEXT_COVERAGE_DEPTH("text/coverageDepth"),

    TEXT_DEPTH_OF_COVERAGE_SUMMARY("text/depthOfCoverage-summary"),

    TEXT_INTERVALS("text/intervals"),

    TEXT_TRANCHES("text/tranches"),

    TEXT_PILEUP("text/pileup"),

    TEXT_PILEUP_CONSENSUS("text/pileup-consensus"),

    TEXT_PLAIN("text/plain"),

    TEXT_QC_GENOME("text/qc_genome"),

    TEXT_VCF("text/vcf"),

    TEXT_RECALIBRATION("text/recalibration"),

    TEXT_QC_STAT("text/qc_stat"),

    TEXT_SAM("text/sam"),

    TEXT_GATK_REPORT("text/gatk_report"),

    TEXT_STAT_SUMMARY("text/stat_summary"),

    TEXT_TRANSCRIPT_QUANT("text/transcript-quant"),

    TRANSCRIPT_QUANTIFICATION_TXT("transcript.quantification.txt"),

    PICARD_MARK_DUPLICATE_METRICS("text/metrics");

    private String name;

    private MimeType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
