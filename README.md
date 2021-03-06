# Payslip-Tool [![codebeat badge](https://codebeat.co/badges/5e154171-d760-41ed-9465-4ad1b298162f)](https://codebeat.co/projects/github-com-dvdkruk-payslip-tool-master) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/92e6eeee856a46e897487924fd79afa8)](https://www.codacy.com/app/damiaan.van.der.kruk/payslip-tool?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=dvdkruk/payslip-tool&amp;utm_campaign=Badge_Grade) [![BCH compliance](https://bettercodehub.com/edge/badge/dvdkruk/payslip-tool?branch=master)](https://bettercodehub.com/)
A simple console application for calculation of monthly payslips.

## How To Build and Run
Clone this repo and open a terminal at the location where you cloned this repo.
Execute the following commands for building and running the application:
```sh
$ mvn package
$ cd target
$ java -jar ./payslip-tool-1.0-SNAPSHOT.jar
```
This launches the interactive mode. Input information for requesting a monthly payslip should have the following format:
```<first_name>,<last_name>,<annual_salary>,<super_rate>,<month>```. For example:
```David,Rudd,60050,9%,March``` results in ```David Rudd,01 March - 31 March,5004,922,4082,450```.

Input information can also be given as arguments to the program.
```sh
$ java -jar ./payslip-tool-1.0-SNAPSHOT.jar David,Rudd,60050,9%,March Ryan,Chen,120000,10%,March
```

## Assumptions
- Calculations are only done for complete months. Partial calculations, like per day or per week, are not possible. 
- Leap year check for determining the last day of the month is based on the current year (```Year.now()```).
- Input format is simplified to the name of the 'month' instead of defining a period, like 'xx month - xx month'.

## Tech
- Java 8
- Maven 3
- Junit 4
- Qulice 1.7

## Qulice
### Rules I Didn't Like
- JavadocMethodCheck -> JavaDoc for all methods and all viability scopes. Makes code very verbose especially for normal getter and setters.
- LineLengthCheck -> Max length is 80 chars, nowadays 120 should be oke.

### Rules I'm Not Sure I Like
- ParameterNameCheck -> Must be alphanumeric lowercase and at least 3 chars long. `int i = 0` for loops is not allowed, also camelcase in variable names is not allowed (which is maybe a good thing).
- RequireThisCheck -> All instance variables must start with this. Good for ready plain code, but also makes the code more verbose.
- HeaderCheck -> First line of every java file should start with a license header. Probably legally required in USA.
- JavadocTagsCheck -> @author tag middle name should start with a capitol letter. Not complied with European surnames like mine; `van der Kruk`.

### Rules I Liked
- ParameterNumberCheck -> No more than 3 parameters are allowed.



