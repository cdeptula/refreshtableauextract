package org.openbi.kettle.plugins.refreshtableauextract;
/*! ******************************************************************************
*
* Refresh Tableau Extract plugin for Pentaho Data Integration
*
* Author: Chris Deptula
* https://github.com/cdeptula/tableauexractrefresh
*
*******************************************************************************
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with
* the License. You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
******************************************************************************/

import static org.pentaho.di.job.entry.validator.AndValidator.putValidators;
import static org.pentaho.di.job.entry.validator.JobEntryValidatorUtils.andValidator;
import static org.pentaho.di.job.entry.validator.JobEntryValidatorUtils.fileExistsValidator;
import static org.pentaho.di.job.entry.validator.JobEntryValidatorUtils.integerValidator;
import static org.pentaho.di.job.entry.validator.JobEntryValidatorUtils.notBlankValidator;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileType;
import org.pentaho.di.cluster.SlaveServer;
import org.pentaho.di.core.CheckResultInterface;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.Result;
import org.pentaho.di.core.ResultFile;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleDatabaseException;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.fileinput.FileInputList;
import org.pentaho.di.core.util.StreamLogger;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.vfs.KettleVFS;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entry.JobEntryBase;
import org.pentaho.di.job.entry.JobEntryInterface;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.metastore.api.IMetaStore;
import org.w3c.dom.Node;


/**
* Refresh Tableau Extract type of Job Entry. You can refresh a Tableau extract on a Tableau server in a Job.
* 
* @author Chris
* @since 2013-12-09
*/
public class RefreshTableauExtract extends JobEntryBase implements Cloneable, JobEntryInterface {
 private static Class<?> PKG = RefreshTableauExtract.class; // for i18n purposes, needed by Translator2!! $NON-NLS-1$

 private int refreshType;
 
 private String tableauClient;
 
 private String server;
 
 private String serverPort;
 
 private String serverUser;
 
 private String serverPassword;
 
 private String dataSource;
 
 private String project;
 
 private String extractFile;
 
 private String sourceUser;
 
 private String sourcePassword;
 
 private String proxyUser;
 
 private String proxyPassword;
 
 private String siteName;
 
 public String[] filePaths;
 
 public String[] wildcards;
 
 private boolean fullRefresh;
 
 private String workingDirectory;
 
 private String[] protocolList=new String[]{"http","https"};
 
 private int protocol;
 
 private boolean processResultFiles;
 
 public RefreshTableauExtract(String name) {
	 super( name, "");
	 tableauClient="tableau";
	 refreshType=0;
	 fullRefresh=false;
	 protocol=0;
 }
 
 public RefreshTableauExtract() {
	 this("");
	 clear();
 }
 
 public Object clone() {
   RefreshTableauExtract je = (RefreshTableauExtract) super.clone();
   return je;
 }

