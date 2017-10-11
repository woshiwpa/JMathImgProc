/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jimageprocessor;

import com.cyzapps.imgproc.ImageMgr;
import com.cyzapps.imgmatrixproc.ImgMatrixConverter;
import com.cyzapps.imgmatrixproc.ImgNoiseFilter;
import com.cyzapps.imgmatrixproc.ImgThreshBiMgr;
import com.cyzapps.uptloadermgr.UPTJavaLoaderMgr;
import com.cyzapps.mathexprgen.SerMFPTranslator;
import com.cyzapps.mathexprgen.SerMFPTranslator.CurPos;
import com.cyzapps.mathexprgen.SerMFPTranslator.SerMFPTransFlags;
import com.cyzapps.mathrecog.CharLearningMgr;
import com.cyzapps.mathrecog.ExprFilter;
import com.cyzapps.mathrecog.ExprRecognizer;
import com.cyzapps.mathrecog.ExprRecognizer.ExprRecognizeException;
import com.cyzapps.mathrecog.ImageChop;
import com.cyzapps.mathrecog.MisrecogWordMgr;
import com.cyzapps.mathrecog.StrokeFinder;
import com.cyzapps.mathrecog.StructExprRecog;
import com.cyzapps.mathrecog.UnitRecognizer;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tonyc
 */
