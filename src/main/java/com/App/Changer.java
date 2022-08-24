package com.App;

import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.util.Random;
import java.io.IOException;

public class Changer {
    public int MAX_FILES_TO_READ = 100;
    private ArrayList<File> files;
    private static Random random = new Random();

    private void readFilePaths(){
        files = new ArrayList<>();

        File f = new File(".");

        int c = 0;
        for(String pathname : f.list()){

            String extension = getExtension(pathname);

            // ignore wrong extensions
            if(!(extension.equals("png") || extension.equals("jpg") || extension.equals("jpeg") )){
                continue;
            }

            if(c >= MAX_FILES_TO_READ){
                break;
            }

            files.add(new File(pathname));
            System.out.println("method readFilePaths() --- file added");
            c += 1;
        }

    }
    private void processImages() throws IOException{
        for(File f : files){
            slightlyEditImg(f);
            f = randomizeFilename(f, getExtension(f.getName()));
        }

    }
    private void slightlyEditImg(File file) throws IOException{
        BufferedImage myPicture = null;
        boolean image_too_small = false;
        boolean image_too_big = false;
        try{
            myPicture = ImageIO.read(file);
        } catch(Exception e){
            System.out.println("failed to read file in slightlyEditImg()");
        }
        int h = myPicture.getHeight();
        int w = myPicture.getWidth();

        // sort things out relating to resolution
        if(h<100 || w <100){
            throw new IOException("height or width too small");
        }
        else if(h < 300 || w < 300){
            image_too_small = true;
        } else if(h > 1500 || w > 2000){
            image_too_big = true;
        }



        // resize via method .resizeImage
        double randommodifier = (((double) random.nextInt(100))/1000) + 0.01 ; // random modifier from 0.01 to 0.11

        if(  ((random.nextBoolean() == true || image_too_small))  && !image_too_big ){ //
            int desired_h = (int) ( (1.00+randommodifier)*(h));
            int desired_w = (int) ( (1.00+randommodifier)*(w));
            System.out.println("desired_h" +desired_h+" w "+desired_w);
            System.out.println("modifying by "+(1.00+randommodifier));
            myPicture = resizeImage(myPicture, desired_w, desired_h);

        } else{
            int desired_h = (int) ( (1.00-randommodifier)*(h) );
            int desired_w = (int) ( (1.00-randommodifier)*(w) );
            System.out.println("desired_h" +desired_h+" w "+desired_w);
            System.out.println("modifying by "+(1.00-randommodifier));
            myPicture = resizeImage(myPicture, desired_w, desired_h);

        }

        String fileName = file.getName();
        String extension = getExtension(fileName);

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i+1);
        }
        ImageIO.write(myPicture, extension, file);
    }
    private String getExtension(String pathname){
        String fileName = pathname;
        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i+1);
        }

        return extension;
    }

    // edit first line in method to change how filenames are generated
    private File randomizeFilename(File file, String initialextension){
        long filename =  Math.abs(random.nextLong()) % ((random.nextLong() +160000000));

        File f = new File(String.valueOf(filename)+"."+initialextension);
        file.renameTo(f );

        return file;

    }
    private BufferedImage resizeImage(BufferedImage original, int targetW, int targetH) throws IOException{
        BufferedImage resizedImage = new BufferedImage(targetW, targetH, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(original, 0, 0, targetW, targetH, null);
        graphics2D.dispose();
        return resizedImage;
    }


    public static void main(String[] args){
        try{ Changer c = new Changer();
            c.readFilePaths();
            c.processImages();
        } catch (Exception e){

        }

    }


}