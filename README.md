Refresh Tableau Extract Plugin
====================

A big thanks to my employer Inquidia Consulting (http://www.inquidia.com) for allowing me to open source and make this plugin available.

The Refresh Tableau Extract plugin allows you to refresh an extract on a Tableau server from PDI. It can perform a full refresh of any extract, update an incremental extract from a database, or append new files to an existing extract.

This plugin utilizes the Tableau command line utility detailed at http://onlinehelp.tableausoftware.com/v8.1/pro/online/en-us/extracting_TDE.html.

System Requirements
-------------------
- Pentaho Data Integration 5.0 and above (Version 1.0 supports PDI 4.3 and above all later versions require 5.0 and above.)
  - The version available for install from the Pentaho marketplace supports Pentaho 5.0-5.3
  - PDI 5.4 requires a special build and manual installation.  This build is available at: https://github.com/cdeptula/refreshtableauextract/releases/tag/2.0.1a
- Tableau 8.0 and above

Installation
------------

Install using the Pentaho Marketplace or unzipping to the ${PDI_HOME}/plugins/jobentries folder.

Building from Source
--------------------
This project uses ant; however, you must manually configure your classpath before building.

1. Edit the build.properties file.
  1. For PDI 5.0 and up: Set pentahoclasspath to ${PDI_HOME}/lib
  2. Set pentahoswtclasspath to ${PDI_HOME}/libswt
2. Build the plugin using "ant dist"
3. The plugin will be in the dist directory.