 public String getXML() {
    StringBuffer retval = new StringBuffer( 300 );

    retval.append( super.getXML() );
    retval.append( "      " ).append( XMLHandler.addTagValue( "refreshType", getRefreshType() ) );
    retval.append( "      " ).append( XMLHandler.addTagValue( "tableauClient", getTableauClient() ) );
    retval.append( "      " ).append( XMLHandler.addTagValue( "server", getServer() ) );
    retval.append( "      " ).append( XMLHandler.addTagValue( "serverPort", getServerPort() ) );
    retval.append( "      " ).append( XMLHandler.addTagValue( "serverUser", getServerUser() ) );
    retval.append( "      " ).append( XMLHandler.addTagValue( "serverPassword", getServerPassword() ) );
    retval.append( "      " ).append( XMLHandler.addTagValue( "dataSource", getDataSource() ) );
    retval.append( "      " ).append( XMLHandler.addTagValue( "extractFile", getExtractFile() ) );
    retval.append( "      " ).append( XMLHandler.addTagValue( "sourceUser", getSourceUser() ) );
    retval.append( "      " ).append( XMLHandler.addTagValue( "sourcePassword", getSourcePassword() ) );
    retval.append( "      " ).append( XMLHandler.addTagValue( "proxyUser", getProxyUser() ) );
    retval.append( "      " ).append( XMLHandler.addTagValue( "proxyPassword", getProxyPassword() ) );
    retval.append( "      " ).append( XMLHandler.addTagValue( "project", getProject() ) );
    retval.append( "      " ).append( XMLHandler.addTagValue( "siteName", getSiteName() ) );
    retval.append( "      " ).append( XMLHandler.addTagValue( "fullRefresh", getFullRefresh() ) );
    retval.append( "      " ).append( XMLHandler.addTagValue( "protocol", getProtocol() ) );
    retval.append( "      " ).append( XMLHandler.addTagValue( "workingDirectory", getWorkingDirectory() ) );
    retval.append( "      " ).append( XMLHandler.addTagValue( "processResultFiles", getProcessResultFiles() ) );

    retval.append( "      <fields>" ).append( Const.CR );
    if ( filePaths != null ) {
      for ( int i = 0; i < filePaths.length; i++ ) {
        retval.append( "        <field>" ).append( Const.CR );
        retval.append( "          " ).append( XMLHandler.addTagValue( "filePath", filePaths[i] ) );
        retval.append( "          " ).append( XMLHandler.addTagValue( "wildcard", wildcards[i] ) );
        retval.append( "        </field>" ).append( Const.CR );
      }
    }
    retval.append( "      </fields>" ).append( Const.CR );

    return retval.toString();
 }

 public void loadXML( Node entrynode, List<DatabaseMeta> databases, List<SlaveServer> slaveServers, Repository rep,
	      IMetaStore metaStore ) throws KettleXMLException {
	    try {
	      super.loadXML( entrynode, databases, slaveServers );
	      setRefreshType( Integer.parseInt(XMLHandler.getTagValue( entrynode, "refreshType" )) );
	      setTableauClient( XMLHandler.getTagValue( entrynode, "tableauClient" ) );
	      setServer( XMLHandler.getTagValue( entrynode, "server" ) );
	      setServerPort( XMLHandler.getTagValue( entrynode, "serverPort" ) );
	      setServerUser( XMLHandler.getTagValue( entrynode, "serverUser" ) );
	      setServerPassword( XMLHandler.getTagValue( entrynode, "serverPassword" ) );
	      setDataSource( XMLHandler.getTagValue( entrynode, "dataSource" ) );
	      setExtractFile( XMLHandler.getTagValue( entrynode, "extractFile" ) );
	      setSourceUser( XMLHandler.getTagValue( entrynode, "sourceUser" ) );
	      setSourcePassword( XMLHandler.getTagValue( entrynode, "sourcePassword" ) );
	      setProxyUser( XMLHandler.getTagValue( entrynode, "proxyUser" ) );
	      setProxyPassword( XMLHandler.getTagValue( entrynode, "proxyPassword" ) );
	      setProject( XMLHandler.getTagValue( entrynode, "project" ) );
	      setSiteName( XMLHandler.getTagValue( entrynode, "siteName" ) );
	      setWorkingDirectory( XMLHandler.getTagValue( entrynode, "workingDirectory" ) );
	      setProtocol( Integer.parseInt(XMLHandler.getTagValue( entrynode, "protocol" )) );
	      setFullRefresh( "Y".equalsIgnoreCase( XMLHandler.getTagValue( entrynode, "fullRefresh" ) ));
	      setProcessResultFiles( "Y".equalsIgnoreCase( XMLHandler.getTagValue( entrynode, "processResultFiles" ) )); 


	      Node fields = XMLHandler.getSubNode( entrynode, "fields" );

	      // How many field arguments?
	      int nrFields = XMLHandler.countNodes( fields, "field" );
	      filePaths = new String[nrFields];
	      wildcards = new String[nrFields];

	      // Read them all...
	      for ( int i = 0; i < nrFields; i++ ) {
	        Node fnode = XMLHandler.getSubNodeByNr( fields, "field", i );

	        filePaths[i] = XMLHandler.getTagValue( fnode, "filePath" );
	        wildcards[i] = XMLHandler.getTagValue( fnode, "wildcard" );
	      }
	    }

	    catch ( KettleXMLException dbe ) {

	    	throw new KettleXMLException( BaseMessages.getString( PKG, "JobRefreshTableauExtract.Error.Exception.UnableLoadXML" ), dbe );
	    }
	  }

