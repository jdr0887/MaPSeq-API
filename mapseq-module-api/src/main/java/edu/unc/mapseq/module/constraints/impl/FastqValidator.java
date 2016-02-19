package edu.unc.mapseq.module.constraints.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.PropertyUtils;

import edu.unc.mapseq.module.constraints.Fastq;

/**
 * 
 * @author jdr0887
 * 
 */
public class FastqValidator implements ConstraintValidator<Fastq, Object> {

    private String platform;

    private String aligner;

    private String ends;

    private String files;

    public void initialize(Fastq annotation) {
        this.aligner = annotation.aligner();
        this.platform = annotation.platform();
        this.ends = annotation.ends();
        this.files = annotation.files();
    }

    @SuppressWarnings("unchecked")
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object platformObj = null;
        Object alignerObj = null;
        Object endsObj = null;
        Object filesObj = null;
        try {
            platformObj = PropertyUtils.getProperty(value, this.platform);
            alignerObj = PropertyUtils.getProperty(value, this.aligner);
            endsObj = PropertyUtils.getProperty(value, this.ends);
            filesObj = PropertyUtils.getProperty(value, this.files);
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (InvocationTargetException e1) {
            e1.printStackTrace();
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
        }

        if (platformObj == null || alignerObj == null || endsObj == null || filesObj == null) {
            return false;
        }

        // Convert platform and aligner to lower case
        String platformValue = platformObj.toString().toLowerCase();
        String alignerValue = alignerObj.toString().toLowerCase();

        // It is fine, so parse it
        int lineCount = 0;
        int readLength = 0;
        String error = null;

        List<File> fileList = (ArrayList<File>) filesObj;

        for (File file : fileList) {

            LineNumberReader ln = null;
            try {
                ln = new LineNumberReader(new FileReader(file));
            } catch (FileNotFoundException e) {
                context.buildConstraintViolationWithTemplate("FileNotFoundException: " + e.getMessage())
                        .addConstraintViolation();
            }

            String line = null;
            try {
                while ((line = ln.readLine()) != null) {
                    lineCount++;

                    // Parse line accordingly
                    if (lineCount % 4 == 1) {
                        // FIXME: Should make sure that for paired-end the names match
                        // First line should start with @
                        if (line.charAt(0) != '@') {
                            error = "Expected line starting with @ at line " + lineCount + " but found:" + line
                                    + System.getProperty("line.separator");
                        }
                    } else if (lineCount % 4 == 2) {
                        // Second line is our sequence
                        if (platformValue.startsWith("solid")) {
                            // Capture length for later (minus primer)
                            readLength = line.length() - 1;

                            // Solid
                            for (int i = 1; i < line.length(); i++) {
                                if (!(line.charAt(i) == '0' || line.charAt(i) == '1' || line.charAt(i) == '2'
                                        || line.charAt(i) == '3' || line.charAt(i) == 'N' || line.charAt(i) == 'n'
                                        || line.charAt(i) == '.')) {
                                    error = "Expected 0123Nn. at line " + lineCount + " but found:" + line
                                            + System.getProperty("line.separator");
                                }
                            }
                        } else if (platformValue.startsWith("illumina")) {
                            // Capture length for later
                            readLength = line.length();

                            // Illumina supports ATGCN.
                            for (int i = 0; i < line.length(); i++) {
                                if (!(line.charAt(i) == 'A' || line.charAt(i) == 'a' || line.charAt(i) == 'T'
                                        || line.charAt(i) == 't' || line.charAt(i) == 'G' || line.charAt(i) == 'g'
                                        || line.charAt(i) == 'C' || line.charAt(i) == 'c' || line.charAt(i) == 'N'
                                        || line.charAt(i) == 'n' || line.charAt(i) == '.')) {
                                    error = "Expected ATGCNatgcn. at line " + lineCount + " but found:" + line
                                            + System.getProperty("line.separator");
                                }
                            }
                        }
                    } else if (lineCount % 4 == 3) {
                        // Third line should be a +
                        if (line.compareTo("+") != 0) {
                            error = "Expected line containing + at line " + lineCount + " but found:" + line
                                    + System.getProperty("line.separator");
                        }
                    } else if (lineCount % 4 == 0) {
                        // Fourth line must be same length as qual
                        if (line.length() != readLength)
                            error = "Expected quality line at line " + lineCount + " of length " + readLength
                                    + " but found:" + line + System.getProperty("line.separator") + " of length "
                                    + line.length();

                        // Parse it to make sure it has right character makeup
                        for (int i = 0; i < line.length(); i++) {
                            if (line.charAt(i) < 33 || line.charAt(i) > 126) {
                                error = "Expected quality line at line " + lineCount
                                        + " composed of valid phred score (chars between ASCII 33 and 126), but found invalid score "
                                        + line + System.getProperty("line.separator");
                            }
                        }
                    }

                    // Return if problem
                    if (error != null) {
                        return false;
                    }
                }

                // When done with while loop, close file
                ln.close();

            } catch (IOException e) {
                context.buildConstraintViolationWithTemplate("IOException: " + e.getMessage()).addConstraintViolation();
            }

            // Check file counts
            // Bfast needs ends followed by each other. So 4 per end repeatedly.
            if (alignerValue.compareTo("bfast") == 0) {
                int endsValue = Integer.valueOf(endsObj.toString());
                if ((lineCount % (4 * endsValue)) != 0) {
                    String msg = "Valid Fastq for bfast must have multiple of 4*number_of_ends lines. In this case, "
                            + ends + " ends requires a multiple of " + endsValue * 4 + " but this file has "
                            + lineCount;
                    context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
                }
            } else {
                if (lineCount % 4 != 0) {
                    context.buildConstraintViolationWithTemplate(
                            "Valid Fastq must have multiple of 4 lines, but this one has " + lineCount)
                            .addConstraintViolation();
                }
            }

        }

        return false;
    }
}
