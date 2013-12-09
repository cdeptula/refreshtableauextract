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

package org.openbi.kettle.plugins.refreshtableauextract;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.Props;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entry.JobEntryDialogInterface;
import org.pentaho.di.job.entry.JobEntryInterface;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.ui.core.gui.WindowProperty;
import org.pentaho.di.ui.core.widget.ColumnInfo;
import org.pentaho.di.ui.core.widget.TableView;
import org.pentaho.di.ui.core.widget.TextVar;
import org.pentaho.di.ui.job.dialog.JobDialog;
import org.pentaho.di.ui.job.entry.JobEntryDialog;
import org.pentaho.di.ui.trans.step.BaseStepDialog;


/**
 * Dialog that allows you to enter the settings for a Refresh Tableau Extract job entry.
 * 
 * @author Chris
 * @since 2013-12-09
 * 
 */
public class RefreshTableauExtractDialog extends JobEntryDialog implements JobEntryDialogInterface {
  private static Class<?> PKG = RefreshTableauExtract.class; // for i18n purposes, needed by Translator2!! $NON-NLS-1$

  private static final String[] FILETYPES = new String[] {
    BaseMessages.getString( PKG, "RefreshTableauExtract.Fileformat.TableauTypes" ),
    BaseMessages.getString( PKG, "RefreshTableauExtract.Fileformat.All" )
     };
  
  private RefreshTableauExtract jobEntry;

  private Label wlName;

  private Text wName;

  private FormData fdlName, fdName;

  private Label wlRefreshType;

  private CCombo wRefreshType;

  private FormData fdlRefreshType, fdRefreshType;
  
  private Label wlTableauClient;
  
  private Button wbTableauClient;

  private TextVar wTableauClient;

  private FormData fdlTableauClient, fdTableauClient, fdbTableauClient;

  private Label wlProtocol;

  private CCombo wProtocol;

  private FormData fdlProtocol, fdProtocol;
  
  private Label wlServer;
  
  private TextVar wServer;

  private FormData fdlServer, fdServer;

  private Label wlServerPort;

  private TextVar wServerPort;

  private FormData fdlServerPort, fdServerPort;

  private Label wlServerUser;

  private TextVar wServerUser;

  private FormData fdlServerUser, fdServerUser;
  
  private Label wlServerPassword;
  
  private TextVar wServerPassword;
  
  private FormData fdlServerPassword, fdServerPassword;

  private Label wlDataSource;

  private TextVar wDataSource;

  private FormData fdlDataSource, fdDataSource;

  private Label wlProject;

  private TextVar wProject;

  private FormData fdlProject, fdProject;

  private Label wlExtractFile;
  
  private TextVar wExtractFile;
  
  private Button wbExtractFile;

  private FormData fdlExtractFile, fdExtractFile, fdbExtractFile;
  
  private Group wRefresh;
  
  private Group wRefreshServer;
  
  private Label wlFullRefresh;

  private Button wFullRefresh;

  private FormData fdlFullRefresh, fdFullRefresh;

  private Label wlSourceUser;

  private TextVar wSourceUser;

  private FormData fdlSourceUser, fdSourceUser;

  private Label wlSourcePassword;

  private TextVar wSourcePassword;

  private FormData fdlSourcePassword, fdSourcePassword;

  private Group wAddFiles;

  private Label wlFile;

  private TextVar wFile;
  
  private Button wbFileFile, wbFileAdd, wbFileDelete, wbFileFolder, wbFileEdit;

  private FormData fdlFile, fdFile, fdbFileFile, fdbFileFolder, fdbFileAdd, fdbFileDelete, fdbFileEdit;
  
  private Label wlWildcard;

  private TextVar wWildcard;
  
  private FormData fdlWildcard, fdWildcard;
  
  private Label wlFiles;

  private TableView wFiles;

  private FormData fdlFiles, fdFiles;
  
  private Label wlProxyUser;

  private TextVar wProxyUser;
  
  private FormData fdlProxyUser, fdProxyUser;
  
  private Label wlProxyPassword;

  private TextVar wProxyPassword;
  
  private FormData fdlProxyPassword, fdProxyPassword;
  
  private Label wlSiteName;

  private TextVar wSiteName;
  
  private FormData fdlSiteName, fdSiteName;

  private Button wOK, wCancel;

  private Listener lsOK, lsCancel;

 // private Shell shell;

  private SelectionAdapter lsDef;

  private boolean backupChanged;

  private Display display;

  private CTabFolder wTabFolder;

  private Composite wGeneralComp, wAdvanceComp;

  private CTabItem wGeneralTab, wAdvanceTab;

  private FormData fdTabFolder, fdGeneralComp, fdAdvanceComp;
  
  private FormData fdRefresh, fdAddFiles, fdRefreshServer;
  
  private Label wlWorkingDirectory;
  
  private Button wbWorkingDirectory;
  
  private TextVar wWorkingDirectory;
  
  private FormData fdlWorkingDirectory, fdbWorkingDirectory, fdWorkingDirectory;
  
  private Label wlResultFiles;
  
  private Button wResultFiles;
  
  private FormData fdlResultFiles, fdResultFiles;

  public RefreshTableauExtractDialog( Shell parent, JobEntryInterface jobEntryInt, Repository rep, JobMeta jobMeta ) {
    super( parent, jobEntryInt, rep, jobMeta );
    jobEntry = (RefreshTableauExtract) jobEntryInt;
  }

