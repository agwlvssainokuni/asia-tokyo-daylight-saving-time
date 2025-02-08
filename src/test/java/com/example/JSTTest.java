/*
 * Copyright 2025 agwlvssainokuni
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example;

import org.apache.commons.lang3.Range;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class JSTTest {

    private final ZoneId zoneId = ZoneId.of("JST", ZoneId.SHORT_IDS);
    private final TimeZone timeZone = TimeZone.getTimeZone("JST");

    private final List<Range<LocalDate>> dst = List.of(
            Range.of(LocalDate.of(1948, 5, 2), LocalDate.of(1948, 9, 12)),
            Range.of(LocalDate.of(1949, 4, 3), LocalDate.of(1949, 9, 11)),
            Range.of(LocalDate.of(1950, 5, 7), LocalDate.of(1950, 9, 10)),
            Range.of(LocalDate.of(1951, 5, 6), LocalDate.of(1951, 9, 9))
    );

    private final LocalTime lt000000 = LocalTime.of(0, 0, 0);
    private final LocalTime lt005959 = LocalTime.of(0, 59, 59);

    @Nested
    class JavaTime {

        Supplier<ZonedDateTime> strict(LocalDateTime ldt) {
            return () -> ZonedDateTime.ofStrict(
                    ldt,
                    zoneId.getRules().getOffset(ldt),
                    zoneId
            );
        }

        Supplier<ZonedDateTime> notStrict(LocalDateTime ldt) {
            return () -> ZonedDateTime.of(
                    ldt,
                    zoneId
            );
        }

        void assertNoError(Supplier<ZonedDateTime> supplier) {
            assertNotNull(supplier.get());
        }

        void assertError(Supplier<ZonedDateTime> supplier) {
            assertThrowsExactly(DateTimeException.class, supplier::get);
        }

        @Nested
        class Strict {

            @Nested
            class Start {
                @Test
                void 前日235959_エラーなし() {
                    dst.stream().map(Range::getMinimum)
                            .map(ld -> ld.atTime(lt000000).minusSeconds(1L))
                            .map(JavaTime.this::strict)
                            .forEach(JavaTime.this::assertNoError);
                }

                @Test
                void 当日000000_エラー発生() {
                    dst.stream().map(Range::getMinimum)
                            .map(ld -> ld.atTime(lt000000))
                            .map(JavaTime.this::strict)
                            .forEach(JavaTime.this::assertError);
                }

                @Test
                void 当日005959_エラー発生() {
                    dst.stream().map(Range::getMinimum)
                            .map(ld -> ld.atTime(lt005959))
                            .map(JavaTime.this::strict)
                            .forEach(JavaTime.this::assertError);
                }

                @Test
                void 当日010000_エラーなし() {
                    dst.stream().map(Range::getMinimum)
                            .map(ld -> ld.atTime(lt005959).plusSeconds(1L))
                            .map(JavaTime.this::strict)
                            .forEach(JavaTime.this::assertNoError);
                }
            }

            @Nested
            class End {
                @Test
                void 前日235959_エラーなし() {
                    dst.stream().map(Range::getMaximum)
                            .map(ld -> ld.atTime(lt000000).minusSeconds(1L))
                            .map(JavaTime.this::strict)
                            .forEach(JavaTime.this::assertNoError);
                }

                @Test
                void 当日000000_エラーなし() {
                    dst.stream().map(Range::getMaximum)
                            .map(ld -> ld.atTime(lt000000))
                            .map(JavaTime.this::strict)
                            .forEach(JavaTime.this::assertNoError);
                }

                @Test
                void 当日005959_エラーなし() {
                    dst.stream().map(Range::getMaximum)
                            .map(ld -> ld.atTime(lt005959))
                            .map(JavaTime.this::strict)
                            .forEach(JavaTime.this::assertNoError);
                }

                @Test
                void 当日010000_エラーなし() {
                    dst.stream().map(Range::getMaximum)
                            .map(ld -> ld.atTime(lt005959).plusSeconds(1L))
                            .map(JavaTime.this::strict)
                            .forEach(JavaTime.this::assertNoError);
                }
            }
        }

        @Nested
        class NotStrict {

            @Nested
            class Start {
                @Test
                void 前日235959_エラーなし() {
                    dst.stream().map(Range::getMinimum)
                            .map(ld -> ld.atTime(lt000000).minusSeconds(1L))
                            .map(JavaTime.this::notStrict)
                            .forEach(JavaTime.this::assertNoError);
                }

                @Test
                void 当日000000_エラーなし() {
                    dst.stream().map(Range::getMinimum)
                            .map(ld -> ld.atTime(lt000000))
                            .map(JavaTime.this::notStrict)
                            .forEach(JavaTime.this::assertNoError);
                }

                @Test
                void 当日005959_エラーなし() {
                    dst.stream().map(Range::getMinimum)
                            .map(ld -> ld.atTime(lt005959))
                            .map(JavaTime.this::notStrict)
                            .forEach(JavaTime.this::assertNoError);
                }

                @Test
                void 当日010000_エラーなし() {
                    dst.stream().map(Range::getMinimum)
                            .map(ld -> ld.atTime(lt005959).plusSeconds(1L))
                            .map(JavaTime.this::notStrict)
                            .forEach(JavaTime.this::assertNoError);
                }
            }

            @Nested
            class End {
                @Test
                void 前日235959_エラーなし() {
                    dst.stream().map(Range::getMaximum)
                            .map(ld -> ld.atTime(lt000000).minusSeconds(1L))
                            .map(JavaTime.this::notStrict)
                            .forEach(JavaTime.this::assertNoError);
                }

                @Test
                void 当日000000_エラーなし() {
                    dst.stream().map(Range::getMaximum)
                            .map(ld -> ld.atTime(lt000000))
                            .map(JavaTime.this::notStrict)
                            .forEach(JavaTime.this::assertNoError);
                }

                @Test
                void 当日005959_エラーなし() {
                    dst.stream().map(Range::getMaximum)
                            .map(ld -> ld.atTime(lt005959))
                            .map(JavaTime.this::notStrict)
                            .forEach(JavaTime.this::assertNoError);
                }

                @Test
                void 当日010000_エラーなし() {
                    dst.stream().map(Range::getMaximum)
                            .map(ld -> ld.atTime(lt005959).plusSeconds(1L))
                            .map(JavaTime.this::notStrict)
                            .forEach(JavaTime.this::assertNoError);
                }
            }
        }
    }

    @Nested
    class UtilCalendar {

        Supplier<Instant> strict(LocalDateTime ldt) {
            return () -> {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeZone(timeZone);
                calendar.set(
                        ldt.getYear(),
                        ldt.getMonthValue() - 1,
                        ldt.getDayOfMonth(),
                        ldt.getHour(),
                        ldt.getMinute(),
                        ldt.getSecond()
                );
                calendar.setLenient(false);
                return calendar.toInstant();
            };
        }

        Supplier<Instant> notStrict(LocalDateTime ldt) {
            return () -> {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeZone(timeZone);
                calendar.set(
                        ldt.getYear(),
                        ldt.getMonthValue() - 1,
                        ldt.getDayOfMonth(),
                        ldt.getHour(),
                        ldt.getMinute(),
                        ldt.getSecond()
                );
                calendar.setLenient(true);
                return calendar.toInstant();
            };
        }

        void assertNoError(Supplier<Instant> supplier) {
            assertNotNull(supplier.get());
        }

        void assertError(Supplier<Instant> supplier) {
            assertThrowsExactly(IllegalArgumentException.class, supplier::get);
        }

        @Nested
        class Strict {

            @Nested
            class Start {
                @Test
                void 前日235959_エラーなし() {
                    dst.stream().map(Range::getMinimum)
                            .map(ld -> ld.atTime(lt000000).minusSeconds(1L))
                            .map(UtilCalendar.this::strict)
                            .forEach(UtilCalendar.this::assertNoError);
                }

                @Test
                void 当日000000_エラー発生() {
                    dst.stream().map(Range::getMinimum)
                            .map(ld -> ld.atTime(lt000000))
                            .map(UtilCalendar.this::strict)
                            .forEach(UtilCalendar.this::assertError);
                }

                @Test
                void 当日005959_エラー発生() {
                    dst.stream().map(Range::getMinimum)
                            .map(ld -> ld.atTime(lt005959))
                            .map(UtilCalendar.this::strict)
                            .forEach(UtilCalendar.this::assertError);
                }

                @Test
                void 当日010000_エラーなし() {
                    dst.stream().map(Range::getMinimum)
                            .map(ld -> ld.atTime(lt005959).plusSeconds(1L))
                            .map(UtilCalendar.this::strict)
                            .forEach(UtilCalendar.this::assertNoError);
                }
            }

            @Nested
            class End {
                @Test
                void 前日235959_エラーなし() {
                    dst.stream().map(Range::getMaximum)
                            .map(ld -> ld.atTime(lt000000).minusSeconds(1L))
                            .map(UtilCalendar.this::strict)
                            .forEach(UtilCalendar.this::assertNoError);
                }

                @Test
                void 当日000000_エラーなし() {
                    dst.stream().map(Range::getMaximum)
                            .map(ld -> ld.atTime(lt000000))
                            .map(UtilCalendar.this::strict)
                            .forEach(UtilCalendar.this::assertNoError);
                }

                @Test
                void 当日005959_エラーなし() {
                    dst.stream().map(Range::getMaximum)
                            .map(ld -> ld.atTime(lt005959))
                            .map(UtilCalendar.this::strict)
                            .forEach(UtilCalendar.this::assertNoError);
                }

                @Test
                void 当日010000_エラーなし() {
                    dst.stream().map(Range::getMaximum)
                            .map(ld -> ld.atTime(lt005959).plusSeconds(1L))
                            .map(UtilCalendar.this::strict)
                            .forEach(UtilCalendar.this::assertNoError);
                }
            }
        }

        @Nested
        class NotStrict {

            @Nested
            class Start {
                @Test
                void 前日235959_エラーなし() {
                    dst.stream().map(Range::getMinimum)
                            .map(ld -> ld.atTime(lt000000).minusSeconds(1L))
                            .map(UtilCalendar.this::notStrict)
                            .forEach(UtilCalendar.this::assertNoError);
                }

                @Test
                void 当日000000_エラーなし() {
                    dst.stream().map(Range::getMinimum)
                            .map(ld -> ld.atTime(lt000000))
                            .map(UtilCalendar.this::notStrict)
                            .forEach(UtilCalendar.this::assertNoError);
                }

                @Test
                void 当日005959_エラーなし() {
                    dst.stream().map(Range::getMinimum)
                            .map(ld -> ld.atTime(lt005959))
                            .map(UtilCalendar.this::notStrict)
                            .forEach(UtilCalendar.this::assertNoError);
                }

                @Test
                void 当日010000_エラーなし() {
                    dst.stream().map(Range::getMinimum)
                            .map(ld -> ld.atTime(lt005959).plusSeconds(1L))
                            .map(UtilCalendar.this::notStrict)
                            .forEach(UtilCalendar.this::assertNoError);
                }
            }

            @Nested
            class End {
                @Test
                void 前日235959_エラーなし() {
                    dst.stream().map(Range::getMaximum)
                            .map(ld -> ld.atTime(lt000000).minusSeconds(1L))
                            .map(UtilCalendar.this::notStrict)
                            .forEach(UtilCalendar.this::assertNoError);
                }

                @Test
                void 当日000000_エラーなし() {
                    dst.stream().map(Range::getMaximum)
                            .map(ld -> ld.atTime(lt000000))
                            .map(UtilCalendar.this::notStrict)
                            .forEach(UtilCalendar.this::assertNoError);
                }

                @Test
                void 当日005959_エラーなし() {
                    dst.stream().map(Range::getMaximum)
                            .map(ld -> ld.atTime(lt005959))
                            .map(UtilCalendar.this::notStrict)
                            .forEach(UtilCalendar.this::assertNoError);
                }

                @Test
                void 当日010000_エラーなし() {
                    dst.stream().map(Range::getMaximum)
                            .map(ld -> ld.atTime(lt005959).plusSeconds(1L))
                            .map(UtilCalendar.this::notStrict)
                            .forEach(UtilCalendar.this::assertNoError);
                }
            }
        }
    }
}
