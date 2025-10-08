

## Functionality Overview

### Model Core

* The pluggable `IEngineImpl` engine configures the JDBC template, connects the persistent object factory, registers plugins and sequences, and manages the attribute and branch cache. This provides an extensible model foundation with undo/redo support on branches.【F:core/src/main/java/com/ramussoft/core/impl/IEngineImpl.java†L44-L142】
* The `initStartBranch` method automatically creates the initial storage branch and ensures the database has a base state the first time the application runs.【F:core/src/main/java/com/ramussoft/core/impl/IEngineImpl.java†L144-L181】

### IDEF0 and DFD Modeling

* The IDEF0 plugin registers dozens of system attributes for functions, streams, and visual properties, handles cleanup of obsolete qualifiers, and stores the model tree for IDEF0/DFD diagrams.【F:idef0-core/src/main/java/com/ramussoft/idef0/IDEF0Plugin.java†L31-L200】

### Diagrams and Visualization

* The `ChartPlugin` creates qualifiers for diagrams, sets, and links, plus system attributes for positioning and connecting diagrams inside sets, powering the visual analytics module.【F:chart-core/src/main/java/com/ramussoft/chart/core/ChartPlugin.java†L17-L197】

### Reporting and Publishing

* `ReportPlugin` introduces a report qualifier with name, linked qualifier, and type attributes (XML, JSSP, DocBook) and provides a functional interface factory for executing report queries.【F:report-core/src/main/java/com/ramussoft/report/ReportPlugin.java†L13-L148】
* The PDF printing plugin adds a GUI action that lets users choose a file and export any `RamusPrintable` implementation to PDF through iText, including font mapping and paginated IDEF0 diagram output.【F:print-to-pdf/src/main/java/print/to/pdf/Plugin.java†L37-L204】

### Data Exchange

* The Excel import wizard opens a workbook, lets you configure column mappings for each sheet, runs batch imports with uniqueness checks, and writes the data transactionally into the model row set.【F:excel-import-export/src/main/java/com/ramussoft/excel/Importer.java†L32-L163】
* The Excel exporter builds a workbook with formatted headers, date support, and data extracted from hierarchical tables in the model view.【F:excel-import-export/src/main/java/com/ramussoft/excel/Exporter.java†L25-L134】

### Client-Server Platform

* The client application assembles the plugin list, initializes the engine and GUI framework, loads additional modules, and manages the lifecycle of the main window and the icon cache cleanup background task.【F:client/src/main/java/com/ramussoft/client/Client.java†L33-L198】
* `ServerIEngineImpl` extends the core for multi-user scenarios by enforcing user access rules, renaming personal streams, storing binary data per branch, and handling write/delete operations with history, while the light server boots the `base-content.xml` Spring context.【F:server/src/main/java/com/ramussoft/server/ServerIEngineImpl.java†L16-L142】【F:server/src/main/java/com/ramussoft/server/LightServer.java†L5-L11】

## How to Start the Application

### Step 1: Install JDK

Download and install the [Oracle JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).

### Step 2: Run the Application

In the console, navigate to the project folder and run:

```bash
./gradlew runLocal
```

### Step 3: Test the Application

#### For Linux (Tested on Ubuntu 20.04 and Fedora 34)

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/Vitaliy-Yakovchuk/ramus.git
   ```

2. **Navigate to the Project Folder:**

   ```bash
   cd ramus
   ```

3. **Run the Application:**

   ```bash
   ./gradlew runLocal
   ```

### Optional: Create a Shortcut to Launch the Application

1. Open your `.bash_aliases` file:
   ```bash
   nano ~/.bash_aliases
   ```

2. Add the following alias to easily launch the application:

   ```bash
   alias ramus='cd ~/path/to/ramus/folder/ && ./gradlew runLocal &'
   ```

3. Save the file and reload it:

   ```bash
   source ~/.bash_aliases
   ```

4. Now, you can simply run `ramus` in the terminal to launch the application.

