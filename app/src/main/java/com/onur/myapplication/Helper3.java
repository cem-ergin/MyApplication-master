package com.onur.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Helper3 {

    FileWriter writer = new FileWriter("/storage/emulated/0/swedish128asdsadasdas.txt");

    float[] dizi = new float[1910];
    public Helper3() throws IOException {


        String path = Environment.getExternalStorageDirectory().toString()+"/swedishfull";
        Log.d("Files", "Path: " + path);
        System.out.println("yol==="+path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        System.out.println("directory = "+directory);
        Log.d("Files", "Size: "+ files.length);
        for (int i = 0; i < files.length; i++)
        {

            Log.d("Files", "FileName:" + files[i].getName());

            Mat frame = Imgcodecs.imread(files[i].getPath(), 1);
            Mat grayImage = new Mat();
            Imgproc.cvtColor(frame, grayImage, Imgproc.COLOR_BGR2GRAY);

            grayImage.convertTo(grayImage, CvType.CV_8U);
            if(Imgcodecs.imwrite("/storage/emulated/0/detectedGrayImage.jpg", grayImage))
            {
                System.out.println(i+".edge is detected .......");
            }

            Bitmap bmp = null;

            Imgproc.cvtColor(frame, grayImage, Imgproc.COLOR_RGB2BGRA);
            bmp = Bitmap.createBitmap(grayImage.cols(), grayImage.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(grayImage, bmp);

            Bitmap bm = getResizedBitmap(bmp,640,480);
            //Bitmap bm = ShrinkBitmap(/storage/emulated/0/Podcasts/1001.jpg, 640, 480);

            histogramLbp(bm,i,files[i].getName());
        }

        for (int i=0 ; i<dizi.length ; i++){
            System.out.println(i+". değer = "+dizi[i]);

        }



    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSizeW, int maxSizeH) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSizeW;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSizeH;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    Bitmap ShrinkBitmap(String file, int width, int height){

        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

        int heightRatio = (int)Math.ceil(bmpFactoryOptions.outHeight/(float)height);
        int widthRatio = (int)Math.ceil(bmpFactoryOptions.outWidth/(float)width);

        if (heightRatio > 1 || widthRatio > 1)
        {
            if (heightRatio > widthRatio)
            {
                bmpFactoryOptions.inSampleSize = heightRatio;
            } else {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }

        bmpFactoryOptions.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        return bitmap;
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
    public int lbpCalculatedPixel(Bitmap img,int x, int y){

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

        int value = 0;

        for (int i = 0 ; i <val_ar.size();i++){

            value+= val_ar.get(i) * power_val[i];
        }

        return value;

    }



    public void histogramLbp(Bitmap grayImg,int sayac, String isim) throws IOException {


        Mat img_lbp  = new Mat(grayImg.getWidth(),grayImg.getHeight(),CvType.CV_8UC1);
        //Utils.bitmapToMat(grayImg, img_lbp);

//        Imgproc.cvtColor(img_lbp, img_lbp, Imgproc.COLOR_RGB2BGRA);
        //Imgproc.cvtColor(img_lbp, img_lbp, Imgproc.COLOR_BGR2HSV);

        int [][] asd = new int[grayImg.getWidth()][grayImg.getHeight()];

        float toplam = 0;
        System.out.println("toplam = " + toplam);

        for(int i = 0 ; i < grayImg.getWidth() ; i ++) {

            for (int j = 0; j < grayImg.getHeight(); j++) {
                asd[i][j] = lbpCalculatedPixel(grayImg,i,j);
                img_lbp.put(i,j,asd[i][j]);

            }

            System.out.println("işlem yapılan resim: "+isim);
            System.out.println((1907-sayac)+"RESIM KALDI");

        }




//        hist_lbp = Imgproc.calcHist(img_lbp, , None, [256], [0, 256])
        List<Mat> bgrPlanes = new ArrayList<>();
        bgrPlanes.add(img_lbp);
        Mat hist_lbp = new Mat();
        float[] range = {0, 50}; //the upper boundary is exclusive
        MatOfFloat histRange = new MatOfFloat(range);



        Imgproc.calcHist(bgrPlanes,new MatOfInt(0),new Mat(),hist_lbp,new MatOfInt(50),histRange);




        System.out.println(hist_lbp.dump());

        double[] histogram = new double[50];
        for(int i = 0 ; i < 50; i++){
            //System.out.print(hist_lbp.get(i,0)[0] + ",");
            histogram[i] = hist_lbp.get(i,0)[0];
        }
        /*for(int i = 0 ; i < 256; i++){
            System.out.print(histogram[i] + ",");
        }*/



        System.out.println("toplam son = " + toplam);
        dizi[sayac] = toplam;

        for (int i = 0; i <50 ; i++) {
            if(i==49){
                writer.write(Double.toString(histogram[i]));
                writer.write(","+isim);
                break;
            }
            writer.write(histogram[i]+",");
        }
        writer.write(System.getProperty( "line.separator" ));

        if(sayac==1124)
            writer.close();


    }

}


