/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2013 the original author or authors.
 */
package org.assertj.examples;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Dates.monthOf;
import static org.assertj.core.util.Dates.parse;
import static org.assertj.core.util.Dates.parseDatetime;
import static org.assertj.core.util.Dates.parseDatetimeWithMs;
import static org.assertj.core.util.Dates.tomorrow;
import static org.assertj.core.util.Dates.yesterday;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.DateAssert;

import org.junit.Test;

/**
 * Date assertions examples.<br>
 *
 * @author Joel Costigliola
 */
public class DateAssertionsExamples extends AbstractAssertionsExamples {

  @Test
  public void basic_date_assertions_examples() {
    // isEqualTo, isAfter, isBefore assertions
    assertThat(theTwoTowers.getReleaseDate()).isEqualTo(parse("2002-12-18")).isEqualTo("2002-12-18")
      .isAfter(theFellowshipOfTheRing.getReleaseDate()).isBefore(theReturnOfTheKing.getReleaseDate())
      .isNotEqualTo(parse("2002-12-19")).isNotEqualTo("2002-12-19");

    assertThat(theTwoTowers.getReleaseDate()).isEqualTo(parse("2002-12-18")).isAfter("2002-12-17")
      .isBefore("2002-12-19");

    assertThat(theReturnOfTheKing.getReleaseDate()).isBeforeYear(2004).isAfterYear(2001);

    // isIn isNotIn works with String based date parameter
    assertThat(theTwoTowers.getReleaseDate()).isIn("2002-12-17", "2002-12-18", "2002-12-19");
    assertThat(theTwoTowers.getReleaseDate()).isNotIn("2002-12-17", "2002-12-19");

    assertThat(new Date(42)).hasTime(42);
  }

  @Test
  public void is_between_date_assertions_examples() {
    // various usage of isBetween assertion, the Two Towers release date being 2002-12-18
    // Note that isBetween(2002-12-17, 2002-12-19) includes start date but end date :
    assertThat(theTwoTowers.getReleaseDate())
      .isBetween(theFellowshipOfTheRing.getReleaseDate(), theReturnOfTheKing.getReleaseDate())
      .isBetween(parse("2002-12-17"), parse("2002-12-19")) // [2002-12-17, 2002-12-19[
      .isBetween("2002-12-17", "2002-12-19") // [2002-12-17, 2002-12-19[
      .isNotBetween("2002-12-17", "2002-12-18") // [2002-12-17, 2002-12-18[
      .isBetween("2002-12-17", "2002-12-18", true, true); // [2002-12-17, 2002-12-18]
  }

  @Test
  public void date_assertions_with_delta_examples() {
    // equals assertion with delta
    Date date1 = new Date();
    Date date2 = new Date(date1.getTime() + 100);
    assertThat(date1).isCloseTo(date2, 100);

    try {
      assertThat(date1).isCloseTo(date2, 99);
    } catch (AssertionError e) {
      logAssertionErrorMessage("isCloseTo date assertion", e);
    }
  }

  @Test
  public void date_assertions_comparison_with_precision_level() {
    // sets dates differing more and more from date1
    Date date1 = parseDatetimeWithMs("2003-01-01T01:00:00.000");
    Date date2 = parseDatetimeWithMs("2003-01-01T01:00:00.555");
    Date date3 = parseDatetimeWithMs("2003-01-01T01:00:55.555");
    Date date4 = parseDatetimeWithMs("2003-01-01T01:55:55.555");
    Date date5 = parseDatetimeWithMs("2003-01-01T05:55:55.555");

    assertThat(date1).isEqualToIgnoringMillis(date2);
    assertThat(date1).isEqualToIgnoringSeconds(date3);
    assertThat(date1).isEqualToIgnoringMinutes(date4);
    assertThat(date1).isEqualToIgnoringHours(date5);

    try {
      assertThat(date1).isEqualToIgnoringMillis(date3);
    } catch (AssertionError e) {
      logAssertionErrorMessage("isEqualToIgnoringMillis", e);
    }
  }

