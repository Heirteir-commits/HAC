Put the spigot jars in this directory for build.
YOU DON'T NEED ALL OF SPIGOT JARS YOU ONLY NEED THE VERSION YOU ARE COMPILING FOR.
Names should be based on their NMS version name.
Example: 1.8.8 = 1_8_R3, 1.9.4 = 1_9_R2, 1.16.4 = 1_16_R3 etc...
Package file names on my local machine:

put_spigot_jars_here/ -
 - 1_8_R3.jar (1.8.8)
 - 1_9_R2.jar (1.9.4)
 - 1_10_R1 (1.10.2)
 - 1_11_R1 (1.11.2)
 - 1_12_R1 (1.12.2)
 - 1_13_R2 (1.13.2)
 - 1_14_R1 (1.14.2)
 - 1_15_R1 (1.15.2)
 - 1_16_R3 (1.16.4)

 You can find the nms version if you just open up the jar with a zip program and go to net/minecraft/server
 the name of the next folder will be what you name the file. Keep in mind this project only supports the latest
 revisions of each MC version if you want to add more you must edit the build.gradle and include a valid version proxy for it.