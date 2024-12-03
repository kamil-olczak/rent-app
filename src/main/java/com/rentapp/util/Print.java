package com.rentapp.util;

import com.rentapp.gui.scene.SceneCtrl;
import javafx.print.PrinterJob;
import javafx.scene.text.Text;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.printing.Scaling;

import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;

import static com.rentapp.gui.scene.SceneCtrl.stage;

public class Print {
    public static boolean printDocument(String jobName, String PDFPath, int copies, javafx.stage.Window windowOwner) {
        try (PDDocument document = PDDocument.load(new File(PDFPath))) {
            System.out.println("Printing document: " + PDFPath);
            PrinterJob job = PrinterJob.createPrinterJob();
            if (job != null) {
                job.getJobSettings().setCopies(copies);
                job.getJobSettings().setJobName(jobName);
                boolean proceed = job.showPrintDialog(windowOwner);
                if (proceed) {
                    try {

                        java.awt.print.PrinterJob awtJob = java.awt.print.PrinterJob.getPrinterJob();
                        awtJob.setPrintable(new PDFPrintable(document, Scaling.ACTUAL_SIZE));
                        awtJob.setCopies(job.getJobSettings().getCopies());
                        awtJob.print();
                        job.endJob();
                        return true;
                    } catch (PrinterException ex) {
                        ex.printStackTrace();
                        SceneCtrl.showMessageWindow("Błąd drukowania", "Nie znaleziono drukarki, lub wystąpił inny problem z drukowaniem.");
                        return false;
                    } finally {
                        job.endJob();
                    }
                }
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            SceneCtrl.showMessageWindow("Błąd drukowania", "Nie odnaleziono dokumentu do wydrukowania. Plik nie został wyeksportowany lub został usunięty.");
            return false;
        }
    }
}
