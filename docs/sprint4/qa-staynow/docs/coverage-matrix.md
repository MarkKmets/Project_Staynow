# Матриця покриття BL (юніт-тести)

Вимога → правило → тести → відповідальні → результат перевірки. Еталон — SRS 1.1 (BL-80 вилучено). Результат технічної перевірки пакета від 06.11.2026 наведено в [розділі review](#review).

| BL | Правило | Клас | Тест-методи | Відповідальний за внесок | Рецензент | Червоний на v1 (звіт) | Запусків / не пройшло (06.11.2026) | Технічний review / PR |
|---|---|---|---|---|---|---|---|---|
| BL-10 | Дати, ночі 1..30, заїзд ≥ today | BookingValidatorTest | checkOutNotAfterCheckIn, allowedNightsAreAccepted, thirtyOneNightsRejected, farAboveMaxNightsRejected | Кмець Марк | Сирота Володимир | BUG-019 (30 ночей) | 9 / 1 | Перевірено 06.11.2026 — [review](#review); [PR](https://github.com/MarkKmets/Project_Staynow/pulls?q=is%3Apr%20head%3Atests%2Fkmets) |
| BL-10 | Заїзд не раніше today | BookingValidatorTest | validBookingHasNoErrors, checkInToday, checkInYesterdayCheckOutTomorrow, bothDatesInThePast, checkInTomorrow | Кмець Марк | Сирота Володимир | BUG-020 | 5 / 1 | Перевірено 06.11.2026 — [review](#review); [PR](https://github.com/MarkKmets/Project_Staynow/pulls?q=is%3Apr%20head%3Atests%2Fkmets) |
| BL-10 | Гості 1..capacity; кілька порушень | BookingValidatorTest | guestsWithinCapacity, guestsOutsideRange, severalViolationsReportedTogether | Тіньков Данііл | Сирота Володимир | BUG-020 (severalViolations) | 10 / 1 | Перевірено 06.11.2026 — [review](#review); [PR](https://github.com/MarkKmets/Project_Staynow/pulls?q=is%3Apr%20head%3Atests%2Ftinkov) |
| BL-20 | Формат email | BookingValidatorTest | validEmails, invalidEmails, nullAndEmptyEmail | Тіньков Данііл | Сирота Володимир | — | 12 / 0 | Перевірено 06.11.2026 — [review](#review); [PR](https://github.com/MarkKmets/Project_Staynow/pulls?q=is%3Apr%20head%3Atests%2Ftinkov) |
| BL-20 | Формат телефону +380 + 9 цифр | BookingValidatorTest | validPhone, invalidPhones, nullAndEmptyPhone | Тіньков Данііл | Сирота Володимир | — | 10 / 0 | Перевірено 06.11.2026 — [review](#review); [PR](https://github.com/MarkKmets/Project_Staynow/pulls?q=is%3Apr%20head%3Atests%2Ftinkov) |
| BL-20 | validate(…, email, phone) | BookingValidatorTest | validateWithContactsOk, validateWithBadEmail, validateWithBadPhone, validateWithDateAndContactErrors | Тіньков Данііл | Сирота Володимир | — | 4 / 0 | Перевірено 06.11.2026 — [review](#review); [PR](https://github.com/MarkKmets/Project_Staynow/pulls?q=is%3Apr%20head%3Atests%2Ftinkov) |
| BL-30 | Вихідна ніч: п’ятниця, субота | PriceCalculatorTest | weekendNight | Гатченко Ярослав | Сирота Володимир | BUG-021 | 7 / 2 | Перевірено 06.11.2026 — [review](#review); [PR](https://github.com/MarkKmets/Project_Staynow/pulls?q=is%3Apr%20head%3Atests%2Fhatchenko) |
| BL-40 | Сезонний коефіцієнт і сусідні дати меж | PriceCalculatorTest | seasonFactor | Гатченко Ярослав | Сирота Володимир | — | 30 / 0 | Перевірено 06.11.2026 — [review](#review); [PR](https://github.com/MarkKmets/Project_Staynow/pulls?q=is%3Apr%20head%3Atests%2Fhatchenko) |
| BL-50 | Ціна ночі, таблиця рішень сезон × вихідна | PriceCalculatorTest | nightPrice, nightPriceRejectsNonPositiveRate | Гатченко Ярослав | Сирота Володимир | BUG-021 | 10 / 4 | Перевірено 06.11.2026 — [review](#review); [PR](https://github.com/MarkKmets/Project_Staynow/pulls?q=is%3Apr%20head%3Atests%2Fhatchenko) |
| BL-50 | Округлення HALF_UP ціни ночі | PriceCalculatorTest | nightPriceRoundsHalfUpAboveHalf, nightPriceRoundsHalfUpAtExactHalf, nightPriceRoundsHalfUpSmallValue | Гатченко Ярослав | Сирота Володимир | BUG-022 | 3 / 3 | Перевірено 06.11.2026 — [review](#review); [PR](https://github.com/MarkKmets/Project_Staynow/pulls?q=is%3Apr%20head%3Atests%2Fhatchenko) |
| BL-50 | Сума, знижка від 7 ночей, винятки | PriceCalculatorTest | totalTwoWeekdayNights, totalFridayNightOnly, totalSundayNightOnly, totalSixNightsNoDiscount, totalAtAndAboveSevenNightsDiscount, totalThirtyNights, totalDiscountRoundsHalfUp, totalAcrossSeasonBoundary, totalSaturdayAndSundayInHighSeasonStart, checkOutNightIsNotCharged, totalRejectsNonPositiveRate, totalSameDayRejected, totalReversedDatesRejected, totalHasTwoDecimalPlaces | Сирота Володимир | Тіньков Данііл | BUG-021 (5 тестів) | 16 / 5 | Перевірено 06.11.2026 — [review](#review); [PR](https://github.com/MarkKmets/Project_Staynow/pulls?q=is%3Apr%20head%3Atests%2Fsyrota) |
| BL-60 | Плата за скасування і повернення | AvailabilityTest | cancellationFee, cancellationFeeRoundsHalfUp, cancellationFeeNegativeTotal, refund | Сімутін Владислав | Сирота Володимир | BUG-023 | 17 / 2 | Перевірено 06.11.2026 — [review](#review); [PR](https://github.com/MarkKmets/Project_Staynow/pulls?q=is%3Apr%20head%3Atests%2Fsimutin) |
| BL-70 | Доступність, book, напіввідкриті інтервали | AvailabilityTest | emptyRegistryIsAvailable, intervalAgainstExistingStay, otherRoomIsIndependent, bookRegistersStay, bookBusyRoomThrows, backToBackBookingsAllowed, freeDatesStillBookableAfterFailedBooking, emptyIntervalRejected, reversedIntervalRejected, staysIsUnmodifiable | Сімутін Владислав | Сирота Володимир | BUG-024 | 20 / 3 | Перевірено 06.11.2026 — [review](#review); [PR](https://github.com/MarkKmets/Project_Staynow/pulls?q=is%3Apr%20head%3Atests%2Fsimutin) |

Методів-тестів: 57 у трьох класах (норматив для 5 осіб ≥ 20; параметризований метод — один тест). Після доповнення сусідніх граничних значень кількість запусків — 153 + 2 зразка = 155. У таблиці наведено результати актуального набору.

**Розподіл методів між учасниками:** Кмець Марк — 9, Тіньков Данііл — 13, Гатченко Ярослав — 7, Сирота Володимир — 14, Сімутін Владислав — 14 (норматив ≥ 3 на особу).

**Стан реалізації:** 57 методів наведені в підготовлених фрагментах поза папкою публікації; у базі основні класи ще порожні.

**UI-тести** — в [ui-scenarios.md](ui-scenarios.md); автор — Гатченко Ярослав (ІТ), опис сценаріїв — Кмець Марк (ТА), рецензент — Сирота Володимир.



## Повнота сусідніх значень у поточній редакції

| Вимога | Доповнення | Актуальний результат |
|---|---|---|
| BL-10 | Додано 3 гостей при capacity=4: тепер є 3/4/5 біля верхньої межі | BookingValidatorTest: 50 запусків, 3 failures |
| BL-40 | Додано 02.06, 30.08, 21.12, 09.01, 02.02, 30.03, 02.11, 29.11 | seasonFactor: 30 наборів, усі успішні |
| BL-50 | До 6 і 7 ночей додано 8 ночей; параметризований метод залишається одним методом | PriceCalculatorTest: 66 запусків, 14 failures |

Методів залишається 57. Повний актуальний набір: 155 запусків, 21 failure, 1 error. Це розширення покриття, а не зміна очікувань під дефекти v1.


## Обґрунтування очікувань

| Вимога | Зіставлені правила | Результат технічної перевірки |
|---|---|---|
| BL-10 | Ночі 1…30, checkIn ≥ TODAY, guests 1…capacity | Поведінкові очікування відповідають формулюванню; найменування кодів у SRS і модулі різняться, див. нижче |
| BL-20 | Email локальна частина@домен.зона, телефон +380 і 9 цифр | Типові валідні й невалідні класи перевіряються; мінімальна довжина зони не визначена SRS однозначно |
| BL-30 | П’ятниця й субота, надбавка 15 % | Очікування обчислені за п’ятницею/суботою; відхилення v1 — BUG-021 |
| BL-40 | Літо і 20.12…10.01 — 1.30; лютий, березень, листопад — 0.80 | Є 30 наборів, зокрема межі року та високосний лютий |
| BL-50 | HALF_UP, [checkIn,checkOut), знижка 10 % від 7 ночей, недодатний тариф/погані дати — виняток | Є окремі перевірки округлення, 6/7/8 ночей, виключення ночі виїзду та винятків |
| BL-60 | 7+ днів — 0; 3…6 — 50 %; менше 3 — 100 % | Перевіряються 2/3/4 і 6/7/8 днів; refund перевіряє різницю |
| BL-70 | Доступність, суміжні інтервали, book зайнятого номера | Є перетини, дотики з двох боків, інший номер і виняток зайнятості |

Розбіжність позначень BL-10: SRS INVALID_DATES ↔ модуль CHECK_OUT_NOT_AFTER_CHECK_IN; INVALID_NIGHTS ↔ NIGHTS_OUT_OF_RANGE; PAST_CHECK_IN ↔ CHECK_IN_IN_PAST; INVALID_GUESTS ↔ GUESTS_OUT_OF_RANGE. Тести використовують фактичний API BookingError; це не дослівний збіг назв зі SRS. Наявність цієї розбіжності задокументовано, а не приховано зміною очікувань.


## Review

**Відповідальний:** Сирота Володимир. **Частину Сироти перевіряє:** Тіньков Данііл. **Дата перевірки матеріалів:** 06.11.2026.

Критерії — чек-лист 5.3 методички. Рішення щодо PR і зауваження до конкретних змін фіксуються в GitHub; гілки наведено в основній таблиці.

| Пункт | Предмет перевірки | Фактичний результат |
|---|---|---|
| 1 | Правило BL або крок TC, ID у DisplayName, очікування з вимоги | Перевірено: 57 основних методів мають явні DisplayName з BL, дев’ять UI-методів — TC-ID. Основні правила зіставлено з розділом 5 SRS 1.1; UI-випадки — з test-cases.xlsx. Розбіжність найменувань кодів BL-10 і часткове UI-покриття описано явно |
| 2 | Arrange Act Assert, одна поведінка | Підготовка об’єктів винесена в BeforeEach; входи параметризованих тестів задані окремо. Пов’язані властивості bookRegistersStay і totalHasTwoDecimalPlaces згруповано через assertAll |
| 3 | Змістові назви | Назви описують умову і результат; номерні test1/test2 для основного набору не використовуються |
| 4 | Межі і параметризація | Перевіряються ночі 0/1/29/30/31, TODAY−1/TODAY/TODAY+1, допустимі й недопустимі гості, сезонні переходи, 6/7/8 ночей знижки, 2/3/4 та 6/7/8 днів скасування, суміжні інтервали |
| 5 | Винятки і BigDecimal | AssertThrows використовує конкретні класи винятків. Гроші задаються BigDecimal; є перевірки HALF_UP, шкали й числового порівняння |
| 6 | Незалежність | Об’єкти модуля створюються перед кожним тестом. Кожен UI-тест створює й закриває власний браузер. Тести не вимагають заданого порядку виконання |
| 7 | Очікування і локатори | Викликів Thread.sleep та implicit waits немає. Очікування видимих серверних помилок і подяки посилено. Локатори відносні: id, CSS, XPath за текстом |
| 8 | Пояснення невдалих тестів | Невдалі юніт-запуски зіставлено з BUG-019…024. Тести не вимкнено, очікування не змінено під дефектний код v1 |

### Зауваження і виправлення

| ID | Зауваження | Рішення | Стан |
|---|---|---|---|
| REV-01 | У BUG-019 поле Expected помилково містило NIGHTS_OUT_OF_RANGE | Записано []: 30 ночей допустимі за BL-10 | Виправлено |
| REV-02 | Частина параметризованих методів не мала явного DisplayName | Додано DisplayName з BL-ID, збережено набори даних | Виправлено |
| REV-03 | Дві групи пов’язаних властивостей перевірялися послідовними asserts | Використано assertAll | Виправлено |
| REV-04 | Негативні UI-тести могли ігнорувати тайм-аут появи серверної помилки | Тепер відсутність видимої відповіді завершує тест невдачею | Виправлено |
| REV-05 | Зникнення поля контакту саме по собі не підтверджувало появу подяки | Додано очікування видимого заголовка подяки | Виправлено |
| REV-08 | Дев’ять методів могли сприйматися як повне виконання всіх кроків дев’яти ручних TC | У ui-scenarios.md наведено конкретні перевірені частини й виключення | Межі покриття задокументовано |
| REV-10 | Після прокручування Submit натискання могло відбутися поза viewport | Миттєве прокручування і явне очікування успішного WebDriver-click з повторним пошуком | Виправлено; 9 UI-тестів двічі успішні |
| REV-09 | SRS і API використовують різні назви чотирьох кодів BL-10 | Додано явне зіставлення позначень; поведінкові очікування не змінено | Розбіжність джерел задокументовано |


Для звітів BUG-019…024 перевірено вимогу, тест, входи, Actual/Expected, середовище, кроки та severity. Звіти реєструються у [спільному трекері](https://github.com/MarkKmets/Project_Staynow/issues) з міткою module.
