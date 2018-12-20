# Gitlab Snippets Eclipse Plugin

This project is built using [Eclipse Tycho](https://www.eclipse.org/tycho/),
[Maven](http://maven.apache.org/download.html) and the
[tycho eclipse plugin archetype](https://github.com/open-archetypes/tycho-eclipse-plugin-archetype)

The first run will take quite a while since maven will download all the
required dependencies in order to build everything.

In order to use the generated eclipse plugins in Eclipse, you will need
[m2e](https://www.eclipse.org/m2e) and the
[m2eclipse-tycho plugin](https://github.com/tesla/m2eclipse-tycho/).

Update sites to install these plugins :

* [m2e stable update site](http://download.eclipse.org/technology/m2e/releases/)
* [m2eclipse-tycho dev update site](http://repo1.maven.org/maven2/.m2e/connectors/m2eclipse-tycho/0.7.0/N/0.7.0.201309291400/)


Tycho eclipse plugin archetype generates the following project structure:

```
- Project
    - pom.xml
    - core/
    - feature/
    - site/
    - test/
```

To build the project and export the plugin binary files, run:

```
  - $ mvn clean install
```

The command above generates the files in the **site/** folder, under the
**site/target/repository/** directory.
All files in the **site/target/repository/** directory must be exported to the update site, so they can be
found by [eclipse marketplace](https://marketplace.eclipse.org/).

To install the plugin, [click here](https://marketplace.eclipse.org/content/gitlab-snippets-plugin).
