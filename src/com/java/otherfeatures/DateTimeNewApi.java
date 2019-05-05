package com.java.otherfeatures;

import java.sql.Date;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DateTimeNewApi {
    public static void main(String[] args) {
        //Local and  Zoned

        /*The new Date and Time APIs are thread-safe, immutable, cacheable, and represent
         a point in time measured to the nanosecond and have the option for backward
         compatibility. It borrows the ideas from Joda-Time and allows the programmers to
         capitalize on the features which were not available in java.util.Date and Calendar.*/


        System.out.println("-----------------------Local----------------------");
        //Simplified date-time API with no complexity of timezone handling

        //---Date
        LocalDate localDate = LocalDate.now();// import is java.time.*
        LocalDate localDate2 = LocalDate.of(2019, 02, 3);
        LocalDate localDate3 = LocalDate.parse("2015-02-20");//Should be in this format only

        //---Time
        LocalTime localTime = LocalTime.now();// static method, no need to create object
        LocalTime sixThirty = LocalTime.of(6, 30);
        LocalTime sevenThirty = LocalTime.parse("06:30").plus(1, ChronoUnit.HOURS);
        boolean isbefore = LocalTime.parse("06:30").isBefore(LocalTime.parse("07:30"));

        //---DateTime
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime localDateTime1 = LocalDateTime.of(2015, Month.FEBRUARY, 20, 06, 30);
        LocalDateTime localDateTime2 = LocalDateTime.parse("2015-03-20 06:30:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime aLDT = LocalDateTime.parse("2015-08-04T10:11:30");// see T otherwise it will throw parsing exception

        //---Formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");// static method
        String formatted = formatter.format(localDateTime);
        LocalDateTime formattedDateTime = LocalDateTime.parse("2015-02-20 06:30:00", formatter);//Can pass as argument

        System.out.println("Local Date: "+localDate);
        System.out.println("Local Date2: "+localDate2);
        System.out.println("Local Date3: "+localDate3);
        System.out.println("Local Time: "+localTime);
        System.out.println("Local Date Time: "+localDateTime);
        System.out.println("Formatted : "+formatter.format(localDateTime));
        System.out.println("formattedDateTime : "+formattedDateTime);

        System.out.println("---------LocalDate to sqlDate-----------");
        LocalDate localDate1 = LocalDate.of(2019, 02, 3);
        Date sqldate = Date.valueOf(localDate1);//Can pass LocalDate to java.sql.Date

        System.out.println("----------Adding Substracting--using plus------------");
        //Prior java8 adding 6 hours to current time
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, 6);
        System.out.println("after add 6 hours: "+cal.getTime());


        // Java8
        LocalTime curTime = LocalTime.now();
        LocalTime sixHoursLater = curTime.plus(6, ChronoUnit.HOURS);//ChronoUnit is form of TemporalUnit
        LocalTime sixHoursPlus = curTime.plusHours(6);

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate previousMonthSameDay = LocalDate.now().minus(1, ChronoUnit.MONTHS);


        System.out.println("--------------Get day , date , month etc--------------");
        Month month = localDateTime.getMonth(); // Enum Month
        int day = localDateTime.getDayOfMonth();
        int seconds = localDateTime.getSecond();
        boolean leapyear = LocalDate.now().isLeapYear();

        // printing date with current time.
        LocalDateTime specificDate =
                localDateTime.withDayOfMonth(24).withYear(2016);


        System.out.println("-------------------Zoned-----------------------");
        //There is no ZonedDate or ZonedTime. Its only ZonedDateTime
        ZonedDateTime localZonedDateTime = ZonedDateTime.now(); //
        ZoneId zoneId = localZonedDateTime.getZone();

        ZoneId tokyoZoneId = ZoneId.of("Asia/Tokyo");
        ZonedDateTime tokyoZonedDateTime = ZonedDateTime.of(LocalDateTime.now(), tokyoZoneId);//Argument is LocalDateTime not ZonedDateTime

        System.out.println("localZonedDateTime : "+localZonedDateTime);
        System.out.println("zoneId : "+zoneId);
        System.out.println("tokyoZonedDateTime : "+tokyoZonedDateTime);

        //---Converting time between zone
        String dateInString = "22-1-2015 10:15:55 AM";
        String DATE_FORMAT = "dd-M-yyyy hh:mm:ss a";
        ZoneId singaporeZoneId = ZoneId.of("Asia/Singapore");

        LocalDateTime ldt = LocalDateTime.parse(dateInString, DateTimeFormatter.ofPattern(DATE_FORMAT));
        ZonedDateTime asiaZonedDateTime = ldt.atZone(singaporeZoneId);// atZone method applied on LocalDateTime

        System.out.println("Date (Singapore) : " + asiaZonedDateTime);

        ZoneId newYokZoneId = ZoneId.of("America/New_York");
        ZonedDateTime nyDateTime = asiaZonedDateTime.withZoneSameInstant(newYokZoneId);// withZoneSameInstant applied on ZonedDateTime
        System.out.println("Date (New York) : " + nyDateTime);


        System.out.println("======================Period==========================");
        //The Period class is widely used to modify values of given a date or to obtain the difference between two dates:
        LocalDate initialDate = LocalDate.parse("2007-05-10");
        LocalDate finalDate = initialDate.plus(Period.ofDays(5));
        Period period = Period.between(finalDate, initialDate);// return Period object
        int five = period.getDays();

        System.out.println("======================Duration==========================");
        //Similar to Period, the Duration class is use to deal with Time.
        LocalTime initialTime = LocalTime.of(6, 30, 0);
        LocalTime finalTime = initialTime.plus(Duration.ofSeconds(30));
        Duration duration = Duration.between(finalTime, initialTime); //Duration.between return Duration
        long secondGap = duration.getSeconds();








    }
}
