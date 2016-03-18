/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jrlmwcunzipper;

import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.progress.ProgressMonitor;

/**
 * 
 * @author joe
 */
public class Unzipper extends Thread
{
   private Status status;
   private Notification notification;
   private ZipFile zipFile;
   
   /* 
    * These get set by the MainUIScene as they must be set before trying
    * to extract files.
    */
   private Path sourceZipPath;
   private Path destinationPath;
   
   // Used for keeping track of state of percentage done since
   // was having issues with ProgressMonitor
   private long filesExtracted;
   private long filesTotal;
    
   public Unzipper()
   {
       status = Status.NOT_STARTED;
       filesExtracted = filesTotal = 0;
   }
   
   @Override
   public void run()
   {
       long tid = Thread.currentThread().getId();
       System.out.println("Extracter thread ID is: " + tid);    
       try
       {
           validatePaths();
           this.zipFile = new ZipFile(sourceZipPath.toString());
           if(zipFile.isEncrypted())
           {
               // TODO: Could ask user for password and use it to decrypt.
               throw new ZipException("Do not support encrypted Zip Files.");
           }
           unzipFiles();
       }
       catch (ZipException e)
       {
           System.err.println("Something went wrong during the extraction.");
           System.err.println(e.getMessage());
       }
           
       onFinish();
   }
   
   public void unzipFiles() throws ZipException
   {
        System.out.println("About to unzip files.");
        status = Status.EXTRACTING;
        zipFile.setRunInThread(false);
        try
        {
            List fileHeaderList = zipFile.getFileHeaders();
            System.out.println("There are " + fileHeaderList.size() + " files to"
                + " be extracted.");
            this.filesTotal = fileHeaderList.size();
            for (int i = 0; i < filesTotal; ++i)
            {
                if (Thread.interrupted())
                {
                    onInterrupt();
                    return;
                }
                final FileHeader fh = (FileHeader) fileHeaderList.get(i);
                zipFile.extractFile(fh, destinationPath.toString());
                final String currentProcessedFile = fh.getFileName();
                System.out.println("Currently processing: " + 
                    currentProcessedFile);
                Thread.sleep(250);
                this.filesExtracted++;
                updatePercentageDone();
                Thread.sleep(4 * 1000);
            }
        }
        catch(ZipException | InterruptedException e)
        {
            
        }
        
   }
   
   public void validatePaths()
   {
       if (sourceZipPath == null)
       {
           throw new NullPointerException("Source zip path is null.");
       }
       if (destinationPath == null)
       {
           throw new NullPointerException("Destination extraction path"
                   + " is null.");
       }
   }
   
   public void onInterrupt()
   {
       System.out.println("In onInterrupt() method.");
       status = Status.INTERRUPTED;
       updatePercentageDone();
   }
   
   public void onFinish()
   {
       System.out.println("In onFinish() method.");
       status = Status.FINISHED;
       updatePercentageDone();
   }
   
   /**
    * Notifies the UI thread about the progression, i.e. percentage complete.
    */
   private void updatePercentageDone()
   {
       if (notification != null)
       {
           // This will be a number between 0.0 and 1.0
           double percentageDone = (double) filesExtracted / filesTotal;
           Platform.runLater(() ->
           {
              notification.handle(percentageDone, status);
           });
        }
   }
   
   public void setOnNotification(Notification notification)
   {
       this.notification = notification;
   }
   
   public void setSourceZipPath(Path path)
   {
       this.sourceZipPath = path;
   }
   
   public void setDestinationPath(Path path)
   {
       this.destinationPath = path;
   }
   
   public void setStatus(Status newStatus)
   {
       this.status = newStatus;
   }
   
   public Status getStatus()
   {
       return status;
   }
}