  @Test
  public void is_today_in_the_future_or_in_the_past_assertions_examples() {
    // some handy shortcuts :
    assertThat(theTwoTowers.getReleaseDate()).isInThePast();
    assertThat(new Date()).isToday();
    assertThat(theSilmarillion.getReleaseDate()).isInTheFuture();
  }

  @Test
  public void is_in_the_same_second_assertions_examples() {
    //let's see the isInSameSecondAs and isInSameSecondWindowAs in action and their difference
    Date date1 = parseDatetimeWithMs("2003-01-01T12:00:01.000");
    Date date2 = parseDatetimeWithMs("2003-01-01T12:00:01.250");
    Date date3 = parseDatetimeWithMs("2003-01-01T12:00:00.999");

    // succeeds because the all the time fields up to seconds are the same
    assertThat(date1).isInSameSecondAs(date2);
    // fails because seconds fields differ even though time difference is only 1ms !
    try {
      assertThat(date1).isInSameSecondAs(date3);
    } catch (AssertionError e) {
      logAssertionErrorMessage("isInSameSecondAs date assertion", e);
    }

    // succeeds because the time difference between dates < 1s
    assertThat(date1).isInSameSecondWindowAs(date2);
    assertThat(date1).isInSameSecondWindowAs(date3);
    // fails because time difference between dates >= 1s
    try {
      assertThat(date1).isInSameSecondWindowAs(parseDatetimeWithMs("2003-01-01T12:00:02.000"));
    } catch (AssertionError e) {
      logAssertionErrorMessage("isInSameSecondWindowAs date assertion", e);
    }

  }

  @Test
  public void is_in_the_same_minute_assertions_examples() {
    //let's see the isInSameSecondAs and isInSameSecondWindowAs in action and their difference
    Date date1 = parseDatetime("2003-01-01T12:01:00");
    Date date2 = parseDatetime("2003-01-01T12:01:30");
    Date date3 = parseDatetime("2003-01-01T12:00:59");

    // succeeds because the all the time fields up to minutes are the same
    assertThat(date1).isInSameMinuteAs(date2);
    try {
      // fails because minutes fields differ even though time difference is only 1s !
      assertThat(date1).isInSameMinuteAs(date3);
    } catch (AssertionError e) {
      logAssertionErrorMessage("isInSameMinuteWindowAs date assertion", e);
    }

    // succeeds because date time difference < 1 min
    assertThat(date1).isInSameMinuteWindowAs(date2);
    assertThat(date1).isInSameMinuteWindowAs(date3);
    try {
      // fails because date time difference >= 1 min
      assertThat(date1).isInSameMinuteWindowAs(parseDatetime("2003-01-01T12:02:00"));
    } catch (AssertionError e) {
      logAssertionErrorMessage("isInSameMinuteWindowAs date assertion", e);
    }

  }

  @Test
  public void is_in_the_same_hour_assertions_examples() {
    //let's see the isInSameSecondAs and isInSameSecondWindowAs in action and their difference
    Date date1 = parseDatetime("2003-01-01T12:00:00");
    Date date2 = parseDatetime("2003-01-01T12:30:00");
    Date date3 = parseDatetime("2003-01-01T11:59:59");
    Date date4 = parseDatetime("2003-01-01T13:00:01");

    // succeeds because the all the time fields up to hours are the same
    assertThat(date1).isInSameHourAs(date2);
    try {
      assertThat(date1).isInSameHourAs(date3);
    } catch (AssertionError e) {
      logAssertionErrorMessage("isInSameHourAs date assertion", e);
    }

    // succeeds because time difference < 1h
    assertThat(date1).isInSameHourWindowAs(date2);
    assertThat(date1).isInSameHourWindowAs(date3);
    try {
      assertThat(date1).isInSameHourWindowAs(date4);
    } catch (AssertionError e) {
      logAssertionErrorMessage("isInSameHourWindowAs date assertion", e);
    }
  }

  @Test
  public void is_in_the_same_day_or_month_or_year_assertions_examples() {
    Date date1 = parseDatetime("2003-04-26T23:17:00");
    Date date2 = parseDatetime("2003-04-26T12:30:00");
    assertThat(date1).isInSameDayAs(date2);
    assertThat(date1).isInSameMonthAs("2003-04-27");
    assertThat(date1).isInSameYearAs("2003-05-13");
  }

