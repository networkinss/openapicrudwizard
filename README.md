# OpenAPI CRUD Wizard

![Build](https://github.com/inssch/openapicreator/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)

## Template ToDo list

- [x] Create a new [IntelliJ Platform Plugin Template][template] project.
- [x] Get familiar with the [template documentation][template].
- [x] Verify the [pluginGroup](/gradle.properties), [plugin ID](/src/main/resources/META-INF/plugin.xml)
  and [sources package](/src/main/kotlin).
- [ ] Review the [Legal Agreements](https://plugins.jetbrains.com/docs/marketplace/legal-agreements.html).
- [ ] [Publish a plugin manually](https://plugins.jetbrains.com/docs/intellij/publishing-plugin.html?from=IJPluginTemplate)
  for the first time.
- [ ] Set the Plugin ID in the above README badges.
- [ ] Set the [Deployment Token](https://plugins.jetbrains.com/docs/marketplace/plugin-upload.html).
- [ ] Click the <kbd>Watch</kbd> button on the top of the [IntelliJ Platform Plugin Template][template] to be notified
  about releases containing new features and fixes.

<!-- Plugin description -->
OpenAPI CRUD Wizards creates a new OpenAPI document including CRUD operations.  
The wizard takes input from:

* Selected text in the editor within any file.
* Selected Yaml or Text file.
* an Excel file (pro version).

After selecting text or a file, choose "OpenAPI CRUD Wizard" from the tools menu.  
A new file will be created in the same directory with name "openapi.yaml" (or added _01-_10 if file exists).

The selected text or Yaml file must have Yaml properties or be a Yaml object.  
Sample:

```
Pet:
  name: Underdog
  amount: 3
  price: 12.05
  status: "available"
  tags: [ dog,4paws ]
  possiblerabatt: [10,15,20]
```

# Future versions

- take input from JSON object as well.
- take database column description as input.
- let you define field names from the database column description with a mapping file (DB to OAS3).

More features or customized plugin at request.

<!-- Plugin description end -->

## Installation

- Using IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "
  openapicreator"</kbd> >
  <kbd>Install Plugin</kbd>

- Manually:

  Download the [latest release](https://github.com/inssch/openapicreator/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
