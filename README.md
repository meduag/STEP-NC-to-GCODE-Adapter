# STEP-NC to GCODE Adapter

This project works with as converter from STEPNC (p21) to GCODE (RS274X)

This code was tested on Windows 10 and Linux Ubuntu 10 

The program used to implement java code was Eclipse. When you use Eclipse, you need to run it with administrator permissions

The package Integrador has a class Integrador.java. In this class is the main()

To execute the program, you must first perform the following steps:

Only on Windows
1- Make a folder on C drive, thus: C:\mod
In this folder, the program put different files

2- In project, you need to reconfigure the path of vecmath.jar library before run the program. The library is in scr folder project

3- In Integrador.java class you need place the path of p21 file ("rutaLec" variable), the folder "Examples p21" has several examples from StepModeler program. 
for example, rutaLec = "C:/mod/furo.p21"; the rutaLec (read path) variable has the path of p21 file, the furo.p21 example file part 21 
(check on linux if is used "\" or "/" on paths)

Only on Linux
1- Make a folder on \home, thus: \home\mod
In this folder, the program put different files

2- In project, you need to reconfigure the path of vecmath.jar library before run the program. The library is in scr folder project

3- In Integrador.java class you need place the path of p21 file ("rutaLec" variable), the folder "Examples p21" has several examples from StepModeler program. 
for example, rutaLec = "\home\mod\furo.p21"; the rutaLec (read path) variable has the path of p21 file, the furo.p21 example file part 21 
(check on linux if is used "\" or "/" on paths)

4- the folder "GUI Axis e Viewer STEP-NC" has a necessary files to LinuxCNC, see the DOC about my Master's degree

for more information, see this turorials

www.youtube.com/user/meduag

impotant
https://www.youtube.com/watch?v=xit3HHE8iWQ

https://www.youtube.com/watch?v=q-de8ONiIuM

https://www.youtube.com/watch?v=cmSFZsJm0Gg

https://www.youtube.com/watch?v=fkI-3XQQ2XQ


Others
https://www.youtube.com/watch?v=cSX13TcaIdw

https://www.youtube.com/watch?v=B1N80q_2fVw

https://www.youtube.com/watch?v=XCtsiGoD1-s&t=32s

https://www.youtube.com/watch?v=gdKPCqR7JVM&t=5s

https://www.youtube.com/watch?v=ArrUYYJPvl4&t=30s

https://www.youtube.com/watch?v=LIiDtnPOZMQ

https://www.youtube.com/watch?v=0rlC4cvpGG8

https://www.youtube.com/watch?v=NXjIF0jbhgI

https://www.youtube.com/watch?v=3YUTuWrZLDk