  @Test
  public void is_within_date_assertions_examples() {
    Date date1 = parseDatetimeWithMs("2003-04-26T13:20:35.017");
    assertThat(date1).isWithinMillisecond(17);
    assertThat(date1).isWithinSecond(35);
    assertThat(date1).isWithinMinute(20);
    assertThat(date1).isWithinHourOfDay(13);
    assertThat(date1).isWithinDayOfWeek(Calendar.SATURDAY);
    assertThat(date1).isWithinMonth(4);
    assertThat(date1).isWithinYear(2003);
    // assertThat(date1).hasMillisecond(17);
    // assertThat(date1).hasSecond(35);
    // assertThat(date1).hasMinute(20);
    // assertThat(date1).hasHourOfDay(13);
    // assertThat(date1).hasDayOfWeek(Calendar.SATURDAY);
    // assertThat(date1).hasMonth(4);
    // assertThat(date1).hasYear(2003);
  }

  @Test
  public void date_assertions_with_date_represented_as_string() {
    // You can use date String representation with various format (we take care of parsing String as Date):
    // - yyyy-MM-dd
    // - yyyy-MM-dd'T'HH:mm:ss,
    // - yyyy-MM-dd'T'HH:mm:ss.SSS,
    assertThat(theTwoTowers.getReleaseDate()).isEqualTo("2002-12-18");
    assertThat(theTwoTowers.getReleaseDate()).isEqualTo("2002-12-18T00.00.00");
    assertThat(theTwoTowers.getReleaseDate()).isEqualTo("2002-12-18T00.00.00.000");

    // but you can use custom date format if you prefer
    DateFormat userCustomDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    assertThat(theTwoTowers.getReleaseDate()).withDateFormat(userCustomDateFormat).isEqualTo("18/12/2002");
    // once set, custom format is used for all following assertions !
    assertThat(theReturnOfTheKing.getReleaseDate()).isEqualTo("17/12/2003");

    // you can easily get back to default date formats ...
    assertThat(theTwoTowers.getReleaseDate()).withDefaultDateFormats().isEqualTo("2002-12-18");
    // ... which is then used for all following assertions
    assertThat(theReturnOfTheKing.getReleaseDate()).isEqualTo("2003-12-17");

    // another way of using custom date format by calling static method useDateFormat
    DateAssert.useDateFormat("dd/MM/yyyy");
    assertThat(theTwoTowers.getReleaseDate()).isEqualTo("18/12/2002");
    assertThat(theReturnOfTheKing.getReleaseDate()).isEqualTo("17/12/2003");

    // switch back to default date formats
    DateAssert.useIsoDateFormat();
    assertThat(theTwoTowers.getReleaseDate()).isEqualTo("2002-12-18");
    assertThat(theReturnOfTheKing.getReleaseDate()).isEqualTo("2003-12-17");

    // you can switch back to default easily with one of
    DateAssert.useDefaultDateFormats();
    Assertions.useDefaultDateFormats();
    assertThat(theTwoTowers.getReleaseDate()).isEqualTo("2002-12-18T00.00.00.000");
    // choose whatever approach suits you best !
  }

  @Test
  public void date_assertions_with_custom_comparison_examples() {
    // the Two Towers release date is 2002-12-18
    assertThat(theTwoTowers.getReleaseDate()).usingComparator(yearAndMonthComparator).isEqualTo("2002-12-01")
      .isEqualTo("2002-12-02") // same year and month
      .isNotEqualTo("2002-11-18") // same year but different month
      .isBetween("2002-12-01", "2002-12-10", true, true)
      .isNotBetween("2002-12-01", "2002-12-10") // 2002-12-10 is excluded
      .isIn("2002-12-01") // ok same year and month
      .isNotIn("2002-11-01", "2002-10-01"); // same year but different month
    // build date away from today by one day (subtract one day if we are at the end of the month, otherwise we add one)
    Date oneDayFromTodayInSameMonth = monthOf(tomorrow()) == monthOf(new Date()) ? tomorrow() : yesterday();
    assertThat(oneDayFromTodayInSameMonth).usingComparator(yearAndMonthComparator).isToday();
  }

}
