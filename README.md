Refresh Tableau Extract Plugin
====================

A big thanks to my employer OpenBI (http://www.openbi.com) for allowing me to open source and make this plugin available.

The Refresh Tableau Extract plugin allows you to refresh an extract on a Tableau server from PDI. It can perform a full refresh of any extract, update an incremental extract from a database, or append new files to an existing extract.

This plugin utilizes the Tableau command line utility detailed at http://onlinehelp.tableausoftware.com/v8.1/pro/online/en-us/extracting_TDE.html.

System Requirements
-------------------
- Pentaho Data Integration 4.3 and above
- Tableau 8.0 and above

Installation
------------

Install using the Pentaho Marketplace or unzipping to the ${PDI_HOME}/plugins/jobentries folder.

Building from Source
--------------------
This project uses ant; however, you must manually configure your classpath before building.

1. Edit the build.properties file.
  1. For PDI 5.0 and up: Set pentahoclasspath to ${PDI_HOME}/lib
  2. For PDI 4.5 and below: Set pentahoclasspath to ${PDI_HOME}/libext
  3. Set pentahoswtclasspath to ${PDI_HOME}/libswt
2. Build the plugin using "ant dist"
3. The plugin will be in the dist directory.
