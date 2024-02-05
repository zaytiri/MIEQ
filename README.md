# MIEQ
 A simple query builder.

# How to use

1. Download the latest version of _mieq-vx.x.x.jar_ file available in the Releases page.
2. Move the download .jar file into the following folder:
    ``<your_project>/build/libs/``
    
    - If you don't want to use the suggested path you can create another folder for external libraries. You only have to add that custom folder as a library folder by right-clicking the folder and choosing 'Add as Library' option.
      - <img src="https://github.com/zaytiri/MIEQ/blob/main/readme-images/1.png" width="200" height="400" />
3. Add the following dependency in your project's build.gradle:
    ```
    dependencies {
        implementation files('libs/mieq-vx.x.x.jar')
    }
    ```
4. Reload Gradle.

Note: Replace the _vx.x.x_ for the downloaded version.