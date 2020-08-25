package com.shivsoftech.processor;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;
import com.shivsoftech.core.Parameters;
import com.shivsoftech.util.CharMap;
import com.shivsoftech.util.Constants;
import com.shivsoftech.util.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.shivsoftech.util.Constants.*;

public class MergeProcessor<E> extends AbstractProcessor {

    private static Logger LOG = LoggerFactory.getLogger(MergeProcessor.class);

    private SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);

    @Override
    public void doProcess() {
        LOG.info("Process initiated.");
        performOperation();
        LOG.info("Process finished.");
    }

    private void performOperation() {

        // TODO Get the list from Context
        List<Parameters> listParams = (List<Parameters>) context.get(Constants.MERGE_PARAMS_LIST);

        if (Utility.isEmpty(listParams)) {
            LOG.info("Required parameters not available to merge files.");
            return;
        }
        LOG.info("Found {} files to generate after merge.", listParams.size());

        // TODO Loop over it
        listParams.stream().forEach(this::processFiles);

    }

    private boolean isFilesExist(Parameters parameters) {

        boolean isFilesExist = true;
        if (!Utility.isFileExist(parameters.getFront())) {
            LOG.error("File not found : "+parameters.getFront());
            isFilesExist = false;
        }
        if (!Utility.isFileExist(parameters.getMain())) {
            LOG.error("File not found : "+parameters.getMain());
            isFilesExist = false;
        }
        if (!Utility.isFileExist(parameters.getLast())) {
            LOG.error("File not found : "+parameters.getLast());
            isFilesExist = false;
        }
        if (Utility.isFileExist(parameters.getOutput())) {
            LOG.warn("File will be overwritten : "+parameters.getOutput());
        } else {
            // Create directory if needed
            Utility.createDirectory(parameters.getOutput());
        }

        return isFilesExist;
    }

    private boolean populateDateInFrontPage(Parameters parameters) {

        boolean isDatePopulated = false;

        String tempFile = Utility.generateTemparoryFilename();
        //LOG.info("Temparory File : " + tempFile);

        parameters.setTemp(tempFile);

        try {

            PdfReader reader = new PdfReader(parameters.getFront());
            PdfDictionary dict = reader.getPageN(1);
            PdfObject object = dict.getDirectObject(PdfName.CONTENTS);
            PdfArray refs = null;
            if (dict.get(PdfName.CONTENTS).isArray()) {
                refs = dict.getAsArray(PdfName.CONTENTS);
            } else if (dict.get(PdfName.CONTENTS).isIndirect()) {
                refs = new PdfArray(dict.get(PdfName.CONTENTS));
            }
            for (int index = 0; index < refs.getArrayList().size(); index++) {
                PRStream stream = (PRStream) refs.getDirectObject(index);
                byte[] data = PdfReader.getStreamBytes(stream);

                stream.setData(getModifiedContent(data));
            }
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(tempFile));
            stamper.close();
            reader.close();
            isDatePopulated = true;
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
            isDatePopulated = false;
        }
        return isDatePopulated;
    }

    private byte[] getModifiedContent(byte[] data) {

        String date = formatter.format(new Date());
        String[] splittedDate = date.split(",");

        String content = new String(data);
        content = content.replace(FIND_STRING_PART1, CharMap.get(splittedDate[0]));
        content = content.replace(FIND_STRING_PART2, CharMap.get(splittedDate[1]));
        return content.getBytes();
    }

    private void processFiles(Parameters parameters) {

        // Validate file existance
        if (isFilesExist(parameters)) {

            boolean isDatePopulated = populateDateInFrontPage(parameters);

            mergePdf(parameters, isDatePopulated);

            if (isDatePopulated) {
                deleteTempFile(parameters);
            }
        }
    }

    private boolean deleteTempFile(Parameters parameters) {
        return Utility.deleteFile(parameters.getTemp());
    }

    private void mergePdf(Parameters parameters, boolean useTempFile) {
        try {
            List<InputStream> list = new ArrayList<InputStream>();

            if (useTempFile) {
                list.add(new FileInputStream(new File(parameters.getTemp())));
            } else {
                list.add(new FileInputStream(new File(parameters.getFront())));
            }
            list.add(new FileInputStream(new File(parameters.getMain())));
            list.add(new FileInputStream(new File(parameters.getLast())));

            OutputStream outputStream = new FileOutputStream(parameters.getOutput());
            mergePdf(list, outputStream);

            LOG.info("File merged : "+parameters.getOutput());

        } catch (Exception ex) {
            LOG.error(ex.getMessage());
            //ex.printStackTrace();
        }
    }

    private void mergePdf(List<InputStream> list, OutputStream outputStream) throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);
        document.open();
        PdfContentByte pdfContentByte = pdfWriter.getDirectContent();

        for (InputStream inputStream : list) {
            PdfReader pdfReader = new PdfReader(inputStream);
            for (int index = 1; index <= pdfReader.getNumberOfPages(); index++) {
                document.newPage();
                PdfImportedPage page = pdfWriter.getImportedPage(pdfReader, index);
                pdfContentByte.addTemplate(page, 0, 0);
            }
        }

        outputStream.flush();
        document.close();
        outputStream.close();
    }
}