 // Load the jobentry from repository
 public void loadRep( Repository rep, IMetaStore metaStore, ObjectId id_jobentry, List<DatabaseMeta> databases,
      List<SlaveServer> slaveServers ) throws KettleException {
    try {
      setRefreshType( (int)rep.getJobEntryAttributeInteger( id_jobentry, "refreshType" ) );
      setTableauClient( rep.getJobEntryAttributeString( id_jobentry, "tableauClient" ) );
      setServer( rep.getJobEntryAttributeString( id_jobentry, "server" ) );
      setServerPort( rep.getJobEntryAttributeString( id_jobentry, "serverPort" ) );
      setServerUser( rep.getJobEntryAttributeString( id_jobentry, "serverUser" ) );
      setServerPassword( rep.getJobEntryAttributeString( id_jobentry, "serverPassword" ) );
      setDataSource( rep.getJobEntryAttributeString( id_jobentry, "dataSource" ) );
      setExtractFile( rep.getJobEntryAttributeString( id_jobentry, "extractFile" ) );
      setSourceUser( rep.getJobEntryAttributeString( id_jobentry, "sourceUser" ) );
      setSourcePassword( rep.getJobEntryAttributeString( id_jobentry, "sourcePassword" ) );
      setProxyUser( rep.getJobEntryAttributeString( id_jobentry, "proxyUser" ) );
      setProxyPassword( rep.getJobEntryAttributeString( id_jobentry, "proxyPassword" ) );
      setProject( rep.getJobEntryAttributeString( id_jobentry, "project" ) );
      setSiteName( rep.getJobEntryAttributeString( id_jobentry, "siteName" ) );
      setWorkingDirectory( rep.getJobEntryAttributeString( id_jobentry, "workingDirectory" ) );
      setProtocol( Integer.parseInt(rep.getJobEntryAttributeString( id_jobentry, "protocol" )) );
      setFullRefresh( rep.getJobEntryAttributeBoolean( id_jobentry, "fullRefresh" ) );
      setProcessResultFiles( rep.getJobEntryAttributeBoolean( id_jobentry, "processResultFiles" ) );


      // How many arguments?
      int argnr = rep.countNrJobEntryAttributes( id_jobentry, "filePaths" );
      filePaths = new String[argnr];
      wildcards = new String[argnr];

      // Read them all...
      for ( int a = 0; a < argnr; a++ ) {
        filePaths[a] = rep.getJobEntryAttributeString( id_jobentry, a, "filePath" );
        wildcards[a] = rep.getJobEntryAttributeString( id_jobentry, a, "wildcard" );
      }
    } catch ( KettleException dbe ) {

      throw new KettleException( BaseMessages.getString( PKG, "JobRefreshTableauExtract.Error.Exception.UnableLoadRep" )
          + id_jobentry, dbe );
    }
  }

