package com.rentapp.util;

import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.rentapp.gui.scene.SceneCtrl;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PDFExport {
    public static final String RENT_DOCS_DIR = ".\\Protocols\\";
    public static final String CONTRACTS_DIR = ".\\Contracts\\";
    private static final String RENT_DOC_SRC = "protocol_tmpl.pdf";
    private static final String CONTRACT_SRC = "contract_tmpl.pdf";
    private static final FontProgram FONT_PROGRAM;
    private static final FontProgram FONT_PROGRAM_BOLD;
    static {
        try {
            FONT_PROGRAM = FontProgramFactory.createFont("tahoma.ttf");
            FONT_PROGRAM_BOLD = FontProgramFactory.createFont("Tahoma-Bold.ttf");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean exportContract(String contractNumber, String date, String [] clientInfo){

        String dest = CONTRACTS_DIR + clientInfo[1] + '_' + contractNumber.replace('/', '_') + ".pdf";
        String [] clientInfoCopy = Arrays.copyOf(clientInfo, clientInfo.length);

        try (PdfDocument pdfDoc = new PdfDocument(new PdfReader(CONTRACT_SRC), new PdfWriter(dest))){
            PdfPage page = pdfDoc.getPage(1);
            PdfCanvas canvas = new PdfCanvas(page);

            PdfFont font = PdfFontFactory.createFont(FONT_PROGRAM, PdfEncodings.CP1250);
            PdfFont fontBold = PdfFontFactory.createFont(FONT_PROGRAM_BOLD, PdfEncodings.CP1250);

            //place contract number on the first page
            float x = 260;
            float y = 700;
            canvas.beginText()
                    .setFontAndSize(fontBold, 14)
                    .moveText(x, y)
                    .showText(contractNumber)
                    .endText();

            //place date on the first page

            x = 78;
            y = 678;
            canvas.beginText()
                    .setFontAndSize(font, 9)
                    .moveText(x, y)
                    .showText(date)
                    .endText();

            //place client info on the first page
            x = 260;
            y = 580;
            float leading = 11.5f;
            //replace new line characters with space in client adress
            clientInfoCopy[2] = clientInfo[2].replace('\n', ' ');
            canvas.beginText()
                    .setFontAndSize(fontBold, 9)
                    .moveText(x, y);
            for(String line : clientInfoCopy) {
                canvas.newlineText()
                        .showText(line)
                        .moveText(0, -leading);
            }
            canvas.endText();

            //place date on the second page
            page = pdfDoc.getPage(2);
            canvas = new PdfCanvas(page);

            x = 258;
            y = 136;
            canvas.beginText()
                    .setFontAndSize(font, 11)
                    .moveText(x, y)
                    .showText(date)
                    .endText();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            SceneCtrl.showMessageWindow("Błąd podczas eksportu umowy", "Nie udało się wyeksportować umowy. Brak dostępu do wzoru.");
            return false;
        }
    }

    public static boolean exportRentDocument(String rentID, String contractNumber, String [] clientInfo, String [] rentInfo, List<String> notes, String date) {



        String dest = RENT_DOCS_DIR + clientInfo[1] + '_' +  rentID.replace('/', '_') + ".pdf";

        try (PdfDocument pdfDoc = new PdfDocument(new PdfReader(RENT_DOC_SRC), new PdfWriter(dest))){
            PdfPage page = pdfDoc.getPage(1);
            PdfCanvas canvas = new PdfCanvas(page);

            PdfFont font = PdfFontFactory.createFont(FONT_PROGRAM, PdfEncodings.CP1250);
            PdfFont fontBold = PdfFontFactory.createFont(FONT_PROGRAM_BOLD, PdfEncodings.CP1250);
            //place client rent id
            float x = 445;
            float y = 718.5f;
            canvas.beginText()
                    .setFontAndSize(fontBold, 14)
                    .moveText(x, y)
                    .showText(rentID)
                    .endText();

            //place contract number
            x = 250;
            y = 675;
            canvas.beginText()
                    .setFontAndSize(fontBold, 9)
                    .moveText(x, y)
                    .showText(contractNumber)
                    .endText();

            //place client info
            y = 639.5f;
            float leading = 13;
            String [] splited;
            //clintInfo[2] is adress
            if(clientInfo[2].contains("\n")){
                splited = splitStringAtFirstOccurrence(clientInfo[2],'\n');
                splited[1] = splited[1].replace('\n', ' ');
                clientInfo[2] = splited[0];
                clientInfo = addElementToArrayAfterIndex(clientInfo, splited[1], 2);
            } else {
                clientInfo = addElementToArrayAfterIndex(clientInfo, "", 2);
            }
            canvas.beginText()
                    .setFontAndSize(fontBold, 9)
                    .moveText(x, y);
            for (String line : clientInfo) {
                canvas.newlineText()
                        .showText(line)
                        .moveText(0, -leading);
            }
            canvas.endText();

            //place rent info in format:
            //equipment name
            //model
            //serial number
            //year
            //equipment value
            //equipID
            //annual service
            //
            //
            //accessory names
            //accessory prices
            //total per day gross
            //deposit value
            //probable return date
            //
            //
            //rent start date
            //rent start hour
            y = 525f;
            leading = 12.4f;
            canvas.beginText()
                    .setFontAndSize(fontBold, 9)
                    .moveText(x, y);
            for(String line : rentInfo) {
                canvas.newlineText()
                        .showText(line)
                        .moveText(0, -leading);
            }
            canvas.endText();

            //place notes
            x = 19.5f;
            y = 290f;
            leading = 12.4f;
            canvas.beginText()
                    .setFontAndSize(fontBold, 9)
                    .moveText(x, y);
            for(String line : notes) {
                canvas.newlineText()
                        .showText(line)
                        .moveText(0, -leading);
            }
            canvas.endText();

            //place date
            x = 374;
            y = 183;
            canvas.beginText()
                    .setFontAndSize(font, 10)
                    .moveText(x, y)
                    .showText(date)
                    .endText();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            SceneCtrl.showMessageWindow("Błąd podczas eksportu protokołu", "Nie udało się wyeksportować protkołu. Brak dostępu do wzoru.");
            return false;
        }
    }

    public static String[] splitStringAtFirstOccurrence(String input, char delimiter) {
        int index = input.indexOf(delimiter);
        if (index == -1) {
            return new String[]{input};
        } else {
            String before = input.substring(0, index);
            String after = input.substring(index + 1);
            return new String[]{before, after};
        }
    }

    public static String[] addElementToArrayAfterIndex(String [] arr, String toAdd, int index){
        String []res = new String[arr.length + 1];
        for (int i = 0; i < arr.length; i++) {
            if(i == index){
                res[i] = arr[i];
                res[i+1] = toAdd;
            } else if(i > index) {
                res[i+1] = arr[i];
            }else {
                res[i] = arr[i];
            }
        }
        return res;
    }
}
