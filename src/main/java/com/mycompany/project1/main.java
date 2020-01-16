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
        Instant start = Instant.now();
        for (int z = 0; z < toMB * xMB; z++) {
            outputStream.write(strToBytes);
        }
        Instant finish = Instant.now();
        long time = Duration.between(start, finish).toMillis();
        outputStream.close();
        return time;
    }

    public static long readTest(String absoluteFilePath) throws FileNotFoundException, IOException {
        FileInputStream inputStream = new FileInputStream(absoluteFilePath);
        //long iterationNumber = 0;
        int data;
        Instant start = Instant.now();

        do {
            byte[] buf = new byte[1024];
            data = inputStream.read(buf);
            //iterationNumber++;
        } while (data != -1);

        Instant finish = Instant.now();

        long time = Duration.between(start, finish).toMillis();
        //System.out.println("Liczba iteracji: " + iterationNumber);
        inputStream.close();
        return time;
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

                        int sizeOfLine = 1024;
                        int toMB = 1024;
                        int xMB = 2048;

                        String absoluteFilePath = usb[i] + "fileTest.txt";
                        File file = new File(absoluteFilePath);
                        file.delete();
                        if (file.createNewFile()) {
                            for (int step = 0; step < 2; step++) {
                                
                                Random r = new Random();
                                String str = (Character.toString((char) (r.nextInt(26) + 'a')));
                                String repeated = str.repeat(sizeOfLine);
                                System.out.println("Letter: " + str);

                                long timeWrite1 = writeTest(absoluteFilePath, repeated, toMB, xMB);

                                System.out.println("created fileTests.txt");
                                System.out.println("after: " + usb[i].getFreeSpace());
                                System.out.println("time write: " + timeWrite1);
                                System.out.println(1.0 * sizeOfLine / 1024 * toMB / 1024 * xMB / (1.0 * timeWrite1 / 1000) + "MB/s");
                            }
                            for (int step = 0; step < 2; step++) {
                                long timeRead = readTest(absoluteFilePath);
                                System.out.println("time read: " + timeRead);
                                System.out.println(1.0 * sizeOfLine / 1024 * toMB / 1024 * xMB / (1.0 * timeRead / 1000) + "MB/s");
                            }
                        } else {
                            System.out.println("USB " + drive_name[i] + " removed ");
                        }
                        usb_detected[i] = if_detected;
                    }
                }
            }
        }
    }
}