 // Save the attributes of this job entry
 //
 public void saveRep( Repository rep, IMetaStore metaStore, ObjectId id_job ) throws KettleException {
   try {
     rep.saveJobEntryAttribute( id_job, getObjectId(), "refreshType", getRefreshType() );
     rep.saveJobEntryAttribute( id_job, getObjectId(), "tableauClient", getTableauClient() );
     rep.saveJobEntryAttribute( id_job, getObjectId(), "server", getServer() );
     rep.saveJobEntryAttribute( id_job, getObjectId(), "serverPort", getServerPort() );
     rep.saveJobEntryAttribute( id_job, getObjectId(), "serverUser", getServerUser() );
     rep.saveJobEntryAttribute( id_job, getObjectId(), "serverPassword", getServerPassword() );
     rep.saveJobEntryAttribute( id_job, getObjectId(), "dataSource", getDataSource() );
     rep.saveJobEntryAttribute( id_job, getObjectId(), "project", getProject() );
     rep.saveJobEntryAttribute( id_job, getObjectId(), "extractFile", getExtractFile() );
     rep.saveJobEntryAttribute( id_job, getObjectId(), "sourceUser", getSourceUser() );
     rep.saveJobEntryAttribute( id_job, getObjectId(), "sourcePassword", getSourcePassword() );
     rep.saveJobEntryAttribute( id_job, getObjectId(), "proxyUser", getProxyUser() );
     rep.saveJobEntryAttribute( id_job, getObjectId(), "proxyPassword", getProxyPassword() );
     rep.saveJobEntryAttribute( id_job, getObjectId(), "siteName", getSiteName() );
     rep.saveJobEntryAttribute( id_job, getObjectId(), "protocol", getProtocol() );
     rep.saveJobEntryAttribute( id_job, getObjectId(), "fullRefresh", getFullRefresh() );
     rep.saveJobEntryAttribute( id_job, getObjectId(), "processResultFiles", getProcessResultFiles() );
     rep.saveJobEntryAttribute( id_job, getObjectId(), "workingDirectory", getWorkingDirectory() );


     // save the filepaths...
     if ( filePaths != null ) {
         for ( int i = 0; i < filePaths.length; i++ ) {
           rep.saveJobEntryAttribute( id_job, getObjectId(), i, "filePath", filePaths[i] );
           rep.saveJobEntryAttribute( id_job, getObjectId(), i, "wildcard", wildcards[i] );
         }
       }
   } catch ( KettleDatabaseException dbe ) {
	   throw new KettleDatabaseException( BaseMessages.getString( PKG, "JobRefreshTableauExtract.Error.Exception.UnableSaveRep" )
		          + getObjectId(), dbe );
   }
 }

 public void clear() {
   super.clear();

   setRefreshType(0);
   setTableauClient(null);
   setServer(null);
   setServerPort(null);
   setServerUser(null);
   setServerPassword(null);
   setDataSource(null);
   setProject(null);
   setExtractFile(null);
   setSourceUser(null);
   setSourcePassword(null);
   setProxyUser(null);
   setProxyPassword(null);
   setSiteName(null);
   setFilePaths(null);
   setWildcards(null);
 }

 public void setRefreshType( int n ) {
   refreshType = n;
 }

 public int getRefreshType() {
   return refreshType;
 }
 
 public void setTableauClient( String n ) {
   tableauClient = n;
 }

 public String getTableauClient() {
   return tableauClient;
 }

 public void setServer( String n ) {
   server = n;
 }

 public String getServer() {
   return server;
 }

 public void setServerPort( String n ) {
   serverPort = n;
 }

 public String getServerPort() {
   return serverPort;
 }
 
 public void setServerUser( String n ) {
   serverUser = n;
 }

 public String getServerUser() {
   return serverUser;
 }
 
 public void setServerPassword( String n ) {
   serverPassword = n;
 }

 public String getServerPassword() {
   return serverPassword;
 }
 
 public void setDataSource( String n ) {
   dataSource = n;
 }

 public String getDataSource() {
   return dataSource;
 }

 public void setProject( String n ) {
   project = n;
 }

 public String getProject() {
   return project;
 }
 
 public void setExtractFile( String n ) {
   extractFile = n;
 }

 public String getExtractFile() {
   return extractFile;
 }
 
 public void setSourceUser( String n ) {
   sourceUser = n;
 }

 public String getSourceUser() {
   return sourceUser;
 }
 
 public void setSourcePassword( String n ) {
   sourcePassword = n;
 }

 public String getSourcePassword() {
   return sourcePassword;
 }
 
 public void setProxyUser( String n ) {
   proxyUser = n;
 }

 public String getProxyUser() {
   return proxyUser;
 }
 
 public void setProxyPassword( String n ) {
   proxyPassword = n;
 }

 public String getProxyPassword() {
   return proxyPassword;
 }
 
 public void setSiteName( String n ) {
   siteName = n;
 }

