package com.wilsonvillerobotics.firstteamscouter.utilities;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import org.jibble.simpleftp.*;
import org.apache.commons.net.ftp.*;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class FTPTransfer extends AsyncTask<File, Integer, Integer> {

	Context ctx;
	String ftpServerNameOrIP;
	String ftpUserName;
	String ftpPassword;
	SimpleFTP ftp;
	
	public FTPTransfer(Context ctx, String ftpServer, String uName, String pwd) {
		this.ctx = ctx;
		this.ftpServerNameOrIP = ftpServer;
		this.ftpUserName = uName;
		this.ftpPassword = pwd;
	}
	
	public Boolean uploadFile(File fileToFTP) {
		Boolean result = true;
		try
		{
		    //this.ftp = new SimpleFTP();

		    //Toast.makeText(this.ctx, "FTPTransfer::uploadFile : Server: " + this.ftpServerNameOrIP, Toast.LENGTH_SHORT).show();
		    //FTSUtilities.printToConsole("FTPTransfer::uploadFile : Server: " + this.ftpServerNameOrIP);
		    // Connect to an FTP server on port 21.
		    //this.ftp.connect(this.ftpServerNameOrIP, 21, this.ftpUserName, this.ftpPassword);

			this.setupConnection();
			
		    // Set binary mode.
		    this.ftp.bin();

		    this.ftp.stor(fileToFTP);
		    
		    // Change to a new working directory on the FTP server.
		    this.ftp.cwd("testdir");

		    // Upload some files.
		    this.ftp.stor(fileToFTP);

		    // You can also upload from an InputStream, e.g.
		    //ftp.stor(new FileInputStream(new File("test.png")), "test.png");
		    //ftp.stor(someSocket.getInputStream(), "blah.dat");

		    // Quit from the FTP server.
		    //this.ftp.disconnect();
		    this.tearDownConnection();
		}
		catch (IOException e)
		{
			result = false;
			//Toast.makeText(this.ctx, "FTPTransfer::uploadFile : IOException: " + e.getMessage(), Toast.LENGTH_LONG).show();
		    FTSUtilities.printToConsole("FTPTransfer::uploadFile : IOException: " + e.getMessage());
		    e.printStackTrace();
		}
		return result;
	}
	
	public Boolean uploadFiles(SimpleFTP ftp, File fileToFTP) {
		Boolean result = true;
		try
		{
		    //Toast.makeText(this.ctx, "FTPTransfer::uploadFiles : File: " + fileToFTP.getName(), Toast.LENGTH_SHORT).show();
		    FTSUtilities.printToConsole("FTPTransfer::uploadFiles : File: " + fileToFTP.getName());

		    // Set binary mode.
		    this.ftp.bin();

		    this.ftp.stor(fileToFTP);
		    
		    // Change to a new working directory on the FTP server.
		    this.ftp.cwd("testdir");

		    // Upload some files.
		    this.ftp.stor(fileToFTP);
		}
		catch (IOException e)
		{
			result = false;
			//Toast.makeText(this.ctx, "FTPTransfer::uploadFiles : IOException: " + e.getMessage(), Toast.LENGTH_LONG).show();
		    FTSUtilities.printToConsole("FTPTransfer::uploadFiles : IOException: " + e.getMessage());
		    e.printStackTrace();
		}
		return result;
	}
	
	public void setupConnection() {
		if(this.ftp == null) {
			this.ftp = new SimpleFTP();
	
		    Toast.makeText(this.ctx, "FTPTransfer::setupConnection : Server: " + this.ftpServerNameOrIP, Toast.LENGTH_SHORT).show();
		    FTSUtilities.printToConsole("FTPTransfer::setupConnection : Server: " + this.ftpServerNameOrIP);
		    // Connect to an FTP server on port 21.
		    try {
				ftp.connect(this.ftpServerNameOrIP, 21, this.ftpUserName, this.ftpPassword);
			} catch (IOException e) {
				Toast.makeText(this.ctx, "FTPTransfer::setupConnection : IOException: " + e.getMessage(), Toast.LENGTH_SHORT).show();
			    FTSUtilities.printToConsole("FTPTransfer::setupConnection : Server: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	public void tearDownConnection() {
		//Toast.makeText(this.ctx, "FTPTransfer::tearDownConnection", Toast.LENGTH_SHORT).show();
	    FTSUtilities.printToConsole("FTPTransfer::tearDownConnection");
		if(this.ftp != null) {
			try {
				this.ftp.disconnect();
			} catch (IOException e) {
				//Toast.makeText(this.ctx, "FTPTransfer::tearDownConnection : " + e.getMessage(), Toast.LENGTH_SHORT).show();
			    FTSUtilities.printToConsole("FTPTransfer::tearDownConnection : " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
   
   public Boolean upload(File fileToUpload) {
	   Boolean result = true;
	   
	   FTPClient ftpClient = new FTPClient();
	   
	   try {
			ftpClient.connect(InetAddress.getByName(this.ftpServerNameOrIP));
			ftpClient.login(this.ftpUserName, this.ftpPassword);
			ftpClient.changeWorkingDirectory("testdir");
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
	   
			BufferedInputStream buffIn=null;
			buffIn=new BufferedInputStream(new FileInputStream(fileToUpload));
			ftpClient.enterLocalPassiveMode();
			ftpClient.storeFile("test.txt", buffIn);
			buffIn.close();
			ftpClient.logout();
			ftpClient.disconnect();
	   } catch (FileNotFoundException e) {
		   result = false;
		   Toast.makeText(ctx, "FileNotFoundException during upload: " + e.getMessage(), Toast.LENGTH_LONG).show();
		   FTSUtilities.printToConsole("FTPTransfer::upload : FileNotFoundException!");
		   e.printStackTrace();
	   } catch (SocketException e) {
		   result = false;
		   Toast.makeText(ctx, "SocketException during upload: " + e.getMessage(), Toast.LENGTH_LONG).show();
		   FTSUtilities.printToConsole("FTPTransfer::upload : SocketException!");
		   e.printStackTrace();
	   } catch (UnknownHostException e) {
		   result = false;
		   Toast.makeText(ctx, "UnknownHostException during upload: " + e.getMessage(), Toast.LENGTH_LONG).show();
		   FTSUtilities.printToConsole("FTPTransfer::upload : UnknownHostException!");
		   e.printStackTrace();
	   } catch (IOException e) {
		   result = false;
		   Toast.makeText(ctx, "IOException during upload: " + e.getMessage(), Toast.LENGTH_LONG).show();
		   FTSUtilities.printToConsole("FTPTransfer::upload : IOException!");
		   e.printStackTrace();
	   }
	   
	   return result;
   }
	
   /**
    * Download a file from a FTP server. A FTP URL is generated with the
    * following syntax:
    * ftp://user:password@host:port/filePath;type=i.
    *
    * @param ftpServer , FTP server address (optional port ':portNumber').
    * @param user , Optional user name to login.
    * @param password , Optional password for user.
    * @param fileName , Name of file to download (with optional preceeding
    *            relative path, e.g. one/two/three.txt).
    * @param destination , Destination file to save.
    * @throws MalformedURLException, IOException on error.
    */
   public void download(String fileName, File destination ) throws MalformedURLException, IOException {
      if (this.ftpServerNameOrIP != null && fileName != null && destination != null)
      {
         StringBuffer sb = new StringBuffer( "ftp://" );
         // check for authentication else assume its anonymous access.
         if (this.ftpUserName != null && this.ftpPassword != null)
         {
            sb.append( this.ftpUserName );
            sb.append( ':' );
            sb.append( this.ftpPassword );
            sb.append( '@' );
         }
         sb.append( this.ftpServerNameOrIP );
         sb.append( '/' );
         sb.append( fileName );
         /*
          * type ==&gt; a=ASCII mode, i=image (binary) mode, d= file directory
          * listing
          */
         sb.append( ";type=i" );
         BufferedInputStream bis = null;
         BufferedOutputStream bos = null;
         try
         {
            URL url = new URL( sb.toString() );
            URLConnection urlc = url.openConnection();

            bis = new BufferedInputStream( urlc.getInputStream() );
            bos = new BufferedOutputStream( new FileOutputStream(
                  destination.getName() ) );

            int i;
            while ((i = bis.read()) != -1)
            {
               bos.write( i );
            }
         }
         finally
         {
            if (bis != null)
               try
               {
                  bis.close();
               }
               catch (IOException ioe)
               {
                  ioe.printStackTrace();
               }
            if (bos != null)
               try
               {
                  bos.close();
               }
               catch (IOException ioe)
               {
                  ioe.printStackTrace();
               }
         }
      }
      else
      {
         System.out.println( "Input not available" );
      }
   }

	@Override
	protected Integer doInBackground(File... files) {
		Integer fileCount = 0;
		
		this.setupConnection();
		for(File f : files) {
			if(uploadFiles(ftp, f)) fileCount++;
		}
		this.tearDownConnection();
		return fileCount;
	}
	
	protected void onProgressUpdate(Integer... progress) {
        //setProgressPercent(progress[0]);
    }

    protected void onPostExecute(Integer result) {
    	//Toast.makeText(this.ctx, "FTPTransfer::onPostExecute : numFiles Transfered: " + result, Toast.LENGTH_SHORT).show();
    }
}
