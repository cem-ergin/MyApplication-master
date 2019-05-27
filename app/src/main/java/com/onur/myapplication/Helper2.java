package com.onur.myapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Writer;
import java.util.Arrays;

/**
 * Created by onur on 9.08.2018.
 */

public class Helper2 {

        public Helper2(String path, Activity activity, String name){
        Mat frame = Imgcodecs.imread(path, 1);
        Size sz = new Size(640,480);
        Imgproc.resize( frame, frame, sz );

        Mat grayImage = new Mat();
//        Mat detectedEdges = new Mat();
        Imgproc.cvtColor(frame, grayImage, Imgproc.COLOR_BGR2GRAY);

        grayImage.convertTo(grayImage, CvType.CV_8U);
        if(Imgcodecs.imwrite("/storage/emulated/0/detectedGrayImage.jpg", grayImage))
        {
            System.out.println("edge is detected .......");
        }

//        Mat dest = new Mat();
//        Core.add(dest, Scalar.all(0), dest);
//        frame.copyTo(dest, detectedEdges);
//        Imgproc.blur(frame, frame, new Size(5, 5));
//        Imgproc.dilate(frame, frame, new Mat(), new Point(-1, -1), 1);
//        Imgproc.erode(frame, frame, new Mat(), new Point(-1, -1), 3);
//        //Imgproc.threshold(frame, frame, 100, 200, Imgproc.THRESH_BINARY);
//        Imgproc.Canny(frame, detectedEdges, 50,150,3, false);
//
//        detectedEdges.convertTo(detectedEdges,CvType.CV_8U);
//        if(Imgcodecs.imwrite("/storage/emulated/0/detectedEdges.jpg", detectedEdges))
//        {
//            System.out.println("edge is detected .......");
//        }

        Bitmap myBitmap = BitmapFactory.decodeFile("/storage/emulated/0/detectedGrayImage.jpg");
        Bitmap bMap = myBitmap.copy(Bitmap.Config.ARGB_8888, true);
        for(int i = 0 ; i < bMap.getWidth(); i ++){
            for(int j=0;j<bMap.getHeight();j++){
                //System.out.println(Color.red(bMap.getPixel(i,j)));
                //float renkOrtalama = (((3*Color.red(bMap.getPixel(i,j))) +Color.blue(bMap.getPixel(i,j)) + (6*Color.green(bMap.getPixel(i,j)) ))/10);
                float renkOrtalama = 255-((Color.red(bMap.getPixel(i,j)) +Color.blue(bMap.getPixel(i,j)) + Color.green(bMap.getPixel(i,j) ))/3);
                //System.out.println(renkOrtalama);
                //System.out.println("ren ortalama = " + renkOrtalama);
                if(renkOrtalama>100){
                    bMap.setPixel(i,j,Color.WHITE);

                }else{
                    bMap.setPixel(i,j,Color.BLACK);
                }
            }
        }

        try {
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream("/storage/emulated/0/sonuc.jpg"); //here is set your file path where you want to save or also here you can set file object directly

                bMap.compress(Bitmap.CompressFormat.PNG, 100, outputStream); // bitmap is your Bitmap instance, if you want to compress it you can compress reduce percentage
                // PNG is a lossless format, the compression factor (100) is ignored
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (outputStream != null) {
                        outputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Mat canny = Imgcodecs.imread("/storage/emulated/0/sonuc.jpg", 1);

        Imgproc.morphologyEx(canny, canny, Imgproc.MORPH_CLOSE, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(25,25)));


        canny.convertTo(canny,CvType.CV_8U);
        if(Imgcodecs.imwrite("/storage/emulated/0/detectedArea.jpg", canny))
        {
            System.out.println("edge is detected .......");
        }

            Mat detectedEdges = new Mat();


            Core.add(canny, Scalar.all(0), canny);
            frame = canny;
            frame.copyTo(canny, detectedEdges);
            Imgproc.blur(frame, frame, new Size(5, 5));
            Imgproc.dilate(frame, frame, new Mat(), new Point(-1, -1), 1);
            Imgproc.erode(frame, frame, new Mat(), new Point(-1, -1), 3);
            //Imgproc.threshold(frame, frame, 100, 200, Imgproc.THRESH_BINARY);
            Imgproc.Canny(frame, detectedEdges, 50,150,3, false);

            detectedEdges.convertTo(detectedEdges,CvType.CV_8U);
            if(Imgcodecs.imwrite("/storage/emulated/0/detectedEdges.jpg", detectedEdges))
            {
                System.out.println("edge is detected .......");
            }


        int x1=100000;
        int y1=100000;
        int x2 = 0;
        int y2 = 0;

        Bitmap myBitmapEdge = BitmapFactory.decodeFile("/storage/emulated/0/detectedEdges.jpg");
        Bitmap myBitmapArea = BitmapFactory.decodeFile("/storage/emulated/0/detectedArea.jpg");

        Bitmap bMapEdge = myBitmapEdge.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap bMapArea = myBitmapArea.copy(Bitmap.Config.ARGB_8888, true);


        int cevreSayac = 0 ;
        int alanSayac = 0;
        float uzaklikX = 0;
            float uzaklikY = 0;
            float Cx = 0 , Cy = 0 ;


        for(int i = 0 ; i < bMapEdge.getWidth(); i ++){
            for(int j=0;j<bMapEdge.getHeight();j++){
                if(Color.red(bMapEdge.getPixel(i,j))>0){
                    if(x1>i){
                        x1 = i;
                    }
                    if(x2<i){
                        x2 = i;
                    }
                    if(y1>j){
                        y1 = j ;
                    }
                    if(y2 <j){
                        y2 =j;
                    }
                    cevreSayac++;
                }
                if(Color.red(bMapArea.getPixel(i,j))>0){
                    alanSayac++;
                    Cx += i;
                    Cy += j;
                }
            }
        }

        Cx = (Cx)/(float)(alanSayac);
        Cy = (Cy)/(float)(alanSayac);
        System.out.println("cx = " + Cx + " Cy = " + Cy);

            float minX = 10000;
            float maxX=0;
            float maxUzalik = 0;
            float X1 = 0;
            float X2 = 0;

            for (int j = 0; j < bMapEdge.getHeight(); j++) {
                minX = 10000;
                maxX=0;


                for (int i = 0; i < bMapEdge.getWidth(); i++) {
                    if (Color.red(bMapEdge.getPixel(i, j)) > 0) {

                        if(i<minX) {
                            minX = i;
                        }
                        if(maxX<i) {
                            maxX=i;
                        }
                        uzaklikX = (float) Math.sqrt((Math.pow((maxX - minX), 2)));
                        if(uzaklikX>maxUzalik){
                            maxUzalik = uzaklikX;
                            X1 = minX;
                            X2 = maxX;

                        }

                    }
                }
            }

            uzaklikX = (float) Math.sqrt((Math.pow((X2 - X1), 2)));
            System.out.println("X uzakligi " +uzaklikX + "-------- " + X2 + " ----------" + X1);


            float minY = 10000;
            float maxY = 0;
            float maxUzalikY = 0;
            float Y1 = 0;
            float Y2 = 0;

            for (int j = 0; j < bMapEdge.getWidth(); j++) {
                minY = 10000;
                maxY = 0;


                for (int i = 0; i < bMapEdge.getHeight(); i++) {
                    if (Color.red(bMapEdge.getPixel(j, i)) > 0) {

                        if(i<minY) {
                            minY = i;
                        }
                        if(maxY<i) {
                            maxY=i;
                        }
                        uzaklikY = (float) Math.sqrt((Math.pow((maxY - minY), 2)));
                        if(uzaklikY>maxUzalikY){
                            maxUzalikY = uzaklikY;

                            Y1 = minY;
                            Y2 = maxY;

                        }

                    }
                }
            }


            uzaklikY = (float) Math.sqrt((Math.pow((Y2 - Y1), 2)));
            System.out.println("Y uzakligi "+uzaklikY + "-------- " + Y2 + " ----------" + Y1);

            float Cap = (float) Math.sqrt((Math.pow(uzaklikX, 2))+(Math.pow(uzaklikY, 2)));

            System.out.println("Cap = " + Cap);



            float Amax =0,Amin=10000,tmpUzaklik=0;
            float toplamAgrilikX = 0,toplamAgrilikY=0;


            for (int i = 0; i < bMapEdge.getWidth(); i++) {
                for (int j = 0; j < bMapEdge.getHeight(); j++) {
                    if (Color.red(bMapEdge.getPixel(i, j)) > 0) {
                        toplamAgrilikX += i;
                        toplamAgrilikY += j;
                    }
                }
            }

            toplamAgrilikX = toplamAgrilikX/cevreSayac;
            toplamAgrilikY = toplamAgrilikY/cevreSayac;

            System.out.println("Merkez X,Y = " + toplamAgrilikX + " ---- " + toplamAgrilikY);
            //
//            for x in range(len(cevreNoktalari)):
//            uzaklik = uzaklik + sqrt(((cevreNoktalari[x][1] - Cx) ** 2) + ((cevreNoktalari[x][0] - Cy) ** 2))
//
//            CortalamaUzaklik = uzaklik / len(cevreNoktalari)


            for (int i = 0; i < bMapEdge.getWidth(); i++) {
                for (int j = 0; j < bMapEdge.getHeight(); j++) {
                    if (Color.red(bMapEdge.getPixel(i, j)) > 0) {
                        tmpUzaklik = (float) Math.sqrt((Math.pow(i-toplamAgrilikX, 2))+(Math.pow(j-toplamAgrilikY, 2)));
                        if (Amax < tmpUzaklik) {
                            Amax = tmpUzaklik;
                        }
                        if (Amin > tmpUzaklik) {
                            Amin = tmpUzaklik;
                        }
                    }
                }
            }

            tmpUzaklik = tmpUzaklik/cevreSayac;

            System.out.println(" Agirlik merkezine ortalama uzaklik = "  + tmpUzaklik );


            System.out.println("Amax = " + Amax + "Amin = " + Amin);

            System.out.println("Cap/Amax+Amin = " +  (Cap/(Amax+Amin)) );



//            ozellik9 = (sqrt(((capX[1] - Cx) ** 2) + ((capX[0] - Cy) ** 2))) / (
//                    sqrt(((capY[1] - Cx) ** 2) + ((capY[0] - Cy) ** 2)))
//            ozellik91 = (sqrt(((capY[1] - Cx) ** 2) + ((capY[0] - Cy) ** 2))) / (
//                    sqrt(((capX[1] - Cx) ** 2) + ((capX[0] - Cy) ** 2)))
//    #8
//            if (ozellik91 >= ozellik9):
//    #    print("CAP NOKTALARININ AGIRLIK MERKEZINE UZAKLIGINN ORANI = ", ozellik91)
//            ozellik9=ozellik91
//    #else:
//    #    print("CAP NOKTALARININ AGIRLIK MERKEZINE UZAKLIGINN ORANI = ", ozellik9)


            float ozellik9 = (float) (Math.sqrt((Math.pow((X2-toplamAgrilikX), 2))+(Math.pow(Y2-toplamAgrilikY, 2))))/(float)(Math.sqrt((Math.pow((X1-toplamAgrilikX), 2))+(Math.pow(Y1-toplamAgrilikY, 2))));
            float ozellik91 = (float) (Math.sqrt((Math.pow((X1-toplamAgrilikX), 2))+(Math.pow(Y1-toplamAgrilikY, 2))))/(float)(Math.sqrt((Math.pow((X2-toplamAgrilikX), 2))+(Math.pow(Y2-toplamAgrilikY, 2))));


            if(ozellik91 >= ozellik9){
                ozellik9 = ozellik91;
            }

            System.out.println("En uzak iki noktanin agirlik merkezine uzakliginin orani = " + ozellik9);

            System.out.println(bMap.getHeight());
        System.out.println(bMap.getWidth());
        System.out.println("cevre pixel sayisi = " + cevreSayac);
        System.out.println("alan pixel sayisi = " + alanSayac);

        System.out.println("x1 = " + x1 + " x1 = " + x2);
        System.out.println("y1 = " + y1 + " y2 = " + y2);
        System.out.println("boy/en = " + (float)(y2-y1)/(float)(x2-x1));
        System.out.println("cevre noktalari / boy + en = " + ((float)cevreSayac/((float)(y2-y1)+(float)(x2-x1))));
        System.out.println("alan/boy*en = " + ((float)alanSayac/((float)(y2-y1)*(float)(x2-x1))));
        System.out.println("dis alan / ic alan = " + (((float)(x2-x1)*(float)(y2-y1))-(float)alanSayac)/(float)alanSayac);

        String sonuc =
                 (float)(y2-y1)/(float)(x2-x1) + "," +
                ((float)cevreSayac/((float)(y2-y1)+(float)(x2-x1))) + "," +
                 ((float)alanSayac/((float)(y2-y1)*(float)(x2-x1)))+ "," +
//                 (((float)(x2-x1)*(float)(y2-y1))-(float)alanSayac)/(float)alanSayac + ","+
                         ozellik9 + ","+
                         (Cap/(Amax+Amin)) +","+name +".jpg" ;


            File file = new File("/storage/emulated/0/flaviadusukcozunurlukdeneme.txt");
            if(!file.exists())
            {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // write code for saving data to the file
            }

            appendStringToFile(sonuc+"\n" , file);

            //System.out.println("cevre ortalama uzaklik = " + CortalamaUzaklik);


//            String[] parse = sonuc.split(",");
//
//
//            int satirlar = 0;
//            try {
//                File file = new File("/storage/emulated/0/deneme.txt");
//                LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(file));
//                lineNumberReader.skip(Long.MAX_VALUE);
//                satirlar = lineNumberReader.getLineNumber();
//                lineNumberReader.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//
//            float[] uzakliklar = new float[satirlar];
//            float[] uzakliklar2 = new float[satirlar];
//            String[] lines = new String[satirlar];
//
//
//            try(BufferedReader br = new BufferedReader(new FileReader("/storage/emulated/0/deneme.txt"))) {
//
//
//                int j = 0;
//                for(String line; (line = br.readLine()) != null; ) {
//                    System.out.println(line);
//                    lines[j] = line;
//                    float hesapla = 0;
//
//                    String[] parse2 = line.split(",");
//                    for(int i = 0 ; i < parse.length-1;i++) {
//                        float a = Float.parseFloat(parse2[i]);
//                        float b = Float.parseFloat(parse[i]);
//                        hesapla += (Math.pow((a-b),2));
//                    }
//                    uzakliklar[j] = hesapla;
//                    uzakliklar2[j] = hesapla;
//                    System.out.println("satir = " +line + hesapla);
//                    j++;
//
//
//                }
//                // line is not visible here.
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            Arrays.sort(uzakliklar2);
//            for (int i = 0 ; i < uzakliklar2.length;i++ ){
//                System.out.println("uzakliklar = " + uzakliklar[i]);
//                System.out.println("uzakliklar2 =" +uzakliklar2[i]);
//            }
//
//            String textSonuc="Yapraga ait oznitelik bilgileri = " +(float)(y2-y1)/(float)(x2-x1) + "," +
//                    ((float)cevreSayac/((float)(y2-y1)+(float)(x2-x1))) + "," +
//                    ((float)alanSayac/((float)(y2-y1)*(float)(x2-x1)))+ "," +
//                    (((float)(x2-x1)*(float)(y2-y1))-(float)alanSayac)/(float)alanSayac + "\nBenzer en yakin 3 yaprak ornegi;";
//
//            //ucuncu ozellik kaldirilcak
//            for(int i = 0 ; i < 3 ; i ++ ){
//                System.out.println("index = " + findIndex(uzakliklar,uzakliklar2[i]));
//                System.out.println("line = " +lines[findIndex(uzakliklar,uzakliklar2[i])]);
//                textSonuc+= ""+lines[findIndex(uzakliklar,uzakliklar2[i])] + "\nHesaplanan uzaklik = " + uzakliklar2[i]+"\n";
//            }
//
//            TextView text = (TextView) activity.findViewById(R.id.text);
//            text.setText(textSonuc);


        }

    public static int findIndex(float arr[], float t)
    {

        // if array is Null
        if (arr == null) {
            return -1;
        }

        // find length of array
        int len = arr.length;
        int i = 0;

        // traverse in the array
        while (i < len) {

            // if the i-th element is t
            // then return the index
            if (arr[i] == t) {
                return i;
            }
            else {
                i = i + 1;
            }
        }
        return -1;
    }

    public void appendStringToFile(final String appendContents, final File file) {
        try {
                if (file != null && file.canWrite()) {
                    file.createNewFile(); // ok if returns false, overwrite
                    Writer out = new BufferedWriter(new FileWriter(file, true), 1024);
                    out.write(appendContents);
                    out.close();
                }

        } catch (IOException e) {
               Log.e("fos error", "Error appending string data to file " + e.getMessage(), e);
        }
    }

}