 public String getSiteName() {
   return siteName;
 }
 
 public void setFilePaths(String[] n)
 {
	 filePaths=n;
 }
 
 public String[] getFilePaths()
 {
	 return filePaths;
 }
 
 public void setWildcards(String[] n)
 {
	 wildcards=n;
 }
 
 public String[] getWildcards()
 {
	 return wildcards;
 }
 
 public String getRealValue( String n ) {
   return environmentSubstitute( n );
 }

 public void setFullRefresh(boolean n)
 {
	 fullRefresh=n;
 }
 
 public boolean getFullRefresh()
 {
	 return fullRefresh;
 }
 
 public void setFullRefresh(String n)
 {
	 fullRefresh=Boolean.getBoolean(n);
 }
 
 public void setProcessResultFiles(boolean n)
 {
	 processResultFiles=n;
 }
 
 public boolean getProcessResultFiles()
 {
	 return processResultFiles;
 }
 
 public void setProcessResultFiles(String n)
 {
	 processResultFiles=Boolean.getBoolean(n);
 }
 
 public int getProtocol()
 {
	 return protocol;
 }
 
 public void setProtocol(int n)
 {
	 protocol=n;
 }
 
 public String[] getProtocolList()
 {
	 return protocolList;
 }
 
 public String getWorkingDirectory()
 {
	 return workingDirectory;
 }
 
 public void setWorkingDirectory(String n)
 {
	 workingDirectory=n;
 }
 
 public boolean validate() throws KettleException {
	 boolean result=true;
	 if(getRealValue(getTableauClient())==null || getRealValue(getTableauClient()).isEmpty())
	 {
		 logError(BaseMessages.getString( PKG, "RefreshTableauExtract.Validate.TableauClientEmpty" ));
		 result=false;
	 } else if(getRealValue(getServer())==null || getRealValue(getServer()).isEmpty())
	 {
		 logError(BaseMessages.getString( PKG, "RefreshTableauExtract.Validate.TableaServerEmpty" ));
		 result=false;
	 } else if(getRealValue(getServerUser())==null || getRealValue(getServerUser()).isEmpty())
	 {
		 logError(BaseMessages.getString( PKG, "RefreshTableauExtract.Validate.TableaServerUserEmpty" ));
		 result=false;
	 } else if(getRealValue(getServerPassword())==null || getRealValue(getServerPassword()).isEmpty())
	 {
		 logError(BaseMessages.getString( PKG, "RefreshTableauExtract.Validate.TableaServerPasswordEmpty" ));
		 result=false;
	 } else if(getRealValue(getDataSource())==null || getRealValue(getDataSource()).isEmpty())
	 {
		 logError(BaseMessages.getString( PKG, "RefreshTableauExtract.Validate.DataSourceEmpty" ));
		 result=false;
	 }
	 if(getRefreshType()==0){
		 if(getRealValue(getExtractFile())==null || getRealValue(getExtractFile()).isEmpty())
		 {
			 logError(BaseMessages.getString( PKG, "RefreshTableauExtract.Validate.ExtractFileEmpty" ));
			 result=false;
		 }
	 }
	 return result;
 }
 
 private FileObject createTemporaryShellFile( FileObject tempFile, String fileContent ) throws Exception {
	    // Create a unique new temporary filename in the working directory, put the script in there
	    // Set the permissions to execute and then run it...
	    //
	    if ( tempFile != null && fileContent != null ) {
	      try {
	        tempFile.createFile();
	        OutputStream outputStream = tempFile.getContent().getOutputStream();
	        outputStream.write( fileContent.getBytes() );
	        outputStream.close();
	        if ( !Const.getOS().startsWith( "Windows" ) ) {
	          String tempFilename = KettleVFS.getFilename( tempFile );
	          // Now we have to make this file executable...
	          // On Unix-like systems this is done using the command "/bin/chmod +x filename"
	          //
	          ProcessBuilder procBuilder = new ProcessBuilder( "chmod", "+x", tempFilename );
	          Process proc = procBuilder.start();
	          // Eat/log stderr/stdout all messages in a different thread...
	          StreamLogger errorLogger = new StreamLogger( log, proc.getErrorStream(), toString() + " (stderr)" );
	          StreamLogger outputLogger = new StreamLogger( log, proc.getInputStream(), toString() + " (stdout)" );
	          new Thread( errorLogger ).start();
	          new Thread( outputLogger ).start();
	          proc.waitFor();
	        }

	      } catch ( Exception e ) {
	        throw new Exception( "Unable to create temporary file to execute script", e );
	      }
	    }
	    return tempFile;
	  }
 
