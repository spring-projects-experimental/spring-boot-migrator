# Setup SMB STS Integration Plugin
There are two components to the SBM STS integration:
1. **rest-service**- it is supposed to run on the **localhost** on port 8080 by default and respond to migration requests (i.e. **scan** and ** apply* HTTP requests)
2. **org.springframework.sbm** eclipse plugin which depends on STS plugins and is able to talk to the SBM **rest-service**, show information from **rest-service** and organize user interactions with SBM via Eclipse UI.

## Steps to Deploy and Try
1. Download STS 4.11 or higher. If not available yet then download one of the snapshot builds from here: http://dist.springsource.com/snapshot/STS4/nightly-distributions.html
2. Open STS and go to **File -> Import...**./ Note import wizard dialog has opened
3. Go to **Maven -> Existing Maven Projects**. Click **Browse** button and navigate to SBM root folder, click **Open**, select all projects and click **Finish** in the wizard dialog
4. Go to **File -> Import...** and then in the wizard dialog select **General -> Existing Project into Workspace**
5. Click **Browse** and navigate to `<SBM Root Folder>/ide-integrations/eclipse/org.springframework.sbm` and import it into the workspace
6. Find **rest-service** under **Local** in the Boot Dashboard viwe, right-click on it and **(Re)start** it.
7. Go to **Run -> Run Configurations...**, Right click on **Eclipse Application** and select **Create New Configuration** then click **Run** button in the dialog.
8. Note that Eclipse runtime workbench has started and it should have the **org.springframework.sbm** plugin in it. From now an on instructions are to be completed in the runtime workbench
9. Import one of the sample J2EE projects from SBM root folder as a Mane project into workspace. (Apply steps 2 and 3 in the runtime workbench but for the sample projects froom SBM this time)
10. Right click on the imported project. Find **Spring** sub-menu, click on it and then note **Migrate Project** item in the sub-menu and click it.

Try to use the **Migrate Project** dialog to migrate the project with SBM.
