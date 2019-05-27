package com.onur.myapplication;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by onur on 9.08.2018.
 */



public class Helper {

    float toplam = 0;
    //LbpHistogram lbp;
    int[] mapping;
    Mat[] matlist;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public Helper(String path, final Activity activity, String name){
        Mat frame = Imgcodecs.imread(path, 1);
        Mat grayImage = new Mat();
//        Mat detectedEdges = new Mat();
        Imgproc.cvtColor(frame, grayImage, Imgproc.COLOR_BGR2GRAY);

        grayImage.convertTo(grayImage, CvType.CV_8U);
        if(Imgcodecs.imwrite("/storage/emulated/0/detectedGrayImage.jpg", grayImage))
        {
            System.out.println("edge is detected .......");
        }

        Bitmap bmp = null;

        Imgproc.cvtColor(frame, grayImage, Imgproc.COLOR_RGB2BGRA);
        bmp = Bitmap.createBitmap(grayImage.cols(), grayImage.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(grayImage, bmp);


        histogramLbp(bmp);



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
                //System.out.println(Color.red(bMap.getPixel(i,j)));
                //float renkOrtalama = (((3*Color.red(bMap.getPixel(i,j))) +Color.blue(bMap.getPixel(i,j)) + (6*Color.green(bMap.getPixel(i,j)) ))/10);
                //System.out.println(renkOrtalama);
                //System.out.println("ren ortalama = " + renkOrtalama);
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


//            File file = new File("/storage/emulated/0/deneme.txt");
//            if(!file.exists())
//            {
//                try {
//                    file.createNewFile();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                // write code for saving data to the file
//            }
//
//            appendStringToFile(sonuc+"\n" , file);

        //System.out.println("cevre ortalama uzaklik = " + CortalamaUzaklik);


        String[] parse = sonuc.split(",");


        int satirlar = 0;
        try {
            File file = new File("/storage/emulated/0/deneme.txt");
            LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(file));
            lineNumberReader.skip(Long.MAX_VALUE);
            satirlar = lineNumberReader.getLineNumber();
            lineNumberReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        float[] uzakliklar = new float[satirlar];
        float[] uzakliklar2 = new float[satirlar];
        String[] lines = new String[satirlar];


        float[] maxOzellik= new float[]{0, 0, 0, 0, 0};
        float[] minOzellik= new float[]{1000000, 1000000, 1000000, 1000000,1000000};

        try(BufferedReader br = new BufferedReader(new FileReader("/storage/emulated/0/deneme.txt"))) {

            for(String line; (line = br.readLine()) != null; ) {
                String[] maxParse = line.split(",");
                for(int i = 0;i<5;i++){
                    if(maxOzellik[i]<Float.parseFloat(maxParse[i])){
                        maxOzellik[i] = Float.parseFloat(maxParse[i]);
                    }if(minOzellik[i]>Float.parseFloat(maxParse[i])){
                        minOzellik[i] =  Float.parseFloat(maxParse[i]);
                    }
                }
            }
            // line is not visible here.
        } catch (IOException e) {
            e.printStackTrace();
        }

        int yaprakSayac = 0;
        if(Float.parseFloat(parse[0]) < minOzellik[0] || Float.parseFloat(parse[0]) > maxOzellik[0] ) {

            yaprakSayac++;
        }
        if(Float.parseFloat(parse[1]) < minOzellik[1] || Float.parseFloat(parse[1]) > maxOzellik[1] ) {

            yaprakSayac++;

        }if(Float.parseFloat(parse[2]) < minOzellik[2] || Float.parseFloat(parse[2]) > maxOzellik[2] ) {

            yaprakSayac++;

        }if(Float.parseFloat(parse[3]) < minOzellik[3] || Float.parseFloat(parse[3]) > maxOzellik[3] ) {

            yaprakSayac++;

        }if(Float.parseFloat(parse[4]) < minOzellik[4] || Float.parseFloat(parse[4]) > maxOzellik[4] ) {
            yaprakSayac++;

        }
        if(yaprakSayac>=2 || Float.isNaN((float)(y2-y1)/(float)(x2-x1))|| Float.isNaN(  ((float)cevreSayac/((float)(y2-y1)+(float)(x2-x1)))) || Float.isNaN(((float)alanSayac/((float)(y2-y1)*(float)(x2-x1)))) || Float.isNaN(ozellik9)|| Float.isNaN((Cap/(Amax+Amin))) ) {

            TextView title = (TextView) activity.findViewById(R.id.title);
            title.setVisibility(View.VISIBLE);
            title.setText("Seçilen görüntü yaprak değildir veya eşleştirilemedi.");

            TextView first = (TextView) activity.findViewById(R.id.first);
            TextView second = (TextView) activity.findViewById(R.id.second);
            TextView third = (TextView) activity.findViewById(R.id.third);
            first.setVisibility(View.GONE);
            second.setVisibility(View.GONE);
            third.setVisibility(View.GONE);
            ImageView imageView= (ImageView)activity.findViewById(R.id.firstImage);
            ImageView imageView2= (ImageView)activity.findViewById(R.id.secondImage);
            ImageView imageView3= (ImageView)activity.findViewById(R.id.thirdImage);
            imageView.setVisibility(View.GONE);
            imageView2.setVisibility(View.GONE);
            imageView3.setVisibility(View.GONE);

        }else if(yaprakSayac>=1) {
            TextView title = (TextView) activity.findViewById(R.id.title);
            title.setVisibility(View.VISIBLE);
            title.setText("Seçilen görüntü yaprak değildir veya eşleştirilemedi.");

            TextView first = (TextView) activity.findViewById(R.id.first);
            TextView second = (TextView) activity.findViewById(R.id.second);
            TextView third = (TextView) activity.findViewById(R.id.third);
            first.setVisibility(View.GONE);
            second.setVisibility(View.GONE);
            third.setVisibility(View.GONE);
            ImageView imageView= (ImageView)activity.findViewById(R.id.firstImage);
            ImageView imageView2= (ImageView)activity.findViewById(R.id.secondImage);
            ImageView imageView3= (ImageView)activity.findViewById(R.id.thirdImage);
            imageView.setVisibility(View.GONE);
            imageView2.setVisibility(View.GONE);
            imageView3.setVisibility(View.GONE);
        }else {


            try(BufferedReader br = new BufferedReader(new FileReader("/storage/emulated/0/deneme.txt"))) {


                int j = 0;
                for(String line; (line = br.readLine()) != null; ) {
                    System.out.println(line);
                    lines[j] = line;
                    float hesapla = 0;

                    String[] parse2 = line.split(",");
                    for(int i = 0 ; i < parse.length-1;i++) {
                        float a = Float.parseFloat(parse2[i])/maxOzellik[i];
                        float b = Float.parseFloat(parse[i])/maxOzellik[i];
                        hesapla += (Math.pow((a-b),2));
                    }
                    uzakliklar[j] = hesapla;
                    uzakliklar2[j] = hesapla;
                    System.out.println("satir = " +line + hesapla);
                    j++;


                }
                // line is not visible here.
            } catch (IOException e) {
                e.printStackTrace();
            }

            Arrays.sort(uzakliklar2);
            for (int i = 0 ; i < uzakliklar2.length;i++ ){
                System.out.println("uzakliklar = " + uzakliklar[i]);
                System.out.println("uzakliklar2 =" +uzakliklar2[i]);
            }

            TextView title = (TextView) activity.findViewById(R.id.title);

            TextView first = (TextView) activity.findViewById(R.id.first);
            TextView second = (TextView) activity.findViewById(R.id.second);
            TextView third = (TextView) activity.findViewById(R.id.third);
            title.setVisibility(View.VISIBLE);
            title.setText("En Yakın Yaprak Türleri:");
            first.setVisibility(View.VISIBLE);
            second.setVisibility(View.VISIBLE);
            third.setVisibility(View.VISIBLE);

            String textSonuc="Yapraga ait oznitelik bilgileri = " +(float)(y2-y1)/(float)(x2-x1) + "," +
                    ((float)cevreSayac/((float)(y2-y1)+(float)(x2-x1))) + "," +
                    ((float)alanSayac/((float)(y2-y1)*(float)(x2-x1)))+ "," +
                    ozellik9 + ","+
                    (Cap/(Amax+Amin)) +","+name +".jpg"  + "\nBenzer en yakin 3 yaprak ornegi;";

            //ucuncu ozellik kaldirilcak
            final int imagesIdArr[] = new int[]{R.drawable.a1001,R.drawable.a1060,R.drawable.a1123,R.drawable.a1195,
                    R.drawable.a1268,R.drawable.a1324,R.drawable.a1386,R.drawable.a1438,R.drawable.a1497,
                    R.drawable.a1552,R.drawable.a2001,R.drawable.a2051,R.drawable.a2114,R.drawable.a2166,R.drawable.a2231,
                    R.drawable.a2291, R.drawable.a2347,R.drawable.a2424,R.drawable.a2486,R.drawable.a2547,R.drawable.a2616,
                    R.drawable.a3001,R.drawable.a3056,
                    R.drawable.a3111,R.drawable.a3176,
                    R.drawable.a3230,R.drawable.a3282,R.drawable.a3335,R.drawable.a3390,R.drawable.a3447,
                    R.drawable.a3511,R.drawable.a3566};

            for(int i = 0 ; i < 3 ; i ++ ){
                System.out.println("index = " + findIndex(uzakliklar,uzakliklar2[i]));
                System.out.println("line = " +lines[findIndex(uzakliklar,uzakliklar2[i])]);
                String siniflandirma[] = lines[findIndex(uzakliklar,uzakliklar2[i])].split(",");
                String[] siniflandirmaImage = siniflandirma[5].split("\\.");
                final String[] parseSinif = siniflandirmaKontrol(Integer.parseInt(siniflandirmaImage[0])).split(",");

                textSonuc+= siniflandirmaKontrol(Integer.parseInt(siniflandirmaImage[0]))+" - "+lines[findIndex(uzakliklar,uzakliklar2[i])] + "\nHesaplanan uzaklik = " + uzakliklar2[i]+"\n";
                if(i ==0){
                    ImageView imageView= (ImageView)activity.findViewById(R.id.firstImage);
                    imageView.setImageResource(imagesIdArr[Integer.parseInt(parseSinif[1])]);
                    imageView.setVisibility(View.VISIBLE);

                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final View alertDialog= activity.getLayoutInflater().inflate(R.layout.custom_dialog, null);
                            ImageView imageView= (ImageView) alertDialog
                                    .findViewById(R.id.selectedImage);
                            imageView.setImageResource(imagesIdArr[Integer.parseInt(parseSinif[1])]);

                            final AlertDialog.Builder alertadd = new AlertDialog.Builder(activity);

                            alertadd.setView(alertDialog);
                            alertadd.setPositiveButton("Kapat", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            alertadd.show();

                        }
                    });
                    first.setText("1. Benzer Yaprak Türü;" + parseSinif[0]);
                }if(i ==1){
                    ImageView imageView= (ImageView)activity.findViewById(R.id.secondImage);
                    imageView.setVisibility(View.VISIBLE);

                    imageView.setImageResource(imagesIdArr[Integer.parseInt(parseSinif[1])]);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final View alertDialog= activity.getLayoutInflater().inflate(R.layout.custom_dialog, null);
                            ImageView imageView= (ImageView) alertDialog
                                    .findViewById(R.id.selectedImage);
                            imageView.setImageResource(imagesIdArr[Integer.parseInt(parseSinif[1])]);

                            final AlertDialog.Builder alertadd = new AlertDialog.Builder(activity);

                            alertadd.setView(alertDialog);
                            alertadd.setPositiveButton("Kapat", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            alertadd.show();

                        }
                    });
                    second.setText("2. Benzer Yaprak Türü;" + parseSinif[0]);
                }if(i ==2){
                    ImageView imageView= (ImageView)activity.findViewById(R.id.thirdImage);
                    imageView.setVisibility(View.VISIBLE);

                    imageView.setImageResource(imagesIdArr[Integer.parseInt(parseSinif[1])]);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final View alertDialog= activity.getLayoutInflater().inflate(R.layout.custom_dialog, null);
                            ImageView imageView= (ImageView) alertDialog
                                    .findViewById(R.id.selectedImage);
                            imageView.setImageResource(imagesIdArr[Integer.parseInt(parseSinif[1])]);

                            final AlertDialog.Builder alertadd = new AlertDialog.Builder(activity);

                            alertadd.setView(alertDialog);
                            alertadd.setPositiveButton("Kapat", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            alertadd.show();

                        }
                    });

                    third.setText("3. Benzer Yaprak Türü;" + parseSinif[0] + " toplam = "+toplam);
                }

            }

        }


    }


    public String siniflandirmaKontrol(int line){
        float puan = 0;

        if(line<=1059){
            return "Dev Bambu,0";
        }
        if(line>1059&&line<=1122){
            return "At Kestanesi,1";
        }
        if(line>1122&&line<=1194){
            return "Hanım Tuzluğu,2";
        }
        if(line>1194&&line<=1267){
            return "Çin Erguvanı,3";
        }
        if(line>1267&&line<=1323){
            return "İndigo Ağacı,4";
        }
        if(line>1323&&line<=1385){
            return "Japon Akçaağacı,5";
        }
        if(line>1385&&line<=1437){
            return "Defnegiller,6";
        }
        if(line>1437&&line<=1496){
            return "Defnegiller,7";
        }
        if(line>1496&&line<=1551){
            return "Aralya,8";
        }
        if(line>1551&&line<=1616){
            return "Çin Tarçını,9";
        }
        if(line>2000&&line<=2050){
            return "Altuni Fener Ağacı,10";
        }
        if(line>2050&&line<=2113){
            return "Kuş Üzümü,11";
        }
        if(line>2113&&line<=2165){
            return "Yıldız Çalısı,12";
        }
        if(line>2165&&line<=2230){
            return "Kış Tatlısı,13";
        }
        if(line>2230&&line<=2290){
            return "Kafur Ağacı,14";
        }
        if(line>2290&&line<=2346){
            return "Japan Arrowwood,15";
        }
        if(line>2346&&line<=2423){
            return "Osmantus Fragrans,16";
        }
        if(line>2423&&line<=2485){
            return "Himalaya Sediri,17";
        }
        if(line>2485&&line<=2546){
            return "Mabet Ağacı,18";
        }
        if(line>2546&&line<=2612){
            return "Lagerstroemia,19";
        }
        if(line>2615&&line<=2675){
            return "Zakkum,20";
        }
        if(line>3000&&line<=3055){
            return "Süs Kirazı,21";
        }
        if(line>3055&&line<=3110){
            return "Parlak Yapraklı Kurtbağrı,22";
        }
        if(line>3110&&line<=3175){
            return "Toona Sinensis,23";
        }
        if(line>3175&&line<=3229){
            return "Şeftali,24";
        }
        if(line>3229&&line<=3281){
            return "Ford Woodlotus,25";
        }
        if(line>3281&&line<=3334){
            return "Akçaağaç,26";
        }
        if(line>3334&&line<=3389){
            return "Beale's barberry,27";
        }
        if(line>3389&&line<=3446){
            return "Büyük Çiçekli Manolya,28";
        }
        if(line>3446&&line<=3510){
            return "Kanada Kavağı,29";
        }
        if(line>3510&&line<=3563){
            return "Çin Lale Ağacı,30";
        }
        if(line>3565&&line<=3621){
            return "Mandalina,31";
        }
        return "bilinmiyor";
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

    public double lbpCalculatedPixel(Bitmap img,int x, int y){

        int center = img.getPixel(x,y);

        ArrayList<Integer> val_ar = new ArrayList<Integer>();


        val_ar.add(getPixel(img,center,x-1,y+1));
        val_ar.add(getPixel(img,center,x,y+1));
        val_ar.add(getPixel(img,center,x+1,y+1));
        val_ar.add(getPixel(img,center,x+1,y));
        val_ar.add(getPixel(img,center,x+1,y-1));
        val_ar.add(getPixel(img,center,x,y-1));
        val_ar.add(getPixel(img,center,x-1,y-1));
        val_ar.add(getPixel(img,center,x-1,y));

        int[] power_val = {1,2,4,8,16,32,64,128};

        double value = 0;

        for (int i = 0 ; i <val_ar.size();i++){

            value+= val_ar.get(i) * power_val[i];
        }

        return value;

    }

    public int getPixel(Bitmap img,int center,int x , int y){

        int new_value = 0;
        try{
            if(img.getPixel(x,y) >=center){
                new_value = 1;
            }}catch (Exception e){
            System.out.println(e);
        }
        return new_value;


    }

    public void histogramLbp(Bitmap grayImg){


        Mat img_lbp  = new Mat();
        Utils.bitmapToMat(grayImg, img_lbp);

        Imgproc.cvtColor(img_lbp, img_lbp, Imgproc.COLOR_RGB2BGRA);

        double [][] asd = new double[grayImg.getWidth()][grayImg.getHeight()];

        toplam = 0;
        System.out.println("toplam = " + toplam);

        for(int i = 0 ; i < grayImg.getWidth() ; i ++) {

            for (int j = 0; j < grayImg.getHeight(); j++) {
                asd[i][j] = lbpCalculatedPixel(grayImg,i,j);
                toplam += asd[i][j]/(grayImg.getHeight()*grayImg.getWidth());

                //img_lbp.put(i,j,lbpCalculatedPixel(grayImg,i,j));
                //img_lbp.put(i,j,asd[i][j]);

            }
            System.out.println("toplam = " + toplam);

        }


        System.out.println("toplam son = " + toplam);


        MatOfInt histSize = new MatOfInt(256);
        List<Mat> hsv_planes = new ArrayList<Mat>();

        Mat v_hist = new Mat();
        Imgproc.calcHist(Arrays.asList(img_lbp), new MatOfInt(0),
                new Mat(), v_hist, histSize, new MatOfFloat(0f, 256f));

        List<Mat> list = Arrays.asList(v_hist);

        for(int i = 0 ; i< list.size();i++){
            System.out.print(list.get(i) + ",");
        }

    }

}