  public JobEntryInterface open() {
    Shell parent = getParent();
    display = parent.getDisplay();

    shell = new Shell( parent, props.getJobsDialogStyle() );
    props.setLook( shell );
    JobDialog.setShellImage( shell, jobEntry );

    ModifyListener lsMod = new ModifyListener() {
      public void modifyText( ModifyEvent e ) {
        jobEntry.setChanged();
      }
    };
    backupChanged = jobEntry.hasChanged();

    FormLayout formLayout = new FormLayout();
    formLayout.marginWidth = Const.FORM_MARGIN;
    formLayout.marginHeight = Const.FORM_MARGIN;

    shell.setLayout( formLayout );
    shell.setText( BaseMessages.getString( PKG, "RefreshTableauExtract.Title" ) );

    int middle = props.getMiddlePct();
    int margin = Const.MARGIN;

    // Name line
    wlName = new Label( shell, SWT.RIGHT );
    wlName.setText( BaseMessages.getString( PKG, "RefreshTableauExtract.Name.Label" ) );
    props.setLook( wlName );
    fdlName = new FormData();
    fdlName.left = new FormAttachment( 0, 0 );
    fdlName.top = new FormAttachment( 0, 0 );
    fdlName.right = new FormAttachment( middle, 0 );
    wlName.setLayoutData( fdlName );

    wName = new Text( shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
    props.setLook( wName );
    wName.addModifyListener( lsMod );
    fdName = new FormData();
    fdName.top = new FormAttachment( 0, 0 );
    fdName.left = new FormAttachment( middle, margin );
    fdName.right = new FormAttachment( 100, 0 );
    wName.setLayoutData( fdName );

    wTabFolder = new CTabFolder( shell, SWT.BORDER );
    props.setLook( wTabFolder, Props.WIDGET_STYLE_TAB );

    // ////////////////////////
    // START OF GENERAL TAB ///
    // ////////////////////////

    wGeneralTab = new CTabItem( wTabFolder, SWT.NONE );
    wGeneralTab.setText( BaseMessages.getString( PKG, "RefreshTableauExtract.Tab.General.Label" ) );

    wGeneralComp = new Composite( wTabFolder, SWT.NONE );
    props.setLook( wGeneralComp );

    FormLayout generalLayout = new FormLayout();
    generalLayout.marginWidth = 3;
    generalLayout.marginHeight = 3;
    wGeneralComp.setLayout( generalLayout );
    
    // /////////////////////
    // Tableau Client line
    // /////////////////////
    wlTableauClient = new Label( wGeneralComp, SWT.RIGHT );
    wlTableauClient.setText( BaseMessages.getString( PKG, "RefreshTableauExtract.TableauClient.Label" ) );
    props.setLook( wlTableauClient );
    fdlTableauClient = new FormData();
    fdlTableauClient.left = new FormAttachment( 0, 0 );
    fdlTableauClient.top = new FormAttachment( 0, margin );
    fdlTableauClient.right = new FormAttachment( middle, 0 );
    wlTableauClient.setLayoutData( fdlTableauClient );

    wbTableauClient = new Button( wGeneralComp, SWT.PUSH | SWT.CENTER );
    props.setLook( wbTableauClient );
    wbTableauClient.setText( BaseMessages.getString( PKG, "System.Button.Browse" ) );
    fdbTableauClient = new FormData();
    fdbTableauClient.top = new FormAttachment( 0, margin );
    fdbTableauClient.right = new FormAttachment( 100, 0 );
    wbTableauClient.setLayoutData( fdbTableauClient );

    wTableauClient = new TextVar( jobMeta, wGeneralComp, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
    props.setLook( wTableauClient );
    wTableauClient.addModifyListener( lsMod );
    fdTableauClient = new FormData();
    fdTableauClient.left = new FormAttachment( middle, margin );
    fdTableauClient.right = new FormAttachment( wbTableauClient, -margin );
    fdTableauClient.top = new FormAttachment( 0, margin );
    wTableauClient.setLayoutData( fdTableauClient );

    // ///////////////////
    //Server Protocol
    // ///////////////////
    wlProtocol = new Label( wGeneralComp, SWT.RIGHT );
    wlProtocol.setText( BaseMessages.getString( PKG, "RefreshTableauExtract.Protocol.Label" ) );
    props.setLook( wlProtocol );
    fdlProtocol = new FormData();
    fdlProtocol.left = new FormAttachment( 0, 0 );
    fdlProtocol.right = new FormAttachment( middle, 0 );
    fdlProtocol.top = new FormAttachment( wTableauClient, margin );
    wlProtocol.setLayoutData( fdlProtocol );
    wProtocol = new CCombo( wGeneralComp, SWT.SINGLE | SWT.READ_ONLY | SWT.BORDER );
    wProtocol.setItems( jobEntry.getProtocolList() );
    props.setLook( wProtocol );
    fdProtocol = new FormData();
    fdProtocol.left = new FormAttachment( middle, margin );
    fdProtocol.top = new FormAttachment( wTableauClient, margin );
    fdProtocol.right = new FormAttachment( wbTableauClient, 0 );
    wProtocol.setLayoutData( fdProtocol );
    
    // /////////////////////
    // Server line
    // /////////////////////
    wlServer = new Label( wGeneralComp, SWT.RIGHT );
    wlServer.setText( BaseMessages.getString( PKG, "RefreshTableauExtract.Server.Label" ) );
    props.setLook( wlServer );
    fdlServer = new FormData();
    fdlServer.left = new FormAttachment( 0, 0 );
    fdlServer.top = new FormAttachment( wProtocol, margin );
    fdlServer.right = new FormAttachment( middle, 0 );
    wlServer.setLayoutData( fdlServer );

    wServer = new TextVar( jobMeta, wGeneralComp, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
    props.setLook( wServer );
    wServer.addModifyListener( lsMod );
    fdServer = new FormData();
    fdServer.left = new FormAttachment( middle, margin );
    fdServer.right = new FormAttachment( wbTableauClient, -margin );
    fdServer.top = new FormAttachment( wProtocol, margin );
    wServer.setLayoutData( fdServer );
    
    // /////////////////////
    // Server Port line
    // /////////////////////
    wlServerPort = new Label( wGeneralComp, SWT.RIGHT );
    wlServerPort.setText( BaseMessages.getString( PKG, "RefreshTableauExtract.ServerPort.Label" ) );
    props.setLook( wlServerPort );
    fdlServerPort = new FormData();
    fdlServerPort.left = new FormAttachment( 0, 0 );
    fdlServerPort.top = new FormAttachment( wServer, margin );
    fdlServerPort.right = new FormAttachment( middle, 0 );
    wlServerPort.setLayoutData( fdlServerPort );

    wServerPort = new TextVar( jobMeta, wGeneralComp, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
    props.setLook( wServerPort );
    wServerPort.addModifyListener( lsMod );
    fdServerPort = new FormData();
    fdServerPort.left = new FormAttachment( middle, margin );
    fdServerPort.right = new FormAttachment( wbTableauClient, -margin );
    fdServerPort.top = new FormAttachment( wServer, margin );
    wServerPort.setLayoutData( fdServerPort );
    
    // /////////////////////
    // Server User line
    // /////////////////////
    wlServerUser = new Label( wGeneralComp, SWT.RIGHT );
    wlServerUser.setText( BaseMessages.getString( PKG, "RefreshTableauExtract.ServerUser.Label" ) );
    props.setLook( wlServerUser );
    fdlServerUser = new FormData();
    fdlServerUser.left = new FormAttachment( 0, 0 );
    fdlServerUser.top = new FormAttachment( wServerPort, margin );
    fdlServerUser.right = new FormAttachment( middle, 0 );
    wlServerUser.setLayoutData( fdlServerUser );

    wServerUser = new TextVar( jobMeta, wGeneralComp, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
    props.setLook( wServerUser );
    wServerUser.addModifyListener( lsMod );
    fdServerUser = new FormData();
    fdServerUser.left = new FormAttachment( middle, margin );
    fdServerUser.right = new FormAttachment( wbTableauClient, -margin );
    fdServerUser.top = new FormAttachment( wServerPort, margin );
    wServerUser.setLayoutData( fdServerUser );
    
    // /////////////////////
    // Server Password line
    // /////////////////////
    wlServerPassword = new Label( wGeneralComp, SWT.RIGHT );
    wlServerPassword.setText( BaseMessages.getString( PKG, "RefreshTableauExtract.ServerPassword.Label" ) );
    props.setLook( wlServerPassword );
    fdlServerPassword = new FormData();
    fdlServerPassword.left = new FormAttachment( 0, 0 );
    fdlServerPassword.top = new FormAttachment( wServerUser, margin );
    fdlServerPassword.right = new FormAttachment( middle, 0 );
    wlServerPassword.setLayoutData( fdlServerPassword );

    wServerPassword = new TextVar( jobMeta, wGeneralComp, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
    props.setLook( wServerPassword );
    wServerPassword.addModifyListener( lsMod );
    wServerPassword.setEchoChar('*');
    fdServerPassword = new FormData();
    fdServerPassword.left = new FormAttachment( middle, margin );
    fdServerPassword.right = new FormAttachment( wbTableauClient, -margin );
    fdServerPassword.top = new FormAttachment( wServerUser, margin );
    wServerPassword.setLayoutData( fdServerPassword );
    
    // /////////////////////
    // Data Sourceline
    // /////////////////////
    wlDataSource = new Label( wGeneralComp, SWT.RIGHT );
    wlDataSource.setText( BaseMessages.getString( PKG, "RefreshTableauExtract.DataSource.Label" ) );
    props.setLook( wlDataSource );
    fdlDataSource = new FormData();
    fdlDataSource.left = new FormAttachment( 0, 0 );
    fdlDataSource.top = new FormAttachment( wServerPassword, margin );
    fdlDataSource.right = new FormAttachment( middle, 0 );
    wlDataSource.setLayoutData( fdlDataSource );

    wDataSource = new TextVar( jobMeta, wGeneralComp, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
    props.setLook( wDataSource );
    wDataSource.addModifyListener( lsMod );
    fdDataSource = new FormData();
    fdDataSource.left = new FormAttachment( middle, margin );
    fdDataSource.right = new FormAttachment( wbTableauClient, -margin );
    fdDataSource.top = new FormAttachment( wServerPassword, margin );
    wDataSource.setLayoutData( fdDataSource );
   
    // /////////////////////
    // Project line
    // /////////////////////
    wlProject = new Label( wGeneralComp, SWT.RIGHT );
    wlProject.setText( BaseMessages.getString( PKG, "RefreshTableauExtract.Project.Label" ) );
    props.setLook( wlProject );
    fdlProject = new FormData();
    fdlProject.left = new FormAttachment( 0, 0 );
    fdlProject.top = new FormAttachment( wDataSource, margin );
    fdlProject.right = new FormAttachment( middle, 0 );
    wlProject.setLayoutData( fdlProject );

    wProject = new TextVar( jobMeta, wGeneralComp, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
    props.setLook( wProject );
    wProject.addModifyListener( lsMod );
    fdProject = new FormData();
    fdProject.left = new FormAttachment( middle, margin );
    fdProject.right = new FormAttachment( wbTableauClient, -margin );
    fdProject.top = new FormAttachment( wDataSource, margin );
    wProject.setLayoutData( fdProject );
    
    // /////////////////////
    // Working Directory line
    // /////////////////////
    wlWorkingDirectory = new Label( wGeneralComp, SWT.RIGHT );
    wlWorkingDirectory.setText( BaseMessages.getString( PKG, "RefreshTableauExtract.WorkingDirectory.Label" ) );
    props.setLook( wlWorkingDirectory );
    fdlWorkingDirectory = new FormData();
    fdlWorkingDirectory.left = new FormAttachment( 0, 0 );
    fdlWorkingDirectory.top = new FormAttachment( wProject, margin );
    fdlWorkingDirectory.right = new FormAttachment( middle, 0 );
    wlWorkingDirectory.setLayoutData( fdlWorkingDirectory );
    
    wbWorkingDirectory = new Button( wGeneralComp, SWT.PUSH | SWT.CENTER );
    props.setLook( wbWorkingDirectory );
    wbWorkingDirectory.setText( BaseMessages.getString( PKG, "System.Button.Browse" ) );
    fdbWorkingDirectory = new FormData();
    fdbWorkingDirectory.top = new FormAttachment( wProject, margin );
    fdbWorkingDirectory.right = new FormAttachment( 100, 0 );
    wbWorkingDirectory.setLayoutData( fdbWorkingDirectory );

    wWorkingDirectory = new TextVar( jobMeta, wGeneralComp, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
    props.setLook( wWorkingDirectory );
    wWorkingDirectory.addModifyListener( lsMod );
    fdWorkingDirectory = new FormData();
    fdWorkingDirectory.left = new FormAttachment( middle, margin );
    fdWorkingDirectory.right = new FormAttachment( wbWorkingDirectory, -margin );
    fdWorkingDirectory.top = new FormAttachment( wProject, margin );
    wWorkingDirectory.setLayoutData( fdWorkingDirectory );
    
    wbWorkingDirectory.addSelectionListener( new SelectionAdapter() {
        public void widgetSelected( SelectionEvent e ) {
          DirectoryDialog ddialog = new DirectoryDialog( shell, SWT.OPEN );
          if ( wWorkingDirectory.getText() != null ) {
            ddialog.setFilterPath( jobMeta.environmentSubstitute( wWorkingDirectory.getText() ) );
          }

          // Calling open() will open and run the dialog.
          // It will return the selected directory, or
          // null if user cancels
          String dir = ddialog.open();
          if ( dir != null ) {
            // Set the text box to the new selection
            wWorkingDirectory.setText( dir );
          }

        }
      } );
    
    // ///////////////////
    //Refresh Type
    // ///////////////////
    wlRefreshType = new Label( wGeneralComp, SWT.RIGHT );
    wlRefreshType.setText( BaseMessages.getString( PKG, "RefreshTableauExtract.RefreshType.Label" ) );
    props.setLook( wlRefreshType );
    fdlRefreshType = new FormData();
    fdlRefreshType.left = new FormAttachment( 0, 0 );
    fdlRefreshType.right = new FormAttachment( middle, 0 );
    fdlRefreshType.top = new FormAttachment( wWorkingDirectory, margin );
    wlRefreshType.setLayoutData( fdlRefreshType );
    wRefreshType = new CCombo( wGeneralComp, SWT.SINGLE | SWT.READ_ONLY | SWT.BORDER );
    wRefreshType.setItems( new String[] {BaseMessages.getString( PKG, "RefreshTableauExtract.RefreshType.Refresh.Label" ),
    		BaseMessages.getString( PKG, "RefreshTableauExtract.RefreshType.RefreshServer.Label" ),
    		BaseMessages.getString( PKG, "RefreshTableauExtract.RefreshType.Add.Label" )} );
    props.setLook( wRefreshType );
    fdRefreshType = new FormData();
    fdRefreshType.left = new FormAttachment( middle, margin );
    fdRefreshType.top = new FormAttachment( wWorkingDirectory, margin );
    fdRefreshType.right = new FormAttachment( wbTableauClient, 0 );
    wRefreshType.setLayoutData( fdRefreshType );
    
    wRefreshType.addSelectionListener( new SelectionAdapter() {
        public void widgetSelected( SelectionEvent e ) {
         updateActive();
          }

        }
       );
    
    // ////////////////////////
    // START OF Refresh GROUP
    //
    wRefresh = new Group( wGeneralComp, SWT.SHADOW_NONE );
    props.setLook( wRefresh );
    wRefresh.setText( BaseMessages.getString( PKG, "RefreshTableauExtract.Refresh.Group.Label" ) );

    FormLayout groupLayout = new FormLayout();
    groupLayout.marginWidth = 10;
    groupLayout.marginHeight = 10;
    
    fdRefresh=new FormData();
    fdRefresh.left = new FormAttachment(0,0);
    fdRefresh.top=new FormAttachment(wRefreshType,margin);
    fdRefresh.right=new FormAttachment(100,0);
    
    wRefresh.setLayout(groupLayout);
    wRefresh.setLayoutData( fdRefresh );

    // /////////////////////
    // Extract File Line 
    // /////////////////////
    wlExtractFile = new Label( wRefresh, SWT.RIGHT );
    wlExtractFile.setText( BaseMessages.getString( PKG, "RefreshTableauExtract.ExtractFile.Label" ) );
    props.setLook( wlExtractFile );
    fdlExtractFile = new FormData();
    fdlExtractFile.left = new FormAttachment( 0, 0 );
    fdlExtractFile.top = new FormAttachment( wProject, margin );
    fdlExtractFile.right = new FormAttachment( middle, 0 );
    wlExtractFile.setLayoutData( fdlExtractFile );
    
    wbExtractFile = new Button( wRefresh, SWT.PUSH | SWT.CENTER );
    props.setLook( wbExtractFile );
    wbExtractFile.setText( BaseMessages.getString( PKG, "System.Button.Browse" ) );
    fdbExtractFile = new FormData();
    fdbExtractFile.top = new FormAttachment( wProject, margin );
    fdbExtractFile.right = new FormAttachment( 100, 0 );
    wbExtractFile.setLayoutData( fdbExtractFile );

    wExtractFile = new TextVar( jobMeta, wRefresh, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
    props.setLook( wExtractFile );
    wExtractFile.addModifyListener( lsMod );
    fdExtractFile = new FormData();
    fdExtractFile.left = new FormAttachment( middle, margin );
    fdExtractFile.right = new FormAttachment( wbExtractFile, -margin );
    fdExtractFile.top = new FormAttachment( wProject, margin );
    wExtractFile.setLayoutData( fdExtractFile );
    
    // ////////////////////////
    // START OF Refresh Server GROUP
    //
    wRefreshServer = new Group( wGeneralComp, SWT.SHADOW_NONE );
    props.setLook( wRefreshServer );
    wRefreshServer.setText( BaseMessages.getString( PKG, "RefreshTableauExtract.RefreshServer.Group.Label" ) );

    fdRefreshServer=new FormData();
    fdRefreshServer.left = new FormAttachment(0,0);
    fdRefreshServer.top=new FormAttachment(wRefresh,margin);
    fdRefreshServer.right=new FormAttachment(100,0);
    
    wRefreshServer.setLayout(groupLayout);
    wRefreshServer.setLayoutData( fdRefreshServer );
    
    // //////////////////////
    // Full Refresh Line
    // /////////////////////
    wlFullRefresh = new Label( wRefreshServer, SWT.RIGHT );
    wlFullRefresh.setText( BaseMessages.getString( PKG, "RefreshTableauExtract.FullRefresh.Label" ) );
    props.setLook( wlFullRefresh );
    fdlFullRefresh = new FormData();
    fdlFullRefresh.left = new FormAttachment( 0, 0 );
    fdlFullRefresh.top = new FormAttachment( 0, margin );
    fdlFullRefresh.right = new FormAttachment( middle, 0 );
    wlFullRefresh.setLayoutData( fdlFullRefresh );
    wFullRefresh = new Button( wRefreshServer, SWT.CHECK );
    props.setLook( wFullRefresh );
    fdFullRefresh = new FormData();
    fdFullRefresh.left = new FormAttachment( middle, margin );
    fdFullRefresh.top = new FormAttachment( 0, margin );
    fdFullRefresh.right = new FormAttachment( 100, 0 );
    wFullRefresh.setLayoutData( fdFullRefresh );
    
    // //////////////////////
    // Source User Line
    // /////////////////////
    wlSourceUser = new Label( wRefreshServer, SWT.RIGHT );
    wlSourceUser.setText( BaseMessages.getString( PKG, "RefreshTableauExtract.SourceUser.Label" ) );
    props.setLook( wlSourceUser );
    fdlSourceUser = new FormData();
    fdlSourceUser.left = new FormAttachment( 0, 0 );
    fdlSourceUser.top = new FormAttachment( wFullRefresh, margin );
    fdlSourceUser.right = new FormAttachment( middle, 0 );
    wlSourceUser.setLayoutData( fdlSourceUser );
    wSourceUser = new TextVar( jobMeta, wRefreshServer, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
    wSourceUser.setText( "" );
    props.setLook( wSourceUser );
    fdSourceUser = new FormData();
    fdSourceUser.left = new FormAttachment( middle, margin );
    fdSourceUser.top = new FormAttachment( wFullRefresh, margin );
    fdSourceUser.right = new FormAttachment( 100, 0 );
    wSourceUser.setLayoutData( fdSourceUser );
    
    // //////////////////////
    // Source Password Line
    // /////////////////////
    wlSourcePassword = new Label( wRefreshServer, SWT.RIGHT );
    wlSourcePassword.setText( BaseMessages.getString( PKG, "RefreshTableauExtract.SourcePassword.Label" ) );
    props.setLook( wlSourcePassword );
    fdlSourcePassword = new FormData();
    fdlSourcePassword.left = new FormAttachment( 0, 0 );
    fdlSourcePassword.top = new FormAttachment( wSourceUser, margin );
    fdlSourcePassword.right = new FormAttachment( middle, 0 );
    wlSourcePassword.setLayoutData( fdlSourcePassword );
    wSourcePassword = new TextVar( jobMeta, wRefreshServer, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
    wSourcePassword.setText( "" );
    wSourcePassword.setEchoChar('*');
    props.setLook( wSourcePassword );
    fdSourcePassword = new FormData();
    fdSourcePassword.left = new FormAttachment( middle, margin );
    fdSourcePassword.top = new FormAttachment( wSourceUser, margin );
    fdSourcePassword.right = new FormAttachment( 100, 0 );
    wSourcePassword.setLayoutData( fdSourcePassword );

    // ////////////////////////
    // START OF AddFile GROUP
    //
    wAddFiles = new Group( wGeneralComp, SWT.SHADOW_NONE );
    props.setLook( wAddFiles );
    wAddFiles.setText( BaseMessages.getString( PKG, "RefreshTableauExtract.AddFiles.Group.Label" ) );
    
    fdAddFiles=new FormData();
    fdAddFiles.left = new FormAttachment(0,0);
    fdAddFiles.top=new FormAttachment(wRefreshServer,margin);
    fdAddFiles.right=new FormAttachment(100,0);
    
    wAddFiles.setLayout(groupLayout);
    wAddFiles.setLayoutData( fdAddFiles );
    
    // //////////////////////
    // Files from result Line
    // /////////////////////
    wlResultFiles = new Label( wAddFiles, SWT.RIGHT );
    wlResultFiles.setText( BaseMessages.getString( PKG, "RefreshTableauExtract.ResultFiles.Label" ) );
    props.setLook( wlResultFiles );
    fdlResultFiles = new FormData();
    fdlResultFiles.left = new FormAttachment( 0, 0 );
    fdlResultFiles.top = new FormAttachment( 0, margin );
    fdlResultFiles.right = new FormAttachment( middle, 0 );
    wlResultFiles.setLayoutData( fdlResultFiles );
    wResultFiles = new Button( wAddFiles, SWT.CHECK );
    props.setLook( wResultFiles );
    fdResultFiles = new FormData();
    fdResultFiles.left = new FormAttachment( middle, margin );
    fdResultFiles.top = new FormAttachment( 0, margin );
    fdResultFiles.right = new FormAttachment( 100, 0 );
    wResultFiles.setLayoutData( fdResultFiles );
    
    wResultFiles.addSelectionListener(new SelectionAdapter() {
    	public void widgetSelected( SelectionEvent e )
    	{
    		updateActive();
    	}
    });
    // //////////////////////
    // fILE Line
    // /////////////////////
    wlFile = new Label( wAddFiles, SWT.RIGHT );
    wlFile.setText( BaseMessages.getString( PKG, "RefreshTableauExtract.File.Label" ) );
    props.setLook( wlFile );
    fdlFile = new FormData();
    fdlFile.left = new FormAttachment( 0, 0 );
    fdlFile.top = new FormAttachment( wResultFiles, margin );
    fdlFile.right = new FormAttachment( middle, 0 );
    wlFile.setLayoutData( fdlFile );
    
    // Browse Source folders button ...
    wbFileFolder = new Button( wAddFiles, SWT.PUSH | SWT.CENTER );
    props.setLook( wbFileFolder );
    wbFileFolder.setText( BaseMessages.getString( PKG, "RefreshTableauExtract.BrowseFolders.Label" ) );
    fdbFileFolder = new FormData();
    fdbFileFolder.right = new FormAttachment( 100, 0 );
    fdbFileFolder.top = new FormAttachment( wResultFiles, margin );
    wbFileFolder.setLayoutData( fdbFileFolder );
    
    wbFileFolder.addSelectionListener( new SelectionAdapter() {
        public void widgetSelected( SelectionEvent e ) {
          DirectoryDialog ddialog = new DirectoryDialog( shell, SWT.OPEN );
          if ( wFile.getText() != null ) {
            ddialog.setFilterPath( jobMeta.environmentSubstitute( wFile.getText() ) );
          }

          // Calling open() will open and run the dialog.
          // It will return the selected directory, or
          // null if user cancels
          String dir = ddialog.open();
          if ( dir != null ) {
            // Set the text box to the new selection
            wFile.setText( dir );
          }

        }
      } );

      // Browse Source files button ...
      wbFileFile = new Button( wAddFiles, SWT.PUSH | SWT.CENTER );
      props.setLook( wbFileFile );
      wbFileFile.setText( BaseMessages.getString( PKG, "RefreshTableauExtract.BrowseFiles.Label" ) );
      fdbFileFile = new FormData();
      fdbFileFile.right = new FormAttachment( wbFileFolder, -margin );
      fdbFileFile.top = new FormAttachment( wResultFiles, margin );
      wbFileFile.setLayoutData( fdbFileFile );

      // Browse Source file add button ...
      wbFileAdd = new Button( wAddFiles, SWT.PUSH | SWT.CENTER );
      props.setLook( wbFileAdd );
      wbFileAdd.setText( BaseMessages.getString( PKG, "RefreshTableauExtract.FilenameAdd.Button" ) );
      fdbFileAdd = new FormData();
      fdbFileAdd.right = new FormAttachment( wbFileFile, -margin );
      fdbFileAdd.top = new FormAttachment( wResultFiles, margin );
      wbFileAdd.setLayoutData( fdbFileAdd );

      wbFileFile.addSelectionListener( new SelectionAdapter() {
        public void widgetSelected( SelectionEvent e ) {
          FileDialog dialog = new FileDialog( shell, SWT.OPEN );
          dialog.setFilterExtensions( new String[] {"*.tde;*.txt;*.csv;*.tabB;*.xls;*.xlsx;*.xlsm;*.xlsb","*" } );
          if ( wFile.getText() != null ) {
            dialog.setFileName( jobMeta.environmentSubstitute( wFile.getText() ) );
          }
          dialog.setFilterNames( FILETYPES );
          if ( dialog.open() != null ) {
            wFile.setText( dialog.getFilterPath() + Const.FILE_SEPARATOR + dialog.getFileName() );
          }
        }
      } );
    
      wFile = new TextVar( jobMeta, wAddFiles, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
      wFile.setText( "" );
      props.setLook( wFile );
      fdFile = new FormData();
      fdFile.left = new FormAttachment( middle, margin );
      fdFile.top = new FormAttachment( wResultFiles, margin );
      fdFile.right = new FormAttachment( wbFileAdd, 0 );
      wFile.setLayoutData( fdFile );
      
      // Whenever something changes, set the tooltip to the expanded version:
      wFile.addModifyListener( new ModifyListener() {
        public void modifyText( ModifyEvent e ) {
          wFile.setToolTipText( jobMeta.environmentSubstitute( wFile.getText() ) );
        }
      } );
      
      // //////////////////////
      // Wildcard Line
      // /////////////////////
      wlWildcard = new Label( wAddFiles, SWT.RIGHT );
      wlWildcard.setText( BaseMessages.getString( PKG, "RefreshTableauExtract.Wildcard.Label" ) );
      props.setLook( wlWildcard );
      fdlWildcard = new FormData();
      fdlWildcard.left = new FormAttachment( 0, 0 );
      fdlWildcard.top = new FormAttachment( wFile, margin );
      fdlWildcard.right = new FormAttachment( middle, 0 );
      wlWildcard.setLayoutData( fdlWildcard );
      wWildcard = new TextVar( jobMeta, wAddFiles, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
      wWildcard.setText( "" );
      props.setLook( wWildcard );
      fdWildcard = new FormData();
      fdWildcard.left = new FormAttachment( middle, margin );
      fdWildcard.top = new FormAttachment( wFile, margin );
      fdWildcard.right = new FormAttachment( wbFileAdd, 0 );
      wWildcard.setLayoutData( fdWildcard );
      
      // /////////////////////////
      // Edit and Delete buttons
      // ////////////////////////
      
      // Buttons to the right of the screen...
      wbFileDelete = new Button( wAddFiles, SWT.PUSH | SWT.CENTER );
      props.setLook( wbFileDelete );
      wbFileDelete.setText( BaseMessages.getString( PKG, "RefreshTableauExtract.FilenameDelete.Button" ) );
      wbFileDelete.setToolTipText( BaseMessages.getString( PKG, "RefreshTableauExtract.FilenameDelete.Tooltip" ) );
      fdbFileDelete = new FormData();
      fdbFileDelete.right = new FormAttachment( 100, 0 );
      fdbFileDelete.top = new FormAttachment( wWildcard, 40 );
      wbFileDelete.setLayoutData( fdbFileDelete );

      wbFileEdit = new Button( wAddFiles, SWT.PUSH | SWT.CENTER );
      props.setLook( wbFileEdit );
      wbFileEdit.setText( BaseMessages.getString( PKG, "RefreshTableauExtract.FilenameEdit.Button" ) );
      wbFileEdit.setToolTipText( BaseMessages.getString( PKG, "RefreshTableauExtract.FilenameEdit.Tooltip" ) );
      fdbFileEdit = new FormData();
      fdbFileEdit.right = new FormAttachment( 100, 0 );
      fdbFileEdit.left = new FormAttachment( wbFileDelete, 0, SWT.LEFT );
      fdbFileEdit.top = new FormAttachment( wbFileDelete, margin );
      wbFileEdit.setLayoutData( fdbFileEdit );
      
      wlFiles = new Label( wAddFiles, SWT.NONE );
      wlFiles.setText( BaseMessages.getString( PKG, "RefreshTableauExtract.Files.Label" ) );
      props.setLook( wlFiles );
      fdlFiles = new FormData();
      fdlFiles.left = new FormAttachment( 0, 0 );
      fdlFiles.right = new FormAttachment( middle, -margin );
      fdlFiles.top = new FormAttachment( wWildcard, margin );
      wlFiles.setLayoutData( fdlFiles );

      int rows =
          jobEntry.filePaths == null ? 1 : ( jobEntry.filePaths.length == 0 ? 0
              : jobEntry.filePaths.length );
      final int FieldsRows = rows;

      ColumnInfo[] colinf =
          new ColumnInfo[] {
            new ColumnInfo( BaseMessages.getString( PKG, "RefreshTableauExtract.Files.Filename.Label" ),
                ColumnInfo.COLUMN_TYPE_TEXT, false ),
            new ColumnInfo( BaseMessages.getString( PKG, "RefreshTableauExtract.Files.Wildcard.Label" ),
                ColumnInfo.COLUMN_TYPE_TEXT, false ), };

      colinf[0].setUsingVariables( true );
      colinf[0].setToolTip( BaseMessages.getString( PKG, "RefreshTableauExtract.Files.Filename.Tooltip" ) );
      colinf[1].setUsingVariables( true );
      colinf[1].setToolTip( BaseMessages.getString( PKG, "RefreshTableauExtract.Files.Wildcard.Tooltip" ) );

      wFiles =
          new TableView( jobMeta, wAddFiles, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI, colinf, FieldsRows, lsMod,
              props );

      fdFiles = new FormData();
      fdFiles.left = new FormAttachment( 0, 0 );
      fdFiles.top = new FormAttachment( wlFiles, margin );
      fdFiles.right = new FormAttachment( wbFileDelete, -margin );
      fdFiles.bottom = new FormAttachment( 100, -margin );
      wFiles.setLayoutData( fdFiles );

      // Add the file to the list of files...
      SelectionAdapter selA = new SelectionAdapter() {
        public void widgetSelected( SelectionEvent arg0 ) {
          wFiles
              .add( new String[] { wFile.getText(), wWildcard.getText() } );
          wFile.setText( "" );
          wWildcard.setText( "" );
          wFiles.removeEmptyRows();
          wFiles.setRowNums();
          wFiles.optWidth( true );
        }
      };
      wbFileAdd.addSelectionListener( selA );

      // Delete files from the list of files...
      wbFileDelete.addSelectionListener( new SelectionAdapter() {
        public void widgetSelected( SelectionEvent arg0 ) {
          int[] idx = wFiles.getSelectionIndices();
          wFiles.remove( idx );
          wFiles.removeEmptyRows();
          wFiles.setRowNums();
        }
      } );

      // Edit the selected file & remove from the list...
      wbFileEdit.addSelectionListener( new SelectionAdapter() {
        public void widgetSelected( SelectionEvent arg0 ) {
          int idx = wFiles.getSelectionIndex();
          if ( idx >= 0 ) {
            String[] string = wFiles.getItem( idx );
            wFile.setText( string[0] );
            wWildcard.setText( string[1] );
            wFiles.remove( idx );
          }
          wFiles.removeEmptyRows();
          wFiles.setRowNums();
        }
      } );

      fdGeneralComp = new FormData();
      fdGeneralComp.left = new FormAttachment( 0, 0 );
      fdGeneralComp.top = new FormAttachment( 0, 0 );
      fdGeneralComp.right = new FormAttachment( 100, 0 );
      fdGeneralComp.bottom = new FormAttachment( 100, 0 );
      wGeneralComp.setLayoutData( fdGeneralComp );

      wGeneralComp.layout();
      wGeneralTab.setControl( wGeneralComp );
      props.setLook( wGeneralComp );

      // ///////////////////////////////////////////////////////////
      // / END OF GENERAL TAB
      // ///////////////////////////////////////////////////////////
      
      // //////////////////////////////////////////////////////////
      // Start of Advanced Tab
      // /////////////////////////////////////////////////////////
    
      wAdvanceTab = new CTabItem( wTabFolder, SWT.NONE );
      wAdvanceTab.setText( BaseMessages.getString( PKG, "RefreshTableauExtract.Tab.Advanced.Label" ) );

      wAdvanceComp = new Composite( wTabFolder, SWT.NONE );
      props.setLook( wAdvanceComp );

      FormLayout advancelayout = new FormLayout();
      advancelayout.marginWidth = 3;
      advancelayout.marginHeight = 3;
      wAdvanceComp.setLayout( advancelayout );
      
      // /////////////////////
      // Site Name line
      // /////////////////////
      wlSiteName = new Label( wAdvanceComp, SWT.RIGHT );
      wlSiteName.setText( BaseMessages.getString( PKG, "RefreshTableauExtract.SiteName.Label" ) );
      props.setLook( wlSiteName );
      fdlSiteName = new FormData();
      fdlSiteName.left = new FormAttachment( 0, 0 );
      fdlSiteName.top = new FormAttachment( wName, margin );
      fdlSiteName.right = new FormAttachment( middle, 0 );
      wlSiteName.setLayoutData( fdlSiteName );

      wSiteName = new TextVar( jobMeta, wAdvanceComp, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
      props.setLook( wSiteName );
      wSiteName.addModifyListener( lsMod );
      fdSiteName = new FormData();
      fdSiteName.left = new FormAttachment( middle, margin );
      fdSiteName.right = new FormAttachment( 100, -margin );
      fdSiteName.top = new FormAttachment( wName, margin );
      wSiteName.setLayoutData( fdSiteName );
      
      wlProxyUser = new Label( wAdvanceComp, SWT.RIGHT );
      wlProxyUser.setText( BaseMessages.getString( PKG, "RefreshTableauExtract.ProxyUser.Label" ) );
      props.setLook( wlProxyUser );
      fdlProxyUser = new FormData();
      fdlProxyUser.left = new FormAttachment( 0, 0 );
      fdlProxyUser.top = new FormAttachment( wSiteName, margin );
      fdlProxyUser.right = new FormAttachment( middle, 0 );
      wlProxyUser.setLayoutData( fdlProxyUser );

      wProxyUser = new TextVar( jobMeta, wAdvanceComp, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
      props.setLook( wProxyUser );
      wProxyUser.addModifyListener( lsMod );
      fdProxyUser = new FormData();
      fdProxyUser.left = new FormAttachment( middle, margin );
      fdProxyUser.right = new FormAttachment( 100, -margin );
      fdProxyUser.top = new FormAttachment( wSiteName, margin );
      wProxyUser.setLayoutData( fdProxyUser );
      
      wlProxyPassword = new Label( wAdvanceComp, SWT.RIGHT );
      wlProxyPassword.setText( BaseMessages.getString( PKG, "RefreshTableauExtract.ProxyPassword.Label" ) );
      props.setLook( wlProxyPassword );
      fdlProxyPassword = new FormData();
      fdlProxyPassword.left = new FormAttachment( 0, 0 );
      fdlProxyPassword.top = new FormAttachment( wProxyUser, margin );
      fdlProxyPassword.right = new FormAttachment( middle, 0 );
      wlProxyPassword.setLayoutData( fdlProxyPassword );

      wProxyPassword = new TextVar( jobMeta, wAdvanceComp, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
      props.setLook( wProxyPassword );
      wProxyPassword.addModifyListener( lsMod );
      wProxyPassword.setEchoChar('*');
      fdProxyPassword = new FormData();
      fdProxyPassword.left = new FormAttachment( middle, margin );
      fdProxyPassword.right = new FormAttachment( 100, -margin );
      fdProxyPassword.top = new FormAttachment( wProxyUser, margin );
      wProxyPassword.setLayoutData( fdProxyPassword );
      
      fdAdvanceComp = new FormData();
      fdAdvanceComp.left = new FormAttachment( 0, 0 );
      fdAdvanceComp.top = new FormAttachment( 0, 0 );
      fdAdvanceComp.right = new FormAttachment( 100, 0 );
      fdAdvanceComp.bottom = new FormAttachment( 100, 0 );
      wAdvanceComp.setLayoutData( fdAdvanceComp );

      wAdvanceComp.layout();
      wAdvanceTab.setControl( wAdvanceComp );
      props.setLook( wAdvanceComp );
      
   // //////////////////////////////////////////////////////
   // End of Advance Tab
   // /////////////////////////////////////////////////////

    fdTabFolder = new FormData();
    fdTabFolder.left = new FormAttachment( 0, 0 );
    fdTabFolder.top = new FormAttachment( wName, margin );
    fdTabFolder.right = new FormAttachment( 100, 0 );
    fdTabFolder.bottom = new FormAttachment( 100, -50 );
    wTabFolder.setLayoutData( fdTabFolder );

    // Some buttons
    wOK = new Button( shell, SWT.PUSH );
    wOK.setText( BaseMessages.getString( PKG, "System.Button.OK" ) );
    wCancel = new Button( shell, SWT.PUSH );
    wCancel.setText( BaseMessages.getString( PKG, "System.Button.Cancel" ) );

    BaseStepDialog.positionBottomButtons( shell, new Button[] { wOK, wCancel }, margin, wTabFolder );

    // Add listeners
    lsCancel = new Listener() {
      public void handleEvent( Event e ) {
        cancel();
      }
    };
    lsOK = new Listener() {
      public void handleEvent( Event e ) {
        ok();
      }
    };

    wOK.addListener( SWT.Selection, lsOK );
    wCancel.addListener( SWT.Selection, lsCancel );

    lsDef = new SelectionAdapter() {
      public void widgetDefaultSelected( SelectionEvent e ) {
        ok();
      }
    };
    wName.addSelectionListener( lsDef );
    wTableauClient.addSelectionListener( lsDef );

    wbTableauClient.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( SelectionEvent e ) {
        FileDialog dialog = new FileDialog( shell, SWT.OPEN );
        dialog.setFilterExtensions( new String[] { "*.exe", "*;*.*" } );
        dialog.setFilterNames( new String[] {".exe - Executable","All Files"} );

        if ( wTableauClient.getText() != null ) {
          dialog.setFileName( wTableauClient.getText() );
        }

        if ( dialog.open() != null ) {
          wTableauClient.setText( dialog.getFilterPath() + Const.FILE_SEPARATOR + dialog.getFileName() );
        }
      }
    } );
    
    wbExtractFile.addSelectionListener( new SelectionAdapter() {
        public void widgetSelected( SelectionEvent e ) {
          FileDialog dialog = new FileDialog( shell, SWT.OPEN );
          dialog.setFilterExtensions( new String[] { "*.tde;*.txt;*.csv;*.tabB;*.xls;*.xlsx;*.xlsm;*.xlsb","*"} );
          dialog.setFilterNames( FILETYPES );

          if ( wExtractFile.getText() != null ) {
            dialog.setFileName( wExtractFile.getText() );
          }

          if ( dialog.open() != null ) {
            wExtractFile.setText( dialog.getFilterPath() + Const.FILE_SEPARATOR + dialog.getFileName() );
          }
        }
      } );

    // Detect [X] or ALT-F4 or something that kills this window...
    shell.addShellListener( new ShellAdapter() {
      public void shellClosed( ShellEvent e ) {
        cancel();
      }
    } );
	
    getData();
    updateActive();

    wTabFolder.setSelection( 0 );

    BaseStepDialog.setSize( shell );

    shell.open();
    props.setDialogSize( shell, "RefreshTableauExtractDialogSize" );
    while ( !shell.isDisposed() ) {
      if ( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
    
    return jobEntry;
  }

  public void dispose() {
    WindowProperty winprop = new WindowProperty( shell );
    props.setScreen( winprop );
    shell.dispose();
  }
  
  public void updateActive() {
	  boolean refresh=false;
	  if(wRefreshType.getSelectionIndex()==0)
	  {
		  refresh=true;
	  }
	  boolean refreshServer=false;
	  if(wRefreshType.getSelectionIndex()==1)
	  {
		  refreshServer=true;
	  }
	  boolean appendFile=false;
	  boolean appendResult=false;
	  if(wRefreshType.getSelectionIndex()==2)
	  {
		  appendResult=true;
		  if(!wResultFiles.getSelection())
		  {
			  appendFile=true;
		  }
	  }
	  
	  wlExtractFile.setEnabled(refresh);
	  wbExtractFile.setEnabled(refresh);
	  wExtractFile.setEnabled(refresh);
	  wlSourceUser.setEnabled(refreshServer);
	  wSourceUser.setEnabled(refreshServer);
	  wlSourcePassword.setEnabled(refreshServer);
	  wSourcePassword.setEnabled(refreshServer);
	  wlFullRefresh.setEnabled(refreshServer);
	  wFullRefresh.setEnabled(refreshServer);
	  wFile.setEnabled(appendFile);
	  wlFile.setEnabled(appendFile);
	  wbFileAdd.setEnabled(appendFile);
	  wbFileFile.setEnabled(appendFile);
	  wbFileFolder.setEnabled(appendFile);
	  wbFileEdit.setEnabled(appendFile);
	  wbFileDelete.setEnabled(appendFile);
	  wlWildcard.setEnabled(appendFile);
	  wWildcard.setEnabled(appendFile);
	  wFiles.setEnabled(appendFile);
	  wlFiles.setEnabled(appendFile);
	  wFiles.table.setEnabled(appendFile);
	  wResultFiles.setEnabled(appendResult);
	  wlResultFiles.setEnabled(appendResult);
	  
	  if(appendFile)
	  {
		  wFiles.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
	  } else {
		  wFiles.setForeground(display.getSystemColor(SWT.COLOR_GRAY));
	  }
  }

  public String nullToEmpty(String n)
  {
	  return n!=null ? n : "";
  }
  
  public void getData() {
    wName.setText( nullToEmpty(jobEntry.getName() )) ;
    wTableauClient.setText( nullToEmpty(jobEntry.getTableauClient() )) ;
    wServer.setText( nullToEmpty(jobEntry.getServer() )) ;
    wServerPort.setText(nullToEmpty(jobEntry.getServerPort()));
    wServerUser.setText(nullToEmpty(jobEntry.getServerUser()));
    wServerPassword.setText(nullToEmpty(jobEntry.getServerPassword()));
    wDataSource.setText(nullToEmpty(jobEntry.getDataSource()));
    wProject.setText(nullToEmpty(jobEntry.getProject()));
    wExtractFile.setText(nullToEmpty(jobEntry.getExtractFile()));
    wSourceUser.setText(nullToEmpty(jobEntry.getSourceUser()));
    wSourcePassword.setText(nullToEmpty(jobEntry.getSourcePassword()));
    wProxyUser.setText(nullToEmpty(jobEntry.getProxyUser()));
    wProxyPassword.setText(nullToEmpty(jobEntry.getProxyPassword()));
    wSiteName.setText(nullToEmpty(jobEntry.getSiteName()));
    wWorkingDirectory.setText(nullToEmpty(jobEntry.getWorkingDirectory()));
   	wRefreshType.select(jobEntry.getRefreshType());
   	wProtocol.select(jobEntry.getProtocol());
   	wFullRefresh.setSelection(jobEntry.getFullRefresh());
   	wResultFiles.setSelection(jobEntry.getProcessResultFiles());

    if ( jobEntry.filePaths != null ) {
      for ( int i = 0; i < jobEntry.filePaths.length; i++ ) {
        TableItem ti = wFiles.table.getItem( i );
        if ( jobEntry.filePaths[i] != null ) {
          ti.setText( 1, nullToEmpty(jobEntry.filePaths[i] ));
          ti.setText(2,nullToEmpty(jobEntry.wildcards[i]));
        }
      }
      wFiles.setRowNums();
      wFiles.optWidth( true );
    }
    
    wName.selectAll();
    wName.setFocus();
  }

  private void cancel() {
    jobEntry.setChanged( backupChanged );

    jobEntry = null;
    dispose();
  }

  private void ok() {
    if ( Const.isEmpty( wName.getText() ) ) {
      MessageBox mb = new MessageBox( shell, SWT.OK | SWT.ICON_ERROR );
      mb.setText( BaseMessages.getString( PKG, "System.StepJobEntryNameMissing.Title" ) );
      mb.setMessage( BaseMessages.getString( PKG, "System.JobEntryNameMissing.Msg" ) );
      mb.open();
      return;
    }
    jobEntry.setTableauClient( wTableauClient.getText() );
    jobEntry.setServer( wServer.getText() );
    jobEntry.setServerPort( wServerPort.getText() );
    jobEntry.setServerUser( wServerUser.getText() );
    jobEntry.setServerPassword( wServerPassword.getText() );
    jobEntry.setDataSource( wDataSource.getText() );
    jobEntry.setProject( wProject.getText() );
    jobEntry.setExtractFile( wExtractFile.getText() );
    jobEntry.setSourceUser( wSourceUser.getText() );
    jobEntry.setSourcePassword( wSourcePassword.getText() );
    jobEntry.setProxyUser( wProxyUser.getText() );
    jobEntry.setProxyPassword( wProxyPassword.getText() );
    jobEntry.setSiteName( wSiteName.getText() );
    jobEntry.setWorkingDirectory( wWorkingDirectory.getText() );
    jobEntry.setRefreshType(wRefreshType.getSelectionIndex());
    jobEntry.setProtocol(wProtocol.getSelectionIndex());
    jobEntry.setFullRefresh(wFullRefresh.getSelection());
    jobEntry.setProcessResultFiles(wResultFiles.getSelection());

    int nritems = wFiles.nrNonEmpty();
    int nr = 0;
    for ( int i = 0; i < nritems; i++ ) {
      String filePath = wFiles.getNonEmpty( i ).getText( 1 );
      if ( filePath != null && filePath.length() != 0 ) {
        nr++;
      }
    }
    jobEntry.filePaths = new String[nr];
    jobEntry.wildcards = new String[nr];
    nr = 0;
    for ( int i = 0; i < nritems; i++ ) {
      String filePath = wFiles.getNonEmpty( i ).getText( 1 );
      if ( filePath != null && filePath.length() != 0 ) {
        jobEntry.filePaths[nr] = filePath;
        jobEntry.wildcards[nr] = wFiles.getNonEmpty(i).getText(2);
        nr++;
      }
    }

    dispose();
  }
}
