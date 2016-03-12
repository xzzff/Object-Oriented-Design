/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jrlmwcunzipper;

import java.nio.file.Path;
import javafx.application.Platform;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.progress.ProgressMonitor;

/**
 * 
 * @author joe
 */
public class Unzipper extends Thread
{
   private Status status;
   private Notification notification;
   private final ProgressMonitor progressMonitor;
   
   /* 
    * These get set by the MainUIScene as they must be set before trying
    * to extract files.
    */
   private Path sourceZipPath;
   private Path destinationPath;
    
   public Unzipper()
   {
       status = Status.NOT_STARTED;
       this.progressMonitor = new ProgressMonitor();
   }
   
   @Override
   public void run()
   {
       status = Status.EXTRACTING;
       try
       {
           unzipFiles(sourceZipPath, destinationPath);
       }
       catch (ZipException e)
       {
           System.err.println("Caught the Zip Exception. Uh oh.");
           System.exit(0);
       }
       
       System.out.println("Should be unzipping files right now which could"
           + "take a long time.");
           
       onFinish();
   }
   
   public void unzipFiles(Path source, Path destination) throws ZipException
   {
       if (source == null)
       {
           System.err.println("Invalid zip file or no zip file selected.");
       }
       if (destination == null)
       {
           System.err.println("Invalid destination directory. Please try again.");
       }
       
       try
       {
           ZipFile zipFile = new ZipFile(source.toString());
           zipFile.extractAll(destination.toString());
       }
       catch(ZipException e)
       {
           System.err.println("Error in unzipped the files.");
           throw e;
       }
   }
   
   public void onInterrupt()
   {
       System.out.println("In onInterrupt() method.");
       status = Status.INTERRUPTED;
       notifyPercentageDone();
   }
   
   public void onFinish()
   {
       System.out.println("In onFinish() method.");
       status = Status.FINISHED;
       notifyPercentageDone();
   }
   
   /**
    * Notifies the UI thread about the progression, i.e.
    * percentage complete.
    */
   private void notifyPercentageDone()
   {
       if (notification != null)
       {
           Platform.runLater(() -> {
              notification.handle(progressMonitor.getPercentDone(), status);
           });
        }
   }
   
   public void setOnNotification(Notification n)
   {
       this.notification = n;
   }
   
   public void setSourceZipPath(Path path)
   {
       this.sourceZipPath = path;
   }
   
   public void setDestinationPath(Path path)
   {
       this.destinationPath = path;
   }
}
