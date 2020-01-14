/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.project1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 *
 * @author Patryk
 */
public class main {

    public static void main(String[] args) throws IOException, InterruptedException {

        String[] drive_name = new String[]{"E", "F", "G", "H", "I", "J", "K", "L", "M", "N"};
        File[] usb = new File[drive_name.length];

        boolean[] usb_detected = new boolean[drive_name.length];

        for (int i = 0; i < drive_name.length; ++i) {
            usb[i] = new File(drive_name[i] + ":/");
            usb_detected[i] = usb[i].canWrite();
        }

        System.out.println("Please insert USB");
        detect(usb, drive_name, usb_detected);
    }

    public static long writeTest(String absoluteFilePath, String repeated, int toMB, int xMB) throws FileNotFoundException, IOException {
        FileOutputStream outputStream = new FileOutputStream(absoluteFilePath);
        byte[] strToBytes = repeated.getBytes();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
        Instant start = Instant.now();
        for (int z = 0; z < toMB * xMB; z++) {
            outputStream.write(strToBytes);
        }
        Instant finish = Instant.now();
        now = LocalDateTime.now();
        System.out.println(dtf.format(now));
        long timeElapsed = Duration.between(start, finish).toMillis();
        outputStream.close();
        return timeElapsed;
    }

    public static long readTest(String absoluteFilePath) throws FileNotFoundException, IOException {
        FileInputStream inputStream = new FileInputStream(absoluteFilePath);
        //long iter = 0;
        int data = 0;
        System.out.println(java.time.Clock.systemUTC().instant());  
        Instant start = Instant.now();

        do {
            byte[] buf = new byte[1024];
            data = inputStream.read(buf);
            //iter++;
        } while (data != -1);

        Instant finish = Instant.now();
        System.out.println(java.time.Clock.systemUTC().instant());  

        long timeElapsed = Duration.between(start, finish).toMillis();
        //System.out.println("Liczba iteracji: " + iter);
        inputStream.close();
        return timeElapsed;
    }

    public static void detect(File[] usb, String[] drive_name, boolean[] usb_detected) throws IOException, InterruptedException {
        while (true) {
            for (int i = 0; i < drive_name.length; ++i) {
                boolean if_detected;
                if_detected = usb[i].canWrite();
                if (if_detected != usb_detected[i]) {
                    if (if_detected) {
                        System.out.println("USB " + drive_name[i] + " detected ");
                        System.out.println("before: " + usb[i].getFreeSpace());

                        Integer sizeOfLine = 1024;
                        int toMB = 1024;
                        int xMB = 1024;

                        //if (file.createNewFile()) {
                        //for (int step = 0; step < 3; step++) {
                        //}
                        //for (int step = 0; step < 3; step++) {
                        //zapis 1 start
                        String absoluteFilePath1 = usb[i] + "fileTest1.txt";
                        File file1 = new File(absoluteFilePath1);
                        //file1.delete();
                        Random r = new Random();
                        String str = (Character.toString((char) (r.nextInt(26) + 'a')));
                        String repeated = str.repeat(sizeOfLine);
                        System.out.println("Letter: " + str);

                        long timeWrite1 = writeTest(absoluteFilePath1, repeated, toMB, xMB);

                        System.out.println("created fileTests.txt");
                        System.out.println("after: " + usb[i].getFreeSpace());
                        System.out.println("time write: " + timeWrite1);
                        System.out.println(1.0 * sizeOfLine / 1024 * toMB / 1024 * xMB / (1.0 * timeWrite1 / 1000) + "MB/s");
                        //zapis 1 koniec

                        //zapis 2 start
                        String absoluteFilePath2 = usb[i] + "fileTest2.txt";
                        File file2 = new File(absoluteFilePath2);
                        //file2.delete();
                        Random r2 = new Random();
                        String str2 = (Character.toString((char) (r2.nextInt(26) + 'a')));
                        String repeated2 = str2.repeat(sizeOfLine);
                        System.out.println("Letter: " + str);

                        long timeWrite2 = writeTest(absoluteFilePath2, repeated2, toMB, xMB);

                        System.out.println("created fileTests.txt");
                        System.out.println("after: " + usb[i].getFreeSpace());
                        System.out.println("time write: " + timeWrite2);
                        System.out.println(1.0 * sizeOfLine / 1024 * toMB / 1024 * xMB / (1.0 * timeWrite2 / 1000) + "MB/s");
                        //zapis 2 koniec

                        //odczyt1
                        long timeRead1 = readTest(absoluteFilePath1);
                        System.out.println("time read: " + timeRead1);
                        System.out.println(1.0 * sizeOfLine / 1024 * toMB / 1024 * xMB / (1.0 * timeRead1 / 1000) + "MB/s");

                        //odczyt2
                        long timeRead2 = readTest(absoluteFilePath2);
                        System.out.println("time read: " + timeRead2);
                        System.out.println(1.0 * sizeOfLine / 1024 * toMB / 1024 * xMB / (1.0 * timeRead2 / 1000) + "MB/s");

                        //}
                        //}
                    } else {
                        System.out.println("USB " + drive_name[i] + " removed ");
                    }
                    usb_detected[i] = if_detected;
                }
            }
        }
    }
}