public class JImageProcessor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
         
        int nTestMode = 0;
        if (args.length != 1) {
        	System.out.println("Need one parameter which is an integer.");
        	System.out.println("If the parameter is -1, the prototype images in the prototypes folder will be converted to JAVA source files.");
        	System.out.println("Please run this mode only in the prototypes folder in project CoreMathImgProc. Also running this mode will change");
        	System.out.println("source codes in project CoreMathImgProc. So developer has to rebuild CoreMathImgProc and its dependent projects.");
        	System.out.println("If the parameter is 0, will run the regression test. Please run this mode in the regression_test folder in");
        	System.out.println("project JMathImageProc.");
        	System.out.println("If the parameter is 1, will run additional test. Please run this mode in the regression_test folder in project");
        	System.out.println("JMathImageProc.");
        	System.out.println("If the parameter is 2, will test functionality to thin strokes of a character. Please run this mode in the");
        	System.out.println("regression_test folder in project JMathImageProc.");
        	System.out.println("If the parameter is 3 or 4, will run adhoc test. Please run this mode in the regression_test folder in project");
        	System.out.println("JMathImageProc.");
        	System.out.println("By default the parameter value is 0.");
        	return;
        }
        try {
        	nTestMode = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
        	nTestMode = 0;
        }
        
        int nTestMode0PrintRecogMode = ExprRecognizer.RECOG_SPRINT_MODE;
        int nTestMode0HandwritingRecogMode = ExprRecognizer.RECOG_SHANDWRITING_MODE;
        int nTestMode3and4RecogMode = ExprRecognizer.RECOG_SPRINT_MODE;
        boolean bLoadPrintChars = false;
        boolean bLoadSPrintChars = true;
        boolean bLoadSHandwritingChars = true;
        if (nTestMode == -1)    {
            // generate UPT loader java files
            generateUPTsJAVALoader();
        } else if (nTestMode == 0)    {
            CharLearningMgr clm = new CharLearningMgr();
            FileInputStream fis = null;
            try {
                fis = new FileInputStream("clm.xml");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(JImageProcessor.class.getName()).log(Level.SEVERE, null, ex);
            }
            InputStream is = fis;
            if (is != null) {
                clm.readFromXML(is);
                try {
                    fis.close();
                } catch (IOException ex) {
                    Logger.getLogger(JImageProcessor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            MisrecogWordMgr mwm = new MisrecogWordMgr();
            fis = null;
            try {
                fis = new FileInputStream("mwm.xml");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(JImageProcessor.class.getName()).log(Level.SEVERE, null, ex);
            }
            is = fis;
            if (is != null) {
                mwm.readFromXML(is);
                try {
                    fis.close();
                } catch (IOException ex) {
                    Logger.getLogger(JImageProcessor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            // load prototypes, this is much quicker as it is coded in source.
            UPTJavaLoaderMgr.load(bLoadPrintChars, bLoadSPrintChars, bLoadSHandwritingChars);
            //String[] strFolders = getPrototypeFolders();
            //ImageMgr.loadUnitProtoTypesBMPs2UPTs(UnitRecognizer.msUPTMgr, strFolders, "prototypes");
        
            ExprRecognizer.setRecognitionMode(nTestMode0PrintRecogMode); // print mode on
            recognizeMathExpr("regtest/recog_regtest1.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest2.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest3.png", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest4.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest5.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest6.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest7.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest8.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest9.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest10.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest11.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest12.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest13.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest14.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest15.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest16.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest17.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest18.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest19.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest20.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest21.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest22.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest23.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest24.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest25.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest26.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest27.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest28.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest29.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest30.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest31.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest32.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest33.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest34.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest35.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest36.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest37.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest38.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest39.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest40.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest41.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest42.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest43.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest44.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest45.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest46.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest47.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest48.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest49.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest50.bmp", clm, mwm, false);
            ExprRecognizer.setRecognitionMode(nTestMode0HandwritingRecogMode); // s-handwriting mode on
            recognizeMathExpr("regtest/recog_regtest51.bmp", clm, mwm, false);
            ExprRecognizer.setRecognitionMode(nTestMode0PrintRecogMode); // print mode on
            recognizeMathExpr("regtest/recog_regtest52.bmp", clm, mwm, false);
            ExprRecognizer.setRecognitionMode(nTestMode0HandwritingRecogMode); // handwriting mode on
            recognizeMathExpr("regtest/recog_regtest53.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest54.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest55.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest56.bmp", clm, mwm, false);
            ExprRecognizer.setRecognitionMode(nTestMode0PrintRecogMode); // print mode on
            recognizeMathExpr("regtest/recog_regtest57.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest58.bmp", clm, mwm, false);
            recognizeMathExpr("regtest/recog_regtest59.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest60.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest61.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest62.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest63.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest64.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest65.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest66.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest67.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest68.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest69.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest70.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest71.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest72.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest73.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest74.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest75.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest76.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest77.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest78.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest79.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest80.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest81.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest82.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest83.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest84.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest85.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest86.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest87.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest88.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest89.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest90.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest91.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest92.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest93.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest94.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest95.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest96.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest97.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest98.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest99.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest100.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest101.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest102.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest103.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest104.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest105.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest106.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest107.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest108.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest109.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest110.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest111.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest112.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest113.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest114.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest115.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest116.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest117.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest118.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest119.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest120.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest121.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest122.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest123.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest124.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest125.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest126.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest127.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest128.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest129.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest130.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest131.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest132.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest133.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest134.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest135.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest136.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest137.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest138.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest139.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest140.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest141.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest142.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest143.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest144.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest145.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest146.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest147.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest148.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest149.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest150.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest151.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest152.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest153.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest154.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest155.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest156.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest157.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest158.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest159.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest160.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest161.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest162.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest163.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest164.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest165.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest166.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest167.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest168.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest169.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest170.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest171.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest172.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest173.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest174.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest175.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest176.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest177.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest178.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest179.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest180.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest181.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest182.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest183.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest184.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest185.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest186.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest187.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest188.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest189.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest190.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest191.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest192.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest193.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest194.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest195.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest196.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest197.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest198.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest199.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest200.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest201.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest202.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest203.bmp", clm, mwm, true);
            recognizeMathExpr("regtest/recog_regtest204.bmp", clm, mwm, true);
        } else if (nTestMode == 1) {
            int nPixelDiv = 100;
            preprocessImage("20140208_074640.jpg", "preptest", "prepresult", nPixelDiv, true);
            preprocessImage("20140208_113623.jpg", "preptest", "prepresult", nPixelDiv, true);
            preprocessImage("20140208_114044.jpg", "preptest", "prepresult", nPixelDiv, true);
            preprocessImage("IMG_20140207_222045.jpg", "preptest", "prepresult", nPixelDiv, true);
            preprocessImage("IMG_20140207_222106.jpg", "preptest", "prepresult", nPixelDiv, true);
            preprocessImage("IMG_20140207_230141.jpg", "preptest", "prepresult", nPixelDiv, true);
            preprocessImage("IMG_20140207_230618.jpg", "preptest", "prepresult", nPixelDiv, true);
            preprocessImage("IMG_20140207_230752.jpg", "preptest", "prepresult", nPixelDiv, true);
            preprocessImage("IMG_20140208_072658.jpg", "preptest", "prepresult", nPixelDiv, true);
            preprocessImage("handwriting.bmp", "preptest", "prepresult", nPixelDiv, true);
            preprocessImage("mathrecog1.bmp", "preptest", "prepresult", nPixelDiv, true);
            preprocessImage("mathrecog2.bmp", "preptest", "prepresult", nPixelDiv, true);
            preprocessImage("mathrecog3.bmp", "preptest", "prepresult", nPixelDiv, true);
            preprocessImage("preptest1.bmp", "preptest", "prepresult", nPixelDiv, true);
            preprocessImage("preptest2.bmp", "preptest", "prepresult", nPixelDiv, true);
            preprocessImage("preptest3.bmp", "preptest", "prepresult", nPixelDiv, true);
            preprocessImage("preptest4.bmp", "preptest", "prepresult", nPixelDiv, true);
            preprocessImage("preptest5.bmp", "preptest", "prepresult", nPixelDiv, true);
            preprocessImage("preptest6.bmp", "preptest", "prepresult", nPixelDiv, true);
            preprocessImage("preptest7.bmp", "preptest", "prepresult", nPixelDiv, true);
            preprocessImage("preptest8.bmp", "preptest", "prepresult", nPixelDiv, true);
            preprocessImage("preptest9.bmp", "preptest", "prepresult", nPixelDiv, true);
            preprocessImage("preptest10.bmp", "preptest", "prepresult", nPixelDiv, true);
        } else if (nTestMode == 2) {
            testThinImage("thinimgtest", "thinimgresult");
        } else {
            if (nTestMode == 3 || nTestMode == 4) {
                int nPixelDiv = 100;
                byte[][] biMatrix = preprocessImage("mr_initial.bmp", ".", ".", nPixelDiv, nTestMode == 3);                
                ImageChop imgChop = new ImageChop();
                /* From test it is found that after rectify there are more noises, effect is worse, so comment this part.
                imgChop.setImageChop(biMatrix, 0, 0, biMatrix.length, biMatrix[0].length, ImageChop.TYPE_UNKNOWN);
                ImageChop imgChopThinned = StrokeFinder.thinImageChop(imgChop, true);
                double dAngle = ImgRectifier.calcRectifyAngleHough(imgChopThinned.mbarrayImg);
                biMatrix = ImgRectifier.adjustMatrixByAngle(biMatrix, -dAngle, new double[2]);
                biMatrix = StrokeFinder.smoothStroke(biMatrix, 0, 0, biMatrix.length, biMatrix[0].length, 19);*/
                BufferedImage image_rectified = ImageMgr.convertBiMatrix2Img(biMatrix);
                ImageMgr.saveImg(image_rectified, "mr_rectified.bmp");
                
                if (biMatrix.length > 0 && biMatrix[0].length > 0)  {
                    imgChop.setImageChop(biMatrix, 0, 0, biMatrix.length, biMatrix[0].length, ImageChop.TYPE_UNKNOWN);
                    imgChop = StrokeFinder.thinImageChop(imgChop, true);
                    BufferedImage image_finalized = ImageMgr.convertBiMatrix2Img(imgChop.mbarrayImg);
                    ImageMgr.saveImg(image_finalized, "mr_finalized.bmp");
                }
            }
            CharLearningMgr clm = new CharLearningMgr();
            FileInputStream fis = null;
            try {
                fis = new FileInputStream("clm.xml");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(JImageProcessor.class.getName()).log(Level.SEVERE, null, ex);
            }
            InputStream is = fis;
            if (is != null) {
                clm.readFromXML(is);
                try {
                    fis.close();
                } catch (IOException ex) {
                    Logger.getLogger(JImageProcessor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            MisrecogWordMgr mwm = new MisrecogWordMgr();
            fis = null;
            try {
                fis = new FileInputStream("mwm.xml");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(JImageProcessor.class.getName()).log(Level.SEVERE, null, ex);
            }
            is = fis;
            if (is != null) {
                mwm.readFromXML(is);
                try {
                    fis.close();
                } catch (IOException ex) {
                    Logger.getLogger(JImageProcessor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            UPTJavaLoaderMgr.load(bLoadPrintChars, bLoadSPrintChars, bLoadSHandwritingChars);
            ExprRecognizer.setRecognitionMode(nTestMode3and4RecogMode); // change to 1 for handwriting mode.
            //findSkeleton("recog_test2.bmp", "testrecog_thin.bmp");
            //recognizeMathExpr("mathrecog1_1.bmp", clm, mwm);
            //recognizeMathExpr("seperate.bmp", clm, mwm);
            recognizeMathExpr("mr_finalized.bmp", clm, mwm, true);
            //recognizeMathExpr("mr_smoothed.bmp", clm, mwm);
            //recognizeMathExpr("regtest/recog_regtest24.bmp", clm, mwm, true);
            
            recognizeMathExpr("regtest/recog_regtest204.bmp", clm, mwm, true);
            recognizeMathExpr("todo/filter1/mr_finallyproc(1).bmp", clm, mwm, true);
            recognizeMathExpr("todo/filter1/mr_finallyproc(2).bmp", clm, mwm, true);
            recognizeMathExpr("todo/filter1/mr_finallyproc(3).bmp", clm, mwm, true);
            recognizeMathExpr("todo/filter1/mr_finallyproc(4).bmp", clm, mwm, true);
            recognizeMathExpr("todo/filter1/mr_finallyproc(5).bmp", clm, mwm, true);
            recognizeMathExpr("todo/filter1/mr_finallyproc(7).bmp", clm, mwm, true);
            recognizeMathExpr("todo/filter1/mr_finallyproc(8).bmp", clm, mwm, true);
            //recognizeMathExpr("mr_finalized.bmp", clm, mwm);
        }
    }
    
    public static void recognizeMathExpr(String strImageFile, CharLearningMgr clm, MisrecogWordMgr mwm, boolean bFilter) throws InterruptedException   {
        BufferedImage image = ImageMgr.readImg(strImageFile);
        byte[][] biMatrix = ImageMgr.convertImg2BiMatrix(image);
        ImageChop imgChop = new ImageChop();
        imgChop.setImageChop(biMatrix, 0, 0, biMatrix.length, biMatrix[0].length, ImageChop.TYPE_UNKNOWN);
        imgChop = imgChop.convert2MinContainer();
        System.out.println("Now reading image file " + strImageFile + " :");
        long startTime = System.nanoTime();
        String strOutput = "\\Exception";
        try {
            StructExprRecog ser = ExprRecognizer.recognize(imgChop, null, -1, 0, 0);
            StructExprRecog serOld = ser;
            //System.out.println("Before raw filter, image " + strImageFile + " includes expression : " + ser.toString());
            if (bFilter) {
                ser = ExprFilter.filterRawSER(ser, null);
                serOld = ser;
            }
            if (ser != null) {
                //System.out.println("Before restruct, image " + strImageFile + " includes expression : " + ser.toString());
                ser = ser.restruct();
                serOld = ser;
                if (bFilter) {
                    ser = ExprFilter.filterRestructedSER(ser, null, null);
                    serOld = ser;
                }
                if (ser != null) {
                    ser.rectifyMisRecogChars1stRnd(clm);
                    ser.rectifyMisRecogChars2ndRnd();
                    ser.rectifyMisRecogWords(mwm);
                    SerMFPTransFlags smtFlags = new SerMFPTransFlags();
                    smtFlags.mbConvertAssign2Eq = true;
                    strOutput = SerMFPTranslator.cvtSer2MFPExpr(ser, null, new CurPos(0), mwm, smtFlags);
                } else {
                    strOutput = "NO VALID EXPRESSION FOUND.";
                }
            } else {
                strOutput = "NO VALID EXPRESSION FOUND.";
            }
        } catch(ExprRecognizeException e)   {
            if (e.getMessage().compareTo(ExprRecognizer.TOO_DEEP_CALL_STACK) == 0)    {
                strOutput = "Expression is too complicated to recognize.";
            }
        }
        long endTime = System.nanoTime();
        System.out.println("After restruct, image " + strImageFile + " includes expression :\n" + strOutput);
        //System.out.println(String.format("recognize %s takes %s", strImageFile, toString(endTime - startTime)));
    }
    
    public static byte[][] preprocessImage(String strImageFile, String strSrcFolder, String strDestFolder, int nPixelDiv, boolean bFilterSmooth) throws InterruptedException  {
        System.out.println("Now processing image file " + strImageFile);
        if (bFilterSmooth) {
            BufferedImage image = ImageMgr.readImg(strSrcFolder + File.separator + strImageFile);
            int[][] grayMatrix = ImageMgr.convertImg2GrayMatrix(image);
            BufferedImage image_grayed = ImageMgr.convertGrayMatrix2Img(grayMatrix);
            ImageMgr.saveImg(image_grayed, "mr_grayed.bmp");

            grayMatrix = ImgNoiseFilter.filterNoiseNbAvg4Gray(grayMatrix, 1);
            BufferedImage image_filtered = ImageMgr.convertGrayMatrix2Img(grayMatrix);
            ImageMgr.saveImg(image_filtered, "mr_filtered.bmp");

            int nWHMax = Math.max(grayMatrix.length, grayMatrix[0].length);
            int nEstimatedStrokeWidth = (int)Math.ceil((double)nWHMax/(double)nPixelDiv);
            byte[][] biMatrix = ImgThreshBiMgr.convertGray2Bi2ndD(grayMatrix, (int)Math.max(3.0, nEstimatedStrokeWidth / 2.0));  // selected value was 6.
            BufferedImage image_bilized1 = ImageMgr.convertBiMatrix2Img(biMatrix);
            ImageMgr.saveImg(image_bilized1, "mr_bilized1.bmp");
            ImageChop imgChop = new ImageChop();
            imgChop.setImageChop(biMatrix, 0, 0, biMatrix.length, biMatrix[0].length, ImageChop.TYPE_UNKNOWN);
            double dAvgStrokeWidth = imgChop.calcAvgStrokeWidth();

            int nFilterR = (int)Math.ceil((dAvgStrokeWidth/2.0 - 1)/2.0);
            biMatrix = ImgNoiseFilter.filterNoiseNbAvg4Bi(biMatrix, nFilterR, 1);
            biMatrix = ImgNoiseFilter.filterNoiseNbAvg4Bi(biMatrix, nFilterR, 2);
            imgChop.setImageChop(biMatrix, 0, 0, biMatrix.length, biMatrix[0].length, ImageChop.TYPE_UNKNOWN);
            BufferedImage image_smoothed1 = ImageMgr.convertBiMatrix2Img(imgChop.mbarrayImg);
            ImageMgr.saveImg(image_smoothed1, "mr_smoothed1.bmp");

            biMatrix = ImgNoiseFilter.filterNoisePoints4Bi(biMatrix, (int)dAvgStrokeWidth);
            imgChop.setImageChop(biMatrix, 0, 0, biMatrix.length, biMatrix[0].length, ImageChop.TYPE_UNKNOWN);
            BufferedImage image_smoothed2 = ImageMgr.convertBiMatrix2Img(imgChop.mbarrayImg);
            ImageMgr.saveImg(image_smoothed2, "mr_smoothed2.bmp");
            ImageMgr.saveImg(image_smoothed2, strDestFolder + File.separator + strImageFile + ".bmp");
            return biMatrix;
        } else {
            BufferedImage image = ImageMgr.readImg(strSrcFolder + File.separator + strImageFile);
            byte[][] biMatrix = ImageMgr.convertImg2BiMatrix(image);
            return biMatrix;
        }
    }
    
    public static void generateUPTsJAVALoader() {
        String[] strFolders = getPrototypeFolders();
        ImageMgr.loadUnitProtoTypesBmps2JAVA(UnitRecognizer.msUPTMgrPrint, strFolders, "prototypes_print", "uptloadersprint", UPTJavaLoaderMgr.LOAD_UPTS_JAVA_CNT_PRINT);
        ImageMgr.loadUnitProtoTypesBmps2JAVA(UnitRecognizer.msUPTMgrSPrint, strFolders, "prototypes_sprint", "uptloaderssprint", UPTJavaLoaderMgr.LOAD_UPTS_JAVA_CNT_SPRINT);
        ImageMgr.loadUnitProtoTypesBmps2JAVA(UnitRecognizer.msUPTMgrSHandwriting, strFolders, "prototypes_shandwriting", "uptloadersshandwriting", UPTJavaLoaderMgr.LOAD_UPTS_JAVA_CNT_SHANDWRITING);
    }
    
    public static String[] getPrototypeFolders()    {
        String[] strFolders = new String[118];
        int idx = 0;
        strFolders[idx++] = "add";
        strFolders[idx++] = "backward_slash";
        strFolders[idx++] = "big_A";
        strFolders[idx++] = "big_B";
        strFolders[idx++] = "big_C";
        strFolders[idx++] = "big_D";
        strFolders[idx++] = "big_DELTA";
        strFolders[idx++] = "big_E";
        strFolders[idx++] = "big_F";
        strFolders[idx++] = "big_G";
        strFolders[idx++] = "big_H";
        strFolders[idx++] = "big_I";
        strFolders[idx++] = "big_J";
        strFolders[idx++] = "big_K";
        strFolders[idx++] = "big_L";
        strFolders[idx++] = "big_M";
        strFolders[idx++] = "big_N";
        strFolders[idx++] = "big_O";
        strFolders[idx++] = "big_OMEGA";
        strFolders[idx++] = "big_P";
        strFolders[idx++] = "big_PHI";
        strFolders[idx++] = "big_PI";
        strFolders[idx++] = "big_Q";
        strFolders[idx++] = "big_R";
        strFolders[idx++] = "big_S";
        strFolders[idx++] = "big_SIGMA";
        strFolders[idx++] = "big_T";
        strFolders[idx++] = "big_THETA";
        strFolders[idx++] = "big_U";
        strFolders[idx++] = "big_V";
        strFolders[idx++] = "big_W";
        strFolders[idx++] = "big_X";
        strFolders[idx++] = "big_Y";
        strFolders[idx++] = "big_Z";
        strFolders[idx++] = "brace";
        strFolders[idx++] = "close_brace";
        strFolders[idx++] = "close_round_bracket";
        strFolders[idx++] = "close_square_bracket";
        strFolders[idx++] = "dollar";
        strFolders[idx++] = "dot";
        strFolders[idx++] = "eight";
        strFolders[idx++] = "euro";
        strFolders[idx++] = "five";
        strFolders[idx++] = "forward_slash";
        strFolders[idx++] = "four";
        strFolders[idx++] = "infinite";
        strFolders[idx++] = "integrate";
        strFolders[idx++] = "integrate_circle";
        strFolders[idx++] = "larger";
        strFolders[idx++] = "left_arrow";
        strFolders[idx++] = "multiply";
        strFolders[idx++] = "nine";
        strFolders[idx++] = "one";
        strFolders[idx++] = "pound";
        strFolders[idx++] = "right_arrow";
        strFolders[idx++] = "round_bracket";
        strFolders[idx++] = "seven";
        strFolders[idx++] = "six";
        strFolders[idx++] = "smaller";
        strFolders[idx++] = "small_a";
        strFolders[idx++] = "small_alpha";
        strFolders[idx++] = "small_b";
        strFolders[idx++] = "small_beta";
        strFolders[idx++] = "small_c";
        strFolders[idx++] = "small_d";
        strFolders[idx++] = "small_delta";
        strFolders[idx++] = "small_e";
        strFolders[idx++] = "small_epsilon";
        strFolders[idx++] = "small_eta";
        strFolders[idx++] = "small_f";
        strFolders[idx++] = "small_g";
        strFolders[idx++] = "small_gamma";
        strFolders[idx++] = "small_h";
        strFolders[idx++] = "small_i_without_dot";
        strFolders[idx++] = "small_k";
        strFolders[idx++] = "small_lambda";
        strFolders[idx++] = "small_m";
        strFolders[idx++] = "small_mu";
        strFolders[idx++] = "small_n";
        strFolders[idx++] = "small_o";
        strFolders[idx++] = "small_omega";
        strFolders[idx++] = "small_p";
        strFolders[idx++] = "small_phi";
        strFolders[idx++] = "small_pi";
        strFolders[idx++] = "small_psi";
        strFolders[idx++] = "small_q";
        strFolders[idx++] = "small_r";
        strFolders[idx++] = "small_rho";
        strFolders[idx++] = "small_s";
        strFolders[idx++] = "small_sigma";
        strFolders[idx++] = "small_t";
        strFolders[idx++] = "small_tau";
        strFolders[idx++] = "small_theta";
        strFolders[idx++] = "small_u";
        strFolders[idx++] = "small_v";
        strFolders[idx++] = "small_w";
        strFolders[idx++] = "small_x";
        strFolders[idx++] = "small_xi";
        strFolders[idx++] = "small_y";
        strFolders[idx++] = "small_z";
        strFolders[idx++] = "small_zeta";
        strFolders[idx++] = "sqrt_long";
        strFolders[idx++] = "sqrt_medium";
        strFolders[idx++] = "sqrt_short";
        strFolders[idx++] = "sqrt_tall";
        strFolders[idx++] = "sqrt_very_tall";
        strFolders[idx++] = "square_bracket";
        strFolders[idx++] = "star";
        strFolders[idx++] = "subtract";
        strFolders[idx++] = "three";
        strFolders[idx++] = "two";
        strFolders[idx++] = "vertical_line";
        strFolders[idx++] = "word_sin";
        strFolders[idx++] = "word_cos";
        strFolders[idx++] = "word_tan";
        strFolders[idx++] = "word_lim";
        strFolders[idx++] = "yuan";
        strFolders[idx++] = "zero";
        return strFolders;
    }

    public static String[] getPrototypeTestFolders()    {
        String[] strFolders = new String[1];
        strFolders[0] = "right_arrow";
        return strFolders;
    }
    
    public static String toString(long nanoSecs) {
		int minutes    = (int) (nanoSecs / 60000000000.0);
		int seconds    = (int) (nanoSecs / 1000000000.0)  - (minutes * 60);
		int millisecs  = (int) ( ((nanoSecs / 1000000000.0) - (seconds + minutes * 60)) * 1000);


		if (minutes == 0 && seconds == 0)	{
			return millisecs + "ms";
		} else if (minutes == 0 && millisecs == 0)	{
			return seconds + "s";
		} else if (seconds == 0 && millisecs == 0)	{
			return minutes + "min";
		} else if (minutes == 0)	{
			return seconds + "s " + millisecs + "ms";
		} else if (seconds == 0)	{
			return minutes + "min " + millisecs + "ms";
		} else if (millisecs == 0)	{
			return minutes + "min " + seconds + "s";
		}
		return minutes + "min " + seconds + "s " + millisecs + "ms";
	}

    public static void testThinImage(String strSrcFolder, String strDestFolder) throws InterruptedException  {
        System.out.println("Now test thinning image algorithm, source image folder is " + strSrcFolder + ", destination folder is " + strDestFolder);
        File folder = new File(strSrcFolder);
        for (File fProtoType : folder.listFiles())  {
            if (fProtoType.isFile())    {
                String strFileName = fProtoType.getName();
                String strFilePath = fProtoType.getPath();
                BufferedImage image = ImageMgr.readImg(strFilePath);
                byte[][] biMatrix = ImageMgr.convertImg2BiMatrix(image);
                ImageChop imgChop = new ImageChop();
                imgChop.setImageChop(biMatrix, 0, 0, biMatrix.length, biMatrix[0].length, ImageChop.TYPE_UNKNOWN);
                imgChop = StrokeFinder.thinImageChop(imgChop, true);
                BufferedImage image_thinned = ImageMgr.convertBiMatrix2Img(imgChop.mbarrayImg);
                ImageMgr.saveImg(image_thinned, strDestFolder + File.separator + strFileName);
            }
        }
        return;
    }   
}
