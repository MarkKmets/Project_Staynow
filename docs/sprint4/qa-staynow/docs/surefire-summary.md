# Протокол запуску Surefire

**Команда:** Backrooms. **Модуль:** StayNow v1.0.0. **Дата перевірки:** 06.11.2026. **Об’єкт:** повний локальний набір із 57 основних юніт-методів і 9 UI-сценаріїв.

## Середовище

Linux amd64, OpenJDK 17.0.20, Maven 3.9.9, JUnit Jupiter 5.10.2, Surefire 3.2.5, Selenium 4.21.0. Для UI — Chrome for Testing headless shell і ChromeDriver 157.0.8103.38. Умови перевірочного середовища наведено в [аудиті](../../audit.md).

## Юніт тести

Команда: `mvn clean test`. Завершення: **06.11.2026, 13:26:07 +02:00**.

**Tests run: 155, Failures: 21, Errors: 1, Skipped: 0 — BUILD FAILURE.**

| Клас | Запусків | Failures | Errors | Skipped |
|---|---|---|---|---|
| BookingValidatorTest | 50 | 3 | 0 | 0 |
| PriceCalculatorTest | 66 | 14 | 0 | 0 |
| AvailabilityTest | 37 | 4 | 1 | 0 |
| ExampleTest | 2 | 0 | 0 | 0 |

## Невдалі тести і дефекти

| Тест, що не пройшов | Дефект |
|---|---|
| `BookingValidatorTest.allowedNightsAreAccepted[5]` (30 ночей) | BUG-019 |
| `BookingValidatorTest.checkInYesterdayCheckOutTomorrow`, `severalViolationsReportedTogether` | BUG-020 |
| `PriceCalculatorTest`: `weekendNight[5]`, `weekendNight[7]`, `nightPrice[4]`, `[5]`, `[6]`, `[7]`, `totalFridayNightOnly`, `totalSundayNightOnly`, `totalSixNightsNoDiscount`, `totalAcrossSeasonBoundary`, `totalSaturdayAndSundayInHighSeasonStart` | BUG-021 |
| `PriceCalculatorTest.nightPriceRoundsHalfUp*` (3) | BUG-022 |
| `AvailabilityTest.cancellationFee[3]`, `refund[1]` (7 днів) | BUG-023 |
| `AvailabilityTest.intervalAgainstExistingStay[8]`, `[9]`, `backToBackBookingsAllowed` (**Error**: `IllegalStateException`) | BUG-024 |

Усі 22 невдалі виконання відповідають шести дефектам v1. backToBackBookingsAllowed завершується Error через IllegalStateException; решта 21 — Failure. Інші 133 виконання успішні. Очікування взято з SRS; дефектні тести збережено для регресії v2.

## UI тести

Команда: `mvn clean test "-DexcludedGroups=none" "-Dtest=*UiTest"`.

| Прогін | Завершення 06.11.2026 | Tests run | Failures | Errors | Skipped | Результат |
|---|---|---|---|---|---|---|
| 1 | 12:37:30 +02:00 | 9 | 0 | 0 | 0 | BUILD SUCCESS |
| 2 | 12:39:42 +02:00 | 9 | 0 | 0 | 0 | BUILD SUCCESS |

В обох прогонах: HomePageUiTest — 2, AdminLoginUiTest — 3, ContactFormUiTest — 4. Під час розширення юніт-набору UI-код не змінювався.