 public Result execute( Result previousResult, int nr ) throws KettleException {
	 Result result=previousResult;
	 result.setResult(validate());
	 if(!result.getResult())
	 {
		 return result;
	 }
	 String[] commands;
	 String tableauCommand=getRealValue(getTableauClient()).trim();
	 if(tableauCommand.toLowerCase().endsWith(".exe"))
	 {
		 tableauCommand=tableauCommand.substring(0, tableauCommand.length()-4);
	 }
	 tableauCommand="\""+tableauCommand+"\"";
	 if(getRefreshType()==0 || getRefreshType()==1)
	 {
		 tableauCommand+=" refreshextract";
	 } else if (getRefreshType()==2)
	 {
		 tableauCommand+=" addfiletoextract";
	 } else {
		 logError(BaseMessages.getString( PKG, "RefreshTableauExtract.Error.InvalidRefreshType" ));
		 result.setResult(false);
		 return result;
	 }
	 
	 tableauCommand+=" --server "+protocolList[getProtocol()]+"://"+getRealValue(getServer());
	 if(getRealValue(getServerPort())!=null && !getRealValue(getServerPort()).isEmpty())
	 {
		 tableauCommand+=":"+getRealValue(getServerPort());
	 }
	 
	 tableauCommand+=" --username "+getRealValue(getServerUser());
	 tableauCommand+=" --password "+getRealValue(getServerPassword());
	 tableauCommand+=" --datasource \""+getRealValue(getDataSource())+"\"";
	 
	 if(getRealValue(getSiteName())!=null && !getRealValue(getSiteName()).isEmpty())
	 {
		 tableauCommand+=" --site \""+getRealValue(getSiteName())+"\"";
	 }
	 if(getRealValue(getProject())!=null && !getRealValue(getProject()).isEmpty())
	 {
		 tableauCommand+=" --project \""+getRealValue(getProject())+"\"";
	 }
	 if(getRealValue(getProxyUser())!=null && !getRealValue(getProxyUser()).isEmpty())
	 {
		 tableauCommand+=" --proxy-username "+getRealValue(getProxyUser());
	 }
	 if(getRealValue(getProxyPassword())!=null && !getRealValue(getProxyPassword()).isEmpty())
	 {
		 tableauCommand+=" --proxy-password "+getRealValue(getProxyPassword());
	 }
	 
	 if(getRefreshType()==0)
	 {
		 commands=new String[1];
		 tableauCommand+=" --original-file \""+getRealValue(getExtractFile())+"\"";
		 commands[0]=new String(tableauCommand);
	 } else if (getRefreshType()==1)
	 {
		 commands=new String[1];
		 if(getFullRefresh())
		 {
			 tableauCommand+=" --force-full-refresh";
		 }
		 if(getRealValue(getSourceUser())!=null && !getRealValue(getSourceUser()).isEmpty())
		 {
			 tableauCommand+=" --source-username "+getRealValue(getSourceUser());
		 }
		 if(getRealValue(getSourcePassword())!=null & !getRealValue(getSourcePassword()).isEmpty())
		 {
			 tableauCommand+=" --source-password "+getRealValue(getSourcePassword());
		 }
		 commands[0]=new String(tableauCommand);
	 } else {
		 String[] fileStrings = null;
		 if(processResultFiles)
		 {
			 if(result!=null && previousResult.getResultFiles().size()>0)
			 {
				
				int size = previousResult.getResultFiles().size();
		        if ( log.isBasic() ) {
		          logBasic( BaseMessages.getString( PKG, "RefreshTableauExtract.FilesFound", "" + size ) );
		        }
		        try
		        {
			        List<ResultFile> resultFiles=previousResult.getResultFilesList();
			        List<String> files=new ArrayList<String>();
			        Iterator<ResultFile> it = resultFiles.iterator();
			        while(it.hasNext())
			        {
			        	ResultFile f=it.next();
			        	FileObject o=f.getFile();
			        	if(o.getType().equals(FileType.FILE))
			        	{
			        		if(o.exists())
			        		{
			        			files.add(o.getName().toString().startsWith("file:///") ? o.getName().toString().substring(8) : o.getName().toString());
			        		} else {
			        			logBasic(BaseMessages.getString( PKG, "RefreshTableauExtract.FileNotExist", "" + o.getName() ));
			        		}
			        	} else {
			        		logBasic(BaseMessages.getString( PKG, "RefreshTableauExtract.ResultNotFile", "" + o.getName() ));
			        	}
			        }
			        if(files.size()>0)
			        {
			        	Iterator<String> ite = files.iterator();
			        	fileStrings=new String[files.size()];
			        	int i=0;
			        	while (ite.hasNext())
			        	{
			        		fileStrings[i]=ite.next();
			        		i++;
			        	}
			        	
			        } else {
			        	logBasic(BaseMessages.getString(PKG,"RefreshTableauExtract.NoFilesOnResult"));
						 result.setResult(true);
						 return result;
			        }
		        } catch (Exception ex) {
		        	logError(ex.toString());
		        	result.setResult(false);
		        	return result;
		        }
			 } else {
				 logBasic(BaseMessages.getString(PKG,"RefreshTableauExtract.NoFilesOnResult"));
				 result.setResult(true);
				 return result;
			 }
		 } else {
		      // Get source and destination files, also wildcard
		      String[] vFilePaths = filePaths;
		      String[] vWildcards = wildcards;
		      boolean[] includeSubfolders=new boolean[vFilePaths.length];
		      String[] fileRequired=new String[vFilePaths.length];
		      
		      for (int i=0;i<includeSubfolders.length;i++)
		      {
		    	  includeSubfolders[i]=false;
		      }
		      FileInputList files=FileInputList.createFileList(this,vFilePaths,vWildcards,fileRequired,includeSubfolders);
		      fileStrings=new String[files.getFileStrings().length];
		      fileStrings=files.getFileStrings();
		 }
	      commands=new String[fileStrings.length];
	      for(int i=0;i<fileStrings.length;i++)
	      {
	    	commands[i]=new String(tableauCommand+" --file \""+fileStrings[i]+"\"");
	      }
	 }
	 
	 FileObject fileObject = null;
	 String realScript="";
	 FileObject tempFile = null;

	 for(int i=0;i<commands.length;i++)
	 {
	//	 realScript+="echo Running: "+commands[i]+"\n";
		 realScript+=commands[i]+"\n";
		 if(log.isDetailed())
		 {
			 logDetailed(BaseMessages.getString(PKG,"RefreshTableauExtract.Commands",commands[i]));
		 }
	 }
	 try {
	     // What's the exact command?
	     String[] command;

	     if ( log.isBasic() ) {
	       logBasic( BaseMessages.getString( PKG, "RefreshTableuaExtract.RunningOn", Const.getOS() ) );
	     }

	     if ( Const.getOS().equals( "Windows 95" ) ) {
	       //base = new String[] { "command.com", "/C" };
	         tempFile = KettleVFS.createTempFile( "kettle", "shell.bat", null, this );
	         fileObject = createTemporaryShellFile( tempFile, realScript );
	         command=new String[]{"command.com","/C","\""+Const.optionallyQuoteStringByOS( KettleVFS.getFilename( fileObject ) )+"\""};
	     } else if ( Const.getOS().startsWith( "Windows" ) ) {
	       //base = new String[] { "cmd.exe", "/C" };
           tempFile = KettleVFS.createTempFile( "kettle", "shell.bat", null, this );
	       fileObject = createTemporaryShellFile( tempFile, realScript );
	       command=new String[]{"cmd.exe","/C","\""+Const.optionallyQuoteStringByOS( KettleVFS.getFilename( fileObject ) )+"\""};
	     } else {
	       tempFile = KettleVFS.createTempFile( "kettle", "shell", null, this );
	       fileObject = createTemporaryShellFile( tempFile, realScript );
	       command=new String[]{Const.optionallyQuoteStringByOS( KettleVFS.getFilename( fileObject ) )};
	     }
	 
	 
		 ProcessBuilder pb=new ProcessBuilder(command);
		 
		 Map<String, String> env = pb.environment();
	     String[] variables = listVariables();
	     for ( int i = 0; i < variables.length; i++ ) {
	       env.put( variables[i], getVariable( variables[i] ) );
	     }

	     if ( getWorkingDirectory() != null && !Const.isEmpty( Const.rtrim( getRealValue(getWorkingDirectory()) ) ) ) {
	       String vfsFilename = environmentSubstitute( getRealValue(getWorkingDirectory()) );
	       File file = new File( KettleVFS.getFilename( KettleVFS.getFileObject( vfsFilename, this ) ) );
	       pb.directory( file );
	     }
		 
	     if ( log.isDetailed() ) {
	         logDetailed( BaseMessages.getString( PKG, "RefreshTableauExtract.CommandStarted" ) );
	       }
		 Process proc=pb.start();
		// any error message?
	     StreamLogger errorLogger = new StreamLogger( log, proc.getErrorStream(), "(stderr)" );

	     // any output?
	     StreamLogger outputLogger = new StreamLogger( log, proc.getInputStream(), "(stdout)" );

	     // kick them off
	     new Thread( errorLogger ).start();
	     new Thread( outputLogger ).start();

	     proc.waitFor();
		     
	     if ( log.isDetailed() ) {
	         logDetailed( BaseMessages.getString( PKG, "RefreshTableauExtract.CommandFinished" ) );
	     }
	     // What's the exit status?
	     result.setExitStatus( proc.exitValue() );
	     if ( result.getExitStatus() != 0 ) {
	       logError( BaseMessages.getString( PKG, "RefreshTableauExtract.ExitStatus", ""
	           + result.getExitStatus() )) ;
	       result.setResult(false);
	     }

	     // close the streams
	     // otherwise you get "Too many open files, java.io.IOException" after a lot of iterations
	     proc.getErrorStream().close();
	     proc.getOutputStream().close();

		     
		      
	 } catch(Exception ex) {
		 logError(ex.toString());
		 result.setResult(false);
	 } finally {
       // If we created a temporary file, remove it...
       //
       if ( tempFile != null ) {
         try {
           tempFile.delete();
         } catch ( Exception e ) {
           BaseMessages.getString( PKG, "RefreshTableauExtract.UnexpectedError", tempFile.toString(), e.toString() );
         }
       }
     }
		 
	 
   return result;
   
 }


 public boolean evaluates() {
   return true;
 }

 public boolean isUnconditional() {
   return true;
 }

 @Override
 public void check( List<CheckResultInterface> remarks, JobMeta jobMeta, VariableSpace space, Repository repository,
     IMetaStore metaStore ) {
   andValidator().validate( this, "tableauClient", remarks, putValidators( notBlankValidator(),fileExistsValidator() ) );
   andValidator().validate(this, "server", remarks, putValidators(notBlankValidator()));
   andValidator().validate(this, "serverPort", remarks, putValidators(integerValidator()));
   andValidator().validate(this, "serverUser", remarks, putValidators(notBlankValidator()));
   andValidator().validate(this, "serverPassword", remarks, putValidators(notBlankValidator()));
   andValidator().validate(this, "dataSource", remarks, putValidators(notBlankValidator()));
   if(getRefreshType()==0)
   {
	   andValidator().validate(this, "extractFile", remarks, putValidators(notBlankValidator(),fileExistsValidator()));
   }

   
 }
 
 

}
